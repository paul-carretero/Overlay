package core;

import overlay.NodeMQ;

public class NetworkHandler
{
	private NodeMQ node;
	private int[][] matrix;
	private Routage routage;
	
	public NetworkHandler(NodeMQ node, int[][] matrix)
	{
		this.node = node;
		this.matrix = matrix;
		
		this.routage = new Routage();
	}
	
	public void listener(MessageListener s)
	{
		this.node.addListener(s);
	}
	
	public void sendLeft(String msg)
	{
		int destinationID = (this.node.getID() - 1) % this.matrix.length;
		String queueName = this.routage.getChannelName(destinationID);
		node.sendMessage(queueName, new Message(this.node.getID(), destinationID, msg));
	}
	
	public void sendRight(String msg)
	{
		int destinationID = (this.node.getID() + 1) % this.matrix.length;
		String queueName = this.routage.getChannelName(destinationID);
		node.sendMessage(queueName, new Message(this.node.getID(), destinationID, msg));
	}
}
