package client;

import core.MessageListener;

import java.util.Arrays;

import core.Message;
import core.NetworkHandler;

public class RingNode extends Thread implements MessageListener
{
	private int id;
	private NetworkHandler network;
	
	public RingNode(int id,final int[][] matrix)
	{
		this.id = id;
		
		this.network = new NetworkHandler(id, matrix);
		this.network.setListener(this);
	}
	
	@Override
	public void run()
	{
		this.network.sendRight("test id = " + id);
		
		while(!interrupted())
		{
			syncWait();
		}
	}
	
	synchronized private void syncWait()
	{
		try
		{
			wait(500);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}


	@Override
	public void receive(Message msg)
	{
		System.out.println("Je suis le node " + id + " et j'ai reçu le message : " + msg.getMessage());
		//traitement ...
	}
}
