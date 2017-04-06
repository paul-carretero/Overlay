package core;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import overlay.NodeMQ;
import routage.IRoutage;
import routage.Routeur;

/**
 * Classe permettant d'abstraire la gestion du r�seau � la couche client.
 * Offre des m�thode permettant d'envoyer des messages au noeud pr�c�dent et suivant dans le RING virtuel.
 */
public class NetworkHandler
{
	/**
	 * Matrice d'adjacence du r�seau physique
	 */
	private int[][]		matrix;
	/**
	 * Interface de routage
	 * @see IRoutage
	 */
	private IRoutage	routage;
	/**
	 * Couche g�rant les commnications bas niveau avec le protocole RabbitMQ
	 * @see NodeMQ
	 */
	private NodeMQ		node;
	/**
	 * ID du noeux consid�r�
	 */
	private final int	myId;
	
	/**
	 * @param myId ID du node courrant
	 * @param matrix matrice d'adjacence repr�sentant le r�seau physique
	 */
	public NetworkHandler(int myId, final int[][] matrix)
	{
		this.myId = myId;
		this.matrix = matrix;
		this.routage = new Routeur(myId, matrix);
		
		try
		{
			this.node = new NodeMQ(myId, matrix, this.routage);
		}
		catch (IOException | TimeoutException e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * permet aux application client de s'ajouter afin de recevoir les message re�us par la couche physique
	 * @param s un objet client impl�mentant MessageListener
	 * @see MessageListener
	 */
	public void setListener(MessageListener s)
	{
		this.node.addListener(s);
	}
	
	/**
	 * M�thode permettant � une application client d'envoyer un message sur le noeud de gauche dans la topologie virtuelle du RING.
	 * @param msg message (�ventuellement s�rializ�) � envoyer
	 */
	public void sendLeft(String msg)
	{
		int destinationID = (this.myId - 1 + this.matrix.length) % this.matrix.length;
		String queueName = this.routage.getChannelName(destinationID);
		this.node.sendMessage(queueName, new Message(this.myId, destinationID, msg));
	}
	
	/**
	 * M�thode permettant � une application client d'envoyer un message sur le noeud de droite dans la topologie virtuelle du RING.
	 * @param msg message (�ventuellement s�rializ�) � envoyer
	 */
	public void sendRight(String msg)
	{
		int destinationID = (this.myId + 1) % this.matrix.length;
		String queueName = this.routage.getChannelName(destinationID);
		this.node.sendMessage(queueName, new Message(this.myId, destinationID, msg));
	}
}
