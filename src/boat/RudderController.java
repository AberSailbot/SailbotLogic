package boat;

import utils.Config;
import utils.Utils;

/**
 * @author David Capper <dmc2@aber.ac.uk>
 * @author Kamil Mrowiec <kam20@aber.ac.uk>
 * @version 1.0 (4 May 2013)
 */
public class RudderController{

	int epsilon = 2; 
	double dt = 0.3; // 300ms loop time
	double MAX = 90; // For Current Saturation
	double MIN = -90;
	double Kp = 1.3; // 1.8 for simulator, 1.3 for the boat
	double Ki = 0.05;
	double Kd = 0.01;
	
	double previousError = 0;
	int previousHeading;
	double integral = 0;
	

	public RudderController(){
		this.Kp = Config.getDouble("Kp");
		this.Ki = Config.getDouble("Ki");
		this.Kd = Config.getDouble("Kd");
		this.dt = Config.getDouble("dt");
		this.epsilon = Config.getInt("epsilon");
		this.MIN = Config.getDouble("MIN");
		this.MAX = Config.getDouble("MAX");
	}

	
	/**
	 * Calculates required rudder adjustment.
	 * @param desiredHeading
	 * @return
	 */
	public int getRequiredChange(int currentHeading, int desiredHeading){

		double error;
		double derivative;
		
		double output;
		
		//If desired heading changes, integral is reset.
		if(previousHeading!=desiredHeading){
			integral = 0;
			previousHeading = desiredHeading;
		}

		// Caculate P,I,D
		//error = desiredHeading - boat.getHeading();
		error = Utils.getHeadingDifference(currentHeading, desiredHeading);
		// In case of error too small then stop intergration
		if(Math.abs(error) > epsilon){
			integral = integral + error * dt;
		}
		
		derivative = (error - previousError) / dt;
		output = Kp * error + Ki * integral + Kd * derivative;

		// Saturation Filter
		if(output > MAX){
			output = MAX;
		}else if(output < MIN){
			output = MIN;
		}
		// Update error
		previousError = error;

		System.out.print("P: " + Kp * error);
		System.out.print(",  I: " + Ki * integral);
		System.out.println(",  D:" + Kd * derivative);
		System.out.println("PID output: " + output);
		return (int) Math.round(output);
	}

	
}
