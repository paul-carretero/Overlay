package overlay;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeoutException;

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

public class NodeMQ extends Thread implements Consumer
{
	private List<String> queues;
	private List<MessageListener> listeners;
	private int id;
	private Channel channel;
	
	private RingNode node;
	private final Semaphore semaphore;
	
	public NodeMQ(int i, int[][] matrix, final Semaphore semaphore) throws IOException, TimeoutException
	{
		this.queues = new ArrayList<String>();
		this.id = i;
		this.listeners = new ArrayList<MessageListener>();
		
	    ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    Connection connection = factory.newConnection();
	    this.channel = connection.createChannel();
	    
	    this.node = new RingNode(i, new NetworkHandler(this, matrix));
	    this.semaphore = semaphore;
	    
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
	
	public void addNeighbor(int id)
	{
		// Convention de nommage des queues de communication entre chaque noeud
		String queueName = (this.id > id) ? id + "Q" + this.id : this.id + "Q" + id;
		this.queues.add(queueName);
	}
	
	public int getID()
	{
		return this.id;
	}
	
	@Override
	public void run()
	{
		this.node.load();
		
		semaphore.release();
		
		while(!this.isInterrupted())
		{
			// TODO : DO SOMETHING
		}
	}
	
	public void addListener(MessageListener listener)
	{
		this.listeners.add(listener);
	}
	
	public void sendMessage(String queue, Message message)
	{
		try
		{
			channel.basicPublish("", queue, null, message.toString().getBytes());
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
		try
		{
			Message msg = Message.deserialize(new String(body, "UTF-8"));
			System.out.println("[NODEMQ] Je suis le node " + id + " et j'ai reçu le message : " + msg.getMessage());
			this.listeners.forEach(x -> x.receive(msg));
		}
		catch (ClassNotFoundException e)
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
