import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

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
public interface AnySerialPort {

		public InputStream getInputStream();
		public OutputStream getOutputStream();
		public ArrayList getPortsList();
		public String connect(String s, int i);
		public void close();
	
	}
