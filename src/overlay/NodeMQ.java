package overlay;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;

//import org.json.JSONException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

import client.RingNode;
import core.Message;
import core.MessageListener;
import core.NetworkHandler;

public class NodeMQ implements Consumer
{
	private List<String> queues;
	private List<MessageListener> listeners;
	private int id;
	private Channel channel;
	
	public NodeMQ(int id, final int[][] matrix) throws IOException, TimeoutException
	{
		this.id = id;
		this.listeners = new ArrayList<MessageListener>();
		
	    ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    Connection connection = factory.newConnection();
	    this.channel = connection.createChannel();
	    
	    queues = new ArrayList<String>();
	    initializeQueues(matrix[id]);
	    
	    System.err.println("NODEMQ CONSTRUCTOR");
	    System.out.println(Arrays.toString(matrix[id]));
	    
	    try
		{
			for(String queue : this.queues) 
			{
				this.channel.queueDeclare(queue, false, false, false, null);
				this.channel.basicConsume(queue, true, this);
			}
		}
		catch (IOException e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void initializeQueues(int[] matrixLine)
	{
	
		for(int i = 0; i < matrixLine.length; i++)
		{
			if(matrixLine[i] == 1 && this.id != i)
			{
				addNeighbor(i);
			}
		}
	}

	public void addNeighbor(int id)
	{
		// Convention de nommage des queues de communication entre chaque noeud
		String queueName = (this.id > id) ? id + "Q" + this.id : this.id + "Q" + id;
		
		System.out.println("Queue : " + queueName);
		
		this.queues.add(queueName);
	}
	
	public int getID()
	{
		return this.id;
	}
	
	public void addListener(MessageListener listener)
	{
		this.listeners.add(listener);
	}
	
	public void sendMessage(String queue, Message message)
	{
		try
		{
			channel.basicPublish("", queue, null, message.serialize().getBytes());
			System.out.println(" [x] Sent '" + message.getMessage() + "' [" + queue + "]");
		}
		catch (IOException e)
		{
			System.err.println("[SendMessage] Error : " + e.getMessage());
			e.printStackTrace();
		}
	    
	}
	
	@Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException 
	{
		Message msg;
		try
		{
			msg = Message.deserialize(new String(body, "UTF-8"));
			//System.out.println("[NODEMQ] Je suis le node " + id + " et j'ai reçu le message : " + msg.getMessage());
			this.listeners.forEach(x -> x.receive(msg));
		}
		catch (JSONException e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
    }
	
	@Override
	public void handleCancel(String arg0) throws IOException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleCancelOk(String arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleConsumeOk(String arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleRecoverOk(String arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleShutdownSignal(String arg0, ShutdownSignalException arg1)
	{
		// TODO Auto-generated method stub
		
	}
}
