package boat;

import main.Position;
import main.Target;

/**
 * @author David Capper <dmc2@aber.ac.uk>
 * @version 0.2 (20 August 2013)
 */

public class PreyBoat extends Boat {

    Target target;

    public PreyBoat()
    {
        super();
    }

    public PreyBoat(Target t)
    {
        super();
        this.target = t;
    }

    @Override
    public void sail()
    {
        int desiredHeading;

        //get the position of the other boat
        target.updateState();
        //work out where it is relative to me
        double targetDistance = Position.getDistanceBetween(getPosition(), target.getPosition());
        double targetHeading = Position.getHeadingBetween(getPosition(), target.getPosition());
        //sail directly away if they are under a certain distance, otherwise, head up wind
        if (targetDistance < 50)
        {
            desiredHeading = (((int) Math.abs(targetHeading) ) + 180) % 380;
            keepHeading( desiredHeading );
        } else {
             desiredHeading = (getAbsoluteWindDirection() + 180) % 360;
            keepHeading( desiredHeading );
        }
    }
}
