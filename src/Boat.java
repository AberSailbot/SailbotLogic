import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: thip
 * Date: 17/04/2013
 * Time: 11:37
 */
public class Boat
{
    public BoatBehavior behavior;
    public Communication communication;
    public Actuation actuation;
    public Sensing sensing;
    public Route route;

    public Boat()
    {
        behavior = new PIDBehavior ();
        communication = new Communication ();
        actuation = new Actuation ();
        sensing = new Sensing ();
        route = new Route();

    }

    public void update ()
    {


        try
        {
            sensing.refresh ( this );
        } catch ( IOException e )
        {
            e.printStackTrace ();  //To change body of catch statement use File | Settings | File Templates.
        }

        System.out.println ("ping" + '\n' + sensing.toString ());


        behavior.runOn ( this );

        if ( behavior.needsToChange () )
        {
            behavior = behavior.newBehavior ();
        }



        actuation.UpdateTo ( this );

        try
        {
            Thread.sleep ( 1000);
        } catch ( InterruptedException e )
        {
            e.printStackTrace ();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
