package game;

public class Lane {

	private int number;
	private int width;
	private int key;
	
	public Lane(int key, int number, int width) {
		this.number = number;
		this.width = width;
		this.key = key;
	}
	
	public int getLaneNumber() {
		return number;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getKey(){
		return key;
	}
}
