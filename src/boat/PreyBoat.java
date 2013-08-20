package boat;

import main.Target;

/**
 * @author David Capper <dmc2@aber.ac.uk>
 * @version 0.1 (19 August 2013)
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

        //get the position of the other boat
        target.updateState();
        //work out where it is relative to me
        //sail directly away if they are under a certain distance, otherwise, head up wind
    }
}
