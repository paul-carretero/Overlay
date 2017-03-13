package layer1;

import core.Message;

public interface ISubscriber
{
	public void receive(Message msg);
}
