package core;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import overlay.NodeMQ;
import routage.IRoutage;
import routage.Routeur;

public class NetworkHandler
{
	private int[][] matrix;
	private IRoutage routage;
	private NodeMQ node;
	private int myId;
	
	public NetworkHandler(int myId, final int[][] matrix)
	{
		this.matrix = matrix;
		
		try
		{
			this.node = new NodeMQ(myId, matrix);
		}
		catch (IOException | TimeoutException e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		
		this.routage = new Routeur(myId, matrix);
		this.myId = myId;
	}
	
	public void setListener(MessageListener s)
	{
		this.node.addListener(s);
	}
	
	public void sendLeft(String msg)
	{
		int destinationID = (myId - 1 + this.matrix.length) % this.matrix.length;
		String queueName = this.routage.getChannelName(destinationID);
		node.sendMessage(queueName, new Message(myId, destinationID, msg));
	}
	
	public void sendLeft(Message msg)
	{
		int destinationID = (myId - 1 + this.matrix.length) % this.matrix.length;
		String queueName = this.routage.getChannelName(destinationID);
		node.sendMessage(queueName, msg);
	}
	
	public void sendRight(String msg)
	{
		int destinationID = (myId + 1) % this.matrix.length;
		String queueName = this.routage.getChannelName(destinationID);
		node.sendMessage(queueName, new Message(myId, destinationID, msg));
	}
	
	public void sendRight(Message msg)
	{
		int destinationID = (myId + 1) % this.matrix.length;
		String queueName = this.routage.getChannelName(destinationID);
		node.sendMessage(queueName, msg);
	}
}
