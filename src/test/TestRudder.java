/**
 * 
 */
package test;

import main.Boat;

/**
 * @author Kamil Mrowiec <kam20@aber.ac.uk>
 * @version 1.0 (21 May 2013)
 */
public class TestRudder{

	/**
	 * @param args
	 */
	public static void main(String[] args){
		Boat boat = new Boat();
		
		for(int i = 90; i<=270; i+=10){
			try{
				System.out.print("Setting rudder to " + i + " degrees.");
				boat.com.sendMessage("set rudder " + i);
				Thread.sleep(1000);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

}
