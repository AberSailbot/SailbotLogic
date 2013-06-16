package boat;

import java.io.IOException;

import main.Communication;
import main.Position;
import main.Utils;
import main.Waypoints;


/**
 * @author thip
 * @author Kamil Mrowiec <kam20@aber.ac.uk> 
 * @version 1.0 (4 May 2013)
 */
public abstract class Boat{

	private static Boat instance;
	
	/**
	 * How close can we sail to the wind.
	 */
	public static final int HOW_CLOSE = 45;
	
	/**
	 * For tacking, specifies straight line distance (to desired heading),
	 * covered in single tack cycle (in meters).
	 */
	public static final double TACK_DISTANCE = 20.0;
	//TODO Make this distance proportional to waypoint distance?
	
	/**
	 * How long should the boat stay inside the box (for station keeping)
	 * (in seconds, originally 5 minutes - 300 seconds);
	 */
	public static final int BOX_TIME = 300;
	
	
	public Waypoints waypoints;
	public Communication com;
	private RudderController rudderController;
	
	/*
	 * Current sensor data. Need to be updated (using readSensors()) 
	 * in every loop, before adjustHeading() is called
	 */
	protected int heading;
	protected Position position;
	protected int absoluteWindDirection; 
	
	
	/*
	 * Sail and rudder position that are to be sent to actuators.
	 */
	private int rudderPosition;
	private int sailPosition;

    private long sailLastUpdated;

	/*
	 * Those variables are used to maintain tacking.
	 */
	private double distOnLeft, distOnRight;
	private char currentSide = 'L'; //Left or right
	private boolean tackingSet = false;
	private Position startPoint;
	private int targetHeading;
	double currentDistance, targetDistance;
	
	
	
	public abstract void sail();
	
	
	public static Boat getInstance(){
		return instance;
	}
	
	public static void createBoat(String type, Waypoints wps){
		if(type.equals("RaceBoat")) 
			instance = new RaceBoat(wps);
		else if(type.equals("StationKeepingBoat")) 
			instance = new StationKeepingBoat(wps);
	}
	
	public static void createBoat(String type){
		if(type.equalsIgnoreCase("RaceBoat")) 
			instance = new RaceBoat();
		else if(type.equalsIgnoreCase("StationKeepingBoat")) 
			instance = new StationKeepingBoat();
	}
	
    /**
     * Creates boat with given waypoints.
     */
	protected Boat(Waypoints wps){
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
	protected Boat(){
		waypoints = new Waypoints();
		com = new Communication();

        position = new Position ();
	}

	/**
	 * Calculates sail and rudder positions required to keep the boat on desired heading.
	 * This method sends the actual commands to sail winch and rudder servo.
	 * Must be called in a loop (with timer, not continuously), after reading sensors.
	 * 
	 * @param desiredHeading
	 */
	public void adjustHeading(int desiredHeading){
		//Checking if desired course is directly sailable. 
		if(Math.abs(Utils.getHeadingDifference(desiredHeading, absoluteWindDirection)) > HOW_CLOSE){
			//If course to waypoint is sailable
			tackingSet = false;
			targetHeading = desiredHeading;
			int adjustment = rudderController.getRequiredChange(desiredHeading);
			rudderPosition = 180 + adjustment;
		}else{
			//If course to waypoint is not directly sailable
					
			//If just started going towards the wind, tacking needs to be set up.
			if(!tackingSet){
				int angle = Utils.getHeadingDifference(absoluteWindDirection-45, desiredHeading);
				angle = Math.abs(angle);
				distOnLeft = TACK_DISTANCE * Math.cos(Math.toRadians(angle));
				distOnRight = TACK_DISTANCE * Math.sin(Math.toRadians(angle));
				//Checking which side is favorable, i. e. closer to desired heading.
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
			//Actually adjusting rudder 
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

		updateRudder();
		updateSail();
		
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
