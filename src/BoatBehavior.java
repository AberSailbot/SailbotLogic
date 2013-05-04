/**
 * @author thip
 * @author Kamil Mrowiec <kam20@aber.ac.uk>
 * @version 1.0 (4 May 2013)
 */
public abstract class BoatBehavior{
	
	protected int windDirection;
	protected int heading;
	protected int desiredHeading;
	protected int sailPosition;
	
	abstract void applyBehavior(Boat boat);
	abstract boolean needsToChange();
	abstract BoatBehavior newBehavior();
	
	protected void setSail(){
		
		// Shamelessly stolen from Colin (for now) (yeah, for now lol)
		if(windDirection < 180){
			if(windDirection < 70) 			sailPosition = 0;
			else if(windDirection < 80) 	sailPosition = 18;
			else if(windDirection < 90)		sailPosition = 36;
			else if(windDirection < 110)	sailPosition = 54;
			else							sailPosition = 72;
		}else{
			if(windDirection >= 290)		sailPosition = 0;
			else if(windDirection >= 280)	sailPosition = 342;
			else if(windDirection >= 270)	sailPosition = 324;
			else if(windDirection >= 250)	sailPosition = 306;
			else							sailPosition = 288;
		}
		System.out.println("I am setting the sail to " + sailPosition + '\n');
	}
	
	protected int getHeadingDifference(int heading1, int heading2){
		int result = heading1 - heading2;

		if(result < -180)
			return 360 + result;
		if(result > 180)
			return 0 - (360 - result);
		return result;
	}
}
