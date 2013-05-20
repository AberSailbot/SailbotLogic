/**
 * 
 */


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.LinkedList;

/**
 * Manages waypoints. Similar to what is used in the telemetry program.
 * @author Kamil Mrowiec <kam20@aber.ac.uk>
 * @version 1.0 (6 May 2013)
 */
public class Waypoints{
	
	/**
	 * Represents a GPS coordinate.
	 * @author Kamil Mrowiec <kam20@aber.ac.uk>
	 * @version 1.0 (20 May 2013)
	 */
	public class Coordinate{
		Double lat, lon;
		
		public Coordinate(double lat, double lon){
			this.lat = lat;
			this.lon = lon;
		}

		public Double getLat(){
			return lat;
		}

		public void setLat(Double lat){
			this.lat = lat;
		}

		public Double getLon(){
			return lon;
		}

		public void setLon(Double lon){
			this.lon = lon;
		}
		
	}
	
	
	LinkedList<Coordinate> points = new LinkedList<Coordinate>();
	int nextWaypointNumber = 0;
	
	public Coordinate getNextWaypoint(){
		if(points.size() <= nextWaypointNumber || nextWaypointNumber<0) 
			nextWaypointNumber = 0;
		return points.get(nextWaypointNumber);
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

	public LinkedList<Coordinate> getPoints(){
		return points;
	}

	public void setPoints(LinkedList<Coordinate> points){
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
	
	public void readFromFile(File file){
		try{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine())!=null){
				String[] latlon = line.split(";");
				this.points.add(new Coordinate(new Double(latlon[0]), new Double(latlon[1])));
			}
			reader.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	public void saveToFile(File file){
		try{
			file.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));

			StringBuffer sb = new StringBuffer();
			
			for(Coordinate c : this.points){
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
	
	
}
