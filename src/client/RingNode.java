package client;

import core.MessageListener;
import org.json.JSONException;
import core.NetworkHandler;

/**
 * Thread représentant un Noeud du Ring
 * Cette classe regroupe les fonctions offerte à une application utilisateur dans le contexte d'une communication en anneau.
 */
public class RingNode extends Thread implements MessageListener
{
	/**
	 * ID du noeud courrant
	 */
	private int					id;
	/**
	 * Objet bas niveau permettant une abstraction du réseau physique
	 */
	private NetworkHandler		network;
	/**
	 * constante indiquant que le message est en broadcast
	 */
	private static final int	BROADCAST	= -1;
	/**
	 * constante indiquant que le message est destiné au voisin directe du noeud considéré (sans considération de l'ID).
	 */
	private static final int	BASIC_SEND	= -2;
	
	/**
	 * Instancie un Thread utilisateur.
	 * Instancie également un gestionnaire bas niveau du réseau qui gère les communications.
	 * @param id ID du node courrant
	 * @param matrix matrice d'adjacence
	 */
	public RingNode(int id,final int[][] matrix)
	{
		this.id			= id;
		this.network	= new NetworkHandler(id, matrix);
		
		this.network.setListener(this);
	}
	
	@Override
	public void run()
	{
		while(!interrupted())
		{
			syncWait();
			if(this.id == 0)
			{
				broadcast("test ?");
			}
		}
	}
	
	/**
	 * Méthode affichant un message utilisateur reçu, affiche également la source du message et l'ID du node courrant
	 * @param rm un message utilisateur à afficher
	 */
	public void messageDisplay(RingMessage rm)
	{
		System.out.println("[NODE ID = " + rm.getSourceNode() + " ] -> [ME ID = " + this.id + " ] " + rm.getMessage());
	}
	
	/**
	 * Méthode permettant d'envoyer un message utilisateur au node à droite du node actuel sur le RING
	 * @param message un message utilisateur à envoyer
	 */
	public void sendRight(String message)
	{
		RingMessage rm = new RingMessage(this.id, BASIC_SEND , message);
		this.network.sendRight(rm.serialize());
	}
	
	/**
	 * Méthode permettant d'envoyer un message utilisateur au node à gauche du node actuel sur le RING
	 * @param message un message utilisateur à envoyer
	 */
	public void sendLeft(String message)
	{
		RingMessage rm = new RingMessage(this.id, BASIC_SEND , message);
		this.network.sendLeft(rm.serialize());
	}
	
	/**
	 * Méthode de l'application client permettant de transmettre un message à envoyer aux couche réseau inférieur.
	 * Serialize le message passé en paramètre et l'envoi à au node ayant l'ID passé en paramètre 
	 * @param to ID du node destination
	 * @param message un message haut niveau utilisé par l'application client
	 */
	public void sendTo(int to, String message)
	{
		RingMessage rm = new RingMessage(this.id, to , message);
		this.network.sendRight(rm.serialize());
	}
	
	/**
	 * Méthode permettant d'envoyer un message en Broadcast à tous les autres Node du RING
	 * @param message le message à envoyer en broadcast
	 */
	public void broadcast(String message){
		RingMessage rm = new RingMessage(this.id, BROADCAST , message);
		this.network.sendRight(rm.serialize());
	}
	
	/**
	 * Méthode gérant les broadcasts, les renvoyant au node suivant et le receptionnant si l'ID n'est pas identique à l'ID courrante
	 * @param rm un message du RING contenant les méta données d'un message
	 */
	private void broadcastHandler(RingMessage rm)
	{
		if(rm.getDestinationNode() == BROADCAST && rm.getSourceNode() != this.id){
			this.network.sendRight(rm.serialize());
			this.messageDisplay(rm);
		}
	}
	
	/**
	 * Méthode de l'application client permettant de transmettre un message à envoyer aux couche réseau inférieur.
	 * @param rm un message haut niveau utilisé par l'application client
	 */
	private void sendToHandler(RingMessage rm)
	{
		if(rm.getDestinationNode() != this.id){
			this.network.sendRight(rm.serialize());
		}
		else{
			this.messageDisplay(rm);
		}
	}
	
	/**
	 * Méthode de démonstration utilisé pour simuler des envois successifs.
	 */
	synchronized private void syncWait()
	{
		try
		{
			wait(3000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}


	/**
	 * permet de receptionner un message, de le dé-sérializer et d'appliquer le traitement approprié (forward au noeud suivant ou lecture par exemple)
	 */
	@Override
	public void receive(String msg)
	{
		RingMessage ringMessage = null;
		try
		{
			ringMessage = RingMessage.deserialize(msg);
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
		if(ringMessage != null){
			switch (ringMessage.getDestinationNode())
			{
			case BROADCAST:
				this.broadcastHandler(ringMessage);
				break;
			case BASIC_SEND:
				this.messageDisplay(ringMessage);
				break;
			default:
				this.sendToHandler(ringMessage);
				break;
			}
		}
		
	}
}
