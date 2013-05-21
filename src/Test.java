/**
 * @author thip
 * @author Kamil Mrowiec <kam20@aber.ac.uk>
 * @version 1.0 (4 May 2013)
 */
public class Test{

	public static void main(String[] args){

		Position a = new Position(52.41279272900908,-4.093136787414551);
		Position b = new Position(52.41279272900908,-4.093501567840576);
		System.out.println("Distance : " + Position.getDistance(a, b));
		System.out.println("Distance (earlier) : " + Position.getDistanceBetween(a, b));
		System.out.println("Heading : " +Position.getHeading(a, b) );
		
//		Boat boat = new Boat();
//
//		for(;;){
//			try{
//				//boat.update();
//				boat.com.sendMessage("set sail 20");
//				Thread.sleep(2000);
//				boat.com.sendMessage("set rudder 5");
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//		}
	}
}
