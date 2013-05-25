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

/**
 * Manages waypoints. Similar to what is used in the telemetry program.
 * @author Kamil Mrowiec <kam20@aber.ac.uk>
 * @version 1.0 (6 May 2013)
 */
public class Waypoints{
	
	/**
	 * Defines how close the boat needs to get to a waypoint.
	 */
	public static final int WP_REACHED_THRESHOLD = 10;
	
	Boat boat;
	
	LinkedList<Position> points = new LinkedList<Position>();
	int nextWaypointNumber = 0;
	
	public Waypoints(Boat boat){
		this.boat = boat;
	}
	
	public Position getNextWaypoint(){
		if(points.size() <= nextWaypointNumber || nextWaypointNumber<0) 
			nextWaypointNumber = 0;
		return points.get(nextWaypointNumber);
	}
	
	public int getWaypointHeading(){
		return (int) Utils.getHeading(boat.getPosition(), getNextWaypoint());
	}
	
	public double getDistanceToWaypoint(){
		return Utils.getDistance(boat.getPosition(), getNextWaypoint());
	}
	
	public boolean moveToNext(){
		if(points.size() < nextWaypointNumber+1){
			nextWaypointNumber++;
			return true;
		}else{
			nextWaypointNumber = 0;
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

	public Boat getBoat(){
		return boat;
	}

	public void setBoat(Boat boat){
		this.boat = boat;
	}
	
	
	
	
	
}
