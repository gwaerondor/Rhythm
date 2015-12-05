package game;

public class Note {
	private int lane;
	private float targetBeat;
	private boolean destroyed;
	private int bpm;
	
	public Note(int lane, float targetBeat, int bpm) {
		this.lane = lane;
		this.targetBeat = targetBeat;
		this.destroyed = false;
		this.bpm = bpm;
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

	public float getTargetSecond() {
		float beatsPerSecond = (float) bpm /(float) 60.0;
		float secondsPerBeat = 1/beatsPerSecond;
		return secondsPerBeat * targetBeat;
	}
	
	public String toString() {
		return "(Lane: " + lane + ", Beat: " + targetBeat + ")";
	}
}