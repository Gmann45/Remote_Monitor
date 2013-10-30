/** 
 * SerialServer.java
 *
 * Title:			SerialServer
 * Description:		This relays bytes between a serial port and a socket connection.
 * @author			Administrator
 * @version			
 */
//import java.comm.commport


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
public class SerialServer extends java.applet.Applet  implements SerialListener {
	// IMPORTANT: Source code between BEGIN/END comment pair will be regenerated
	// every time the form is saved. All manual changes will be overwritten.
	// BEGIN GENERATED CODE
	// member declarations
	java.awt.Choice baud = new java.awt.Choice();
	java.awt.Checkbox interpretation = new java.awt.Checkbox();
	java.awt.Choice ports = new java.awt.Choice();
	java.awt.TextField socketPort = new java.awt.TextField();
	java.awt.Label softwarePort = new java.awt.Label();
	java.awt.Button sendToSocket = new java.awt.Button();
	java.awt.Button sendToSerial = new java.awt.Button();
	java.awt.TextField sendText = new java.awt.TextField();
	java.awt.TextArea traffic = new java.awt.TextArea();
	java.awt.Checkbox debugButton = new java.awt.Checkbox();
	java.awt.Label socketStatus = new java.awt.Label();
	java.awt.Label serialStatus = new java.awt.Label();
	java.awt.Label numbersFootnote = new java.awt.Label();
	java.awt.Button clearButton = new java.awt.Button();
	java.awt.Button cover = new java.awt.Button();
	// END GENERATED CODE
	String serialType = null;
	static Socketer mySocket;
	static Serial mySerial;
	boolean isStandalone = false;
	static boolean debug = false;
	public SerialServer() {}
	static HashMap argsHash;
	int substituteChar = 0;
	
