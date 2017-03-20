package overlay;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

//import org.json.JSONException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

import core.Message;
import core.MessageListener;

public class NodeMQ extends Thread implements Consumer
{
	private List<String> queues;
	private List<MessageListener> listeners;
	private int id;
	private Channel channel;
	
	public NodeMQ(int i) throws IOException, TimeoutException
	{
		this.queues = new ArrayList<String>();
		this.id = i;
		
		this.listeners = new ArrayList<MessageListener>();
		
	    ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    Connection connection = factory.newConnection();
	    this.channel = connection.createChannel();
	}
	
	public void addNeighbor(int id)
	{
		// Convention de nommage des queues de communication entre chaque noeud
		String queueName = (this.id > id) ? id + "q" + this.id : this.id + "q" + id;
		this.queues.add(queueName);
	}
	
	public int getID()
	{
		return this.id;
	}
	
	@Override
	public void run()
	{
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
	
	public void addListener(MessageListener listener)
	{
		this.listeners.add(listener);
	}
	
	public void sendMessage(String queue, Message message) throws IOException
	{
		channel.basicPublish("", queue, null, message.toString().getBytes());
	    System.out.println(" [x] Sent '" + message.getMessage() + "' [" + queue + "]");
	}
	
	@Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException 
	{
		try
		{
			Message msg = Message.deserialize(new String(body, "UTF-8"));
			this.listeners.forEach(x -> x.receive(msg));
		}
		catch (ClassNotFoundException e)
		{
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
