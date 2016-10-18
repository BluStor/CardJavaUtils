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

### Windows

Assuming you have paired the card and computer.  You open Bluetooth preferences in windows you can find the COM port under advanced settings.  Example: COM3 or COM5

### MAC

Assuming you have paired the card and computer.  Look in your /dev folder.  The parameter you will want to pass will start with "cu.".  If the card is not paired, you will not find these in the folder.

```
ls -l /dev | grep CYBERGATE
crw-rw-rw-  1 root     wheel           31,   7 Oct 18 15:46 cu.CYBERGATE-SerialPortSer
crw-rw-rw-  1 root     wheel           31,   6 Oct 17 10:41 tty.CYBERGATE-SerialPortSer
```

## Command Line

Here is an example of running on mac and windows using a terminal window.  Your parameters could be slightly different. 

I normally run the application in Eclipse, if I want to step through the code.

```
# mac

cd /<where you cloned>/CardJavaUtils/SimpleSerialConnector/bin

java -classpath .:../lib/asg.cliche-110413.jar:../lib/libjSSC-2.8_x86_64.jnilib serial.SerialComm mac /dev/cu.CYBERGATE-SerialPortSer

# win

cd c:\<where you cloned>\CardJavaUtils\SimpleSerialConnector\bin

java -classpath .;..\lib\asg.cliche-110413.jar;..\lib\jssc.jar serial.SerialComm win COM5
```
