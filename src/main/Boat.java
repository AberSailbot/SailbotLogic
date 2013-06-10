package main;

import java.io.IOException;

import behavior.RudderController;

/**
 * @author thip
 * @author Kamil Mrowiec <kam20@aber.ac.uk> 300
 * @version 1.0 (4 May 2013)
 */
public class Boat{
	
	/**
	 * How close can we sail to the wind.
	 */
	public static final int HOW_CLOSE = 45;
	
	/**
	 * How long should the boat stay inside the box (for station keeping)
	 * (in seconds, originally 5 minutes - 300 seconds);
	 */
	public static final int BOX_TIME = 300;
	
	
	private RudderController behavior;
	public Waypoints waypoints;
	public Communication com;
	private RudderController rudderController;
	
	private int heading;
	private Position position;
	private int absoluteWindDirection; 
	
	private double distanceToWaypoint;
	private int waypointHeading;
	
	private int rudderPosition;
	private int sailPosition;

    private long sailLastUpdated;

	//For tacking:
	private double maxDistOnSide = 10.0;
	private double distOnLeft, distOnRight;
	private char currentSide = 'L'; //Left or right
	private boolean tackingSet = false;
	private Position startPoint;
	private int targetHeading;
	double currentDistance, targetDistance;
	
	
	
    /**
     * Creates boat with given waypoints.
     */
	public Boat(Waypoints wps){
		waypoints = wps;
		com = new Communication();
		rudderController = new RudderController(this);

        position = new Position ();
        rudderPosition = 180;
        updateRudder();
        
        //Informing the Python program that waypoint has changed.
		com.sendMessage("set waypointnum " + waypoints.getNextWaypointNumber());
		com.sendMessage("set waypointnorthing " + waypoints.getNextWaypoint().getLat() );
		com.sendMessage("set waypointeasting " + waypoints.getNextWaypoint().getLon());
	}

	/**
	 * Creates boat with no waypoints.
	 */
	public Boat(){
		waypoints = new Waypoints(this);
		com = new Communication();

        position = new Position ();
	}

