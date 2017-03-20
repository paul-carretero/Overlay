package RIPLayer;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import core.Message;
import overlay.ILayer1;

public class ConcreteRIP extends Thread implements RIPLayer{
	
	private int myId;
	private Map<Integer,RIPMapEntry> masterMap;
	private final int refreshRate = 20000; // 20 sec
	private List<String> availableInterface;
	private ILayer1 layer1;
	
	public ConcreteRIP(int myId, ILayer1 layer1){
		this.myId 	= myId;
		this.layer1 = layer1;
		masterMap 	= new ConcurrentHashMap<>();
		
		masterMap.put(myId, new RIPMapEntry(myId, null, 0, myId, Integer.MAX_VALUE));
		availableInterface = layer1.getAvailableInterface();
	}
	
	@Override
	//TODO
	public void run(){
		while(!isInterrupted()){
			broadcastMap();
			cleanMasterMap();
			syncWait();
		}
	}
	
	private void cleanMasterMap() {
		for(Iterator<Map.Entry<Integer, RIPMapEntry>> it = masterMap.entrySet().iterator(); it.hasNext(); ) {
			Entry<Integer, RIPMapEntry> entry = it.next();
			if(entry.getValue().isToDelete()){
				it.remove();
			}
		}
	}

	public void broadcastMap(){
		for(String itf : availableInterface){
			layer1.send(null, itf);
		}
	}
	

	@Override
	public void sendRIPPacket(Message msg, String ChannelName) {
		synchronized (this) {
			this.notify();
		}
		//destID;cost++
		//TODO
	}
	
	private void syncWait(){
		synchronized (this) {
			try {
				this.wait(refreshRate);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	@Override
	/*
	 * peut retourner null si sur soit m�me ou si pas trouv�
	 * �ventuellement impl�menter la v�rification loopback dans layer 1
	 */
	public String getChannelName(int DestinationID) {
		return masterMap.get(DestinationID).getChannelName();
	}
	
}