package routage;

/**
 * 
 */
public class Routeur implements IRoutage {
	
	/**
	 * Matrice d'adjacence (�galement la matrice sur laquelle on effectuera l'algorithme de Floyd-Warshall)
	 */
	private int[][] matrix;
	/**
	 * Matrice de pr�c�dence utilis� pour obtenir le chemin le plus court d'un point � un autre
	 */
	private int[][] predecesseur;
	/**
	 * Taille de la matrice
	 */
	private int		n;
	/**
	 * ID du noeux consid�r�
	 */
	private int		myId;
	/**
	 * Valeur consid�r�e comme infinie (singifie qu'il n'y a pas de chemin vers ce noeud en fonction du noeud consid�r�)
	 */
	private final static int UNREACHABLE = 1000;
	
	/**
	 * initialise l'objet Routeur permettant d'obtenir le nom de la file sRappitMQ sur laquelle envoyer des informations
	 * @param id l'ID du node actuel
	 * @param m une matrice d'adjacence
	 */
	public Routeur(int id, int[][] m){
		this.n 				= m.length;
		this.matrix 		= new int[this.n][this.n];
		cloneMatrix(m);
		this.predecesseur 	= new int[this.n][this.n];
		this.myId			= id;
		initializeMatrix();
	}
	
	/**
	 * clone la matrice pass�e en param�tre dans la variable matrix
	 * @param matrix2 une matrice d'adjacence
	 */
	private void cloneMatrix(final int[][] matrix2)
	{
		for(int i = 0; i < matrix2.length; i++)
		{
			for(int j = 0; j < matrix2.length; j++)
			{
				this.matrix[i][j] = matrix2[i][j];
			}
		}
	}

	/**
	 * applique l'algorithme de Floyd-Warshall sur la matrice d'adjacence. la matrice de "pr�c�dence" est �galement cr��e pour garder en m�moire le chemin le plus court
	 */
	private void initializeMatrix(){
		for(int i = 0; i<this.n; i++){
			for(int j = 0; j < this.n; j++){
				if(this.matrix[i][j] == 0){
					this.matrix[i][j] = UNREACHABLE;
				}
				if(i != j && this.matrix[i][j] < UNREACHABLE){
					this.predecesseur[i][j] = i;
				}
				else{
					this.predecesseur[i][j] = -1;
				}
			}
		}

		for(int k = 0; k < this.n; k++){
			for(int i = 0; i < this.n; i++){
				for(int j = 0; j < this.n; j++){
					if(this.matrix[i][j] > this.matrix[i][k] + this.matrix[k][j]){
						this.matrix[i][j] = this.matrix[i][k] + this.matrix[k][j];
						this.predecesseur[i][j] = this.predecesseur[k][j];
					}
				}
			}
		}		
	}

	/**
	 * @see routage.IRoutage#getChannelName(int)
	 */
	@Override
	public String getChannelName(int destinationID) {
		
		int res = destinationID;
		int destID = destinationID;
		
		while(this.predecesseur[this.myId][destID] > -1){
			res = destID;
			destID = this.predecesseur[this.myId][destID];
		}

		if(this.predecesseur[this.myId][res] > -1 )
		{
			return Integer.toString(this.myId) + "Q" + Integer.toString(res);
		}
		
		return null;
	}
}
