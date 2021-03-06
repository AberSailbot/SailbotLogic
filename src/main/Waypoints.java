package main;
/**
 * 
 */


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.LinkedList;

import utils.Config;
import utils.Utils;

import boat.Boat;

/**
 * Manages waypoints. Similar to what is used in the telemetry program.
 * @author Kamil Mrowiec <kam20@aber.ac.uk>
 * @version 1.0 (6 May 2013)
 */
public class Waypoints{
	
	/**
	 * Defines how close the boat needs to get to a waypoint.
	 */
	public static int WP_REACHED_THRESHOLD = 10;
	
	LinkedList<Position> points = new LinkedList<Position>();
	int nextWaypointNumber = 0;
	
	public Waypoints(){
		WP_REACHED_THRESHOLD = Config.getInt("howCloseToWaypoint");
	}
	
	public Waypoints(LinkedList<Position> points){
		this();
		this.points = points;
	}
	
	public Position getNextWaypoint(){
		if(points.size() <= nextWaypointNumber || nextWaypointNumber<0) 
			return points.getLast();
		return points.get(nextWaypointNumber);
	}
	
	public int getWaypointHeading(){
		return (int) Utils.getHeading(Boat.getInstance().getPosition(), getNextWaypoint());
	}
	
	public double getDistanceToWaypoint(){
		return Utils.getDistance(Boat.getInstance().getPosition(), getNextWaypoint());
	}
	
	public boolean moveToNext(){
		System.out.println("Increasing waypoint number");
		System.out.println("Total waypoints: " + points.size());
		if(points.size() -1 > nextWaypointNumber){
			nextWaypointNumber++;
			System.out.println("WP number now: " + nextWaypointNumber);
			return true;
		}else{
			//System.out.println("Setting WP number to 0");
			//scp nextWaypointNumber = 0;
			return false;
		}
	}
	
	public boolean goToWaypoint(int wpNumber){
		System.out.println("Changing waypoint number");
		if(wpNumber >= 0 && wpNumber < points.size()){
			nextWaypointNumber = wpNumber;
			System.out.println("WP number now: " + wpNumber);
			return true;
		}else{
			return false;
		}
	}

	public LinkedList<Position> getPoints(){
		return points;
	}

	public void setPoints(LinkedList<Position> points){
		this.points = points;
	}
	
	public void clearList(){
		this.points.clear();
	}
	
	public String getStringRepresentation(int wpNumber){
		DecimalFormat df = new DecimalFormat( "###0.0000" );
		String s = "" + wpNumber + ": " 
				+ df.format(points.get(wpNumber).getLat()) 
				+ ", " + df.format(points.get(wpNumber).getLon());
		return s;
	}
	
	public void readFromFile(File file) throws FileNotFoundException, IOException{
		
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine())!=null){
				String[] latlon = line.split(";");
				this.points.add(new Position(new Double(latlon[0]), new Double(latlon[1])));
			}
			reader.close();
	}
	
	
	public void saveToFile(File file){
		try{
			file.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));

			StringBuffer sb = new StringBuffer();
			
			for(Position c : this.points){
				sb.append(c.getLat())
				.append(';').append(c.getLon()).append('\n');
			}
			writer.write(sb.toString());
			writer.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	
	public int getNextWaypointNumber(){
		return nextWaypointNumber;
	}

	public void setNextWaypointNumber(int nextWaypointNumber){
		this.nextWaypointNumber = nextWaypointNumber;
	}

	public boolean isEmpty(){
		return points.isEmpty();
	}

	public Position get(int index){
		return points.get(index);
	}
		
	
}
