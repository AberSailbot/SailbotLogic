import java.io.IOException;

/**
 * @author thip
 * @author Kamil Mrowiec <kam20@aber.ac.uk>
 * @version 1.0 (4 May 2013)
 */
public class Boat{
	public BoatBehavior behavior;
	public Communication com;

	private Position position;
	private int heading;
	private int windDirection;
	private int waypointHeading;
	private Position nextWayPoint;

	private int sailTension;
	private int rudderPosition;

	public Boat(){
		behavior = new PIDBehavior();
		com = new Communication();
		position = new Position();
		nextWayPoint = new Position();
	}

	public void update(){

		try{
			readSensors();
		}catch(IOException e){
			e.printStackTrace();
		}

		behavior.applyBehavior(this);
		behavior = behavior.nextBehavior();
	
		this.updateRudder(rudderPosition);
		this.updateSail(sailTension);

		try{
			Thread.sleep(1000);
		}catch(InterruptedException e){
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		}
	}

	public void readSensors() throws IOException{

		com.sendRequest("get compass");
		heading = Integer.parseInt(com.readMessage());

		com.sendRequest("get wind_dir");
		windDirection = Integer.parseInt(com.readMessage());

		//com.sendRequest("get waypointdir");
		//waypointHeading = Integer.parseInt(com.readMessage());
		waypointHeading = 35;
		
		//com.sendRequest("get3556 waypointnum");
		//int wayPointNumber = Integer.parseInt(com.readMessage());

		//com.sendRequest("get waypointnorthing " + wayPointNumber);
		nextWayPoint
				.setLatitude(Double.parseDouble("52.892335"));

		//com.sendRequest("get waypointeasting " + wayPointNumber);
		nextWayPoint
				.setLongitude(Double.parseDouble("-4.101105"));

		com.sendRequest("get easting");
		double easting = Double.parseDouble(com.readMessage()) / 10000000 * 10 + 100;

		com.sendRequest("get northing");
		double northing = Double.parseDouble(com.readMessage()) / 10000000 * 10 + 100;

		position.set(easting, northing);
	}

	public void updateRudder(int position){

		this.rudderPosition = position;

		try{
			com.sendMessage("set rudder " + rudderPosition);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void updateSail(int tension){

		this.sailTension = tension;
		try{
			com.sendMessage("set sail " + sailTension);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public Position getPosition(){
		return position;
	}

	public void setPosition(Position position){
		this.position = position;
	}

	public int getHeading(){
		return heading;
	}

	public void setHeading(int heading){
		this.heading = heading;
	}

	public int getWindDirection(){
		return windDirection;
	}

	public void setWindDirection(int windDirection){
		this.windDirection = windDirection;
	}

	public int getWaypointHeading(){
		return waypointHeading;
	}

	public void setWaypointHeading(int waypointHeading){
		this.waypointHeading = waypointHeading;
	}

	public Position getNextWayPoint(){
		return nextWayPoint;
	}

	public void setNextWayPoint(Position nextWayPoint){
		this.nextWayPoint = nextWayPoint;
	}
}
