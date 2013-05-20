import static java.lang.Math.*;

/**
 * @author thip
 * @author Kamil Mrowiec <kam20@aber.ac.uk>
 * @version 1.0 (4 May 2013)
 */
public class Position{
	private double lon;
	private double lat;
	
	public Position(double lat, double lon){
		this.lat = lat;
		this.lon = lon;
	}
	
	public Position(){
		
	}
	

	public static double getHeadingBetween(Position a, Position b){
		double deltaLon = b.getLon() - a.getLon();

		double y = Math.sin(deltaLon) * Math.cos(b.getLat());
		double x = Math.cos(a.getLat()) * Math.sin(b.getLat())
				- Math.sin(a.getLat()) * Math.cos(b.getLat())
				* Math.cos(deltaLon);
		double hdg = (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;

		return hdg;
	}

    public static double getDistanceBetween(Position a, Position b)
    {
		return 2 * asin(sqrt(pow((sin((a.getLat() - b.getLat()) / 2)), 2)
				+ cos(a.getLat()) * cos(b.getLat())
				* pow((sin((a.getLon() - b.getLon()) / 2)), 2)));
	}
    
    /**
     * Returns distance between two GPS coordinates, in meters.
     * @param a Point one
     * @param b Point two
     * @return distance in meters
     * 
     * @see http://androidsnippets.com/distance-between-two-gps-coordinates-in-meter
     * @author Kamil Mrowiec <kam20@aber.ac.uk>
     */
    public static double getDistance(Position a, Position b){
    	    double pk = (double) (180/3.14159);
    	    double lat_a = a.getLat() / pk;
    	    double lon_a = a.getLon() / pk;
    	    double lat_b = b.getLat() / pk;
    	    double lon_b = b.getLon() / pk;
    	    double t1 = Math.cos(lat_a)*Math.cos(lon_a)*Math.cos(lat_b)*Math.cos(lon_b);
    	    double t2 = Math.cos(lat_a)*Math.sin(lon_a)*Math.cos(lat_b)*Math.sin(lon_b);
    	    double t3 = Math.sin(lat_a) * Math.sin(lat_b);
    	    double tt = Math.acos(t1 + t2 + t3);
    	    return 6366000*tt;
    }

	public double getLon(){
		return lon;
	}

	public void setLon(double longitude){
		this.lon = longitude;
	}

	public double getLat(){
		return lat;
	}

	public void setLat(double latitude){
		this.lat = latitude;
	}

	public void set(double longitude, double latitude){
		setLon(longitude);
		setLat(latitude);
	}
}
