package game;

import java.util.ArrayList;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Lane {
	private float speedMod;
	private int yPositionOfNoteMark;
	private int widthOfSubLanes;
	private Image line;
	private Image laneSeparator;
	private Image explosion;
	private Image noteImage;
	private Image okImage;
	private Image missImage;
	private Image badImage;
	private Image greatImage;
	private int[] sublanes;
	private boolean displayOK;
	private boolean displayMiss;
	private boolean displayBad;
	private boolean displayGreat;
	private float displayTime;

	public Lane(int yPositionOfNoteMark, int[] sublanes, int widthOfSubLanes) {
		this.yPositionOfNoteMark = yPositionOfNoteMark;
		this.sublanes = sublanes;
		this.widthOfSubLanes = widthOfSubLanes;
		this.speedMod = 1;
		try {
			this.line = new Image("graphics/Note_line.png");
			this.laneSeparator = new Image("graphics/Lane_separator.png");
			this.explosion = new Image("graphics/Explosion.png");
			this.noteImage = new Image("graphics/Note.png");
			this.okImage = new Image("graphics/ok.png");
			this.missImage = new Image("graphics/miss.png");
			this.badImage = new Image("graphics/bad.png");
			this.greatImage = new Image("graphics/great.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public int getYPositionOfNoteMark() {
		return yPositionOfNoteMark;
	}
	
	public int getRightEdge() {
		return 50 + (sublanes.length * widthOfSubLanes) + (sublanes.length+2) * laneSeparator.getWidth();
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
			explosion.draw(startXPosition, halfLanePosition, widthOfSubLanes, yPositionOfNoteMark - halfLanePosition);
		}
	}

	private int getXPositionForSublane(int sublaneNumber) {
		return 50 + sublaneNumber * (widthOfSubLanes + 3);
	}

	private int getYPositionForNote(float currentBeat, float targetBeat, int bpm) {
		return (int) (speedMod * bpm * (currentBeat - targetBeat)) + 550;
	}

	public void increaseSpeedMod() {
		speedMod = speedMod + (float) 0.25;
	}

	public void decreaseSpeedMod() {
		if (speedMod > 0.25) {
			speedMod = speedMod - (float) 0.25;
		}
	}

	public float getSpeedMod() {
		return speedMod;
	}

	public int getLaneForButton(int button) {
		for (int i = 0; i < sublanes.length; i++) {
			if (sublanes[i] == button) {
				return i;
			}
		}
		return -1;
	}

	public void drawNote(int lane, float currentBeat, int bpm) {
		noteImage.draw(getXPositionForSublane(lane), 50 + (currentBeat), widthOfSubLanes, 6);
	}

	public void drawNotes(ArrayList<Note> notes, float currentBeat, int bpm) {
		for (Note note : notes) {
			int yPos = getYPositionForNote(currentBeat, note.getTargetBeat(), bpm);
			if (yPos > 30 && yPos < 600) {
				int xPos = getXPositionForSublane(note.getLane());
				noteImage.draw(xPos, yPos, widthOfSubLanes, 6);
			}
		}
	}

	public void clearDisplays() {
		displayOK = false;
		displayMiss = false;
		displayBad = false;
		displayGreat = false;
	}

	private void drawOK() {
		if (displayOK == true) {
			okImage.draw(250, yPositionOfNoteMark / 2);
		}
	}

	private void drawMiss() {
		if (displayMiss == true) {
			missImage.draw(250, yPositionOfNoteMark / 2);
		}
	}

	private void drawBad() {
		if (displayBad == true) {
			badImage.draw(250, yPositionOfNoteMark / 2);
		}
	}
	
	private void drawGreat() {
		if (displayGreat == true) {
			greatImage.draw(250, yPositionOfNoteMark / 2);
		}
	}
	
	public void drawGrades() {
		drawMiss();
		drawBad();
		drawOK();
		drawGreat();
	}

	public boolean noteShouldBeDrawn(Note note, float currentBeat, int bpm) {
		int yPos = getYPositionForNote(currentBeat, note.getTargetBeat(), bpm);
		if (yPos > 30) {
			if (yPos < 600) {
				if (!note.isDestroyed()) {
					return true;
				}
			}
		}
		return false;
	}

	private int getStartPositionForButton(int button) {
		int laneNumber = getLaneForButton(button);
		return 50 + (laneNumber * (widthOfSubLanes + laneSeparator.getWidth()));
	}

	public void greatHit(float time) {
		clearDisplays();
		displayGreat = true;
		displayTime = time;
	}
	
	public void okHit(float time) {
		clearDisplays();
		displayOK = true;
		displayTime = time;
	}

	public void miss(float time) {
		clearDisplays();
		displayMiss = true;
		displayTime = time;
	}
	
	public void bad(float time) {
		clearDisplays();
		displayBad = true;
		displayTime = time;
	}

	public void updateTimer(float newTime) {
		if (displayTime - newTime < -1.5) {
			clearDisplays();
		}
	}
}
