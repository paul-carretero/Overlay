package client;

import core.MessageListener;
import core.Message;
import core.NetworkHandler;

public class RingNode implements Runnable, MessageListener
{
	private int id;
	private NetworkHandler network;
	
	public RingNode(int id, NetworkHandler network)
	{
		this.id = id;
		this.network = network;
	}
	
	@Override
	public void run()
	{
		// exemple
		this.network.listener(this);
		
		if(this.id == 0)
			this.network.sendRight("test");
	}

	@Override
	public void receive(Message msg)
	{
		System.out.println("J'ai reçu le message : " + msg.getMessage());
		this.network.sendRight(msg);
	}
}
