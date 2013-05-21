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
			socket = new Socket("localhost", 5555);//10006);
			transmit = new PrintWriter(socket.getOutputStream(), true);
			receive = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));

		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public void sendRequest(String message) throws IOException{
		transmit.println(message);
	}

	public String readMessage() throws IOException{
		//System.out.print("Received message : ");
		String message = "";
		char[] messageByte = new char[1];
		receive.read(messageByte);

		
		while(messageByte[0] != '\u0000' && messageByte[0] !='$' ){
			
			message = message + messageByte[0];
			receive.read(messageByte);
		}
		//System.out.println(message);
		return message;

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

	public void sendMessage(String s) throws IOException{
		sendRequest(s);
		readMessage(); // clears the buffer;
	}
}
