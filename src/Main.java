

public class Main
{


    public static void main ( String[] args )
    {

        Boat boat = new Boat();

        for ( int i = 0; i < 300 ; i++)
        {
            boat.update ();
            try
            {
                Thread.sleep(1000);
            } catch ( InterruptedException e )
            {
                e.printStackTrace ();
            }
        }
    }
}
