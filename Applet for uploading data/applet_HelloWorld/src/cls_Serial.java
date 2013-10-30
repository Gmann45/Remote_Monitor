import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import gnu.io.*;

public class cls_Serial {

    InputStream in;
    OutputStream out;
    String portName;
    int comNumber;
    int baudrate;
    int data;
    int parity;
    int stopbit;

// -----------------------------------------------------------------------------
    
    cls_Serial(int comNumber, int baudrate, int data, int parity, int stopbit)
    {

        this.portName = "COM" + comNumber;
        this.comNumber = comNumber;
        this.baudrate = baudrate;
        this.data = data;
        this.parity = parity;
        this.stopbit = stopbit;
        


        try
        {
            gnu.io.CommPortIdentifier portIdentifier = gnu.io.CommPortIdentifier.getPortIdentifier(portName);
            if ( portIdentifier.isCurrentlyOwned() )
            {
                System.out.println("Error: Port is currently in use");
            }
            else
            {
                CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
                if ( commPort instanceof SerialPort )
                {
                    SerialPort serialPort = (SerialPort) commPort;
                    serialPort.setSerialPortParams(this.baudrate,this.data,this.parity,this.stopbit);
                    
                    in = serialPort.getInputStream();
                    out = serialPort.getOutputStream();

                }
                else
                {
                    System.out.println("Error: Only serial ports are handled by this example.");
                }
            }

        }
        
         catch (Exception ex)
        {
            System.out.println("Serial constructor except");
        }
        
   
    }

// -----------------------------------------------------------------------------
    
      public void writeCom (byte[] byteArray)
        {
            try
            {
                this.out.write(byteArray);
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            }
        }

// -----------------------------------------------------------------------------

        public String readCom ()
        {
            byte[] buffer = new byte[1024];
            int len = -1;
            try
            {
                if ( (len = this.in.read(buffer)) > -1 )
                {
                    return(new String( buffer,0,len));
                }
                else
                {
                    return "";
                }
            }
            catch ( IOException e )
            {
                e.printStackTrace();
                return "";
            }
        }

// -----------------------------------------------------------------------------

}
