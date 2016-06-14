package serial.packet;

import java.io.IOException;
import java.io.InputStream;

import jssc.SerialPort;

public class DataPacketBuilder {
    public static final String TAG = DataPacketBuilder.class.getSimpleName();

    public static DataPacket build(SerialPort serialPort) throws Exception {
        byte[] header = readHeader(serialPort);
        int packetSize = getPacketSize(header);
        int channel = getPacketChannel(header);
        byte[] payload = readPayload(serialPort, packetSize);
        byte[] checksum = readChecksum(serialPort);

        return new DataPacket(payload, channel);
    }

    public static byte[] toPacketBytes(byte[] data, int channel) {
        int packetSize = data.length + 5;
        byte channelByte = getChannelByte(channel);
        byte msb = getMSB(packetSize);
        byte lsb = getLSB(packetSize);

        byte[] packet = new byte[data.length + 5];
        packet[0] = channelByte;
        packet[1] = msb;
        packet[2] = lsb;
        for (int i = 0; i < data.length; i++) {
            packet[i + 3] = data[i];
        }

        packet[packet.length - 2] = DataPacket.MOST_SIGNIFICANT_BIT;
        packet[packet.length - 1] = DataPacket.LEAST_SIGNIFICANT_BIT;
        return packet;
    }

    private static int getPacketSize(byte[] header) {
        byte packetSizeMSB = header[1];
        byte packetSizeLSB = header[2];
        int packetSize = (int) packetSizeMSB << 8;
        packetSize += (int) packetSizeLSB & 0xFF;
        return packetSize;
    }

    private static int getPacketChannel(byte[] header) {
        return (int) header[0];
    }

    private static byte[] readHeader(SerialPort serialPort) throws Exception {
        return fillByteArrayFromStream(serialPort, DataPacket.HEADER_SIZE);
    }

    private static byte[] readPayload(SerialPort serialPort, int packetSize) throws Exception {
        int payloadsize = packetSize - (DataPacket.HEADER_SIZE + DataPacket.CHECKSUM_SIZE);
        return fillByteArrayFromStream(serialPort, payloadsize);
    }
    
    // GKBluetoothMultiplexer Line 260
    private static byte[] readChecksum(SerialPort serialPort) throws Exception {
        return fillByteArrayFromStream(serialPort, DataPacket.CHECKSUM_SIZE);
    }

    // GKBluetoothMultiplexer Line 264
    private static byte[] fillByteArrayFromStream(SerialPort serialPort, int length) throws Exception {
        byte[] data = new byte[length];
        data = serialPort.readBytes(length);
        return data;
    }

    private static byte getChannelByte(int channel) {
        return (byte) (channel & 0xff);
    }

    private static byte getMSB(int size) {
        return (byte) (size >> 8);
    }

    private static byte getLSB(int size) {
        return (byte) (size & 0xff);
    }
}