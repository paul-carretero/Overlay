package RIPLayer;

public class RIPMapEntry {
	
	protected final int	destinationId; // aussi la clé
	protected String 	channelName;
	protected int 		cost;
	protected int 		source;
	protected Timer		timer;
	
	public RIPMapEntry(int destinationId, String channelName, int cost, int source, int timeout){
		this.destinationId	= destinationId;
		this.channelName 	= channelName;
		this.cost 			= cost;
		this.source 		= source;
		this.timer 			= new Timer(timeout);
	}
	
	public void resetTimer(){
		timer.resetTimer();
	}
	
	public boolean isToDelete(){
		return !timer.isValid();
	}
	
	public void setCost(int cost){
		this.cost = cost;
		timer.resetTimer();
	}
	
	public void setChannelName(String channelName){
		this.channelName = channelName;
		timer.resetTimer();
	}
	
	public void setsource(int source){
		this.source = source;
		timer.resetTimer();
	}
	
	public String getChannelName(){
		return channelName;
	}
	
	public int getDestinationId(){
		return destinationId;
	}
	
	public int getcost(){
		return cost;
	}
	
	public int source(){
		return source;
	}
}
