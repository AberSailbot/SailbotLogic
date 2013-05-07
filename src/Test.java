/**
 * @author thip
 * @author Kamil Mrowiec <kam20@aber.ac.uk>
 * @version 1.0 (4 May 2013)
 */
public class Test{

	public static void main(String[] args){

		Boat boat = new Boat();

		for(;;){
			try{
				//boat.update();
				boat.com.sendMessage("set sail 20");
				Thread.sleep(2000);
				boat.com.sendMessage("set rudder 5");
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
