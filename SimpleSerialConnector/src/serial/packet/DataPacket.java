package serial.packet;

public class DataPacket {
    public static final int HEADER_SIZE = 3;
    public static final int CHECKSUM_SIZE = 2;

    public static final byte MOST_SIGNIFICANT_BIT = 0x00;
    public static final byte LEAST_SIGNIFICANT_BIT = 0x00;

    private byte[] mPayload;
    private int mChannel;

    public DataPacket(byte[] payload, int channel) {
        mPayload = payload;
        mChannel = channel;
    }

    public byte[] getPayload() {
        return mPayload;
    }

    public int getChannel() {
        return mChannel;
    }
}
