package serial.channel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import jssc.SerialPort;
import serial.SerialComm;
import serial.packet.DataPacketBuilder;

public class ChannelManager {
	
	private BlockingQueue<Byte>[] mChannelBuffers=null;
	//InputStream in = null;
	//OutputStream out = null;
	
	SerialPort serialPort = null;
	
    private static final byte CARRIAGE_RETURN = 13;
    private static final byte LINE_FEED = 10;
    
    private static final int UPLOAD_DELAY_MILLIS = 100;
    // private static final int UPLOAD_DELAY_MILLIS = 50;
    // private static final int UPLOAD_DELAY_MILLIS = 5;
    // private static final int UPLOAD_DELAY_MILLIS = 1;


    public void setBlockingQue(BlockingQueue<Byte>[] mChannelBuffers) {
    	this.mChannelBuffers= mChannelBuffers;
    }
    public void setIOStreams(SerialPort serialPort) {
    	this.serialPort = serialPort;
    }

    public String readCommand() throws Exception {
    	
    	StringBuffer sb = new StringBuffer();
    	
        List<Byte> byteList = new ArrayList<>();
        mChannelBuffers[SerialComm.COMMAND_CHANNEL].drainTo(byteList);
        byte[] bytes = new byte[byteList.size()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = byteList.get(i);
        }
        sb.append(new String(bytes, Charset.forName("ISO-8859-7")));
        return sb.toString();	    	
    }
    
    public String readData() throws Exception {
    	StringBuffer sb = new StringBuffer();
    	
    	byte[] b = new byte[0];
    	b = readDataChannel();
    	sb.append(new String(b, Charset.forName("ISO-8859-7")));
    	
    	return sb.toString();
    }
    
    public byte[] readDataChannel() throws IOException, InterruptedException {
        List<Byte> byteList = new ArrayList<>();
        mChannelBuffers[SerialComm.DATA_CHANNEL].drainTo(byteList);
        byte[] bytes = new byte[byteList.size()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = byteList.get(i);
        }
        return bytes;
    }
    
    public byte[] readCommandChannelLine() throws BufferEmptyException, IOException, InterruptedException {
        return readLine(SerialComm.COMMAND_CHANNEL);
    }
    private byte[] readLine(int channel) throws BufferEmptyException, IOException, InterruptedException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte a = read(channel);
        byte b = read(channel);
        while (a != CARRIAGE_RETURN && b != LINE_FEED) {
            bytes.write(a);
            a = b;
            b = read(channel);
        }
        return bytes.toByteArray();
    }
    private byte read(int channel) throws BufferEmptyException, IOException, InterruptedException {
        byte[] buffer = new byte[1];
        read(buffer, channel);
        return buffer[0];
    }
    
    private int read(byte[] data, int channel) throws BufferEmptyException, IOException, InterruptedException {
        int bytesRead = 0;
        int totalRead = 0;
        while (totalRead < data.length && bytesRead != -1) {
            bytesRead = readFromBuffer(data, bytesRead, data.length - bytesRead, channel);
            if (bytesRead != -1) {
                totalRead += bytesRead;
            }
        }
        return totalRead;
    }
    private int readFromBuffer(byte[] data, int off, int len, int channel) throws BufferEmptyException, IOException, InterruptedException {
        BlockingQueue<Byte> buffer = mChannelBuffers[channel];
        int bytesRead = 0;
        if (buffer.size() <= 0) {
        	throw new BufferEmptyException("No response from card, buffer empty");
        }
        for (int i = 0; i < len; i++) {
            data[off + i] = buffer.take();
            bytesRead = i;
        }
        return bytesRead + 1;
    }
    
    // GKBluetooth : private Response get(String method, String cardPath)
    // Line 178
    public void getFile(String dest ) throws Exception {
    	File file = new File(dest);
        FileOutputStream fos = new FileOutputStream(file.getAbsolutePath(), false);
        try {
			byte[] data = readDataChannel();
			fos.write(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			fos.close();
		}
    }
        
    // GKBluetooth : public Response put(String cardPath, InputStream inputStream) 
    // Line 60
    public void putFile(String path) throws Exception {      
        
        int bytesRead;
        byte[] buffer = new byte[SerialComm.PACKET_SIZE];
        
        FileInputStream inputStream = new FileInputStream(path);
        long size=0;
        int i=0;
        try {
			do {
			    bytesRead = inputStream.read(buffer, 0, buffer.length);
			    if (bytesRead == -1) {
			        continue;
			    }
			    if (bytesRead < buffer.length) {
			        writeToDataChannel(Arrays.copyOf(buffer, bytesRead));
			        size+=bytesRead;
			    } else {
			        writeToDataChannel(buffer);
			        size+=buffer.length;
			    }
			    //if (i%10 == 0) {
			    	if (i==0) {
			    		System.out.println("Uploading... = " + size);
			    	} else {
			    		System.out.print(", " + size);
			    	}
			    //}
			    i++;
			    Thread.sleep(UPLOAD_DELAY_MILLIS);
			} while (bytesRead != -1);
			System.out.println("\nTotal size data uploaded = " + size);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			inputStream.close();
		} 
    	
    }
    
    public void writeToDataChannel(byte[] data) throws Exception {
        write(data, SerialComm.DATA_CHANNEL);
    }
    
    private void write(byte[] data, int channel) throws Exception {
        byte[] packetBytes = DataPacketBuilder.toPacketBytes(data, channel);
        serialPort.writeBytes(packetBytes);
    }

	
}
