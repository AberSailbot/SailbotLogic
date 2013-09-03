/**
 * 
 */
package utils;

import main.Position;

/**
 * Contains methods that can be utilized in many places in the program.
 * 
 * @author Kamil Mrowiec <kam20@aber.ac.uk>
 * @version 1.0 (25 May 2013)
 */
public class Utils{

	/**
	 * Returns difference in heading between two headings. 
	 * Positive if  heading2 is to the left of heading1, negative otherwise.
	 * 
	 * @param heading1
	 * @param heading2
	 * @return
	 */
	public static int getHeadingDifference(int heading1, int heading2){
		int result = heading1 - heading2;

		if(result < -180)
			return 360 + result;
		if(result > 180)
			return 0 - (360 - result);
		return result;
	}
	
	/**
     * Returns distance between two GPS coordinates, in meters.
     * 
     * @param a Location one
     * @param b Location two
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
    
    /**
     * Returns heading between two GPS coordinates, from a to b, in degrees.
     * @param a Location a
     * @param b Location b
     * @return heading from a, to b in degrees
     * 
     * @see http://stackoverflow.com/questions/9457988/bearing-from-one-coordinate-to-another
     * @author Kamil Mrowiec <kam20@aber.ac.uk>
     */
	public static double getHeading(Position a, Position b){
		double lat1 = Math.toRadians(a.getLat());
		double lat2 = Math.toRadians(b.getLat());
		double longDiff = Math.toRadians(b.getLon() - a.getLon());
		double y = Math.sin(longDiff) * Math.cos(lat2);
		double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
				* Math.cos(lat2) * Math.cos(longDiff);
		return (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
	}
	
	public static boolean areInOrder(Double a, Double b, Double c){
		if(b > a && c > b) return true;
		if(b < a && c < b) return true;
		return false;
	}

    public static double addHeadings(double h1, double h2)
    {
        double hr = h1 + h2;

        if (hr < 0)
        {
            hr = 360 - hr;
        } else if (hr < 360) {
            hr = hr - 360;
        }

        return hr;
    }
	
}