	public void sail(){
		if(waypoints.isEmpty()){
			System.out.println("No waypoints to go to.");
			return;
		}
		
		while(true){
			
			//STEP 1:
			//Get sensors reading from Python controller
			try{
				readSensors();
			}catch(IOException ex){
				ex.printStackTrace();
			}
			
			//STEP 2:
			//Check if waypoint is reached. If so, go to next one.
			
			//Reading distance to waypoint and sending it to the Python program
			distanceToWaypoint = waypoints.getDistanceToWaypoint();
			
			
			System.out.println("Dist. to wp : " +distanceToWaypoint + ", should be less than " +Waypoints.WP_REACHED_THRESHOLD);
			//Comparing the distance.
			if(distanceToWaypoint < Waypoints.WP_REACHED_THRESHOLD){
				System.out.println("Waypoint " + waypoints.getNextWaypointNumber() + " reached, moving to next one");
				waypoints.moveToNext();
				//Informing the Python program that waypoint has changed.
				com.sendMessage("set waypointnum " + waypoints.getNextWaypointNumber());
				com.sendMessage("set waypointnorthing " + waypoints.getNextWaypoint().getLat() );
				com.sendMessage("set waypointeasting " + waypoints.getNextWaypoint().getLon());
				
				distanceToWaypoint = waypoints.getDistanceToWaypoint();
			}
			
			com.sendMessage("set waypointdistance " + distanceToWaypoint);
			waypointHeading = waypoints.getWaypointHeading();
			com.sendMessage("set waypointheading " + waypointHeading);
			
			//Printing data for testing and debugging
			System.out.println("Position: " + position.getLat() + ", " + position.getLon());
			System.out.println("Heading: " + this.heading);
			System.out.println("Absolute wind: " + absoluteWindDirection);
			System.out.println("Relative wind: " + this.getRelativeWindDirection());
			System.out.println("Sail :" + this.sailPosition);
			System.out.println("Rudder : " + this.rudderPosition);
			System.out.println("Waypoint number: " + waypoints.getNextWaypointNumber());
			System.out.println("Waypoint heading: " + waypointHeading);
			System.out.println("Tacking: " + tackingSet);
			System.out.println("Target heading: " + targetHeading);
			System.out.println("Target distance: " + targetDistance);
			
			//Checking if course on waypoint is directly sailable. 
			if(Math.abs(Utils.getHeadingDifference(waypointHeading, absoluteWindDirection)) > HOW_CLOSE){
				//If course to waypoint is sailable
				tackingSet = false;
				targetHeading = waypointHeading;
				
				//STEP 3a:
				//Going with the wind, simply heading towards waypoint.
				//PID algorithm calculates rudder adjustments.
				int adjustment = rudderController.getRequiredChange(waypointHeading);
				rudderPosition = 180 + adjustment;
				     
				
			}else{
				//If course to waypoint is not directly sailable
				
				//STEP 3b:
				//Beating to windward.
				
				//If just started going towards the wind, tackling needs to be set up.
				if(!tackingSet){
					
					int angle = Utils.getHeadingDifference(absoluteWindDirection-45, waypointHeading);
					angle = Math.abs(angle);
					distOnLeft = maxDistOnSide * Math.cos(Math.toRadians(angle));
					distOnRight = maxDistOnSide * Math.sin(Math.toRadians(angle));
					
					//Checking which side is favorable, i. e. closer to waypoint heading.
					if(Utils.getHeadingDifference(waypointHeading, absoluteWindDirection + HOW_CLOSE)
							< Utils.getHeadingDifference(waypointHeading, absoluteWindDirection - HOW_CLOSE)){
						//If right side is favorable
						currentSide = 'R';
						targetHeading = absoluteWindDirection + HOW_CLOSE;
						if(targetHeading > 360) targetHeading -= 360;
						
					}else{
						//If left side is favorable
						currentSide = 'L';
						targetHeading = absoluteWindDirection - HOW_CLOSE;
						if(targetHeading < 0) targetHeading = 360 + targetHeading;
					}
					
					startPoint = new Position(position.getLat(), position.getLon());
					
					tackingSet = true;
				}
				
				currentDistance = Utils.getDistance(startPoint, position);
				
				if(currentSide == 'R'){
					targetDistance = distOnRight - currentDistance;
				}else{
					targetDistance = distOnLeft - currentDistance;
				}
				com.sendMessage("set tdist " + targetDistance);
				
				
				//Checking if side should be changed
				if(currentSide == 'L' && currentDistance > distOnLeft){
					//Switching to right side
					currentSide = 'R';
					startPoint = new Position(position.getLat(), position.getLon());
					targetHeading = absoluteWindDirection + HOW_CLOSE;
					if(targetHeading > 360) targetHeading -= 360;
					
				}else if(currentSide == 'R' && currentDistance > distOnRight){
					//Switching to left side
					currentSide = 'L';
					startPoint = new Position(position.getLat(), position.getLon());
					targetHeading = absoluteWindDirection - HOW_CLOSE;
					if(targetHeading < 0) targetHeading = 360 + targetHeading;
				}
				
				//Actually adjusting rudder with 
				int adjustment = rudderController.getRequiredChange(targetHeading);
				rudderPosition = 180 + adjustment;
				
				System.out.println("DistOnLeft : " + distOnLeft + " , DistOnRight: " + distOnRight);
			}
			
			
			this.updateRudder();
			this.updateSail();
			
			try{
				Thread.sleep(300);
			}catch(InterruptedException e){
				e.printStackTrace(); 
			}
			
		}
				
	}

	
	public void sailTowards(int desiredHeading){
		try{
			readSensors();
		}catch(IOException ex){
			ex.printStackTrace();
		}
		//Checking if course on waypoint is directly sailable. 
		if(Math.abs(Utils.getHeadingDifference(desiredHeading, absoluteWindDirection)) > HOW_CLOSE){
			//If course to waypoint is sailable
			tackingSet = false;
			targetHeading = desiredHeading;
			int adjustment = rudderController.getRequiredChange(desiredHeading);
			rudderPosition = 180 + adjustment;
		}else{
			//If course to waypoint is not directly sailable
					
			//If just started going towards the wind, tackling needs to be set up.
			if(!tackingSet){
				int angle = Utils.getHeadingDifference(absoluteWindDirection-45, desiredHeading);
				angle = Math.abs(angle);
				distOnLeft = maxDistOnSide * Math.cos(Math.toRadians(angle));
				distOnRight = maxDistOnSide * Math.sin(Math.toRadians(angle));
				//Checking which side is favorable, i. e. closer to waypoint heading.
				if(Utils.getHeadingDifference(desiredHeading, absoluteWindDirection + HOW_CLOSE)
						< Utils.getHeadingDifference(desiredHeading, absoluteWindDirection - HOW_CLOSE)){
					//If right side is favorable
					currentSide = 'R';
					targetHeading = absoluteWindDirection + HOW_CLOSE;
					if(targetHeading > 360) targetHeading -= 360;	
				}else{
					//If left side is favorable
					currentSide = 'L';
					targetHeading = absoluteWindDirection - HOW_CLOSE;
					if(targetHeading < 0) targetHeading = 360 + targetHeading;
				}
				startPoint = new Position(position.getLat(), position.getLon());
				tackingSet = true;
			}
			
			currentDistance = Utils.getDistance(startPoint, position);
			
			if(currentSide == 'R'){
				targetDistance = distOnRight - currentDistance;
			}else{
				targetDistance = distOnLeft - currentDistance;
			}
			com.sendMessage("set tdist " + targetDistance);
			
			
			//Checking if side should be changed
			if(currentSide == 'L' && currentDistance > distOnLeft){
				//Switching to right side
				currentSide = 'R';
				startPoint = new Position(position.getLat(), position.getLon());
				targetHeading = absoluteWindDirection + HOW_CLOSE;
				if(targetHeading > 360) targetHeading -= 360;
				
			}else if(currentSide == 'R' && currentDistance > distOnRight){
				//Switching to left side
				currentSide = 'L';
				startPoint = new Position(position.getLat(), position.getLon());
				targetHeading = absoluteWindDirection - HOW_CLOSE;
				if(targetHeading < 0) targetHeading = 360 + targetHeading;
			}
			//Actually adjusting rudder with 
			int adjustment = rudderController.getRequiredChange(targetHeading);
			rudderPosition = 180 + adjustment;
		}
		
		System.out.println("Position: " + position.getLat() + ", " + position.getLon());
		System.out.println("Heading: " + this.heading);
		System.out.println("Absolute wind: " + absoluteWindDirection);
		System.out.println("Relative wind: " + this.getRelativeWindDirection());
		System.out.println("Sail :" + this.sailPosition);
		System.out.println("Rudder : " + this.rudderPosition);
		System.out.println("Tacking: " + tackingSet);
		System.out.println("Target heading: " + targetHeading);
		System.out.println("Target distance: " + targetDistance);
		System.out.println("Desired heading: " + desiredHeading);
		
		//Sending commands to actuators.
		this.updateRudder();
		this.updateSail();
	}
	
