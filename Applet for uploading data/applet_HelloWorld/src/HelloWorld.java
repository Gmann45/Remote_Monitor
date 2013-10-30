import javax.swing.JApplet;
import java.io.*;
import java.net.*;

public class HelloWorld extends JApplet {

    NewJPanel myPanel;
    cls_Serial myPort;
    cls_DB myDB;

    String[] listOfRequiredFiles = { "bin/rxtxSerial.dll" , "lib/ext/RXTXcomm.jar"};
    String relativePathOnServer = "required/"; // for required files

    public static void main(String [] args)
    {
        
    }

    private void checkFilesInJavaHome()
    {
        String javaHomeFolder = System.getProperty("java.home"); // Windows cca: C:\Program Files\Java\jdk1.5.0_01\jre
        
        for (String file : listOfRequiredFiles)
        {

             if (!fileExists(javaHomeFolder+"\\"+file))
             {
                downloadFileFromServer(file,javaHomeFolder);
             }

        }


    }

//    private void checkFiles()
//    {
//
//        String listOfFolders = System.getProperty("java.library.path");
//        System.getProperty("java.ext.dirs");
//
//        // java.ext.dirs = C:\Program Files\Java\jdk1.5.0_01\jre\lib\ext
//        // java.home     = C:\Program Files\Java\jdk1.5.0_01\jre
//
//
//
//        String arrayOfFolders[] = listOfFolders.split(";");
//        boolean isFileInFolders;
//
//        for (String file : listOfRequiredFiles)
//        {
//            isFileInFolders = false;
//
//            for (String folder : arrayOfFolders)
//            {
//                if (fileExists(folder+"\\"+file))
//                {
//                    isFileInFolders = true;
//                    break;
//                }
//            }
//
//            if (!isFileInFolders)
//            {
//                System.out.println("NO - File not found in java-library: " + file);
//                downloadFileFromServer(file,relativePathOnServer);
//            }
//            else
//            {
//            System.out.println("YES - File found in java-library: " + file);
//            }
//        }
//    }

    private boolean fileExists(String filePath)
    {
        File f = new File(filePath);
        return f.exists();
    }

     private void downloadFileFromServer(String relativeSourceFileName, String targetFolder)
    {
        // http://www.javabeat.net/tips/36-file-upload-and-download-using-java.html

        final int buffer_size = 500;
        char[] buffer = new char[buffer_size];
        int count = 0;

        String sourcePath = this.getCodeBase().toString() + relativePathOnServer+relativeSourceFileName; //codeBase = http://www.myserver.com/thisFolder
        String targetPath = targetFolder + "\\" + relativeSourceFileName;
        
        System.out.println("Downloading from : " + sourcePath);
        System.out.println("Downloading to   : " + targetPath);

        try
        {

            URL mujSouborek = new URL(sourcePath);
            URLConnection sourceConnection = mujSouborek.openConnection();
            sourceConnection.setDoInput(true);

            BufferedInputStream bis = new BufferedInputStream( sourceConnection.getInputStream() );
            BufferedOutputStream bos = new BufferedOutputStream( new FileOutputStream(targetPath) );

             while ( (count = bis.read()) != -1)
            {
                bos.write(count);
            }

             bis.close();
             bos.close();

             System.out.println("--- Downloading OK ---");
             System.out.println("");


        }
        catch (Exception ex)
        {
             System.out.println("--- Downloading Error ---");
             System.out.println("");
        }
    }

//    private void downloadFileFromServer2(String fileName, String relativePathOnServer)
//    {
//        // http://www.javabeat.net/tips/36-file-upload-and-download-using-java.html
//
//        final int buffer_size = 500;
//        char[] buffer = new char[buffer_size];
//        int count = 0;
//
//        String sourcePath = this.getCodeBase().toString() + relativePathOnServer + fileName;
//
//        File soubor = new File(".");
//
//        String targetPath = soubor.getAbsolutePath();
//        targetPath = targetPath.substring(0, targetPath.length()-1) + fileName;
//
//        System.out.println("Downloading from : " + sourcePath);
//        System.out.println("Downloading to   : " + targetPath);
//
//        try
//        {
//
//            URL mujSouborek = new URL(sourcePath);
//            URLConnection sourceConnection = mujSouborek.openConnection();
//            sourceConnection.setDoInput(true);
//
//            BufferedInputStream bis = new BufferedInputStream( sourceConnection.getInputStream() );
//            BufferedOutputStream bos = new BufferedOutputStream( new FileOutputStream(targetPath) );
//
//             while ( (count = bis.read()) != -1)
//            {
//                bos.write(count);
//            }
//
//             bis.close();
//             bos.close();
//
//             System.out.println("--- Downloading OK ---");
//             System.out.println("");
//
//
//        }
//        catch (Exception ex)
//        {
//             System.out.println("--- Downloading Error ---");
//             System.out.println("");
//        }
//    }

    public void init()
    {

        
        checkFilesInJavaHome();

        
        try
        {
            //grant  { permission java.security.AllPermission; };

            myPort = new cls_Serial(8,9600,8,1,0);
            myDB = new cls_DB("mysql","pokus","root","");

            myPanel = new NewJPanel(myPort, myDB);
            myPanel.setOpaque(true);
            setContentPane(myPanel);

        }
        catch (Exception ex)
        {

        }
    }


// ========================= PŘÍKLADY ======================================
    
//    public static void copy2(String source, String target) throws IOException
//    {
//
//        FileInputStream fin = new FileInputStream(source);
//        FileOutputStream fout = new FileOutputStream(target);
//
//        byte[] b = new byte[1024];
//        int noOfBytes = 0;
//
//        while ((noOfBytes = fin.read(b)) != -1)
//        {
//            fout.write(b,0,noOfBytes);
//        }
//        fin.close();
//        fout.close();
//
//
//    }
//
//    public static void copy(String source, String dest) throws IOException
//    {
//         FileChannel in = null;
//         FileChannel out = null;
//
//         try
//         {
//              out = new FileOutputStream(dest).getChannel();
//              in = new FileInputStream(source ).getChannel();
//              long size = in.size();
//              MappedByteBuffer buf = in.map(FileChannel.MapMode.READ_ONLY, 0, size);
//              out.write(buf);
//              System.out.println("5");
//         }
//         finally
//         {
//             System.out.println("Finally");
//              if (in != null)          in.close();
//              if (out != null)     out.close();
//         }
//
//         System.out.println("Hotovo");
//    }


}
