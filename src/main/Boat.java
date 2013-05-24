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
	private int windDirection; // Relative to the boat!
	private Position position;
	private double distanceToWaypoint;
	
	private int sailTension;
	private int rudderPosition;
	

    

    /**
     * Creates boat with given waypoints.
     */
	public Boat(Waypoints wps){
		waypoints = wps;
		behavior = new PIDBehavior(this);
		com = new Communication();

        position = new Position ();
        
        try{
        	//Informing the Python program that waypoint has changed.
        	com.sendMessage("set waypointnum " + waypoints.getNextWaypointNumber());
        	com.sendMessage("set waypointnorthing " + waypoints.getNextWaypoint().getLat() );
        	com.sendMessage("set waypointeasting " + waypoints.getNextWaypoint().getLon());
        }catch(IOException ex){
        	ex.printStackTrace();
        }
	}

	/**
	 * Creates boat with no waypoints.
	 */
	public Boat(){
		waypoints = new Waypoints(this);
		behavior = new PIDBehavior(this);
		com = new Communication();

        position = new Position ();
	}

	public void update(){
		if(waypoints.isEmpty()){
			System.out.println("No waypoints to go to.");
			return;
		}
		try{
			//Get sensors reading from Python controller
			readSensors();
			
			//Reading distance to waypoint and sending it to the Python program
			distanceToWaypoint = waypoints.getDistanceToWaypoint();
			com.sendMessage("set waypointdistance " + distanceToWaypoint);
			
			//Check if waypoint is reached, if so, go to next one.
			if(distanceToWaypoint < Waypoints.WP_REACHED_THRESHOLD){
				System.out.println("Waypoint " + waypoints.getNextWaypointNumber() + " reached, moving to next one");
				waypoints.moveToNext();
				
				//Informing the Python program that waypoint has changed.
				com.sendMessage("set waypointnum " + waypoints.getNextWaypointNumber());
				com.sendMessage("set waypointnorthing " + waypoints.getNextWaypoint().getLat() );
				com.sendMessage("set waypointeasting " + waypoints.getNextWaypoint().getLon());
			}

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

		position.set(easting, northing);
		
		//Getting compass and wind sensors readings
		com.sendMessage("get compass");
		heading = Integer.parseInt(com.readMessage());

		//Python code sends absolute wind direction.
		//This code requires wind position relative to the boat,
		//so it needs to be converted here.
		com.sendMessage("get wind_dir");
		windDirection = Integer.parseInt(com.readMessage()) - heading;

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
		return (int) Position.getHeading(position, waypoints.getNextWaypoint());
	}

    public Position getPosition ()
    {
        return position;
    }

    public Position getNextWayPoint ()
    {
        return waypoints.getNextWaypoint();
    }
}