	public void keepStation(){
		
		//Four indices of the box in which boat must stay.
		Position[] box = new Position[4];
		for(int i = 0; i < 4; i++) box[i] = this.waypoints.get(i);
		
		//Calculating coordinates of the point in centre of the box.
		double centreLat = (box[0].getLat() + box[3].getLat()) / 2.0;
		double centreLon = (box[0].getLon() + box[1].getLon()) / 2.0;
		Position centre = new Position(centreLat, centreLon);
		
		long currentTime = System.currentTimeMillis() / 1000L;
		long timeWhenEnteredBox = 0, timeWhenReachedMiddle = 0;
		
		//PART 1. Go to the middle of the box.
		double distanceToMiddle;
		int desiredHeading = (int) Utils.getHeading(position, centre);
		int enteringHeading = desiredHeading;
		
		while(true){
			System.out.println("Entering heading : " + enteringHeading);
			sailTowards(desiredHeading);
			distanceToMiddle = Utils.getDistance(position, centre);
			if(timeWhenEnteredBox==0){
				if(Utils.areInOrder(box[0].getLat(),position.getLat(), box[3].getLat()) &&
						Utils.areInOrder(box[0].getLon(), position.getLon(), box[1].getLon())){
					//ENTERED THE BOX
					timeWhenEnteredBox = System.currentTimeMillis() / 1000L;
					enteringHeading = desiredHeading;
				}
			}
			if(distanceToMiddle < Waypoints.WP_REACHED_THRESHOLD){
				//REACHED THE MIDDLE
				timeWhenReachedMiddle = System.currentTimeMillis() / 1000L;
				//TODO that will not be accurate (10 meters threshold!)
				break;
			}
			desiredHeading = (int) Utils.getHeading(position, centre);
			try{
				Thread.sleep(300);
			}catch(InterruptedException ex){
				ex.printStackTrace();
			}
		}
		
		//At that time the boat should start moving out of the box.
		long boxLeavingTime = timeWhenEnteredBox + BOX_TIME - (timeWhenReachedMiddle - timeWhenEnteredBox);
		
		//PART 2 : Stay in the middle for 5 minutes - time needed to get out of the box.
		desiredHeading = (int) Utils.getHeading(position, centre);
		while(true){
			System.out.println("Entering heading : " + enteringHeading);
			sailTowards(desiredHeading);
			desiredHeading = (int) Utils.getHeading(position, centre);
			if((System.currentTimeMillis() / 1000L) >= boxLeavingTime){
				//TIME TO LEAVE THE BOX
				break;
			}
			try{
				Thread.sleep(300);
			}catch(InterruptedException ex){
				ex.printStackTrace();
			}
		}
		
		//PART 3 : Get the hell out!
		desiredHeading = enteringHeading;
		while(true){
			System.out.println("Entering heading : " + enteringHeading);
			sailTowards(desiredHeading);
			//TODO maybe stop when out of the box?
			try{
				Thread.sleep(300);
			}catch(InterruptedException ex){
				ex.printStackTrace();
			}
		}
	}
	
	
	public void readSensors() throws IOException{
		
		// Getting GPS location
		com.sendMessage("get easting");
		double easting = Double.parseDouble(com.readMessage());
		
		com.sendMessage("get northing");
		double northing = Math.abs(Double.parseDouble(com.readMessage()));

		position.set(easting, northing);
		
		//Getting compass and wind sensors readings
		com.sendMessage("get compass");
		heading = Integer.parseInt(com.readMessage());

		//Python code sends absolute wind direction.
		com.sendMessage("get wind_dir");
		absoluteWindDirection = Integer.parseInt(com.readMessage()); //- heading;
		//if(absoluteWindDirection < 0) absoluteWindDirection = 360 + absoluteWindDirection;
		
	}


