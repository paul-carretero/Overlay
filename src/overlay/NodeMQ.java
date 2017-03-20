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

public class NodeMQ extends Thread implements Consumer
{
	private List<String> queues;
	private int id;
	private Channel channel;
	
	public NodeMQ(int i) throws IOException, TimeoutException
	{
		this.queues = new ArrayList<String>();
		this.id = i;
		
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
	
	/*public void send(int origin, String msg) throws IOException
	{
		Message m = new Message(this.id, origin, msg);
		
	    channel.basicPublish("", queueNameRight, null, m.toString().getBytes());
	    System.out.println(" [x] Sent '" + msg + "' [" + this.queueNameRight + "]");
	    
	}*/
	
	@Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException 
	{
		
		/*Message msg;
		try
		{
			msg = Message.decodeJSON(new String(body, "UTF-8"));
			System.out.println(" [x] Received '" + msg.getMessage() + "' OriginID: " + msg.getOriginalSender());
		    
		    if(this.id != msg.getOriginalSender()) // Il ne s'agit pas de la reception de mon propre message
		    	sendMsg(msg.getOriginalSender(), msg.getMessage());
		}
		catch (JSONException e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
		}*/
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
