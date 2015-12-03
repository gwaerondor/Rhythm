package game;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

public class Song {
	
	private int bpm;
	private String songName;
	private String artistName;
	private String location;
	private Music musicPlayer;
	
	public Song(String songName, String artistName, int bpm, String location){
		this.bpm = bpm;
		this.songName = songName;
		this.artistName = artistName;
		this.location = location;
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
		musicPlayer.fade(2500, 0, true);
	}
	
}