package serial.listeners;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;

import jssc.SerialPort;
import serial.packet.DataPacket;
import serial.packet.DataPacketBuilder;

public class SerialReader implements Runnable {
	private BlockingQueue<Byte>[] mChannelBuffers=null;
	//private InputStream in = null;
	private SerialPort serialPort = null;
	private volatile boolean running = true;
	
    public void terminate() {
        running = false;
    }    
    public void setBlockingQue(BlockingQueue<Byte>[] mChannelBuffers) {
    	this.mChannelBuffers= mChannelBuffers;
    }
	public SerialReader(SerialPort serialPort) {
		this.serialPort = serialPort;
	}

	public void run() {
        while (running) {
            try {
                bufferNextPacket();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
	}
	
    private void bufferNextPacket() throws Exception {
        DataPacket packet = DataPacketBuilder.build(serialPort);
        BlockingQueue<Byte> buffer = mChannelBuffers[packet.getChannel()];
        byte[] bytes = packet.getPayload();
        for (int i = 0; i < bytes.length; i++) {
            buffer.put(bytes[i]);
        }
    }
	
}