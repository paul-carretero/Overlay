package core;
import java.io.Serializable;
import java.io.StringWriter;

import org.json.JSONException;
import org.json.JSONObject;

public class Message implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private int sender;
	private int originalSender;
	private int destination;
	private String message;
	private MessageType type;
	
	/**
	 * @param sender
	 * @param origin
	 * @param message
	 */
	public Message(int from, int to, String message)
	{
		this.type = MessageType.MSG;
		this.setOriginalSender(from);
		this.sender = from;
		this.destination = to;
		this.message = message;
	}

	public int getSender()
	{
		return sender;
	}

	public int getOriginalSender()
	{
		return originalSender;
	}

	public void setOriginalSender(int origin)
	{
		this.originalSender = origin;
	}

	public String getMessage()
	{
		return message;
	}
	
	public int getDestination()
	{
		return this.destination;
	}
	
	public String toString()
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
	}
}
