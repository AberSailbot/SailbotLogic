package test;

import utils.Utils;
import main.Position;

/**
 * @author thip
 * @author Kamil Mrowiec <kam20@aber.ac.uk>
 * @version 1.0 (4 May 2013)
 */
public class TestValues{

	public static void main(String[] args){

		Position a = new Position(52.41279272900908,-4.093136787414551);
		Position b = new Position(52.41279272900908,-4.093501567840576);
		System.out.println("Distance : " + Utils.getDistance(a, b));
		System.out.println("Heading : " + Utils.getHeading(a, b) );
		
		int heading = 40, desired = 340;
		System.out.println("Difference : " + Utils.getHeadingDifference(heading, desired));
		
	}
}
