package RIPLayer;

import core.Message;

public interface RIPLayer {
	public void sendRIPPacket(Message msg, String ChannelName);
	
	/**
	 * 
	 * @param DestinationID 
	 * @return NULL si DestinationID == Me ou route non connue
	 */
	public String getChannelName(int DestinationID); // can be null ! ! ! 
}
