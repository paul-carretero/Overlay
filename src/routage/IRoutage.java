package routage;

/**
 * Interface pour le syst�me de routage. Permet d'obtenir la queue RabbitMQ sur laquelle envoyer un message en fonction de la destination
 */
public interface IRoutage {
	/**
	 * @param destinationID ID du node vers lequel on souhaite envoyer un message
	 * @return le nom de la queue sur laquelle envoyer le message OU null si loopback ou chemin non trouv�
	 */
	public String getChannelName(int destinationID);
}