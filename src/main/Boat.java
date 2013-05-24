package main;

import java.io.IOException;
import behavior.*;

/**
 * @author thip
 * @author Kamil Mrowiec <kam20@aber.ac.uk>
 * @version 1.0 (4 May 2013)
 */
public class Boat{
	
	private BoatBehavior behavior;
	public Waypoints waypoints;
	public Communication com;

	private int heading;
	private int windDirection;
	private Position currentPosition;
	
	private int sailTension;
	private int rudderPosition;

    

    /**
     * Creates boat with given waypoints.
     */
	public Boat(Waypoints wps){
		waypoints = wps;
		behavior = new PIDBehavior(this);
		com = new Communication();

        currentPosition = new Position ();
	}

	/**
	 * Creates boat with no waypoints.
	 */
	public Boat(){
		waypoints = new Waypoints();
		behavior = new PIDBehavior(this);
		com = new Communication();

        currentPosition = new Position ();
	}

	public void update(){
		if(waypoints.isEmpty()){
			System.out.println("No waypoints to go to.");
			return;
		}
		try{
			//Get sensors reading from Python controller
			readSensors();

			//Check if waypoint is reached, if so, go to next one.
			if(waypoints.waypointReached(this.currentPosition)) waypoints.moveToNext();

		}catch(IOException e){
			e.printStackTrace();
		}

		behavior.applyBehavior();
		behavior = behavior.nextBehavior();

		this.updateRudder(rudderPosition);
		this.updateSail(sailTension);

		try{
			Thread.sleep(1000);
		}catch(InterruptedException e){
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		}
	}

	public void readSensors() throws IOException{
		
		// Getting GPS location
		com.sendMessage("get easting");
		double easting = Double.parseDouble(com.readMessage());
		
		com.sendMessage("get northing");
		double northing = Math.abs(Double.parseDouble(com.readMessage()));

		currentPosition.set(easting, northing);
		
		//Getting compass and wind sensors readings
		com.sendMessage("get compass");
		heading = Integer.parseInt(com.readMessage());

		com.sendMessage("get wind_dir");
		windDirection = Integer.parseInt(com.readMessage());

	}

	public void updateRudder(int position){
		this.rudderPosition = position;
		try{
			com.sendMessage("set rudder " + rudderPosition);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void updateSail(int tension){
		this.sailTension = tension;
		try{
			com.sendMessage("set sail " + sailTension);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public int getHeading(){
		return heading;
	}

	public int getWindDirection(){
		return windDirection;
	}

	public int getWaypointHeading(){
		return (int) Position.getHeading(currentPosition, waypoints.getNextWaypoint());
	}

    public Position getPosition ()
    {
        return currentPosition;
    }

    public Position getNextWayPoint ()
    {
        return waypoints.getNextWaypoint();
    }
}
