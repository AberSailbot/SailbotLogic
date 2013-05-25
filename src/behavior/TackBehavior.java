package behavior;

import java.io.IOException;

import main.Boat;
import main.Position;

/**
 * @author thip
 * @author Kamil Mrowiec <kam20@aber.ac.uk>
 * @version 1.0 (4 May 2013)
 */


import static java.lang.Math.*;

public class TackBehavior extends BoatBehavior{
/*
    private Position startPoint;
    private Position target;

    TackBehavior(Boat boat)
    {
        super(boat);
        startPoint = boat.getPosition ();
        target = boat.getNextWayPoint ();
    }

	@Override
	public void applyBehavior(){
		windDirection = boat.getWindDirection();
		desiredHeading = boat.getWaypointHeading();
		heading = boat.getHeading();

		System.out.println("tacking");
		while(5 < Math.abs(getHeadingDifference(heading, desiredHeading + 45))){

			setSail();
			boat.updateRudder(90);
			boat.updateSail(sailPosition);
			
			System.out.print("R");
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
		boat.updateRudder(180); //I believe it should be 180, not zero
		boat.updateSail(sailPosition);
		try{
			Thread.sleep(1000);
		}catch(InterruptedException e){
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		}

		System.out.print("change tack");

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
		boat.updateRudder(180);
		boat.updateSail(sailPosition);
		try{
			Thread.sleep(1000);
		}catch(InterruptedException e){
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		}

	}

	@Override
	public BoatBehavior nextBehavior(){
		if(45 < Math.abs(getHeadingDifference(desiredHeading, windDirectionRelativeToWorld ()))){
			System.out.println("leaving tack");
			return new PIDBehavior(boat);
		}else return this;
		
	}

    public double crossTrackError()
    {
         return asin ( sin ( Position.getDistanceBetween ( startPoint, target ) ) * sin ( Position.getHeadingBetween ( startPoint, boat.getPosition () ) - Position.getHeadingBetween ( startPoint, target ) ) ) ;
    }
*/
}
