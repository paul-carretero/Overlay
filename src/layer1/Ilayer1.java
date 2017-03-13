package layer1;

import core.Message;

public interface Ilayer1
{
	public void send(Message msg);
	public void subscribe(ISubscriber s);
}
