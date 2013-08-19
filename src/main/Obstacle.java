/**
 * 
 */
package main;

/**
 * Contains obstacle data.
 * 
 * @author Kamil Mrowiec <kam20@aber.ac.uk>
 * @version 1.0 (19 Aug 2013)
 */
public class Obstacle{
	
	private Position position;
	private float speed;
	private int heading;
	
	public Obstacle(){
		
	}
	
	public Obstacle(Position position, float speed, int heading){
		super();
		this.position = position;
		this.speed = speed;
		this.heading = heading;
	}
	
	
	public Position getPosition(){
		return position;
	}
	public void setPosition(Position position){
		this.position = position;
	}
	public float getSpeed(){
		return speed;
	}
	public void setSpeed(float speed){
		this.speed = speed;
	}
	public int getHeading(){
		return heading;
	}
	public void setHeading(int heading){
		this.heading = heading;
	}
	
	
}
