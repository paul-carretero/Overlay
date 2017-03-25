package routage;

import java.util.Arrays;

public class Routeur implements IRoutage {
	
	private int[][] matrix;
	private int[][] predecesseur;
	private int		n;
	private int		myId;
	
	public Routeur(int id, int[][] matrix){
		this.matrix 		= matrix;
		this.n 				= matrix.length;
		this.predecesseur 	= new int[n][n];
		this.myId			= id;
		initializeMatrix();
	}
	
	protected void initializeMatrix(){

		for(int i = 0; i<n; i++){
			for(int j = 0; j < n; j++){
				if(matrix[i][j] == 0){
					matrix[i][j] = 1000;
				}
				if(i != j && matrix[i][j] < 1000){
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
		
		for(int i = 0; i < n; i++){
			System.out.println("result = " + Arrays.toString(predecesseur[i]));
		}
		
	}

	@Override
	public int getChannelName(int destinationID) {
		int res = destinationID;
		while(predecesseur[myId][destinationID] > -1){
			res = destinationID;
			destinationID = predecesseur[myId][destinationID];
		}
		return res;
	}
}