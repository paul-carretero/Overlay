package overlay;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import client.RingNode;
import core.NetworkHandler;

public class OverlayProjectLauncher
{
	private static List<NodeMQ> concreteNetwork = new ArrayList<NodeMQ>();
	
	private final static Semaphore SEMAPHORE = new Semaphore(0);
	
	public static void main(String[] args)
	{
		// TODO: Charger la matrice donnée
		//int[][] matrix = new int[5][5];
		int[][] matrix = new int[][] {
			new int[] {1,1,0,0,1,0},
			new int[] {1,1,1,0,1,0},
			new int[] {0,1,1,1,0,0},
			new int[] {0,0,1,1,1,1},
			new int[] {1,1,0,1,1,0},
			new int[] {0,0,0,1,0,1}};
		
		try
		{
			generateConcreteNetwork(matrix); // Génération du réseau "réel"
			
			startOverlay(); // Mise en route du réseau
		}
		catch (IOException | TimeoutException e)
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}
	
	public static void generateConcreteNetwork(int[][] matrix) throws IOException, TimeoutException
	{
		for(int i = 0; i < matrix.length; i++)
		{
			concreteNetwork.add(new NodeMQ(i, matrix, SEMAPHORE));
		}
		
		for(int x = 0; x < matrix.length; x++)
		{
			for(int y = 0; y < matrix[x].length; y++)
			{
				int i = matrix[x][y];
				
				if(x != y && i == 1) // Connection entre nodes x et y (RabbitMQ)
				{
					concreteNetwork.get(x).addNeighbor(concreteNetwork.get(y).getID());
				}
			}
		}
	}
	
	public static void startOverlay()
	{
		// Lancement des threads (noeuds)
		for(NodeMQ concreteNode : concreteNetwork)
		{
			try
			{
				concreteNode.start();
				SEMAPHORE.acquire();
			}
			catch (InterruptedException e)
			{
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
