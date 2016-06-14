package serial.listeners;

import java.util.concurrent.Callable;

import serial.channel.ChannelManager;

public class PollBothChannels implements Callable<String> {
		
	private ChannelManager channelMgr = null;
	
	public void setChannelMgr(ChannelManager channelMgr) {
		this.channelMgr = channelMgr;
	}
	
    @Override
    public String call() throws Exception {
    	String data;
    	while(true) {
    		dump(channelMgr.readCommand());
    		dump(channelMgr.readData());
    		Thread.sleep(100);
    	}
    }
    private void dump(String data) {
		if (data!=null && !data.isEmpty()) {
			System.out.println(data);
		}
    	
    }
}