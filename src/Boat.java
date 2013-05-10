import java.io.IOException;

/**
 * @author thip
 * @author Kamil Mrowiec <kam20@aber.ac.uk>
 * @version 1.0 (4 May 2013)
 */
public class Boat{
	public BoatBehavior behavior;
	public Communication com;

	private int heading;
	private int windDirection;
	private int waypointHeading;

	private int sailTension;
	private int rudderPosition;



    private Position position;
    private Position nextWayPoint;

	public Boat(){
		behavior = new PIDBehavior(this);
		com = new Communication();

        position = new Position ();
        nextWayPoint = new Position ();
	}

	public void update(){
		try{
			readSensors();
		}catch(IOException e){
			e.printStackTrace();
		}

		behavior.applyBehavior();
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

		com.sendRequest("get waypointdir");
		waypointHeading = Integer.parseInt(com.readMessage());

        /////////////////////////

        com.sendMessage ( "get waypointnum" );
        int wayPointNumber = Integer.parseInt ( com.readMessage () );

        com.sendMessage ( "get waypointnorthing " + wayPointNumber );
        nextWayPoint.setLatitude ( Math.abs ( Double.parseDouble ( com.readMessage () )  ) );

        com.sendMessage ( "get waypointeasting " + wayPointNumber );
        nextWayPoint.setLongitude ( Double.parseDouble ( com.readMessage () ) );

        ///////////////////

        com.sendMessage ( "get easting" );
        double easting  = Double.parseDouble ( com.readMessage ());

        com.sendMessage ( "get northing" );
        double northing  = Math.abs(Double.parseDouble ( com.readMessage ()));

        position.set ( easting, northing );
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

	public int getHeading(){
		return heading;
	}

	public int getWindDirection(){
		return windDirection;
	}

	public int getWaypointHeading(){
		return waypointHeading;
	}

    public Position getPosition ()
    {
        return position;
    }

    public Position getNextWayPoint ()
    {
        return nextWayPoint;
    }
}
