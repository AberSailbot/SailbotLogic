/**
 * 
 */
package hardware;

import main.Position;

/**
 * @author Kamil Mrowiec <kam20@aber.ac.uk>
 *
 */
public interface BoatDriver {

	public Integer getHeading();
	public Integer getWind();
	public Position getPosition();
	
	public void setRudder(int value);
	public void setSail(int value);
	
}