	public void start() {
		//figure out what driver to use
		if (isStandalone == false) {
				serialType =  getParameter("SerialDriver");
			} else {
				serialType = (String) argsHash.get("SerialDriver");
			}
		
		if( serialType != null){  //if they did not specify
			serialType= serialType.toLowerCase();
		}
		
		newSerial();
		if (isStandalone == false) {
			System.out.println("This is an Applet");
			useParameters(getParameter("Baud"), getParameter("SerialPort"), getParameter("SocketPort"), getParameter("Sub0ForChar"), getParameter("SerialDriver"));
		} else {
			useParameters((String) argsHash.get("Baud"), (String) argsHash.get("SerialPort"), (String) argsHash.get("SocketPort"), (String) argsHash.get("Sub0ForChar"),(String) argsHash.get("SerialDriver"));
		}
		newSocket();
		connectSerial();
		//		event handling
		baud.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent e) {
				baudItemStateChanged(e);
			}
		});
		interpretation.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent e) {
				interpretationItemStateChanged(e);
			}
		});
		ports.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent e) {
				portsItemStateChanged(e);
			}
		});
		socketPort.addTextListener(new java.awt.event.TextListener() {
			public void textValueChanged(java.awt.event.TextEvent e) {
				socketPortTextValueChanged(e);
			}
		});
		sendToSocket.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				sendToSocketPerformed(e);
			}
		});
		sendToSerial.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				sendToSerialActionPerformed(e);
			}
		});
		sendText.addTextListener(new java.awt.event.TextListener() {
			public void textValueChanged(java.awt.event.TextEvent e) {
				sendTextTextValueChanged(e);
			}
		});
		////	traffic.addTextListener(new java.awt.event.TextListener() {
		//	public void textValueChanged(java.awt.event.TextEvent e) {
		//		trafficTextValueChanged(e);
		//	}
		//});
		debugButton.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent e) {
				debugItemStateChanged(e);
			}
		});
		clearButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				clearActionPerformed(e);
			}
		});
		//populatePorts();
	}
	// Initialize the applet
	public void init() {
		try {
			initComponents();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void useParameters(String _baud, String _serialPort, String _socketPort, String _subChar, String _serialType) {
		System.out.println("Input Parameters Baud:" + _baud + " SerialPort:" + _serialPort + " SocketPort:" + _socketPort + " SubChar:" + _subChar+ " SerialType:" + _serialType);
	
		if (_baud != null) {
			baud.select(_baud);
		}
		if (_subChar != null) {
			substituteChar = Integer.parseInt(_subChar);
		} else {
			substituteChar = 0;
		}
		//else{
		//		baud.select("9600");
		//	}
		if (_serialPort != null) {
			ports.select(_serialPort);
		} //else{
		//	ports.select("COM1") ;
		//	}
		//if ((_baud != null) || (_serialPort != null)){
		//	newSerial();
		//}
		if (_socketPort != null) {
			socketPort.setText(_socketPort);
			//	newSocket();
		}
	}
	public void initComponents() throws Exception {
		// IMPORTANT: Source code between BEGIN/END comment pair will be regenerated
		// every time the form is saved. All manual changes will be overwritten.
		// BEGIN GENERATED CODE
		// the following code sets the frame's initial state
		baud.setVisible(true);
		baud.setLocation(new java.awt.Point(222, 31));
		baud.setSize(new java.awt.Dimension(70, 21));
		interpretation.setVisible(true);
		interpretation.setLabel("Use decimals instead of letters.");
		interpretation.setLocation(new java.awt.Point(43, 175));
		interpretation.setSize(new java.awt.Dimension(200, 20));
		ports.setVisible(true);
		ports.setLocation(new java.awt.Point(222, 10));
		ports.setSize(new java.awt.Dimension(108, 21));
		socketPort.setLocation(new java.awt.Point(18, 10));
		socketPort.setVisible(true);
		socketPort.setText("9001");
		socketPort.setSize(new java.awt.Dimension(40, 20));
		softwarePort.setVisible(true);
		softwarePort.setLocation(new java.awt.Point(60, 10));
		softwarePort.setText("<Socket> 4------ :-) ------- <Serial>");
		softwarePort.setSize(new java.awt.Dimension(160, 20));
		sendToSocket.setVisible(true);
		sendToSocket.setLabel("<TestSend");
		sendToSocket.setLocation(new java.awt.Point(1, 60));
		sendToSocket.setSize(new java.awt.Dimension(80, 20));
		sendToSerial.setVisible(true);
		sendToSerial.setLabel("Test Send>");
		sendToSerial.setLocation(new java.awt.Point(279, 60));
		sendToSerial.setSize(new java.awt.Dimension(80, 20));
		sendText.setLocation(new java.awt.Point(82, 60));
		sendText.setVisible(true);
		sendText.setSize(new java.awt.Dimension(197, 20));
		traffic.setLocation(new java.awt.Point(5, 95));
		traffic.setVisible(true);
		traffic.setSize(new java.awt.Dimension(352, 80));
		debugButton.setFont(new java.awt.Font("Serif", 0, 12));
		debugButton.setVisible(true);
		debugButton.setLabel("debug");
		debugButton.setLocation(new java.awt.Point(130, 39));
		debugButton.setSize(new java.awt.Dimension(50, 20));
		socketStatus.setVisible(true);
		socketStatus.setAlignment(java.awt.Label.CENTER);
		socketStatus.setLocation(new java.awt.Point(65, 26));
		socketStatus.setSize(new java.awt.Dimension(45, 20));
		serialStatus.setVisible(true);
		serialStatus.setAlignment(java.awt.Label.CENTER);
		serialStatus.setLocation(new java.awt.Point(177, 26));
		serialStatus.setSize(new java.awt.Dimension(40, 20));
		numbersFootnote.setVisible(false);
		numbersFootnote.setLocation(new java.awt.Point(49, 79));
		numbersFootnote.setText("*Put spaces between numbers for test send.");
		numbersFootnote.setSize(new java.awt.Dimension(280, 20));
		clearButton.setVisible(true);
		clearButton.setLabel("Clear");
		clearButton.setLocation(new java.awt.Point(260, 178));
		clearButton.setSize(new java.awt.Dimension(58, 19));
		setLocation(new java.awt.Point(0, 0));
		setLayout(null);
		add(cover);
		add(baud);
		add(interpretation);
		add(ports);
		add(socketPort);
		add(softwarePort);
		add(sendToSocket);
		add(sendToSerial);
		add(sendText);
		add(traffic);
		add(debugButton);
		add(socketStatus);
		add(serialStatus);
		add(numbersFootnote);
		add(clearButton);
		setSize(new java.awt.Dimension(356, 274));
		// END GENERATED CODE
		baud.add("110");
		baud.add("300");
		baud.add("1200");
		baud.add("2400");
		baud.add("4800");
		baud.add("9600");
		baud.add("19200");
		baud.add("31250"); //   //31250
		baud.add("38400");
		baud.add("57600");
		baud.add("230400");
		baud.add("460800");
		baud.add("921600");
		baud.select("9600");
		cover.setVisible(true);
		//cover.setLayout(null);
		cover.setLocation(new java.awt.Point(-10, 57));
		cover.setSize(new java.awt.Dimension(964, 287));
	}
	public void relayToSerial(int what) {
	    
		boolean ok = mySerial.send(what).equals("OKAY");
		if (debug) {
			socketStatus.setText("<IN>");
			if (ok) {
				serialStatus.setText("<OUT>");
			}
			showText(what);
		}

	}
	public void gotFromSerial(byte[] _byteArray){
	//public void relayToSocket(int what) {
	    for(int i = 0; i < _byteArray.length; i++){
	        int what = (int) (_byteArray[i] & 0xff);
		if ((substituteChar != 0) && (what == substituteChar))
			what = 0;
		boolean ok = mySocket.send(what).equals("OKAY");
		if (debug) {
			serialStatus.setText("<IN>");
			if (ok) {
				socketStatus.setText("<OUT>");
			}
			showText(what);
		}
	    }
	}
	public void appendDebug(String what) {
		if (traffic.getText().toCharArray().length > 200)
			traffic.setText(traffic.getText().substring(50));
		traffic.setText(traffic.getText() + "\n" + what + "\n");
		String strMessage = traffic.getText();
		traffic.setCaretPosition(strMessage.length());
	}
	public void showText(int what) {
		if (interpretation.getState()) {
			traffic.setText(traffic.getText() + " " + what);
		} else {
			traffic.setText(traffic.getText() + ((char) what));
		}
		String strMessage = traffic.getText();
		traffic.setCaretPosition(strMessage.length());
	}
	// Standard method to start the applet
	//public void start() {
	//	newSocket();
	//	newSerial();
	//}
	public void populatePorts() {
		ports.removeAll();
		ArrayList portList = mySerial.getPortsList();
		for (int i = 0; i < portList.size(); i++) {
			String[] portAndOwner = (String[]) portList.get(i);
			System.out.println("owner" + portAndOwner[1]);
			String port = portAndOwner[0];
			String owner = portAndOwner[1];
			//System.out.println(owner + " " + port + " number of Ports " + portList.size());
			if (owner == null || owner.equals("Port currently not owned")) {
				boolean alreadyThere = false;
				for (int j = 0; j < ports.getItemCount(); j++) {
					String alrdy = ports.getItem(j);
					//System.out.println(port+ " Ports Already " + alrdy );
					if (alrdy.equals(port)) {
						alreadyThere = true;
						break;
					}
				}
				if (alreadyThere == false) {
					ports.add(port);
				}
			} else {
				System.out.println("Can't use it!" + port + owner);
			}
			//ports.add(((String) portList.get(i)));
		}
	}
	
	public void newSerial() {
		serialStatus.setText("!");
		mySerial = new Serial(this, serialType);

		if (ports.getItemCount() == 0) {
			populatePorts();
		}
	}
	
	public void connectSerial() {
		if (ports.getItemCount() == 0) {
			appendDebug("No serial ports found.");
		} else {
			String status = mySerial.connect(ports.getSelectedItem(), Integer.parseInt(baud.getSelectedItem()));
			if (status.startsWith("Got")) {
				serialStatus.setText("<OK>");
				//mySerial.start();
			} else {
				serialStatus.setText("Bad");
				//populatePorts();
			}
			appendDebug(status);
		}
	}
	public void newSocket() {
		mySocket = new Socketer(Integer.parseInt(socketPort.getText()), this);
	}
	public void socketStatus(String status) {
		if (status.startsWith("Got")) {
			socketStatus.setText("<OK>");
		} else if (status.startsWith("Waiting")) {
			socketStatus.setText("Waiting");
		} else {
			socketStatus.setText("Bad");
		}
		appendDebug(status);
	}
	// Standard method to stop the applet
	public void stop() {
		mySerial.kill();
		mySocket.kill();
	}
	// Standard method to destroy the applet
	public void destroy() {}
	// Main entry point when running standalone
	public static void main(String[] args) {
		SerialServer applet = new SerialServer();
		applet.isStandalone = true;
		Frame frame = new Frame();
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				Frame f = (Frame) e.getSource();
				f.setVisible(false);
				f.dispose();
				System.exit(0);
			}
		});
		frame.setTitle("Serial Server");
		frame.add(applet, BorderLayout.CENTER);
		frame.setSize(400, 320);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation((d.width - frame.getSize().width) / 2, (d.height - frame.getSize().height) / 2);
		frame.setVisible(true);
		applet.init();
		argsHash = new HashMap();
		for (int i = 0; i < args.length; i++) {
			String pair = args[i];
			String parts[] = pair.split(":");
			if (parts.length == 2) {
				argsHash.put(parts[0], parts[1]);
			}else{
				System.out.println("Problem with format of parameter "+ pair);
			}
		}
		applet.start();
		frame.pack();
	}
	public void sendTextTextValueChanged(java.awt.event.TextEvent e) {}
	public void interpretationItemStateChanged(java.awt.event.ItemEvent e) {
		numbersFootnote.setVisible(interpretation.getState());
		System.out.println("Interpretation" + interpretation.isVisible());
	}
	public void socketPortTextValueChanged(java.awt.event.TextEvent e) {
		mySocket.kill();
		mySocket = null;
		newSocket();
		System.out.println("Changed Socket");
	}
	public void sendToSocketPerformed(java.awt.event.ActionEvent e) {
		if (interpretation.getState()) {
			StringTokenizer st = new StringTokenizer(sendText.getText(), " ");
			while (st.hasMoreTokens()) {
				try {
					int what = Integer.parseInt(st.nextToken());
					if ((substituteChar != 0) && (what == substituteChar))
						what = 0;
					//System.out.println(substituteChar + "debugSend" + what);
					mySocket.send(what);
				} catch (NumberFormatException nfe) {
					System.out.println("You are are entering letters but you checked the decimal interpretation.");
				}
			}
		} else {
			byte[] asbytes = sendText.getText().getBytes();
			for (int i = 0; i < asbytes.length; i++) {
				int what = asbytes[i];
				if ((substituteChar != 0) && (what == substituteChar))
					what = 0;
				System.out.println(substituteChar + "debugSend" + what);
				mySocket.send(what);
			}
		}
	}
	public void sendToSerialActionPerformed(java.awt.event.ActionEvent e) {
	
		if (interpretation.getState()) {
			StringTokenizer st = new StringTokenizer(sendText.getText(), " ");
			while (st.hasMoreTokens()) {
				try {
                    mySerial.send(Integer.parseInt(st.nextToken()));
                } catch (NumberFormatException e1) {
                	appendDebug("Enter a number.");
                   
                }
			}
		} else {
			byte[] asbytes = sendText.getText().getBytes();
			for (int i = 0; i < asbytes.length; i++) {
				mySerial.send(asbytes[i]);
			}
		}
	}
	public void portsItemStateChanged(java.awt.event.ItemEvent e) {
		mySerial.kill();
		mySerial = null;
		newSerial();
		connectSerial();
		System.out.println("Serial Port Changed");
	}
	public void baudItemStateChanged(java.awt.event.ItemEvent e) {
		mySerial.kill();
		mySerial = null;
		newSerial();
		connectSerial();
		System.out.println("Serial Baud Changed");
	}
	public void debugItemStateChanged(java.awt.event.ItemEvent e) {
		debug = debugButton.getState();
		if (debug) {
			cover.setLocation(-1000, -1000);
		} else {
			cover.setLocation(-10, 57);
			//cover.setVisible(true);
		}
		//System.out.println("debugl Changed" + debug);
	}
	public void clearActionPerformed(java.awt.event.ActionEvent e) {
		traffic.setText("");
	}
}
