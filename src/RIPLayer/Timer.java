package RIPLayer;

public class Timer {
	private long start;
	private int timeout = 30000;
	
	public Timer(int timeout){
		this.start = System.currentTimeMillis();
	}
	
	public int getElapsedSec(){
		return (int) ((System.currentTimeMillis() - start)/1000);
	}
	
	public boolean isValid(){
		return getElapsedSec() < timeout;
	}
	
	public void resetTimer(){
		this.start = System.currentTimeMillis();
	}
}