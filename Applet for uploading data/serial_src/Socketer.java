
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

class Socketer  {
    int port; // we have one variable that contains the socket

    boolean stillRunning = true;

    boolean stillTakingConnections = true;

    String feedback = "Okay";

    OutputStream out;

    InputStream in;

    ServerSocket listener = null;

    Socket client;

    SerialServer parent;
    
    Thread listeningThread;

    Socketer(int _port, SerialServer _parent) {
        //this is a "constructor" method.
        //Being the namesake of the class it is called when the object is made.
        port = _port;
        parent = _parent;
        //let's take parameter variables and make it into an instance variable
        //remember that parameter variables only last for this method and we
        // want to use these again
        listeningThread = new Thread(new ListeningThread());
        listeningThread.start();
    }

    public void kill() {

        stillTakingConnections = false;
        stillRunning = false;
        if (listener != null) {
            try {
                listener.close();
            } catch (IOException e) {
                System.out.println("Couldn't close port" + port + " " + e);
            }
        }
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                System.out.println("Try to close " + e);
            }
        }
    }

    public String send(int output) {
        try {
            if (out != null) {
                out.write(output);
                return "OKAY";
            }
        } catch (IOException e) {
            System.out.println("I/O Error no more port? " + e);
        }
        return "NOT OKAY";
    }
    
//  this is an inner class, separate class that gets access to all the stuff from the other class
	public class ListeningThread implements Runnable{
	    public void run() {
	        try {
	            listener = new ServerSocket(port);
	            System.out.println("Acception Connection" + listener.getLocalPort());
	        } catch (IOException e) {
	            parent.socketStatus("Socket port " + port + " taken");
	            stillRunning = false;
	            stillTakingConnections = false;
	        }
	        while (stillTakingConnections) {
	            try { //we might have to catch some errors later.
	                parent.socketStatus("Waiting for connection on port " + port);
	                client = listener.accept(); //wait for someone to knock
	                in = client.getInputStream();
	                out = client.getOutputStream();
	                parent.socketStatus("Got Connection on port " + port + " from " + client.getInetAddress().getHostName());
	                stillRunning = true;
	                while (stillRunning) {//keep listening for lines
	                    //Thread.sleep(1);

	                    int input = in.read();
	                    if (input == -1) {
	                        stillRunning = false;
	                        parent.socketStatus("Socket Disconnected - 1");
	                    } else {
	                        parent.relayToSerial(input);
	                    }
	                }//end while listening for more lines
	                client.close();

	            } catch (IOException e) {
	                //parent.socketStatus("Socket Disconnected");
	                stillRunning = false;
	                System.out.println("I/O Error They probably left " + e);
	            }
	        }

	    }//end of run
	}//end of the inner class ListeningThread
  

}