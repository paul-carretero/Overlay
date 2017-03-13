package layer1;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;

import core.Message;

public class ClientThread extends Thread implements Consumer
{
	private String queueNameLeft;
	private String queueNameRight;
	private int id;
	private Channel channel;
	
	public ClientThread(int i, String left, String right) throws IOException, TimeoutException
	{
		this.queueNameLeft = left;
		this.queueNameRight = right;
		this.id = i;
		
	    ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("localhost");
	    Connection connection = factory.newConnection();
	    this.channel = connection.createChannel();
	    
	}
	
	@Override
	public void run()
	{
		try
		{
			channel.queueDeclare(queueNameLeft, false, false, false, null);
			channel.queueDeclare(queueNameRight, false, false, false, null);
			this.channel.basicConsume(queueNameLeft, true, this);
			
			if(this.id == 0)
			{
				sendMsg(this.id, "test");
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//public void sendLeft
	public void sendRight(int origin, String msg) throws IOException{
		Message m = new Message(this.id, origin, msg);
		
	    channel.basicPublish("", queueNameRight, null, m.toString().getBytes());
	    System.out.println(" [x] Sent '" + msg + "' [" + this.queueNameRight + "]");
	    
	}
	
	@Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException 
	{
		
		Message msg;
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
