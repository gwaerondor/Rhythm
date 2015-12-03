package game;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Game extends BasicGame
{
	Song currentSong;
	public Game(String gamename)
	{
		super(gamename);
	}

	public void init(GameContainer gc) throws SlickException {
		gc.setTargetFrameRate(60);
	}

	public void update(GameContainer gc, int i) throws SlickException {
		Input input = gc.getInput();
		if (input.isKeyPressed(Input.KEY_1)) {
			currentSong = new Song("Starmine", "Ryu*", 182, "/songs/starmine.ogg");
			currentSong.playSong();
		} else if (input.isKeyPressed(Input.KEY_S)) {
			currentSong.stopSong();
			currentSong = null;
		}
		
	}

	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		if (currentSong != null) {
			g.drawString(currentSong.toString(), 100, 10);
		} else {
			g.drawString("Press number keys to start a song.\n\n1: Starmine", 100, 10);
		}
	}

	public static void main(String[] args)
	{
		try
		{
			AppGameContainer appgc;
			appgc = new AppGameContainer(new Game("Rhythm"));
			appgc.setDisplayMode(800, 600, false);
			appgc.start();
		}
		catch (SlickException ex)
		{
			Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}