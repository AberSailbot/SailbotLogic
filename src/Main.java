import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author thip
 * @author Kamil Mrowiec <kam20@aber.ac.uk>
 * @version 1.01 (21 May 2013)
 */
public class Main{

	public static void main(String[] args){

		String waypointsFile;
		if(args[0]==null){
			System.out.println("Waypoints file not specified, using default : waypoints.txt");
			waypointsFile = "waypoints.txt";
		}else waypointsFile = args[0];
		
		File wpFile = new File(waypointsFile);
		Waypoints wps = new Waypoints();
		
		try{
			wps.readFromFile(wpFile);
		}catch(FileNotFoundException ex){
			System.out.println("Could not load waypoints : File not found.");
			return;
		}catch(IOException ex){
			System.out.println("Could not load waypoints : Error while reading file.");
			ex.printStackTrace();
			return;
		}
		
		Boat boat = new Boat(wps);

		for(;;){
			try{
				boat.update();
				Thread.sleep(100);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
}
