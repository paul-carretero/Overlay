package overlay;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import client.RingNode;

public class OverlayProjectLauncher
{
	private static List<RingNode> clients = new ArrayList<RingNode>();
	
	public static void main(String[] args)
	{
		// TODO: Charger la matrice donnée
		//int[][] matrix = new int[5][5];
		final int[][] matrix = new int[][] {
			new int[] {1,1,0,0,1,0},
			new int[] {1,1,1,0,1,0},
			new int[] {0,1,1,1,0,0},
			new int[] {0,0,1,1,1,1},
			new int[] {1,1,0,1,1,0},
			new int[] {0,0,0,1,0,1}};
		
		for(int i = 0; i < matrix.length; i++)
		{
			for(int x = 0; x < matrix.length; x++)
				System.err.print(matrix[i][x]);
			System.out.println();
			clients.add(new RingNode(i, matrix));
		}
		
		for(RingNode c : clients)
		{
			c.start();
		}
	}
}
