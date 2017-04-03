package client;

import core.MessageListener;
import org.json.JSONException;
import core.NetworkHandler;

public class RingNode extends Thread implements MessageListener
{
	private int id;
	private NetworkHandler network;
	private static final int BROADCAST	= -1;
	private static final int BASIC_SEND	= -2;
	
	public RingNode(int id,final int[][] matrix)
	{
		this.id = id;
		
		this.network = new NetworkHandler(id, matrix);
		this.network.setListener(this);
	}
	
	@Override
	public void run()
	{
		while(!interrupted())
		{
			syncWait();
			if(this.id == 0)
			{
				broadcast("test ?");
			}
		}
	}
	
	public void messageDisplay(RingMessage rm)
	{
		System.out.println("[NODE ID = " + rm.getSourceNode() + " ] -> [ME ID = " + this.id + " ] " + rm.getMessage());
	}
	
	public void sendRight(String message)
	{
		RingMessage rm = new RingMessage(this.id, BASIC_SEND , message);
		this.network.sendRight(rm.serialize());
	}
	
	public void sendLeft(String message)
	{
		RingMessage rm = new RingMessage(this.id, BASIC_SEND , message);
		this.network.sendLeft(rm.serialize());
	}
	
	public void sendTo(int to, String message)
	{
		RingMessage rm = new RingMessage(this.id, to , message);
		this.network.sendRight(rm.serialize());
	}
	
	public void broadcast(String message){
		RingMessage rm = new RingMessage(this.id, BROADCAST , message);
		this.network.sendRight(rm.serialize());
	}
	
	private void broadcastHandler(RingMessage rm)
	{
		if(rm.getDestinationNode() == BROADCAST && rm.getSourceNode() != this.id){
			this.network.sendRight(rm.serialize());
			this.messageDisplay(rm);
		}
	}
	
	private void sendToHandler(RingMessage rm)
	{
		if(rm.getDestinationNode() != this.id){
			this.network.sendRight(rm.serialize());
		}
		else{
			this.messageDisplay(rm);
		}
	}
	
	synchronized private void syncWait()
	{
		try
		{
			wait(3000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}


	@Override
	public void receive(String msg)
	{
		RingMessage ringMessage = null;
		try
		{
			ringMessage = RingMessage.deserialize(msg);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
		if(ringMessage != null){
			switch (ringMessage.getDestinationNode())
			{
			case BROADCAST:
				this.broadcastHandler(ringMessage);
				break;
			case BASIC_SEND:
				this.messageDisplay(ringMessage);
				break;
			default:
				this.sendToHandler(ringMessage);
				break;
			}
		}
		
	}
}
