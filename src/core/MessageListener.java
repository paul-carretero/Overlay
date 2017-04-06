package core;

/**
 * Interface implémentée par l'application client afin de recevoir les messages reçu par la couche réseau
 */
public interface MessageListener
{
	/**
	 * @param string chaine correspondant à un message à destination du noeud courrant
	 */
	public void receive(String string);
}
