package main;

import java.io.IOException;

import behavior.RudderController;

/**
 * @author thip
 * @author Kamil Mrowiec <kam20@aber.ac.uk>
 * @version 1.0 (4 May 2013)
 */
public class Boat{
	
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

    

    /**
     * Creates boat with given waypoints.
     */
	public Boat(Waypoints wps){
		waypoints = wps;
		com = new Communication();
		rudderController = new RudderController(this);

        position = new Position ();
        rudderPosition = 90;
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
			com.sendMessage("set waypointdistance " + distanceToWaypoint);
			
			System.out.println("Dist. to wp : " +distanceToWaypoint + ", should be less than " +Waypoints.WP_REACHED_THRESHOLD);
			//Comparing the distance.
			if(distanceToWaypoint < Waypoints.WP_REACHED_THRESHOLD){
				System.out.println("Waypoint " + waypoints.getNextWaypointNumber() + " reached, moving to next one");
				waypoints.moveToNext();
				//Informing the Python program that waypoint has changed.
				com.sendMessage("set waypointnum " + waypoints.getNextWaypointNumber());
				com.sendMessage("set waypointnorthing " + waypoints.getNextWaypoint().getLat() );
				com.sendMessage("set waypointeasting " + waypoints.getNextWaypoint().getLon());
			}
			
			waypointHeading = waypoints.getWaypointHeading();
			
			//Printing data for testing and debugging
			System.out.println("Position: " + position.getLat() + ", " + position.getLon());
			System.out.println("Heading: " + this.heading);
			System.out.println("Absolute wind: " + absoluteWindDirection);
			System.out.println("Relative wind: " + this.getRelativeWindDirection());
			System.out.println("Sail :" + this.sailPosition);
			System.out.println("Rudder : " + this.rudderPosition);
			System.out.println("Waypoint number: " + waypoints.getNextWaypointNumber());
			System.out.println("Waypoint heading: " + waypointHeading);
			
			//Checking if course on waypoint is directly sailable. 
			if(Math.abs(Utils.getHeadingDifference(waypointHeading, absoluteWindDirection)) > 45){
				//If course to waypoint is sailable
				
				//STEP 3:
				//PID algorithm calculates rudder adjustments.
				int adjustment = rudderController.getRequiredChange(waypointHeading);
				rudderPosition += adjustment;
				     
				
			}else{
				//If course to waypoint is not sailable
				System.out.println("Wind is blowing straight on me, i'm confused :'(");
				
				try{
					Thread.sleep(3000);
				}catch(InterruptedException e){
					e.printStackTrace(); 
				}
				
			}
			
			
			
			
			updateRudder();
			this.updateSail();
			
			try{
				Thread.sleep(300);
			}catch(InterruptedException e){
				e.printStackTrace(); 
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
		absoluteWindDirection = Integer.parseInt(com.readMessage()) - heading;

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
		//TODO So that sail is only updated every n seconds
		int relativeWind = this.getRelativeWindDirection();
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
		}else{
			if(relativeWind >= 290)
				sailPosition = 0;
			else if(relativeWind >= 280)
				sailPosition = 342;
			else if(relativeWind >= 270)
				sailPosition = 324;
			else if(relativeWind >= 250)
				sailPosition = 306;
			else
				sailPosition = 288;
		}
		System.out.println("I am setting the sail to " + sailPosition);
		com.sendMessage("set sail " + sailPosition);
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
