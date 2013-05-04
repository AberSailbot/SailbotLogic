/**
 * @author thip
 * @author Kamil Mrowiec <kam20@aber.ac.uk>
 * @version 1.0 (4 May 2013)
 */
public class Main{

	public static void main(String[] args){

		Boat boat = new Boat();

		for(int i = 0; i < 300; i++){
			boat.update();
			try{
				Thread.sleep(100);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
}
