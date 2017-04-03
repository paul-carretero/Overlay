package overlay;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import org.json.JSONException;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

import core.Message;
import core.MessageListener;
import routage.IRoutage;

public class NodeMQ implements Consumer
{
	private List<MQueue>			queues;
	private List<MessageListener>	listeners;
	private final int 				id;
	private Channel 				channel;
	private IRoutage 				routeur;
	
	public NodeMQ(int id, final int[][] matrix, IRoutage routage) throws IOException, TimeoutException
	{
		this.id 		= id;
		this.listeners 	= new ArrayList<MessageListener>();
		this.routeur 	= routage;
		
	    ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    Connection connection = factory.newConnection();
	    
	    this.channel 	= connection.createChannel();
	    queues 			= new ArrayList<MQueue>();
	    
	    initializeQueues(matrix[id]);
	    
	    try
		{
			for(MQueue queue : this.queues) 
			{
				this.channel.queueDeclare(queue.getName(), false, false, false, null);
				if(queue.getSource() != this.id)
				{
					this.channel.basicConsume(queue.getName(), true, this);
				}
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
		this.queues.add(new MQueue(this.id + "Q" + id , this.id));
		this.queues.add(new MQueue(id + "Q" + this.id , id));
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
		if(queue != null)
		{
			try
			{
				channel.basicPublish("", queue, null, message.serialize().getBytes());
				//System.out.println(" [x] Sent '" + message.getMessage() + "' [" + queue + "]");
			}
			catch (IOException e)
			{
				System.err.println("[SendMessage] Error : " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	@Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException 
	{
		Message msg;
		try
		{
			msg = Message.deserialize(new String(body, "UTF-8"));
			for(MessageListener l : listeners){
				if(msg.getDestination() == this.id){
					l.receive(msg.getMessage());
				}
				else{
					String queue = routeur.getChannelName(msg.getDestination());
					System.out.println("[FORWARD] nodeID = " + id + "[msg.from = " + msg.getSender() + " msg.to = "+ msg.getDestination() +"]");
					sendMessage(queue, msg);
				}
			}
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
