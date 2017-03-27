package routage;

import java.util.Arrays;

public class Routeur implements IRoutage {
	
	private int[][] matrix;
	private int[][] predecesseur;
	private int		n;
	private int		myId;
	private final static int UNREACHABLE = 1000;
	
	public Routeur(int id, final int[][] m){
		this.n 				= m.length;
		this.matrix 		= new int[n][n];
		cloneMatrix(m);
		this.predecesseur 	= new int[n][n];
		this.myId			= id;
		initializeMatrix();
	}
	
	private void cloneMatrix(final int[][] matrix2)
	{
		int[][] matrix = new int[matrix2.length][matrix2.length];
		for(int i = 0; i < matrix2.length; i++)
		{
			for(int j = 0; j < matrix2.length; j++)
			{
				matrix[i][j] = matrix2[i][j];
			}
		}
	}

	protected void initializeMatrix(){

		for(int i = 0; i<n; i++){
			for(int j = 0; j < n; j++){
				if(matrix[i][j] == 0){
					matrix[i][j] = UNREACHABLE;
				}
				if(i != j && matrix[i][j] < UNREACHABLE){
					predecesseur[i][j] = i;
				}
				else{
					predecesseur[i][j] = -1;
				}
			}
		}

		for(int k = 0; k < n; k++){
			for(int i = 0; i < n; i++){
				for(int j = 0; j < n; j++){
					//matrix[i][j] = Math.min(matrix[i][j], matrix[i][k] + matrix[k][j]);
					if(matrix[i][j] > matrix[i][k] + matrix[k][j]){
						matrix[i][j] = matrix[i][k] + matrix[k][j];
						predecesseur[i][j] = predecesseur[k][j];
					}
					
				}
			}
		}
		
		/*for(int i = 0; i < n; i++){
			System.out.println("result = " + Arrays.toString(predecesseur[i]));
		}*/
		
	}

	@Override
	public String getChannelName(int destinationID) {
		int res = destinationID;
		while(predecesseur[myId][destinationID] > -1){
			res = destinationID;
			destinationID = predecesseur[myId][destinationID];
		}
		if(res < myId)
		{
			return Integer.toString(res) + "Q" + Integer.toString(myId);
		}
		else if(res > myId){
			return Integer.toString(myId) + "Q" + Integer.toString(res);
		}
		return null;
	}
}
