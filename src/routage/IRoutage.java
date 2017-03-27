package routage;

public interface IRoutage {
	/*
	 * @return le nom de la queue sur laquelle envoyer le message OU null si loopback
	 */
	public String getChannelName(int destinationID);
}