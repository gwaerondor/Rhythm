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
	Lane lane;
	int sublaneButtons[];
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
		sublaneButtons = new int[8];
		sublaneButtons[0] = Input.KEY_Z;
		sublaneButtons[1] = Input.KEY_X;
		sublaneButtons[2] = Input.KEY_C;
		sublaneButtons[3] = Input.KEY_V;
		sublaneButtons[4] = Input.KEY_B;
		sublaneButtons[5] = Input.KEY_N;
		sublaneButtons[6] = Input.KEY_M;
		sublaneButtons[7] = Input.KEY_COMMA;
		buttonInfo = getButtonInfo();
		songBanners = new Image[2];
		songBanners[0] = new Image("graphics/Starmine_banner.png");
		songBanners[1] = new Image("graphics/Hana_Ranman_banner.png");
		currentlyExploding = new ArrayList<Integer>();
		lane = new Lane(POSITION_OF_NOTE_LINE, sublaneButtons, 40);
		score = new Score();
		scoreXPosition = lane.getRightEdge() + 20;
	}

	private String getButtonInfo() {
		String result = "Lane count: ";
		result += sublaneButtons.length + "\nButtons (left to right): ";
		for (int button : sublaneButtons) {
			result += Input.getKeyName(button) + ", ";
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
		} else if (input.isKeyPressed(Input.KEY_UP)) {
			lane.increaseSpeedMod();
		} else if (input.isKeyPressed(Input.KEY_DOWN)) {
			lane.decreaseSpeedMod();
		}
	}

	private void stopSong() {
		currentSong.stopSong();
		currentSong = null;
		lane.clearDisplays();
	}

	private void checkForPlayInput(Input input) {
		float currentBeat = currentSong.currentBeat();
		lane.updateTimer(currentBeat);
		for (int button : sublaneButtons) {
			if (input.isKeyDown(button)) {
				currentlyExploding.add(button);
			}
			if (input.isKeyPressed(button)) {
				int pressedLane = lane.getLaneForButton(button);
				Note hitNote = currentSong.getNoteCloseToNow(pressedLane);
				currentSong.destroyNote(hitNote);
				if (hitNote != null) {
					lane.hit(currentBeat);
					score.ok();
				} else {
					lane.bad(currentBeat);
					score.bad();
				}
				System.out.println(
						"Button " + Input.getKeyName(button) + " pressed at " + currentSong.currentBPMAndPosition());
			}
		}
	}

	public void checkForMisses() {
		ArrayList<Note> missedNotes = new ArrayList<Note>();
		for (Note note : currentSong.getNotes()) {
			if (note.getTargetBeat() < currentSong.currentBeat() - 0.5) {
				missedNotes.add(note);
				lane.miss(currentSong.currentBeat());
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

		if (currentSong == null) {
			checkForSongSelection(input);
		}

		// else if (currentSong != null) {
		else {
			if (input.isKeyPressed(Input.KEY_P)) {
				stopSong();
			} else {
				checkForPlayInput(input);
				checkForMisses();
			}
		}
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		if (currentSong != null) {
			g.drawString(currentSong.toString(), 100, 10);
			g.drawString(score.toString(), scoreXPosition, lane.getYPositionOfNoteMark() - 50);
			lane.draw();
			lane.drawExplosions(currentlyExploding);
			lane.drawNotes(currentNotes, currentSong.currentBeat(), currentSong.getBPM());
			lane.drawOK();
			lane.drawMiss();
			lane.drawBad();
			if (score.getCombo() > 1) {
				g.drawString("" + score.getCombo() + " COMBO", 250, lane.getYPositionOfNoteMark() / 2 + 30);
			}
			// lane.drawNote(4,currentSong.currentBeat());
		} else {
			g.drawString("Press number keys to start a song. Press P to return to this menu.", 100, 10);
			g.drawString("Speedmod: x" + lane.getSpeedMod() + "(set with up/down arrows)", 100, 30);
			g.drawString(buttonInfo, 100, 550);
			g.drawString("1:", 80, 50 + songBanners[0].getHeight() / 2);
			g.drawString("2:", 80, 50 + songBanners[0].getHeight() + songBanners[1].getHeight() / 2);
			songBanners[0].draw(100, 50);
			songBanners[1].draw(100, 50 + songBanners[0].getHeight());
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