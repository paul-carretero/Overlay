package client;

import java.io.StringWriter;
import org.json.JSONException;
import org.json.JSONObject;

public class RingMessage
{
	private int sourceNode;
	private int	destinationNode;
	private String message;
	
	public RingMessage(int sourceNode, int destinationNode, String message){
		this.sourceNode = sourceNode;
		this.destinationNode = destinationNode;
		this.message = message;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public void setMessage(String message)
	{
		this.message = message;
	}
	
	public int getDestinationNode()
	{
		return destinationNode;
	}
	
	public void setDestinationNode(int destinationNode)
	{
		this.destinationNode = destinationNode;
	}
	
	public int getSourceNode()
	{
		return sourceNode;
	}
	
	public void setSourceNode(int sourceNode)
	{
		this.sourceNode = sourceNode;
	}
	
	public String serialize()
	{
		JSONObject obj = new JSONObject();
		try
		{
			obj.put("sourceNode", this.sourceNode);
			obj.put("destinationNode", this.destinationNode);
			obj.put("message", this.message);
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
	
	public static RingMessage deserialize(String json) throws JSONException
	{
		JSONObject obj = new JSONObject(json);
		int sourceNode = obj.getInt("sourceNode");
		int destinationNode = obj.getInt("destinationNode");
		String message = obj.getString("message");
		
		return new RingMessage(sourceNode, destinationNode, message);
	}
}
