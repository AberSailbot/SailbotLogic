/**
 * Created with IntelliJ IDEA.
 * User: thip
 * Date: 17/04/2013
 * Time: 11:29
 */
public interface BoatBehavior
{
    void runOn ( Boat boat );

    boolean needsToChange ();
    BoatBehavior newBehavior();
}
