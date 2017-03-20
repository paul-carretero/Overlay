package layer1;

import java.util.List;

import core.Message;

public interface Layer1
{
	public void send(Message msg);
	public void send(Message msg, String ChannelName);
	public List<String> getAvailableInterface();
	public void subscribe(ISubscriber s);
}
