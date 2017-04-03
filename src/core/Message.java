package core;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class Message implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private int 	sender;
	private int 	destination;
	private String 	message;
	
	/**
	 * @param sender
	 * @param origin
	 * @param message
	 */
	public Message(int from, int to, String message)
	{
		this.sender = from;
		this.destination = to;
		this.message = message;
	}
	
	@Override
	public String toString(){
		return "[ SENDER = " + sender + " DESTINATION = " + destination + " MESSAGE = " + message + " ]";
	}

	public int getSender()
	{
		return sender;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public int getDestination()
	{
		return this.destination;
	}
	
	public String serialize()
	{
		JSONObject obj = new JSONObject();
		try
		{
			obj.put("from", sender);
			obj.put("to", destination);
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
	
	public static Message deserialize(String json) throws JSONException
	{
		JSONObject obj = new JSONObject(json);
		int sender = obj.getInt("from");
		int destination = obj.getInt("to");
		String message = obj.getString("message");
		
		return new Message(sender, destination, message);
	}
}
