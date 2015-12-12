package game;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Game extends BasicGame {
	private final int POSITION_OF_NOTE_LINE = 550;
	private Song currentSong;
	private SongInterface songInterface;
	private Lane[] lanes;
	private ArrayList<Integer> currentlyExploding;
	private ArrayList<Note> currentNotes;
	private ArrayList<Song> songList;
	private String buttonInfo;
	private Score score;
	private int scoreXPosition;
	private int currentSongSelectionIndex;
	private boolean currentlyInSong;

	public Game(String gamename) {
		super(gamename);
	}

	public void init(GameContainer gc) throws SlickException {
		//gc.setTargetFrameRate(60);
		Image redNote = new Image("graphics/Red_note.png");
		Image whiteNote = new Image("graphics/White_note.png");
		Image blueNote = new Image("graphics/Blue_note.png");
		lanes = new Lane[8];
		lanes[0] = new Lane(Input.KEY_LCONTROL, 0, 70, redNote);
		lanes[1] = new Lane(Input.KEY_Z, 1, 40, whiteNote);
		lanes[2] = new Lane(Input.KEY_S, 2, 30, blueNote);
		lanes[3] = new Lane(Input.KEY_X, 3, 40, whiteNote);
		lanes[4] = new Lane(Input.KEY_D, 4, 30, blueNote);
		lanes[5] = new Lane(Input.KEY_C, 5, 40, whiteNote);
		lanes[6] = new Lane(Input.KEY_F, 6, 30, blueNote);
		lanes[7] = new Lane(Input.KEY_V, 7, 40, whiteNote);
		buttonInfo = getButtonInfo();
		currentlyExploding = new ArrayList<Integer>();
		songInterface = new SongInterface(POSITION_OF_NOTE_LINE, lanes);
		score = new Score();
		scoreXPosition = songInterface.getRightEdge() + 10;
		songList = new ArrayList<Song>();
		loadSongs();
		currentSongSelectionIndex = 0;
		currentlyInSong = false;
		currentSong = songList.get(currentSongSelectionIndex);
	}

	private void loadSongs() {
		songList.add(new Song("Starmine", "Ryu*", 182, "songs/starmine", (float) 0.0));
		songList.add(new Song("Hana Ranman -Flowers-", "TERRA", 160, "songs/Hana_Ranman_Flowers", (float) 0.0));
	}
	
	private String getButtonInfo() {
		String result = "Lane count: ";
		result += lanes.length + "\nButtons (left to right): ";
		for (Lane lane : lanes) {
			result += Input.getKeyName(lane.getKey()) + ", ";
		}
		return result.substring(0, result.length() - 2);
	}

	private void checkForSongSelection(Input input) {
		if (input.isKeyPressed(Input.KEY_ENTER)) {
			score.reset();
			currentSong = songList.get(currentSongSelectionIndex);
			currentNotes = currentSong.getNotes();
			currentlyInSong = true;
			currentSong.playSong();
		} 
	}

	private void stopSong() {
		currentSong.stopSong();
		songInterface.clearDisplays();
		currentlyInSong = false;
	}

	private void checkForSpeedModInput(Input input) {
		if (input.isKeyPressed(Input.KEY_UP)) {
			songInterface.increaseSpeedMod();
		} else if (input.isKeyPressed(Input.KEY_DOWN)) {
			songInterface.decreaseSpeedMod();
		}
	}
	
	private void checkForSelectionInput(Input input) {
		if (input.isKeyPressed(Input.KEY_LEFT)) {
			highlightPreviousSong();
		} else if (input.isKeyPressed(Input.KEY_RIGHT)) {
			highlightNextSong();
		}
	}

	private void highlightPreviousSong() {
		if(currentSongSelectionIndex > 0) {
			currentSongSelectionIndex --;
		} else {
			currentSongSelectionIndex = songList.size() -1;
		}
		currentSong = songList.get(currentSongSelectionIndex);
	}
	
	private void highlightNextSong() {
		if(currentSongSelectionIndex < songList.size() -1) {
			currentSongSelectionIndex ++;
		} else {
			currentSongSelectionIndex = 0;
		}
		currentSong = songList.get(currentSongSelectionIndex);
	}

	private void checkForPlayEvents(Input input) {
		float currentTime = currentSong.currentPosition();
		songInterface.updateTimer(currentTime);
		for (Lane lane : lanes) {
			int key = lane.getKey();
			if (input.isKeyDown(key)) {
				currentlyExploding.add(key);
			}
			if (input.isKeyPressed(key)) {
				Lane pressedLane = songInterface.getLaneForButton(key);
				Note hitNote = currentSong.tryToGetGreatFromLane(pressedLane);
				if (hitNote != null) {
					songInterface.greatHit(currentTime);
					score.great();
				} else {
					hitNote = currentSong.tryToGetOKFromLane(pressedLane);
					if (hitNote != null) {
						songInterface.okHit(currentTime);
						score.ok();
					} else {
						songInterface.bad(currentTime);
						score.bad();
					}
				}
				currentSong.destroyNote(hitNote);
			}
		}
		checkForMisses();
	}

	public void checkForMisses() {
		ArrayList<Note> missedNotes = new ArrayList<Note>();
		for (Note note : currentSong.getNotes()) {
			if (note.getTargetSecond() < currentSong.currentPosition() - 0.15) {
				missedNotes.add(note);
				songInterface.miss(currentSong.currentPosition());
				score.miss();
			}
		}
		for (Note note : missedNotes) {
			currentSong.getNotes().remove(note);
		}
	}

	public void update(GameContainer gc, int i) throws SlickException {
		Input input = gc.getInput();
		currentlyExploding.clear();

		checkForSpeedModInput(input);
		songInterface.catchUpToTargetSpeedMod();

		if (currentlyInSong == false) {
			checkForSongSelection(input);
			checkForSelectionInput(input);
		} else if (songShouldStop(input)) {
			stopSong();
		} else {
			checkForPlayEvents(input);
		}
	}

	private boolean songShouldStop(Input input) {
		if (input.isKeyPressed(Input.KEY_P)) {
			return true;
		}
		if (currentSong.isOver()) {
			return true;
		}
		return false;
	}

	private void renderSong(Graphics g) {
		g.drawString(currentSong.toString() + " Speedmod: x" + songInterface.getSpeedMod(), 100, 10);
		g.drawString(score.toString(), scoreXPosition, songInterface.getYPositionOfNoteMark() - 85);
		songInterface.draw();
		songInterface.drawExplosions(currentlyExploding);
		songInterface.drawNotes(currentNotes, currentSong.currentBeat(), currentSong.getBPM());
		songInterface.drawGrades();
		if (score.getCombo() > 1) {
			g.drawString("" + score.getCombo() + " COMBO", 250, songInterface.getYPositionOfNoteMark() / 2 + 30);
		}
	}

	private void renderMenu(Graphics g) {
		g.drawString("Press Left/Right to select song, enter to start.\nPress P to return to this menu.", 100, 10);
		g.drawString("Speedmod: x" + songInterface.getSpeedMod() + "(set with up/down arrows)", 100, 50);
		g.drawString("Song selection: " + currentSong.toString(), 100, 70);
		g.drawString(buttonInfo, 100, 550);
		for(int i = 0; i < songList.size(); i++){
			Song song = songList.get(i);
			Image banner = song.getBanner();
			g.drawString(""+i+":", 80, 100 + (i*banner.getHeight()));
			song.getBanner().draw(100, 100 + (i*banner.getHeight()));
		}
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		if (currentlyInSong) {
			renderSong(g);
		} else {
			renderMenu(g);
		}
	}

	public static void main(String[] args) {
		try {
			AppGameContainer appgc;
			appgc = new AppGameContainer(new Game("Rhythm"));
			appgc.setDisplayMode(800, 600, false);
			appgc.start();
		} catch (SlickException ex) {
			Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}