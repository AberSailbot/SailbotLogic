import java.io.IOException;

/**
 * @author thip
 * @author Kamil Mrowiec <kam20@aber.ac.uk>
 * @version 1.0 (4 May 2013)
 */
public class TackBehavior extends BoatBehavior{

	@Override
	public void applyBehavior(Boat boat){
		windDirection = boat.getWindDirection();
		desiredHeading = boat.getWaypointHeading();
		heading = boat.getHeading();

		System.out.println("tacking");
		while(5 < Math.abs(getHeadingDifference(heading, desiredHeading + 45))){

			setSail();
			boat.updateRudder(90);
			boat.updateSail(sailPosition);
			
			System.out.println("R");
			try{
				boat.readSensors();
			}catch(IOException e){
				e.printStackTrace(); 
			}
			heading = boat.getHeading();
			desiredHeading = boat.getWaypointHeading();
			windDirection = boat.getWindDirection();

		}

		setSail();
		boat.updateRudder(0);
		boat.updateSail(sailPosition);
		try{
			Thread.sleep(1000);
		}catch(InterruptedException e){
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		}

		System.out.println("change tack");

		while(5 < Math.abs(getHeadingDifference(heading, desiredHeading - 45))){
			setSail();
			boat.updateRudder(270);
			boat.updateSail(sailPosition);

			System.out.println("L");

			try{
				boat.readSensors();
			}catch(IOException e){
				e.printStackTrace(); // To change body of catch statement use
										// File | Settings | File Templates.
			}

			heading = boat.getHeading();
			desiredHeading = boat.getWaypointHeading();
			windDirection = boat.getWindDirection();

		}

		setSail();
		boat.updateRudder(0);
		boat.updateSail(sailPosition);
		try{
			Thread.sleep(1000);
		}catch(InterruptedException e){
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		}

	}

	@Override
	public boolean needsToChange(){

		if(45 < Math.abs(getHeadingDifference(desiredHeading, 270))){
			System.out.println("leaving tack");
			return true;
		}
		return false; // To change body of implemented methods use File |
						// Settings | File Templates.

	}

	@Override
	public BoatBehavior newBehavior(){
		return new PIDBehavior(); 
	}

}
