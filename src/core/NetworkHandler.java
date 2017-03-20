package core;

import overlay.NodeMQ;

public class NetworkHandler
{
	private NodeMQ node;
	private int[][] matrix;
	
	public NetworkHandler(NodeMQ node, int[][] matrix)
	{
		this.node = node;
		this.matrix = matrix;
	}
	
	public void listener(MessageListener s)
	{
		
	}
	
	public void sendLeft(String msg)
	{
		
	}
	
	public void sendRight(String msg)
	{
		
	}
}
