package overlay;

import java.util.ArrayList;
import java.util.List;
import client.RingNode;

public class OverlayProjectLauncher
{
	private static List<RingNode> clients = new ArrayList<RingNode>();
	
	public static void main(String[] args)
	{		
		MatrixReader mr = new MatrixReader();
		
		try
		{
			int[][] matrix;
			matrix = mr.readFile("matrices/matrice2.overlay");
			for(int i = 0; i < matrix.length; i++)
			{
				clients.add(new RingNode(i, matrix));
			}
			
			for(RingNode c : clients)
			{
				c.start();
			}
			
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
