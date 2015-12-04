package game;

public class Note {
	private int lane;
	private float targetBeat;
	private boolean destroyed;

	public Note(int lane, float targetBeat) {
		this.lane = lane;
		this.targetBeat = targetBeat;
		this.destroyed = false;
	}

	public float getTargetBeat() {
		return targetBeat;
	}

	public int getLane() {
		return lane;
	}
	
	public boolean isDestroyed() {
		return destroyed;
	}

	public String toString() {
		return "(Lane: " + lane + ", Beat: " + targetBeat + ")";
	}
}