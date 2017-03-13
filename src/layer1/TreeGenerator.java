package layer1;
import java.util.ArrayList;
import java.util.List;

public class TreeGenerator
{
	public List<Node> getTree(int[][] matrix)
	{
		List<Node> nodes = new ArrayList<Node>();
		for(int i = 0; i < matrix.length; i++)
		{
			nodes.add(new Node(i));
		}
		
		
		for(int x = 0; x < matrix.length; x++)
		{
			for(int y = 0; y < matrix[x].length; y++)
			{
				int i = matrix[x][y];
				
				if(x != y && i == 1) // Connection entre nodes x et y
				{
					nodes.get(x).addNeighbor(nodes.get(y));
				}
			}
		}
		return nodes;
	}
}