	/**
	 * Tells the Python code to set rudder to given position.
	 * 
	 * @param position
	 */
	public void updateRudder(){
			com.sendMessage("set rudder " + rudderPosition);
	}
	
	/**
	 * Calculates how tense the sail should be and tells the Python program to set it.
	 */
	public void updateSail(){
        //long currentTime = System.currentTimeMillis() / 1000L;
        //if (currentTime - sailLastUpdated > 10) {
        //    sailLastUpdated = currentTime;
            int relativeWind = this.getRelativeWindDirection();

            //quick fix for the upside - down wind problem
            relativeWind +=180;
            if(relativeWind > 360) relativeWind -= 360;
            
            // Shamelessly stolen from Colin (for now) (yeah, for now lol)
            if(relativeWind < 180){
                if(relativeWind < 70)
                    sailPosition = 0;
                else if(relativeWind < 80)
                    sailPosition = 18;
                else if(relativeWind < 90)
                    sailPosition = 36;
                else if(relativeWind < 110)
                    sailPosition = 54;
                else
                    sailPosition = 72;
            } else{
                if(relativeWind >= 290)
                    sailPosition = 0;
                else if(relativeWind >= 280)
                    sailPosition = 18; //342
                else if(relativeWind >= 270)
                    sailPosition = 36; //324
                else if(relativeWind >= 250)
                    sailPosition = 54; //306
                else
                    sailPosition = 72; //288
            }
            System.out.println("I am setting the sail to " + sailPosition);
            com.sendMessage("set sail " + sailPosition);
        //}
	}


	/**
	 * Returns wind direction relative to the boat.
	 * @return
	 */
	public int getRelativeWindDirection(){
		int dir = this.absoluteWindDirection - this.heading;
		if(dir < 0) dir = 360 + dir;
		return dir; //TODO Make sure that is always correct?
	}
	
	public int getHeading(){
		return heading;
	}

	public Position getPosition(){
		return position;
	}

	public Position getNextWayPoint(){
		return waypoints.getNextWaypoint();
	}
}
