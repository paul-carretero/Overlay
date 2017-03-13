package layer1;
import java.util.ArrayList;
import java.util.List;

public class Node
{
	private List<Node> neighbors;
	private int id;
	
	public Node(int id)
	{
		this.neighbors = new ArrayList<Node>();
	}
	
	public void addNeighbor(Node node)
	{
		this.neighbors.add(node);
	}
	
	public List<Node> getNeighbors()
	{
		return this.neighbors;
	}
	
	public int getId()
	{
		return this.id;
	}
}
