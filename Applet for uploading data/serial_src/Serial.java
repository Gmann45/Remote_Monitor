
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


public class Serial  {

	public InputStream in;
	public OutputStream out;
	boolean stillRunning = false;
	SerialListener parent;
	private AnySerialPort serialPort = null;

	public Serial(SerialListener _parent, String _platform) {
		parent = _parent;
		if( _platform == null){  //if they did not specify
		    _platform = "rxtx" ;//guess by trying for rxtx
			try {
				Object driver = Class.forName("gnu.io.RXTXCommDriver").newInstance();
			} catch (Exception e) {
				//if it was not there use sun
			    _platform = null;
				System.out.println("Didn't see gnu.io.RXTXCommDriver so will use the standard com.sun.comm.Win32Driver instead");
			}
		}
		
		if ( (_platform == null ) || (_platform.equals("sun"))  ) {
			System.out.println("Using the com.sun.comm.Win32Driver");
			serialPort = new SunCommSerialPort(null,_parent);
		}else{
			System.out.println("Using the RXTXCommDriver");
			serialPort = new GNUSerialPort(null,_parent);
		}

		
	}
	
	
	
	public InputStream getInputStream(){
		return in;
	}
	public OutputStream getOutputStream(){
		return out;
	}
	public ArrayList getPortsList(){
	    return serialPort.getPortsList();
	}
	public String connect(String _port,int _baud){
	    String success=  serialPort.connect(_port, _baud);
	    if (success.startsWith("Got")){
	    out = serialPort.getOutputStream();
		in = serialPort.getInputStream();
	    }
	    return success;
	}

	public void kill() {
		try {
		
			if (serialPort != null) { 
					
				serialPort.close();
				serialPort = null;
							}
			if (stillRunning) {
				stillRunning = false;
				if (in != null)
				in.close();
				if (out != null)  //this used to be serialPort.close
				out.close();
			}
			
		} catch (IOException e) {
			System.out.println("couldn't get streams");
		}

	}
	
	//public void SerialEvent(int _input){  bypassed
	 //   parent.gotFromSerial(_input);
	//}
	
	//public void start(){
	//    out = serialPort.getOutputStream();
	//	in = serialPort.getInputStream();
	//}
	/*public void run() {
		try { //we might have to catch some errors later.
			System.out.println("Started Listening");
			out = serialPort.getOutputStream();
				in = serialPort.getInputStream();
				stillRunning = true;
			while (stillRunning) { //keep listening for lines
				//Thread.sleep(10);
				//if (in.available()>0){
			   // System.out.println("before input");
				int input = in.read();
				//System.out.println("after input" + input);
				parent.relayToSocket(input);
				//}
			} //end while listening for more lines
			//}
		//}catch (InterruptedException e){System.out.println( "Bad Sleep");
		} catch (IOException e) {
			System.out.println("I/O Error no more port? " + e);
		}
	} //end of run
	*/
	public String send(byte[] output,int _off, int _len) {
		
		try {
			if (out != null) {
				out.write(output,_off,_len);
				return "OKAY";
			}
		} catch (IOException e) {
			System.out.println("I/O Error no more port? " + e);
		}
		return "NOT OKAY";
	}
	
	public String send(byte[] output) {
		
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
	public String send(byte output) {
		
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
	public String send(int[] output) {
		try {
			if (out != null) {
				for (int i = 0; i < output.length; i++) {
					out.write(output[i]);
				}
				return "OKAY";
			} else {
				System.out.println("out is null");
			}
		} catch (IOException e) {
			System.out.println("I/O Error no more port? " + e);
		}
		return "NOT OKAY";
	}
	public String send(String output) {
		try {
			if (out != null) {
				out.write(output.getBytes());
				return "OKAY";
			} else {
				System.out.println("out is null");
			}
		} catch (IOException e) {
			System.out.println("I/O Error no more port? " + e);
		}
		return "NOT OKAY";
	}

}