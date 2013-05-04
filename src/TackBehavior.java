import java.io.IOException;

/**
 * Created with IntelliJ IDEA. User: thip Date: 17/04/2013 Time: 11:32
 */
public class TackBehavior implements BoatBehavior{

	int windDirection;
	int desiredHeading;
	int sailPosition;

	@Override
	public void runOn(Boat boat){
		windDirection = boat.getWindDirection();
		desiredHeading = boat.getWaypointHeading();
		int heading = boat.getHeading();

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

	int getHeadingDifference(int heading1, int heading2){
		int result = heading1 - heading2;

		if(result < -180)
			return 360 + result;
		if(result > 180)
			return 0 - (360 - result);
		return result;
	}

	void setSail(){

		// Shamelessly stolen from Colin (for now)

		if(windDirection < 180){
			if(windDirection < 70)
				sailPosition = 0;
			else if(windDirection < 80)
				sailPosition = 18;
			else if(windDirection < 90)
				sailPosition = 36;
			else if(windDirection < 110)
				sailPosition = 54;
			else
				sailPosition = 72;
		}else{
			if(windDirection >= 290)
				sailPosition = 0;
			else if(windDirection >= 280)
				sailPosition = 342;
			else if(windDirection >= 270)
				sailPosition = 324;
			else if(windDirection >= 250)
				sailPosition = 306;
			else
				sailPosition = 288;
		}

		// System.out.println ( "I am setting the sail to " + sailPosition +
		// '\n' );
	}

}
