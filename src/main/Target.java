package main;

/**
 * @author David Capper <dmc2@aber.ac.uk>
 * @version 0.0 (20 August 2013)
 */

public class Target {
    int heading;
    int velocity;
    Position position;

    public Target()
    {
        heading = 0;
        velocity = 0;
        position = new Position(0,0);
    }

    int getHeading(){return this.heading;}
    int getVelocity(){return this.velocity;}
    Position getPosition(){return this.position;}

    public void updateState(){}
}
