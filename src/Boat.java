import java.io.IOException;

/**
 * Created with IntelliJ IDEA. User: thip Date: 17/04/2013 Time: 11:37
 */
public class Boat{
	public BoatBehavior behavior;
	public Communication com;
	public Actuation actuation;
	public Sensing sensing;
	public Route route;

	private Position position;
	private int heading;
	private int windDirection;
	private int waypointHeading;
	private Position nextWayPoint;

	public Boat(){
		behavior = new PIDBehavior();
		com = new Communication();
		actuation = new Actuation();
		sensing = new Sensing();
		route = new Route();

		position = new Position();
		nextWayPoint = new Position();
	}

	public void update(){

		try{
			readSensors();
		}catch(IOException e){
			e.printStackTrace(); 
		}

		//System.out.println("ping" + '\n' + sensing.toString());

		behavior.runOn(this);

		if(behavior.needsToChange()){
			behavior = behavior.newBehavior();
		}

		actuation.UpdateTo(this);

		try{
			Thread.sleep(1000);
		}catch(InterruptedException e){
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		}
	}

	public void readSensors() throws IOException{

		com.sendMessage("get compass");
		heading = Integer.parseInt(com.getMessage());

		com.sendMessage("get wind_dir");
		windDirection = Integer.parseInt(com.getMessage());

		com.sendMessage("get waypointdir");
		waypointHeading = Integer.parseInt(com.getMessage());

		com.sendMessage("get waypointnum");
		int wayPointNumber = Integer.parseInt(com.getMessage());

		com.sendMessage("get waypointnorthing " + wayPointNumber);
		nextWayPoint
				.setLatitude(Double.parseDouble(com.getMessage()) / 10000000 * 10 + 100);

		com.sendMessage("get waypointeasting " + wayPointNumber);
		nextWayPoint
				.setLongitude(Double.parseDouble(com.getMessage()) / 10000000 * 10 + 100);

		com.sendMessage("get easting");
		double easting = Double.parseDouble(com.getMessage()) / 10000000 * 10 + 100;

		com.sendMessage("get northing");
		double northing = Double.parseDouble(com.getMessage()) / 10000000 * 10 + 100;

		position.set(easting, northing);
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
