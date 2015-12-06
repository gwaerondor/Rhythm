package game;

import org.newdawn.slick.Image;

public class Lane {

	private int number;
	private int width;
	private int key;
	private Image image;
	
	public Lane(int key, int number, int width, Image noteImage) {
		this.number = number;
		this.width = width;
		this.key = key;
		this.image = noteImage;
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
	
	public Image getNoteImage(){
		return image;
	}
}
