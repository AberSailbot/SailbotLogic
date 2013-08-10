package boat;

import java.io.IOException;

import utils.Utils;

import main.Position;
import main.Waypoints;

/**
 * @author Kamil Mrowiec <kam20@aber.ac.uk>
 * @version 1.0 (16 Jun 2013)
 */
public class StationKeepingBoat extends Boat{

	/**
	 * How long should the boat stay inside the box (for station keeping)
	 * (in seconds, originally 5 minutes - 300 seconds);
	 */
	public static int BOX_TIME = 300;
	
	public StationKeepingBoat(){
		super();
	}

	@Override
	public void sail(){
		// Four indices of the box in which boat must stay.
		Position[] box = new Position[4];
		for(int i = 0; i < 4; i++)
			box[i] = this.waypoints.get(i);

		// Calculating coordinates of the point in centre of the box.
		double centreLat = (box[0].getLat() + box[3].getLat()) / 2.0;
		double centreLon = (box[0].getLon() + box[1].getLon()) / 2.0;
		Position centre = new Position(centreLat, centreLon);

		long timeWhenEnteredBox = 0, timeWhenReachedMiddle = 0;

		// PART 1. Go to the middle of the box.
		double distanceToMiddle;
		int desiredHeading = (int) Utils.getHeading(position, centre);
		int enteringHeading = desiredHeading;

		while(true){
			System.out.println("Entering heading : " + enteringHeading);
			try{
				readSensors();
			}catch(IOException ex){
				ex.printStackTrace();
			}
			keepHeading(desiredHeading);
			distanceToMiddle = Utils.getDistance(position, centre);
			if(timeWhenEnteredBox == 0){
				if(Utils.areInOrder(box[0].getLat(), position.getLat(),
						box[3].getLat())
						&& Utils.areInOrder(box[0].getLon(), position.getLon(),
								box[1].getLon())){
					// ENTERED THE BOX
					timeWhenEnteredBox = System.currentTimeMillis() / 1000L;
					enteringHeading = desiredHeading;
				}
			}
			if(distanceToMiddle < Waypoints.WP_REACHED_THRESHOLD){
				// REACHED THE MIDDLE
				timeWhenReachedMiddle = System.currentTimeMillis() / 1000L;
				// TODO that will not be accurate (10 meters threshold!)
				break;
			}
			desiredHeading = (int) Utils.getHeading(position, centre);
			try{
				Thread.sleep(300);
			}catch(InterruptedException ex){
				ex.printStackTrace();
			}
		}

		// At that time the boat should start moving out of the box.
		long boxLeavingTime = timeWhenEnteredBox + BOX_TIME
				- (timeWhenReachedMiddle - timeWhenEnteredBox);

		// PART 2 : Stay in the middle for 5 minutes - time needed to get out of
		// the box.
		desiredHeading = (int) Utils.getHeading(position, centre);
		while(true){
			System.out.println("Entering heading : " + enteringHeading);
			try{
				readSensors();
			}catch(IOException ex){
				ex.printStackTrace();
			}
			keepHeading(desiredHeading);
			desiredHeading = (int) Utils.getHeading(position, centre);
			if((System.currentTimeMillis() / 1000L) >= boxLeavingTime){
				// TIME TO LEAVE THE BOX
				break;
			}
			try{
				Thread.sleep(300);
			}catch(InterruptedException ex){
				ex.printStackTrace();
			}
		}

		// PART 3 : Get the hell out!
		desiredHeading = enteringHeading;
		while(true){
			System.out.println("Entering heading : " + enteringHeading);
			try{
				readSensors();
			}catch(IOException ex){
				ex.printStackTrace();
			}
			keepHeading(desiredHeading);
			// TODO maybe stop when out of the box?
			try{
				Thread.sleep(300);
			}catch(InterruptedException ex){
				ex.printStackTrace();
			}
		}
	}
	
}
