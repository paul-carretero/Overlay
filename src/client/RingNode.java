package client;

import core.MessageListener;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.json.JSONException;
import core.NetworkHandler;

/**
 * Thread repr�sentant un Noeud du Ring
 * Cette classe regroupe les fonctions offerte � une application utilisateur dans le contexte d'une communication en anneau.
 */
public class RingNode extends Thread implements MessageListener
{
	
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLUE = "\u001B[36m";
	public static final String ANSI_RED = "\u001B[31m";
	
	/**
	 * ID du noeud courrant
	 */
	private int					id;
	/**
	 * Objet bas niveau permettant une abstraction du r�seau physique
	 */
	private NetworkHandler		network;
	/**
	 * constante indiquant que le message est en broadcast
	 */
	private static final int	BROADCAST	= -1;
	/**
	 * constante indiquant que le message est destin� au voisin directe du noeud consid�r� (sans consid�ration de l'ID).
	 */
	private static final int	BASIC_SEND	= -2;
	
	/**
	 * Instancie un Thread utilisateur.
	 * Instancie �galement un gestionnaire bas niveau du r�seau qui g�re les communications.
	 * @param id ID du node courrant
	 * @param matrix matrice d'adjacence
	 * @throws TimeoutException 
	 * @throws IOException 
	 */
	public RingNode(int id,final int[][] matrix) throws IOException, TimeoutException
	{
		this.id			= id;
		this.network	= new NetworkHandler(id, matrix);
		
		this.network.setListener(this);
	}
	
	@Override
	public void run()
	{
		syncWait(1000);
		if(this.id == 0)
		{
			System.out.println("-------------------------");
			System.out.println("--- BROADCAST EXAMPLE ---");
			System.out.println("-------------------------");
			broadcast("This is a broadcasted message");
		}
		
		syncWait(20000);
		if(this.id == 5)
		{
			System.out.println("-------------------------");
			System.out.println("---  SENDTO EXAMPLE   ---");
			System.out.println("-------------------------");
			sendTo(3, "This message is from node 5 and it's send to node 3");
		}
		
		syncWait(20000);
		if(this.id == 0)
		{
			System.out.println("-------------------------");
			System.out.println("--- SENDLEFT EXAMPLE  ---");
			System.out.println("-------------------------");
			sendLeft("This message is from node 0 and it's leftsended (to node 5)");
		}
		
		syncWait(20000);
		if(this.id == 4)
		{
			System.out.println("-------------------------");
			System.out.println("--- SENDRIGHT EXAMPLE ---");
			System.out.println("-------------------------");
			sendRight("This message is from node 4 and it's rightsended (to node 5)");
		}
		syncWait(2000);
		System.out.println(ANSI_RED + "[Node ID = " + this.id + "] : [       FINISHED      ]" + ANSI_RESET);
	}
	
	/**
	 * M�thode affichant un message utilisateur re�u, affiche �galement la source du message et l'ID du node courrant
	 * @param rm un message utilisateur � afficher
	 */
	public void messageDisplay(RingMessage rm)
	{
		System.out.println("[Node ID = " + this.id + "] : [RECEIVED FROM NODE " + rm.getSourceNode() + " ] : " + rm.getMessage());
	}
	
	/**
	 * M�thode permettant d'envoyer un message utilisateur au node � droite du node actuel sur le RING
	 * @param message un message utilisateur � envoyer
	 */
	public void sendRight(String message)
	{
		RingMessage rm = new RingMessage(this.id, BASIC_SEND , message);
		this.network.sendRight(rm.serialize());
	}
	
	/**
	 * M�thode permettant d'envoyer un message utilisateur au node � gauche du node actuel sur le RING
	 * @param message un message utilisateur � envoyer
	 */
	public void sendLeft(String message)
	{
		RingMessage rm = new RingMessage(this.id, BASIC_SEND , message);
		this.network.sendLeft(rm.serialize());
	}
	
	/**
	 * M�thode de l'application client permettant de transmettre un message � envoyer aux couche r�seau inf�rieur.
	 * Serialize le message pass� en param�tre et l'envoi � au node ayant l'ID pass� en param�tre 
	 * @param to ID du node destination
	 * @param message un message haut niveau utilis� par l'application client
	 */
	public void sendTo(int to, String message)
	{
		RingMessage rm = new RingMessage(this.id, to , message);
		this.network.sendRight(rm.serialize());
	}
	
	/**
	 * M�thode permettant d'envoyer un message en Broadcast � tous les autres Node du RING
	 * @param message le message � envoyer en broadcast
	 */
	public void broadcast(String message){
		RingMessage rm = new RingMessage(this.id, BROADCAST , message);
		this.network.sendRight(rm.serialize());
	}
	
	/**
	 * M�thode g�rant les broadcasts, les renvoyant au node suivant et le receptionnant si l'ID n'est pas identique � l'ID courrante
	 * @param rm un message du RING contenant les m�ta donn�es d'un message
	 */
	private void broadcastHandler(RingMessage rm)
	{
		if(rm.getDestinationNode() == BROADCAST && rm.getSourceNode() != this.id){
			this.network.sendRight(rm.serialize());
			this.messageDisplay(rm);
		}
	}
	
	/**
	 * M�thode de l'application client permettant de transmettre un message � envoyer aux couche r�seau inf�rieur.
	 * @param rm un message haut niveau utilis� par l'application client
	 */
	private void sendToHandler(RingMessage rm)
	{
		if(rm.getDestinationNode() != this.id){
			this.network.sendRight(rm.serialize());
			System.out.println(ANSI_BLUE + "[Node ID = " + this.id + "] : [     RING-FORWARD    ] [FROM = " + rm.getSourceNode() + " TO = "+ rm.getDestinationNode() +"]" + ANSI_RESET);
		}
		else{
			this.messageDisplay(rm);
		}
	}
	
	/**
	 * M�thode de d�monstration utilis� pour simuler des envois successifs.
	 */
	synchronized private void syncWait(int n)
	{
		try
		{
			wait(n);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}


	/**
	 * permet de receptionner un message, de le d�-s�rializer et d'appliquer le traitement appropri� (forward au noeud suivant ou lecture par exemple)
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
