/**
 * Created with IntelliJ IDEA. User: thip Date: 17/04/2013 Time: 20:14
 */
public class PIDBehavior implements BoatBehavior{

	int windDirection;
	int heading;
	int desiredHeading;

	int sailPosition;
	double rudder;

	@Override
	public void runOn(Boat boat){
		windDirection = boat.getWindDirection();
		heading = boat.getHeading();
		desiredHeading = boat.getWaypointHeading();

		setSail();
		setRudder();

		boat.actuation.setSailTension(sailPosition);
		boat.actuation.setRudderPosition((int) Math.round(rudder));

		System.out.println(" I'm pidding ");

	}

	@Override
	public boolean needsToChange(){
		if(45 > Math.abs(getHeadingDifference(desiredHeading, 270))){
			return true;
		}
		return false; 
	}

	@Override
	public BoatBehavior newBehavior(){
		return new TackBehavior(); 
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

		System.out.println("I am setting the sail to " + sailPosition + '\n');
	}

	double PIDcal(int setpoint, int actual_position){

		double epsilon = 0.01;
		double dt = 0.01; // 100ms loop time
		double MAX = 90; // For Current Saturation
		double MIN = -90;
		double Kp = 0.1;
		double Kd = 0.01;
		double Ki = 0.005;

		double pre_error = 0;
		double integral = 0;
		double error;
		double derivative;
		double output;

		// Caculate P,I,D
		error = setpoint - actual_position;

		// In case of error too small then stop intergration
		if(Math.abs(error) > epsilon){
			integral = integral + error * dt;
		}
		derivative = (error - pre_error) / dt;
		output = Kp * error + Ki * integral + Kd * derivative;

		// Saturation Filter
		if(output > MAX){
			output = MAX;
		}else if(output < MIN){
			output = MIN;
		}
		// Update error
		pre_error = error;

		return output;
	}

	void setRudder(){
		rudder = PIDcal(0, getHeadingDifference(desiredHeading, heading));

		System.out.println("I'm setting the rudder to " + rudder + '\n');

		if(rudder < 0){
			rudder = (-1 * rudder);
		}else{
			rudder = (360 - rudder);
		}
	}

	int getHeadingDifference(int heading1, int heading2){
		int result = heading1 - heading2;

		if(result < -180)
			return 360 + result;
		if(result > 180)
			return 0 - (360 - result);
		return result;
	}
}
