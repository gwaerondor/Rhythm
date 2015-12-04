package game;

public class Note {
	private int lane;
	private float targetBeat;
	
	public Note(int lane, float targetBeat) {
		this.lane = lane;
		this.targetBeat = targetBeat;
	}
	
	public float getTargetBeat() {
		return targetBeat;
	}
	
	public int getLane() {
		return lane;
	}
	
	public String toString() {
		return "(Lane: " + lane + ", Beat: " + targetBeat + ")";
	}
}