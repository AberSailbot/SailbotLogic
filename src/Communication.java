/**
 * Created with IntelliJ IDEA.
 * User: thip
 * Date: 17/04/2013
 * Time: 11:28
 */

import java.io.*;
import java.net.*;

public class Communication
{

    Socket socket;

    PrintWriter transmit;
    BufferedReader receive;

    public Communication ()
    {
        try
        {
            socket = new Socket("localhost", 5555);
        } catch ( IOException e )
        {
            e.printStackTrace ();  //To change body of catch statement use File | Settings | File Templates.
        }
        //socket.
        try
        {
            transmit = new PrintWriter ( socket.getOutputStream (), true );
        } catch ( IOException e )
        {
            e.printStackTrace ();  //To change body of catch statement use File | Settings | File Templates.
        }
        try
        {
            receive = new BufferedReader ( new InputStreamReader ( socket.getInputStream () ) );
        } catch ( IOException e )
        {
            e.printStackTrace ();  //To change body of catch statement use File | Settings | File Templates.
        }

        //socket.connect ( );
    }

    public void sendMessage ( String message ) throws IOException
    {
        transmit.println ( message );
    }


    public String getMessage ()  throws  IOException
    {

        String message = "";
        char[]  messageByte = new char[1];
        receive.read ( messageByte );

        while (messageByte[0] != '\u0000')
        {
            message = message + messageByte[0];
            receive.read ( messageByte );
        }


        return message;


    }

    public void clean()
    {
        try { socket.close (); } catch ( Exception e) {} ;
    }

    public boolean isAlive()
    {
        return socket.isConnected ();
    }

    public void sendOneWayMessage ( String s ) throws IOException
    {
        sendMessage ( s );
        getMessage (); //clears the buffer;

    }
}
