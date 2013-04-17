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
