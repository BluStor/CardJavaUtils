# Card Java Utilities

Java command line application used for direct access to the card for executing FTPx REST service calls.  To understand and explore the capabilities of the card API.

The application uses [Java Simple Serial Connector](https://github.com/scream3r/java-simple-serial-connector) to connect to the card over Bluetooth using the SPP (Serial Port Profile).  They provide both native libraries for Windows and Mac OS X.

# Running Examples
- Clone git repository
- Import into eclipse as Java project
- Pair over Bluetooth the CyberGate card with desktop or laptop
- Run Java application with eclipse or command line.

## Java Parameters

The first parameter will be "win" or "mac".  The second parameter will be operating specific.  See windows or mac

### Windows COM Serial Port

Assuming you have paired the card and computer.  Under "Device Manager" expand "Ports (COM & LPT)".  You should see two serial bluetooth entires.  One is incoming and the other is outgoing.  You can try each one, if you are not sure.

![Alt text](/SimpleSerialConnector/img/device_manager.png?raw=true "Optional Title")

### MAC Serial Port

Assuming you have paired the card and computer.  Look in your /dev folder.  The parameter you will want to pass will start with "cu.".  If the card is not paired, you will not find these in the folder.

```
# Older Firmware

In this example, you would use "/dev/cu.CYBERGATE-SerialPortSer" as a parameter for the Java Utility

ls -l /dev | grep CYBERGATE
crw-rw-rw-  1 root     wheel           31,   7 Oct 18 15:46 cu.CYBERGATE-SerialPortSer
crw-rw-rw-  1 root     wheel           31,   6 Oct 17 10:41 tty.CYBERGATE-SerialPortSer

# Newer Firmware

In this example, you would use "/dev/cu.CG-en-SerialPortServerP" as a parameter for the Java Utility

ls -l /dev | grep cu
crw-rw-rw-  1 root     wheel           20,   1 Oct 24 14:03 cu.Bluetooth-Incoming-Port
crw-rw-rw-  1 root     wheel           20,   7 Oct 25 12:23 cu.CG-en-SerialPortServerP
```

## Mac Command Line

Run a terminal window and execute the following commands.  Switch to the directory that you compiled the source.  This has been tested with Java 8.

```
cd /<where you cloned>/CardJavaUtils/SimpleSerialConnector/bin

java -classpath .:../lib/asg.cliche-110413.jar:../lib/libjSSC-2.8_x86_64.jnilib serial.SerialComm mac /dev/cu.CYBERGATE-SerialPortSer

```
## Windows Command Line

Make sure you are running java 8.  I did not get it to work with Java 9.  Type "java -version" to see what you are running.  Or put the fully qualified path, like my example below.

Start -> Run -> cmd.  Run the following command at a command prompt.

```
cd c:\<where you cloned>\CardJavaUtils\SimpleSerialConnector\bin
"C:\Program Files (x86)\Java\jdk1.8.0_151\bin\java" -classpath .;..\lib\asg.cliche-110413.jar;..\lib\jssc.jar serial.SerialComm win COM99
```
