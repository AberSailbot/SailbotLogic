package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.LinkedList;

import boat.Boat;

/**
 * @author David Capper <dmc2@aber.ac.uk>
 * @author Kamil Mrowiec <kam20@aber.ac.uk>
 * @version 1.0 (4 May 2013)
 */
public class Communication{
	
	public static final String HEADING = "compass";
	public static final String LATITUDE = "northing";
	public static final String LONGITUDE = "easting";
	public static final String ABSOLUTE_WIND = "wind_dir";
	
	public static final String WAYPOINTS = "waypoints";
	public static final String WAYPOINT = "waypoint";
	public static final String OPERATION_MODE = "mode";
	public static final String OBSTACLES = "obstacles";
	
	private Socket socket;
	private PrintWriter transmit;
	private BufferedReader receive;

	public Communication(){
		
		try{
			socket = new Socket("localhost", 5555);
			transmit = new PrintWriter(socket.getOutputStream(), true);
			receive = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			socket.setSoTimeout(60); //TODO Is that reasonable?
		}catch(UnknownHostException | SocketException ex){
			System.out.println("Error : Cannot initialize socket connection.");
			System.exit(0);
		}catch(IOException ex){
			ex.printStackTrace();
			System.exit(0);
		}
	}

	public void requestData(String data){
		transmit.println("get " + data);
		transmit.flush();
		this.readMessage();
	}
	
	public void sendMessage(String message){
		transmit.println(message);
		transmit.flush();
		//System.out.println("Sent message : " + message);
	}

	public void readMessage(){
		try{
			String message = "";
			message = receive.readLine();
			if(message != null){
				decodeMessageAndTakeActions(message);
				readMessage(); //Reading again to make sure that no message is missed
			}
			
		}catch(SocketTimeoutException ex){
			return;
		}catch(IOException ex){
			ex.printStackTrace();
		}
		
	}
	
	private void decodeMessageAndTakeActions(String message){
		String[] parts = message.split(" ");
		if(parts.length > 2){
			if("set".equals(parts[0])){
				
				switch(parts[1]){
				case HEADING:
					Boat.getInstance().setHeading(Integer.parseInt(parts[2]));
					break;
				case LATITUDE:
					Boat.getInstance().getPosition().setLat(Double.parseDouble(parts[2]));
					break;
				case LONGITUDE:
					Boat.getInstance().getPosition().setLon(Double.parseDouble(parts[2]));
					break;
				case ABSOLUTE_WIND:
					Boat.getInstance().setAbsoluteWindDirection(Integer.parseInt(parts[2]));
					break;
				case WAYPOINTS:
					this.updateWaypoints(message);
					break;
				case WAYPOINT:
					Boat.getInstance().getWaypoints().setNextWaypointNumber(Integer.parseInt(parts[2]));
					break;
				case OPERATION_MODE:
					Boat.createBoat(parts[2]);
					break;
				case OBSTACLES:
					this.updateObstacles(parts[2]);
					break;
				default:
					System.out.println("Unknown set parameter " + parts[2]);
				}
				
			}else{
				System.out.println("Unknown message " + message);
			}
		}
	}
	
	/**
	 * Changes waypoints in the system.
	 * Waypoint string must be of format:
	 * "lat1;lon1 lat2;lon2 lat3;lon3" etc.
	 * 
	 * @param wps
	 */
	private void updateWaypoints(String wps){
		LinkedList<Position> points = new LinkedList<Position>();
		wps.replace("set waypoints ", ""); //remove beginning of the message
		String[] tokens = wps.split(" ");
		System.out.println("Received " + tokens.length + " wayponts!");
		for(String token : tokens){
			String[] waypoint = token.split(";");
			if(waypoint !=null && waypoint.length == 2){
				points.add(new Position(Double.parseDouble(waypoint[0]), Double.parseDouble(waypoint[1])));
			}
		}
		Boat.getInstance().setWaypoints(new Waypoints(points));
		
	}
	
	/**
	 * Parses obstacle data from string and updates Boat variable.
	 * @param obs data string
	 */
	private void updateObstacles(String obs){
		LinkedList<Obstacle> obstacles = new LinkedList<Obstacle>();
		String[] tokens = obs.split(" ");
		for(String token : tokens){
			String[] data = token.split(";");
			if(data.length == 4){
				obstacles.add(new Obstacle(
						new Position(data[0], data[1]),
						Float.parseFloat(data[2]),
						Integer.parseInt(data[3])));
			}
		}
		Boat.getInstance().setObstacles(obstacles);
	}
	
	public void clean(){
		try{
			socket.close();
		}catch(Exception e){
		}
	}

	public boolean isAlive(){
		return socket.isConnected();
	}

}
