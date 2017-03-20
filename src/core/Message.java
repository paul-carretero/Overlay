package core;
import java.io.Serializable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class Message implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private int 	sender;
	//private int 	originalSender; // A employer dans le layer 2
	private int 	destination;
	private String 	message;
	private boolean	ripMessage;
	
	/**
	 * @param sender
	 * @param origin
	 * @param message
	 */
	public Message(int from, int to, String message, boolean ripMessage)
	{
		this.ripMessage = ripMessage;
		//this.setOriginalSender(from);
		this.sender = from;
		this.destination = to;
		this.message = message;
	}

	public int getSender()
	{
		return sender;
	}

	/*public int getOriginalSender()
	{
		return originalSender;
	}

	public void setOriginalSender(int origin)
	{
		this.originalSender = origin;
	}*/

	public String getMessage()
	{
		return message;
	}
	
	public int getDestination()
	{
		return this.destination;
	}
	
	public boolean isRipMessage() 
	{
		return ripMessage;
	}
	
	/*public String toString()
	{
		JSONObject obj = new JSONObject();
		try
		{
			obj.put("sender", sender);
			obj.put("origin", originalSender);
			obj.put("message", message);
			StringWriter sw = new StringWriter();
			obj.write(sw);
			return sw.toString();
		}
		catch (JSONException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static Message decodeJSON(String json) throws JSONException
	{
		JSONObject obj = new JSONObject(json);
		int sender = obj.getInt("sender");
		int origin = obj.getInt("origin");
		String message = obj.getString("message");
		
		return new Message(sender, origin, message);
	}*/
	
    public String serialize() throws IOException {
        ByteArrayOutputStream arrayOutputStream	= new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream 		= new GZIPOutputStream(arrayOutputStream);
        ObjectOutputStream objectOutputStream 	= new ObjectOutputStream(gzipOutputStream);
        
		objectOutputStream.writeObject(this);
		objectOutputStream.flush();
		
		return new String(Base64.encode(arrayOutputStream.toByteArray()));
    }

    public static Message deserialize(String msg) throws IOException, ClassNotFoundException {
		ByteArrayInputStream arrayInputStream	= new ByteArrayInputStream(Base64.decode(msg));
        GZIPInputStream gzipInputStream			= new GZIPInputStream(arrayInputStream);
        ObjectInputStream objectInputStream 	= new ObjectInputStream(gzipInputStream);
        Object o 								= objectInputStream.readObject();
        
    	if(o instanceof Message){
    		return (Message) o;
    	}
    	return null;
    }
}
