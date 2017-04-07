package overlay;

public class MQueue
{
	public String name;
	public int source;
	
	/**
	 * Construit un nouveau MQueue représentant les informations d'une queue RabbitMQ (unidirectionnel)
	 * @param name Le nom de la queue
	 * @param source Le noeud origine, permettant de connaître le sens de communication de la queue
	 */
	public MQueue(String name, int source){
		this.name = name;
		this.source = source;
	}

	/**
	 * Obtient le nom de la queue
	 * @return Le nom de la queue
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * L'iendetifiant du noeud source
	 * @return
	 */
	public int getSource()
	{
		return this.source;
	}
}
