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

    public int getWaypointHeading ()
    {
        return waypointHeading;
    }

    private int waypointHeading;

    public Sensing ()
    {
        position = new Position ();
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

        boat.communication.sendMessage ( "get easting" );
        double easting  = Double.parseDouble ( boat.communication.getMessage ());

        boat.communication.sendMessage ( "get northing" );
        double northing  = Double.parseDouble ( boat.communication.getMessage ());



        position.set ( easting, northing );



      }

    public String toString()
    {
        return "Heading: " + heading + "\n"
                + "wind: " + windDirection + "\n"
                + "lat: " + position.getLatitude () + "\n"
                + "lon: " + position.getLongitude () + "\n"
                ;
    }

}
