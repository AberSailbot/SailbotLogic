/**
 * 
 */
package test;

import hardware.BoatdDriver;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Kamil Mrowiec <kam20@aber.ac.uk>
 *
 */
public class BoatdIntegrationTest {

	BoatdDriver driver;
	
	@Before
	public void prepareDriver(){
		driver = new BoatdDriver();
	}
	
	@Test
	public void testGettingData(){
		Integer heading = driver.getHeading();
		Integer wind = driver.getWind();
		driver.getPosition();
		
		Assert.assertNotNull(heading);
		Assert.assertNotNull(wind);
	}
	
	@Test
	public void testSettingData(){
		driver.setRudder(10);
		driver.setSail(24);
	}

}