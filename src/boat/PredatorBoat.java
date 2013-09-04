package boat;

import main.Obstacle;
import utils.Utils;

import java.io.IOException;

/**
 * @author David Capper <dmc2@aber.ac.uk>
 * @version 0.1 (20 August 2013)
 */

public class PredatorBoat extends Boat {

    Obstacle target;
    private boolean shouldSail;

    public PredatorBoat()
    {
        super();
        target = obstacles.get(0);
    }

    public PredatorBoat(Obstacle t)
    {
        super();
        this.target = t;
    }

    public void stopSailing()
    {
        shouldSail = false;
    }

    @Override
    public void sail(){

        double headingToTarget;
        double headingDifference;

        while ( shouldSail ){

            try {
                readSensors();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }


            headingToTarget = Utils.getHeading(position, target.getPosition());
            headingDifference = Utils.getHeadingDifference(heading, target.getHeading());

            if (targetDistance > 7)
            {
                if (headingDifference <= 0 )
                {
                    keepHeading((int)Utils.addHeadings(headingToTarget, 10 ));
                }  else {
                    keepHeading((int)Utils.addHeadings(headingToTarget, -10 ));
                }
            } else {
                keepHeading((int)Utils.addHeadings(headingToTarget, 180));
                stopSailing();
            }



        }
    }
}
