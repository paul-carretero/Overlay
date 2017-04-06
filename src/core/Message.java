package core;

import java.io.Serializable;
import java.io.StringWriter;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Classe permettant d'encapsuler un message (chaine de caractère) afin de le transporter sur le réseau physique
 * Chaque message possède un emetteur et un recepteur
 */
public class Message implements Serializable
{
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * ID de l'Emetteur du message
	 */
	private int 	sender;
	/**
	 * ID du destinataire du message
	 */
	private int 	destination;
	/**
	 * Message (eventuellement serializé)
	 */
	private String 	message;
	
	/**
	 * @param from ID de l'Emetteur du message
	 * @param to ID du destinataire du message
	 * @param message Message (eventuellement serializé)
	 */
	public Message(int from, int to, String message)
	{
		this.sender = from;
		this.destination = to;
		this.message = message;
	}
	
	@Override
	public String toString(){
		return "[ SENDER = " + this.sender + " DESTINATION = " + this.destination + " MESSAGE = " + this.message + " ]";
	}

	/**
	 * @return l'ID de l'emmeteur du message
	 */
	public int getSender()
	{
		return this.sender;
	}
	
	/**
	 * @return la chaine de caractère correspondant aux données du message
	 */
	public String getMessage()
	{
		return this.message;
	}
	
	/**
	 * @return l'ID du noeud auquel le message est adressé
	 */
	public int getDestination()
	{
		return this.destination;
	}
	
	/**
	 * Converti ce message en string grâce à la bibliothèque JSON
	 * Permet d'envoyer ce message sous forme de chaine sur le réseau
	 * @return La chaine JSON associée à ce message
	 */
	public String serialize()
	{
		JSONObject obj = new JSONObject();
		try
		{
			obj.put("from", this.sender);
			obj.put("to", this.destination);
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
	 * @param json une instance de la classe Message serializé en JSON
	 * @return une nouvelle instance de Message correspondant aux données présente dans la chaine JSON passé en paramètre
	 * @throws JSONException
	 * @see JSONObject
	 */
	public static Message deserialize(String json) throws JSONException
	{
		JSONObject obj = new JSONObject(json);
		int sender = obj.getInt("from");
		int destination = obj.getInt("to");
		String message = obj.getString("message");
		
		return new Message(sender, destination, message);
	}
}
