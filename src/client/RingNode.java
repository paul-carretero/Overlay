package client;

import core.MessageListener;
import core.Message;
import core.NetworkHandler;

public class RingNode implements Loadable, MessageListener
{
	private int id;
	private NetworkHandler network;
	
	public RingNode(int id, NetworkHandler network)
	{
		this.id = id;
		this.network = network;
		
		// exemple
		this.network.listener(this);
	}
	
	public void load()
	{
		System.out.println("Je suis le node " + id + " et je LOAD().");
		
		if(this.id == 0)
			this.network.sendRight("test");
	}

	@Override
	public void receive(Message msg)
	{
		System.out.println("Je suis le node " + id + " et j'ai reçu le message : " + msg.getMessage());
		this.network.sendRight(msg);
	}
}
