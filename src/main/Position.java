package main;
import static java.lang.Math.*;

/**
 * @author David Capper <dmc2@aber.ac.uk>
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
