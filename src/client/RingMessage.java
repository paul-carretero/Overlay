package client;

import java.io.StringWriter;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Repr�sente un message utilisateur avec ses m�ta-donn�es
 */
public class RingMessage
{
	/**
	 * ID du noeud emetteur du message
	 */
	private int sourceNode;
	
	/**
	 * ID du noeud destination du message
	 */
	private int	destinationNode;
	
	/**
	 * Message utilisateur
	 */
	private String message;
	
	/**
	 * @param sourceNode ID du noeud emetteur du message
	 * @param destinationNode ID du noeud destination du message
	 * @param message Message utilisateur
	 */
	public RingMessage(int sourceNode, int destinationNode, String message){
		this.sourceNode = sourceNode;
		this.destinationNode = destinationNode;
		this.message = message;
	}
	
	/**
	 * @return le message utilisateur
	 */
	public String getMessage()
	{
		return this.message;
	}
	
	/**
	 * @param message Message utilisateur
	 */
	public void setMessage(String message)
	{
		this.message = message;
	}
	
	/**
	 * @return L'ID du node destination
	 */
	public int getDestinationNode()
	{
		return this.destinationNode;
	}
	
	/**
	 * @return l'ID �m�tteur de ce message
	 */
	public int getSourceNode()
	{
		return this.sourceNode;
	}
	
	/**
	 * Converti ce RingMessage en string gr�ce � la biblioth�que JSON
	 * Permet d'envoyer ce message sous forme de chaine sur la couche r�seau
	 * @return La chaine JSON associ�e � ce RingMessage
	 */
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
	
	/**
	 * @param json une instance de la classe RingMessage serializ� en JSON
	 * @return une nouvelle instance d'un RingMessage (Message utilisateur) correspondant aux donn�es pr�sente dans la chaine JSON pass� en param�tre
	 * @throws JSONException 
	 * @see JSONObject
	 */
	public static RingMessage deserialize(String json) throws JSONException
	{
		JSONObject obj = new JSONObject(json);
		int sourceNode = obj.getInt("sourceNode");
		int destinationNode = obj.getInt("destinationNode");
		String message = obj.getString("message");
		
		return new RingMessage(sourceNode, destinationNode, message);
	}
}
