package overlay;

public class MQueue
{
	public String name;
	public int source;
	
	public MQueue(String name, int source){
		this.name = name;
		this.source = source;
	}

	public String getName()
	{
		return name;
	}

	public int getSource()
	{
		return source;
	}
}
