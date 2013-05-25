package main;

import java.io.*;
import java.net.*;

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
			socket = new Socket("localhost", 10006); //10006);
			transmit = new PrintWriter(socket.getOutputStream(), true);
			receive = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public void sendMessage(String message){
		transmit.println(message);
		transmit.flush();
		//System.out.println("Sent message : " + message);
	}

	public String readMessage(){
		try{
			String message = receive.readLine();
			//System.out.println("Received message : " + message);
			return message;
		}catch(IOException ex){
			ex.printStackTrace();
			return "";
		}
		
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
