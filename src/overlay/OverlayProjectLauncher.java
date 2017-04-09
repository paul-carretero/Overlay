package overlay;

import java.net.ConnectException;
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
			String file = "matsrices/matrice2.overlay"; // Fichier par défaut
			
			if(args.length > 0)
				file = args[0];
			
			// Chargement de la matrice à partir du fichier
			matrix = MatrixReader.readFile(file);
			
			// Création des noeuds utilisateur (ring)
			for(int i = 0; i < matrix.length; i++)
			{
				clients.add(new RingNode(i, matrix));
			}
			
			// Lancement des noeud (démarrage des communications)
			for(RingNode c : clients)
			{
				c.start();
			}
			
		}
		catch(ConnectException e)
		{
			System.err.println("Connection échouée, verifiez si le serveur RabbitMQ est actif");
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
			//e.printStackTrace();
		}
	}
}
