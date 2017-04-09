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
			
			if(args.length == 0)
			{
				System.err.println("L'application attends un argument correspondant" +
						" au nom du fichier contenant les informations de la matrice à virtualiser.\n" +
						"Exemple : \n java -jar Overlay.jar matrices/matrice1.overlay");
				System.exit(0);
			}
			
			// Chargement de la matrice à partir du fichier
			matrix = MatrixReader.readFile(args[0]);
			
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
