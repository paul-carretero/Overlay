
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import layer1.ClientThread;

public class Main
{
	private final static int N = 20;
	
	public static void main(String[] args) throws IOException, TimeoutException
	{	
		Layer2.start(dsfsdf);
		/*List<ClientThread> clients = new ArrayList<ClientThread>();
		for(int i = 0;i<N; i++)
		{
			clients.add(new ClientThread(i, "Q"+ ((i == 0) ? (N-1) : (i-1)), "Q"+i));
		}
		
		clients.forEach(x -> x.start());*/
	}

}
