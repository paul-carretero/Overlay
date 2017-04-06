package core;

/**
 * Interface impl�ment�e par l'application client afin de recevoir les messages re�u par la couche r�seau
 */
public interface MessageListener
{
	/**
	 * @param string chaine correspondant � un message � destination du noeud courrant
	 */
	public void receive(String string);
}
