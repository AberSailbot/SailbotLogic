/**
 * Created with IntelliJ IDEA.
 * User: thip
 * Date: 17/04/2013
 * Time: 11:42
 */
public class Position
{
    private double longitude;
    private double latitude;


    public static double getHeadingBetween ( Position a, Position b )
    {
        double deltaLon =  b.getLongitude () - a.getLongitude ();

        double y = Math.sin(deltaLon) * Math.cos(b.getLatitude ());
        double x = Math.cos(a.getLatitude ())*Math.sin(b.getLatitude ()) -
                Math.sin(a.getLatitude ())*Math.cos(b.getLatitude ())*Math.cos(deltaLon);
        double hdg = ( Math.toDegrees ( Math.atan2 ( y, x )   ) + 360 ) %360;

        //hdg = hdg + 180;





        return hdg;
    }


    public double getLongitude ()
    {
        return longitude;
    }

    public void setLongitude ( double longitude )
    {
        this.longitude = longitude;
    }

    public double getLatitude ()
    {
        return latitude;
    }

    public void setLatitude ( double latitude )
    {
        this.latitude = latitude;
    }

    public void set ( double longitude, double latitude )
    {
        setLongitude ( longitude );
        setLatitude ( latitude );
    }
}
