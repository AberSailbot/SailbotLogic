/**
 * 
 */
package boat;

import java.io.IOException;

import main.Waypoints;

/**
 * @author Kamil Mrowiec <kam20@aber.ac.uk>
 * @version 1.0 (16 Jun 2013)
 */
public class RaceBoat extends Boat{

	/*
	 * Heading and distance to actual waypoint.
	 * Updated in every loop iteration.
	 */
	private double waypointDistance;
	private int waypointHeading;
	
	
	public RaceBoat(){
		super();
	}

	public RaceBoat(Waypoints wps){
		super(wps);
	}

	@Override
	public void sail(){
		if(waypoints.isEmpty()){
			System.out.println("No waypoints to go to.");
			return;
		}

		while(true){

			// STEP 1:
			// Get sensors reading from Python controller
			try{
				readSensors();
			}catch(IOException ex){
				ex.printStackTrace();
			}

			// STEP 2:
			// Check if waypoint is reached. If so, go to next one.

			// Reading distance to waypoint and sending it to the Python program
			waypointDistance = waypoints.getDistanceToWaypoint();

			System.out.println("Dist. to wp : " + waypointDistance
					+ ", should be less than " + Waypoints.WP_REACHED_THRESHOLD);
			// Comparing the distance.
			if(waypointDistance < Waypoints.WP_REACHED_THRESHOLD){
				System.out.println("Waypoint "
						+ waypoints.getNextWaypointNumber()
						+ " reached, moving to next one");
				waypoints.moveToNext();
				// Informing the Python program that waypoint has changed.
				com.sendMessage("set waypointnum "
						+ waypoints.getNextWaypointNumber());
				com.sendMessage("set waypointnorthing "
						+ waypoints.getNextWaypoint().getLat());
				com.sendMessage("set waypointeasting "
						+ waypoints.getNextWaypoint().getLon());

				waypointDistance = waypoints.getDistanceToWaypoint();
			}

			com.sendMessage("set waypointdistance " + waypointDistance);
			waypointHeading = waypoints.getWaypointHeading();
			com.sendMessage("set waypointheading " + waypointHeading);

			System.out.println("Waypoint heading : " + waypointHeading);
			
			// STEP 3:
			// Boat knows where it should go, now it just needs to go there.
			adjustHeading(waypointHeading);

			this.updateRudder();
			this.updateSail();

			try{
				Thread.sleep(300);
			}catch(InterruptedException e){
				e.printStackTrace();
			}

		}
	}

	
	
}
