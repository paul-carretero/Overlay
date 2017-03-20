package RIPLayer;

import core.Message;

public interface RIPLayer {
	public void sendRIPPacket(Message msg, String ChannelName);
	public String getChannelName(int DestinationID); // can be null ! ! ! 
}
