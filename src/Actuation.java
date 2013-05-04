import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: thip
 * Date: 17/04/2013
 * Time: 11:28
 */
public class Actuation
{


    private int sailTension;


    private int rudderPosition;


    public void setSailTension ( int sailTension )
    {
        this.sailTension = sailTension;
    }

    public void setRudderPosition ( int rudderPosition )
    {

        this.rudderPosition = rudderPosition;
    }

    public void UpdateTo (Boat boat)
    {
        try
        {
            boat.com.sendOneWayMessage ( "set rudder " + rudderPosition );
            boat.com.sendOneWayMessage ( "set sail " + sailTension );
        } catch ( IOException e )
        {
            e.printStackTrace ();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
