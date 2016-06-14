import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;
import jssc.SerialPortTimeoutException;

public class SimpleSerialConnector {
	
	SerialPort serialPort = null;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//new SimpleSerialConnector().listPorts();
		try {
			new SimpleSerialConnector().connect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	

	
	private void listPorts() {
        String[] portNames = SerialPortList.getPortNames();
        for(int i = 0; i < portNames.length; i++){
            System.out.println(portNames[i]);
        }
	}
	
	private void connect() throws Exception {
	    // In the constructor pass the name of the port with which we work
        // SerialPort serialPort = new SerialPort("COM1");
		// SerialPort serialPort = new SerialPort("/dev/tty.serial-Test1");
		// SerialPort serialPort = new SerialPort("/dev/ttys002");
		serialPort = new SerialPort("/dev/cu.CYBERGATE-SerialPortSer");
		
        try {
            //Open port
            serialPort.openPort();
            //We expose the settings. You can also use this line - serialPort.setParams(9600, 8, 1, 0);
            serialPort.setParams(SerialPort.BAUDRATE_115200, 
                                 SerialPort.DATABITS_8,
                                 SerialPort.STOPBITS_1,
                                 SerialPort.PARITY_NONE);

            String hex = "01 00 1E 4C 49 53 54 20 2F 61 70 70 73 2F 76 61 75 6C 74 2F 64 61 74 61 2F 2A 0D 0A 00 00";

            serialPort.writeBytes(hexStringToByteArray(hex.replaceAll("\\s","")));
            
            Thread.sleep(2000);
            
            byte[] data = serialPort.readBytes();
            
            String output = new String(data);
            
            System.out.println("finished output = " + output);
                        
        } catch (SerialPortException ex) {
            System.out.println(ex);
        } finally {
            //Closing the port
            try {
				serialPort.closePort();
			} catch (SerialPortException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}        	
        }
	}
	
    private byte read() throws SerialPortException{
    byte[] temp = null;
    try {
        temp = serialPort.readBytes(1, 100);
        if (temp == null) {
            throw new SerialPortException(this.serialPort.getPortName(),
                    "SerialCOM : read()", "Can't read from Serial Port");
        } else {
            return temp[0];
        }
    } catch (SerialPortTimeoutException e) {
        System.out.println(e);
        e.printStackTrace();
    }
    return (Byte) null;
}	
	
    private byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    } 
	
}
