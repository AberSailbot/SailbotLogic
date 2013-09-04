package boat;

import main.Obstacle;
import main.Position;
import utils.Utils;

import java.io.IOException;

/**
 * @author David Capper <dmc2@aber.ac.uk>
 * @version 0.2 (20 August 2013)
 */

public class PreyBoat extends Boat {

    Obstacle target;
    private boolean shouldSail;
    private Position centre;
    int desiredHeading = 0;

    public PreyBoat()
    {
        super();
        shouldSail = true;
        target = obstacles.get(0);
    }

    public void stopSailing()
    {
        shouldSail = false;
    }

    public PreyBoat(Obstacle t)
    {
        super();
        this.target = t;
        shouldSail = true;
    }

    @Override
    public void sail()
    {
        double targetDistance;
        double targetHeading;

        // Four indices of the box in which boat must stay.
        Position[] box = new Position[4];
        for(int i = 0; i < 4; i++)
            box[i] = this.waypoints.get(i);

        // Calculating coordinates of the point in centre of the box.
        double centreLat = (box[0].getLat() + box[3].getLat()) / 2.0;
        double centreLon = (box[0].getLon() + box[1].getLon()) / 2.0;
        centre= new Position(centreLat, centreLon);


        while (shouldSail)
        {
            //get the position of the other boat
            try {
                readSensors();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            //work out where it is relative to me
            targetDistance = Position.getDistanceBetween(getPosition(), target.getPosition());
            targetHeading = Position.getHeadingBetween(getPosition(), target.getPosition());

            if ( tooCloseToEdge() ){

                  sailAroundEdge();

            } else {


                //work out where it is relative to me
                targetDistance = Position.getDistanceBetween(getPosition(), target.getPosition());
                targetHeading = Position.getHeadingBetween(getPosition(), target.getPosition());
                //sail directly away if they are under a certain distance, otherwise, head up wind
                if (targetDistance < 50)
                {
                    desiredHeading = (((int) Math.abs(targetHeading) ) + 180) % 380;

                } else {
                     desiredHeading = (getAbsoluteWindDirection() + 180) % 360;

                }
            }

            keepHeading( desiredHeading );
        }
    }

    private void sailAroundEdge() {

        int headingDifferenceFromCentre = Utils.getHeadingDifference( (int)Utils.getHeading(centre, position),
                                                                      (int)Utils.getHeading(centre, target.getPosition()));

        if (headingDifferenceFromCentre <= 0)
        {
            desiredHeading =  (int)Utils.addHeadings(Utils.getHeading(centre, position), 95);
        } else {
            desiredHeading =  (int)Utils.addHeadings(Utils.getHeading(centre, position), -95);
        }
    }

    private boolean tooCloseToEdge() {


        //if we are more than 75m from centre of the box return true

        if ( Position.getDistanceBetween(centre, this.position) > 150)
        {
            return true;
        } else {
            return false;
        }
    }
}

