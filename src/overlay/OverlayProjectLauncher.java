package overlay;

import java.util.ArrayList;
import java.util.List;
import client.RingNode;

public class OverlayProjectLauncher
{
	private static List<RingNode> clients = new ArrayList<RingNode>();
	
	public static void main(String[] args)
	{		
		try
		{
			int[][] matrix;
			
			// Chargement de la matrice � partir du fichier
			matrix = MatrixReader.readFile("matrices/matrice2.overlay");
			
			// Cr�ation des noeuds utilisateur (ring)
			for(int i = 0; i < matrix.length; i++)
			{
				clients.add(new RingNode(i, matrix));
			}
			
			// Lancement des noeud (d�marrage des communications)
			for(RingNode c : clients)
			{
				c.start();
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
