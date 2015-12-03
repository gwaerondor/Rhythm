package game;

import java.util.ArrayList;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Lane {
	private int yPositionOfNoteMark;
	private int widthOfSubLanes;
	private Image line;
	private Image laneSeparator;
	private Image explosion;
	private Image noteImage;
	private int[] sublanes;
	
	public Lane(int yPositionOfNoteMark, int[] sublanes, int widthOfSubLanes) {
		this.yPositionOfNoteMark = yPositionOfNoteMark;
		this.sublanes = sublanes;
		this.widthOfSubLanes = widthOfSubLanes;
		try {
			this.line = new Image("graphics/Note_line.png");
			this.laneSeparator = new Image("graphics/Lane_separator.png");
			this.explosion = new Image("graphics/Explosion.png");
			this.noteImage = new Image("graphics/Note.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public int getYPositionOfNoteMark() {
		return yPositionOfNoteMark;
	}

	public void draw() {
		drawNoteLines();
		drawLaneSeparators();
	}

	public void drawNoteLines() {
		int xPos;
		int separatorWidth = laneSeparator.getWidth();
		for (int i = 0; i < sublanes.length; i++) {
			xPos = 50 + (i * (widthOfSubLanes + separatorWidth));
			line.draw(xPos, yPositionOfNoteMark, widthOfSubLanes, line.getHeight());
		}
	}

	public void drawLaneSeparators() {
		int startingYPosition = 30;
		int lineHeight = line.getHeight();
		int separatorWidth = laneSeparator.getWidth();
		int endPosition = yPositionOfNoteMark - startingYPosition + lineHeight;
		int xPos;
		int amountOfSeparators = sublanes.length + 1;
		for (int i = 0; i < amountOfSeparators; i++) {
			xPos = 50 - 3 + (i * (separatorWidth + widthOfSubLanes));
			laneSeparator.draw(xPos, startingYPosition, laneSeparator.getWidth(), endPosition);
		}
	}

	public void drawExplosions(ArrayList<Integer> buttons) {
		for (int button : buttons) {
			int startXPosition = getStartPositionForButton(button); 
			int halfLanePosition = (yPositionOfNoteMark - 30) / 2;
			explosion.draw(startXPosition, halfLanePosition, widthOfSubLanes, yPositionOfNoteMark-halfLanePosition);
		}
	}

	private int getXPositionForSublane(int sublaneNumber) {
		return 50 + sublaneNumber*(widthOfSubLanes+3);
	}
	
	private int getLaneForButton(int button) {
		for (int i = 0; i < sublanes.length; i++) {
			if (sublanes[i] == button) {
				return i;
			}
		}
		return -1;
	}
	
	public void drawNote(int lane, float currentBeat) {
		noteImage.draw(getXPositionForSublane(lane),50 + (currentBeat),widthOfSubLanes,6);
	}
	
	private int getStartPositionForButton(int button) {
		int laneNumber = getLaneForButton(button); 
		return 50 + (laneNumber * (widthOfSubLanes+laneSeparator.getWidth()));
	}
}
