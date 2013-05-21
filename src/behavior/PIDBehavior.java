package behavior;
import main.Boat;

/**
 * @author thip
 * @author Kamil Mrowiec <kam20@aber.ac.uk>
 * @version 1.0 (4 May 2013)
 */
public class PIDBehavior extends BoatBehavior{

	double rudder;

	public PIDBehavior(Boat boat){
		super(boat);
	}

	@Override
	public void applyBehavior(){
		windDirection = boat.getWindDirection();
		heading = boat.getHeading();
		desiredHeading = boat.getWaypointHeading();

		setSail();
		setRudder();
		boat.updateRudder((int) Math.round(rudder));
		boat.updateSail(sailPosition);
		
		System.out.println(" I'm pidding ");

	}

	@Override
	public BoatBehavior nextBehavior(){
        windDirectionRelativeToWorld ();
		if(45 > Math.abs(getHeadingDifference(desiredHeading, windDirectionRelativeToWorld ())))
			return new TackBehavior(boat);
		else return this;
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

}
