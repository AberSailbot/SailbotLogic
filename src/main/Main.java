package main;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import utils.Config;
import boat.Boat;

/**
 * @author Kamil Mrowiec <kam20@aber.ac.uk>
 * @version 1.1 (10 August 2013)
 */
public class Main{

	/**
	 * 
	 * @param args0 mode ("race" or "station")
	 * @param args1 waypoint file name
	 */
	public static void main(String[] args){

		if(args==null || args.length<1){
			System.out.println("Missing parameters!");
			System.out.println("parameter 1 : mode of operation");
			System.out.println("Those are 'race' or 'station'");
			System.out.println("parameter 2 : name of waypoints file");
		}
		
		try{
			Config.init();
		}catch(IOException ex1){
			System.out.println("Cannot load config. Program will return.");
			ex1.printStackTrace();
			return;
		}
		
		if(args[0].equalsIgnoreCase("race")) Boat.createBoat("RaceBoat");
		else if(args[0].equalsIgnoreCase("station")) Boat.createBoat("StationKeepingBoat");
		else{
			System.out.println("Incorrect mode parameter!");
			return;
		}
		
		String wpFileName;
		if(args.length < 2){
			System.out.println("Waypoints file not specified. No waypoints loaded.");
		}else{
			File wpFile = new File(args[1]);
			Waypoints wps = new Waypoints();
			try{
				wps.readFromFile(wpFile);
				Boat.getInstance().setWaypoints(wps);
			}catch(FileNotFoundException ex){
				System.out.println("Could not load waypoints : File not found.");
			}catch(IOException ex){
				System.out.println("Could not load waypoints : Error while reading file.");
				ex.printStackTrace();
			}
		}
		
		Boat.getInstance().sail();
		
	}
}
