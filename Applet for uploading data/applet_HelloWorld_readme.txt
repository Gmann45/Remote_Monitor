Java Web Applet

1) Open project in NetBeans

2) Right-click the project in NetBeans, choose Properties/Libraries/ADD folder. You can include whole folder with libs.

3) Try to compile

4) If compilation finished well, open command line and navigate to folder with compiled classes (cca: NetBeansProjects/ProjectName/build/classes)

5) Create web certificate like this:
   keytool -genkey -alias here_type_some_alias
    ... And follow instructions. You will be asked for a password. Enter something and dont forget it :)

6) create JAR file from your classes:
   jar -cf my.jar *

b) sign JAR file:
   jarsigner my.jar here_type_alias_you_typed_previously

6) Now create this simple HTML and put it into folder with CLASSES:

<applet code="HelloWorld.class" archive="my.jar" width="600" height="500">
</applet>

7) Run it in browser

8) Allow applet to run = click RUN when you are prompted

This should be it.

You can watch java console to see if there are some errors.

If a *.dll file cannot be found, try following:

Linux:
copy librxtxSerial.so to /usr/lib
copy RXTXcomm.jar to [JDK-directory]/jre/lib/ext/

Windows:
copy rxtxSerial.dll to [JDK-directory]\jre\bin\rxtxSerial.dll
copy RXTXcomm.jar to [JDK-directory]\jre\lib\ext\RXTXcomm.jar

files win32com.dll, comm.jar, javax.comm.properties won't be probably used. If still some error with libraries occures, try this:

http://forums.sun.com/thread.jspa?threadID=702317
http://llk.media.mit.edu/projects/cricket/doc/serial.shtml
# Put comm.jar in C:\Program Files\javasoft\jre\1.1\lib\
# Put javax.comm.properties in C:\Program Files\javasoft\jre\1.1\lib\
# Put win32com.dll in C:\Program Files\javasoft\jre\1.1\bin\ 
