package overlay;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import client.RingNode;
import core.NetworkHandler;

public class OverlayProjectLauncher
{
	private static List<NodeMQ> concreteNetwork = new ArrayList<NodeMQ>();
	private static List<RingNode> ring = new ArrayList<RingNode>();
	
	public static void main(String[] args)
	{
		// TODO: Charger la matrice donnée
		int[][] matrix = new int[5][5];
		
		try
		{
			generateConcreteNetwork(matrix);
			generateRingAbstractNetwork(matrix);
			
			start();
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
			concreteNetwork.add(new NodeMQ(i));
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
	
	public static void generateRingAbstractNetwork(int matrix[][])
	{
		for(int i = 0; i < concreteNetwork.size(); i++)
		{
			RingNode node = new RingNode(i, new NetworkHandler(concreteNetwork.get(i), matrix));
		}
	}
	
	public static void start()
	{
		concreteNetwork.forEach(x -> x.run());
	}
}
