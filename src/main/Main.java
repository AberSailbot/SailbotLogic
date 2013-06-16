package main;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import boat.Boat;

/**
 * @author thip
 * @author Kamil Mrowiec <kam20@aber.ac.uk>
 * @version 1.01 (21 May 2013)
 */
public class Main{

	/**
	 * 
	 * @param args0 mode ("race" or "station")
	 * @param args1 waypoint file name
	 */
	public static void main(String[] args){

		if(args==null || args.length<2){
			System.out.println("Missing parameters!");
			System.out.println("parameter 1 : mode of operation");
			System.out.println("Those are 'race' or 'station'");
			System.out.println("parameter 2 : name of waypoints file");
		}
		
		String waypointsFile;
		if(args[1]==null){
			System.out.println("Waypoints file not specified, using default : waypoints.txt");
			waypointsFile = "waypoints.txt";
		}else waypointsFile = args[1];
		
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
		
		if(args[0].equalsIgnoreCase("race")) Boat.createBoat("RaceBoat", wps);
		else if(args[0].equalsIgnoreCase("station")) Boat.createBoat("StationKeepingBoat", wps);
		else{
			System.out.println("Incorrect mode parameter!");
			return;
		}
		
		Boat boat = Boat.getInstance();
		boat.sail();
		
	}
}
