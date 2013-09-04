package boat;

import main.Obstacle;

/**
 * @author David Capper <dmc2@aber.ac.uk>
 * @version 0.1 (20 August 2013)
 */

public class PredatorBoat extends Boat {

    Obstacle target;

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

    @Override
    public void sail(){
        while (true ){


            keepHeading(0);

            this.updateRudder();
            this.updateSail();

            try{
                Thread.sleep(300);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
