SailbotLogic
================

Brain of the robot, responsible for keeping track of checkpoints and calculating desired sail and rudder positions. Works with both real boat and tracksailAI simulator. 

SailbotLogic is the highest level layer of boat controller. It does not access the hardware directly, instead it communicates with another program over TCP socket (port 5555). The underlying program sends sensors readings (like GPS position, wind direction and boat heading) and receives desired sail and rudder positions. Communication protocol between those two can be found in wiki pages.


###Compiling###

SailbotLogic can be compiled using Apache Ant. Java 1.6 or higher is required.
To compile, simply run ant in the main directory.

>ant

###Running###

Before starting SailbotManager, ensure that either tracksailAI simulator or Python emulator is running.
To start the code in station-keeping branch, go to bin directory and run main.Main class, with two parameters:
 1.operation mode ("station" for station keeping or "race" for just following waypoints)
 2.waypoints file name as a parameter

For station keeping, waypoints file MUST contain four waypoints - positions of the corners of the box, ordered
so the first waypoint is the top left corner (north-western one), and the rest in clockwise order.

e.g.

>cd bin

>java main.Main station waypoints.txt
