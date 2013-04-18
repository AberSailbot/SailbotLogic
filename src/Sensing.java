import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: thip
 * Date: 17/04/2013
 * Time: 11:28
 */
public class Sensing
{
    private int heading;
    private int windDirection;
    private Position position;

    private int waypointHeading;

    private Position nextWayPoint;

    public int getWaypointHeading ()
    {
        //return waypointHeading;

        return (int)Math.round (  Position.getHeadingBetween ( position, nextWayPoint )) ;
    }



    public Sensing ()
    {
        position = new Position ();
        nextWayPoint = new Position ();
    }

    public Position getNextWayPoint ()
    {
        return nextWayPoint;
    }

    public int getHeading ()
    {
        return heading;


    }



    public int getWindDirection ()
    {
        return windDirection;
    }



    public Position getPosition ()
    {
        return position;
    }

    public void refresh ( Boat boat ) throws IOException
    {
        boat.communication.sendMessage ( "get compass" );
        heading = Integer.parseInt ( boat.communication.getMessage () );

        boat.communication.sendMessage ( "get wind_dir" );
        windDirection = Integer.parseInt ( boat.communication.getMessage () );

        boat.communication.sendMessage ( "get waypointdir" );

        //needs to be calculated onboard at some point
        waypointHeading = Integer.parseInt ( boat.communication.getMessage () );

        ///////////////////////////

        boat.communication.sendMessage ( "get waypointnum" );
        int wayPointNumber = Integer.parseInt (boat.communication.getMessage ());

        boat.communication.sendMessage ( "get waypointnorthing " + wayPointNumber );
        nextWayPoint.setLatitude ( Math.abs(Double.parseDouble ( boat.communication.getMessage () )/100000000 * 360) );

        boat.communication.sendMessage ( "get waypointeasting " + wayPointNumber );
        nextWayPoint.setLongitude ( Double.parseDouble ( boat.communication.getMessage () )/100000000 * 360 );

        ///////////////////

        boat.communication.sendMessage ( "get easting" );
        double easting  = Double.parseDouble ( boat.communication.getMessage ())/100000000  * 360;

        boat.communication.sendMessage ( "get northing" );
        double northing  = Math.abs(Double.parseDouble ( boat.communication.getMessage ())/100000000 * 360);



        position.set ( easting, northing );



      }



    public String toString()
    {
        return "Heading: " + heading + "\n"
                + "DesiredHeading: " + getWaypointHeading () + "\n"
                + "wind: " + windDirection + "\n"
                + "lat: " + position.getLatitude () + "\n"
                + "lon: " + position.getLongitude () + "\n"
                ;
    }

}
