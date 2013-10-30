/*
 * Created on Jul 21, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author admin
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
import gnu.io.CommDriver;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;

public class GNUSerialPort implements  SerialPortEventListener ,AnySerialPort{

	  InputStream in;
	  OutputStream out;
	  SerialPort serialPort = null;
	  SerialListener parent;
	  
	  GNUSerialPort(String _drivername, SerialListener _parent){
	      parent = _parent;
	  	if (_drivername == null) _drivername = "gnu.io.RXTXCommDriver";
			 try {
				 CommDriver driver =(CommDriver) Class.forName(_drivername).newInstance();
				 driver.initialize();
			 } catch (Exception e) {
					 System.out.println("Didn't find "+ _drivername);
			 }
			 
	  }
	  
	
	  public OutputStream getOutputStream(){
	  	return out;
	  }
	  public void close(){
		serialPort.close();
	  }
	public InputStream getInputStream(){
		   return in;
		 }
	  public ArrayList getPortsList() {
		  ArrayList ports = new ArrayList();
		  CommPortIdentifier portId;
		
		  Enumeration portList = CommPortIdentifier.getPortIdentifiers();
		  System.out.println("Get List");
		  while (portList.hasMoreElements()) {
			  portId = (CommPortIdentifier) portList.nextElement();
			  if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				  String[] portAndOwner =
					  { portId.getName(), portId.getCurrentOwner()};
				  ports.add(portAndOwner);
				  //if (portId.getCurrentOwner().startsWith("Port currently not owned")){
				  //	ports.add(portId.getName()  );
				  //}
				  System.out.println(
					  "Serial Listing: "
						  + portId.getName()
						  + " "
						  + portId.getCurrentOwner());
			  }
		  }
		  return ports;
	  }
	  public void serialEvent(SerialPortEvent _e){
		   
		      if(_e.getEventType()==SerialPortEvent.DATA_AVAILABLE){

		          try {
	               while(in.available() > 0){
	                  byte[] buffer = new byte[in.available()];
	                  in.read(buffer);
	                  parent.gotFromSerial(buffer);
	                 }
	            } catch (IOException e) {
	                System.out.println("Trouble with the serial event handler. " + e);
	            }
		      }
		  }

	  public String connect(String whichPort, int whichSpeed) {
		  //whoYaGonnaCall = _whoYaGonnaCall;
		  String feedback =
			  "Got connection to " + whichPort + " at " + whichSpeed;
		  System.out.println("Looking for " + whichPort + " at " + whichSpeed);
		
		  try {
			 CommPortIdentifier portId =CommPortIdentifier.getPortIdentifier(whichPort);
			  serialPort =(SerialPort) portId.open("SerialServer " + whichPort, 2000);
			
			  try {
				  serialPort.setSerialPortParams(
					  whichSpeed,
				 SerialPort.DATABITS_8,
				 SerialPort.STOPBITS_1,
				 SerialPort.PARITY_NONE);
			  } catch (UnsupportedCommOperationException e) {
				  feedback = "Could not set speed.";
				  System.out.println("can't set params");
			  }
			  try {
				  in = serialPort.getInputStream();
				  out = serialPort.getOutputStream();
				  if (parent != null){
				      serialPort.addEventListener(this);
				      serialPort.notifyOnDataAvailable(true);
				  }
				  //System.out.println("input" + inputStream + " output" + outputStream);
			  } catch (IOException e) {
				  feedback = "Serial Stream Problem";
				  System.out.println("couldn't get streams");
			  }

		  } catch (Exception e) {
			  feedback = "Port " + whichPort + " in use";
			  System.out.println("Port " + whichPort + " in Use " + e);
		  }

		  return feedback;
	  }

}
