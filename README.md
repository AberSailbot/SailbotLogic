SailbotLogic
================

Brain of the robot, responsible for keeping track of checkpoints and calculating desired sail and rudder positions. Works with both real boat and tracksailAI simulator. 

SailbotLogic is the highest level layer of boat controller. It does not access the hardware directly, instead it communicates with another program over TCP socket (port 5555). The underlying program sends sensors readings (like GPS position, wind direction and boat heading) and receives desired sail and rudder positions. Communication protocol between those two can be found in wiki pages.


###Compiling###

SailbotLogic can be compiled using Apache Ant. Java 1.6 or higher is required.
To compile, simply run ant in the main directory.

>ant

###Running###

To start the program, go to bin directory and run main.Main class, with waypoints file name as a parameter.
Before starting SailbotManager, ensure that either tracksailAI simulator or Python emulator is running.

>cd bin

>java main.Main waypoints.txt


