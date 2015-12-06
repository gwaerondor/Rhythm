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
	Song currentSong;
	SongInterface songInterface;
	Lane[] lanes;
	Image[] songBanners;
	ArrayList<Integer> currentlyExploding;
	ArrayList<Note> currentNotes;
	private String buttonInfo;
	private Score score;
	private int scoreXPosition;

	public Game(String gamename) {
		super(gamename);
	}

	public void init(GameContainer gc) throws SlickException {
		gc.setTargetFrameRate(60);
		lanes = new Lane[8];
		lanes[0] = new Lane(Input.KEY_Z, 0, 70);
		lanes[1] = new Lane(Input.KEY_X, 1, 40);
		lanes[2] = new Lane(Input.KEY_C, 2, 30);
		lanes[3] = new Lane(Input.KEY_V, 3, 40);
		lanes[4] = new Lane(Input.KEY_B, 4, 30);
		lanes[5] = new Lane(Input.KEY_N, 5, 40);
		lanes[6] = new Lane(Input.KEY_M, 6, 30);
		lanes[7] = new Lane(Input.KEY_COMMA, 7, 40);
		buttonInfo = getButtonInfo();
		songBanners = new Image[2];
		songBanners[0] = new Image("graphics/Starmine_banner.png");
		songBanners[1] = new Image("graphics/Hana_Ranman_banner.png");
		currentlyExploding = new ArrayList<Integer>();
		songInterface = new SongInterface(POSITION_OF_NOTE_LINE, lanes);
		score = new Score();
		scoreXPosition = songInterface.getRightEdge() + 10;
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
		if (input.isKeyPressed(Input.KEY_1)) {
			score.reset();
			currentSong = new Song("Starmine", "Ryu*", 182, "songs/starmine.ogg", (float) 0.0);
			currentNotes = currentSong.getNotes();
			currentSong.playSong();
		} else if (input.isKeyPressed(Input.KEY_2)) {
			score.reset();
			currentSong = new Song("Hana Ranman -Flowers-", "TERRA", 160, "songs/Hana_Ranman_Flowers.ogg", (float) 0.0);
			currentNotes = currentSong.getNotes();
			currentSong.playSong();
		}
	}

	private void stopSong() {
		currentSong.stopSong();
		currentSong = null;
		songInterface.clearDisplays();
	}

	private void checkForSpeedModInput(Input input) {
		if (input.isKeyPressed(Input.KEY_UP)) {
			songInterface.increaseSpeedMod();
		} else if (input.isKeyPressed(Input.KEY_DOWN)) {
			songInterface.decreaseSpeedMod();
		}
	}

	private void checkForPlayInput(Input input) {
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
				System.out.println(
						"Key " + Input.getKeyName(key) + " pressed at " + currentSong.currentBPMAndPosition());
			}
		}
	}

	public void checkForMisses() {
		ArrayList<Note> missedNotes = new ArrayList<Note>();
		for (Note note : currentSong.getNotes()) {
			if (note.getTargetSecond() < currentSong.currentPosition() - 0.15) {
				missedNotes.add(note);
				songInterface.miss(currentSong.currentBeat());
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
		
		if (currentSong == null) {
			checkForSongSelection(input);
		} else if (input.isKeyPressed(Input.KEY_P)) {
			stopSong();
		} else {
			checkForPlayInput(input);
			checkForMisses();
		}
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
		g.drawString("Press number keys to start a song. Press P to return to this menu.", 100, 10);
		g.drawString("Speedmod: x" + songInterface.getSpeedMod() + "(set with up/down arrows)", 100, 30);
		g.drawString(buttonInfo, 100, 550);
		g.drawString("1:", 80, 50 + songBanners[0].getHeight() / 2);
		g.drawString("2:", 80, 50 + songBanners[0].getHeight() + songBanners[1].getHeight() / 2);
		songBanners[0].draw(100, 50);
		songBanners[1].draw(100, 50 + songBanners[0].getHeight());
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		if (currentSong == null) {
			renderMenu(g);
		} else {
			renderSong(g);
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