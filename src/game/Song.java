package game;

import java.util.ArrayList;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

public class Song {

	private final float GREAT_TIMING = (float) 0.03;
	private final float OK_TIMING = (float) 0.15;
	
	private int bpm;
	private String songName;
	private String artistName;
	private String location;
	private Music musicPlayer;
	private ArrayList<Note> chart;
	private float startDelay;

	public Song(String songName, String artistName, int bpm, String location, float startDelay) {
		this.bpm = bpm;
		this.songName = songName;
		this.artistName = artistName;
		this.location = location;
		this.startDelay = startDelay;
		this.chart = ChartFileParser.getChartFromFile(location + ".chart", bpm);
	}

	public String toString() {
		return artistName + " - " + songName + " (" + bpm + " BPM)";
	}

	public void playSong() {
		try {
			musicPlayer = new Music(location);
			musicPlayer.play();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public void stopSong() {
		musicPlayer.fade(2000, 0, true);
	}

	public float currentPosition() {
		return musicPlayer.getPosition() - startDelay;
	}

	public float currentBeat() {
		float currentMinutes = currentPosition() / 60;
		return bpm * currentMinutes;
	}

	public String currentBPMAndPosition() {
		float timePosition = currentPosition();
		float minutes = musicPlayer.getPosition() / 60;
		float bpmPosition = bpm * minutes;
		return "BPM pos: " + bpmPosition + ", time pos: " + timePosition;
	}

	public ArrayList<Note> getNotes() {
		return chart;
	}

	public int getBPM() {
		return bpm;
	}

	public Note tryToGetGreatFromLane(int lane) {
		return getNoteCloseToNow(lane, GREAT_TIMING);
	}
	
	public Note tryToGetOKFromLane(int lane) {
		return getNoteCloseToNow(lane, OK_TIMING);
	}
	
	private Note getNoteCloseToNow(int lane, float timing) {
		float beatsPerSecond = (float) bpm /(float) 60.0;
		float secondsPerBeat = 1/beatsPerSecond;
		float targetSecond;
		for (Note note : chart) {
			float targetBeat = note.getTargetBeat();
			targetSecond = secondsPerBeat * targetBeat;
			if (Math.abs(currentPosition() - targetSecond) < timing) {
				if (note.getLane() == lane) {
					return note;
				}
			}
		}
		return null;
	}

	public void destroyNote(Note note) {
		if(chart.remove(note)) {
			System.out.println("Removed note with target time " + note.getTargetSecond() + " and target beat " + note.getTargetBeat());
		}
	}
}
