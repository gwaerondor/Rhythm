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

		songBanners = new Image[2];
		songBanners[0] = new Image("graphics/Starmine_banner.png");
		songBanners[1] = new Image("graphics/Hana_Ranman_banner.png");
		currentlyExploding = new ArrayList<Integer>();
		lane = new Lane(POSITION_OF_NOTE_LINE, sublaneButtons, 40);
	}

	public void update(GameContainer gc, int i) throws SlickException {
		Input input = gc.getInput();
		currentlyExploding.clear();
		if (currentSong == null) {
			if (input.isKeyPressed(Input.KEY_1)) {
				currentSong = new Song("Starmine", "Ryu*", 182, "songs/starmine.ogg");
				currentSong.playSong();
			} else if (input.isKeyPressed(Input.KEY_2)) {
				currentSong = new Song("Hana Ranman -Flowers-", "TERRA", 160, "songs/Hana_Ranman_Flowers.ogg");
				currentSong.playSong();
			}

		}

		if (input.isKeyPressed(Input.KEY_P)) {
			currentSong.stopSong();
			currentSong = null;
		}

		if (currentSong != null) {
			for (int button : sublaneButtons) {
				if (input.isKeyDown(button)) {
					currentlyExploding.add(button);
				}
				if (input.isKeyPressed(button)) {
					System.out.println("Button " + Input.getKeyName(button) + " pressed at "
							+ currentSong.currentBPMAndPosition());
				}
			}
		}
	}

	public void render(GameContainer gc, Graphics g) throws SlickException {
		if (currentSong != null) {
			g.drawString(currentSong.toString(), 100, 10);
			lane.draw();
			lane.drawExplosions(currentlyExploding);
			lane.drawNote(1,currentSong.currentBeat());
			lane.drawNote(4,currentSong.currentBeat());
		} else {
			g.drawString("Press number keys to start a song.", 100, 10);
			g.drawString("1:", 80, 50 + songBanners[0].getHeight() / 2);
			g.drawString("2:", 80, 50 + songBanners[0].getHeight() + songBanners[1].getHeight()/2);
			songBanners[0].draw(100, 50);
			songBanners[1].draw(100, 50+songBanners[0].getHeight());
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