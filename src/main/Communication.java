package main;

import java.io.*;
import java.net.*;

import boat.Boat;

/**
 * @author thip
 * @author Kamil Mrowiec <kam20@aber.ac.uk>
 * @version 1.0 (4 May 2013)
 */
public class Communication{

	Socket socket;
	PrintWriter transmit;
	BufferedReader receive;

	public Communication(){
		
			try{
				socket = new Socket("localhost", 5555);
				transmit = new PrintWriter(socket.getOutputStream(), true);
				receive = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				socket.setSoTimeout(100); //TODO Is that reasonable?
			}catch(UnknownHostException | SocketException ex){
				System.out.println("Error : Cannot initialize socket connection.");
				System.exit(0);
			}catch(IOException ex){
				ex.printStackTrace();
				System.exit(0);
			}
	}

	public void sendMessage(String message){
		transmit.println(message);
		transmit.flush();
		//System.out.println("Sent message : " + message);
	}

	public String readMessage(){
		try{
			String message = "";
			while(true){
				message = receive.readLine();
				if(message == null) return "";
				//System.out.println("Received message : " + message);
				String[] parts = message.split(" ");
				if(parts.length > 2){
					if("set".equals(parts[0]))
					{
						if("waypoints".equals(parts[1])){
							this.changeWaypoints(parts[2]);
						}else if("mode".equals(parts[1])){
							this.changeMode(parts[2]);
						}
					}
				}else break;
			}
			return message;
		}catch(SocketTimeoutException ex){
			return "";
		}catch(IOException ex){
			ex.printStackTrace();
			return "";
		}
		
	}
	
	private void changeWaypoints(String wps){
	}
	
	private void changeMode(String mode){
		Boat.createBoat(mode);
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
