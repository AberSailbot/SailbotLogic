/**
 * 
 */
package test;

import main.Boat;

/**
 * @author Kamil Mrowiec <kam20@aber.ac.uk>
 * @version 1.0 (21 May 2013)
 */
public class TestSail{

	/**
	 * @param args
	 */
	public static void main(String[] args){
		Boat boat = new Boat();

		try{

			System.out.println("Setting sail to 0 degrees.");
			boat.com.sendMessage("set sail 0");
			Thread.sleep(5000);
			for(int i = 10; i <= 360; i += 10){
				System.out.print("Setting sail to " +i+" degrees.");
				boat.com.sendMessage("set sail " + i);
				Thread.sleep(1000);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
