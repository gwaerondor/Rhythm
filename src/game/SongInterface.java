package game;

import java.util.ArrayList;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class SongInterface {
	private float speedMod;
	private int yPositionOfNoteMark;
	private Image line;
	private Image laneSeparator;
	private Image explosion;
	private Image okImage;
	private Image missImage;
	private Image badImage;
	private Image greatImage;
	private Lane[] lanes;
	private boolean displayOK;
	private boolean displayMiss;
	private boolean displayBad;
	private boolean displayGreat;
	private float displayTime;
	private float targetSpeedMod;

	public SongInterface(int yPositionOfNoteMark, Lane[] lanes) {
		this.yPositionOfNoteMark = yPositionOfNoteMark;
		this.lanes = lanes;
		this.speedMod = 1;
		this.targetSpeedMod = 1;
		loadImages();
	}

	private void loadImages() {
		try {
			this.line = new Image("graphics/Note_line.png");
			this.laneSeparator = new Image("graphics/Lane_separator.png");
			this.explosion = new Image("graphics/Explosion.png");
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
		int totalLaneWidth = 0;
		for(Lane lane : lanes) {
			totalLaneWidth += lane.getWidth();
		}
		return 50 + totalLaneWidth + ((2+lanes.length) * laneSeparator.getWidth());
	}

	public void draw() {
		drawNoteLines();
		drawLaneSeparators();
	}

	public void drawNoteLines() {
		int xPos = 50;
		int separatorWidth = laneSeparator.getWidth();
		for(Lane lane : lanes){
			line.draw(xPos, yPositionOfNoteMark, lane.getWidth(), line.getHeight());
			xPos += lane.getWidth() + separatorWidth;
		}
	}

	public void drawLaneSeparators() {
		int startingYPosition = 30;
		int lineHeight = line.getHeight();
		int separatorWidth = laneSeparator.getWidth();
		int endPosition = yPositionOfNoteMark - startingYPosition + lineHeight;
		int xPos = 50 - separatorWidth;
		for(Lane lane : lanes) {
			laneSeparator.draw(xPos, startingYPosition, laneSeparator.getWidth(), endPosition);
			xPos += lane.getWidth() + separatorWidth;
		}
		laneSeparator.draw(xPos, startingYPosition, laneSeparator.getWidth(), endPosition);
	}

	public void drawExplosions(ArrayList<Integer> buttons) {
		for (int button : buttons) {
			int startXPosition = getStartPositionForButton(button);
			Lane lane = getLaneForButton(button);
			int halfLanePosition = (yPositionOfNoteMark - 30) / 2;
			explosion.draw(startXPosition, halfLanePosition, lane.getWidth(), yPositionOfNoteMark - halfLanePosition);
		}
	}

	private int getXPositionForSublane(int sublaneNumber) {
		int xPos = 50;
		for(Lane lane : lanes) {
			if(lane.getLaneNumber() < sublaneNumber) {
				xPos += lane.getWidth();
			}
		}
		return xPos + (sublaneNumber*laneSeparator.getWidth());
	}

	private int getYPositionForNote(float currentBeat, float targetBeat, int bpm) {
		return (int) (speedMod * bpm * (currentBeat - targetBeat)) + 550;
	}

	public void increaseSpeedMod() {
		targetSpeedMod = targetSpeedMod + (float) 0.25;
	}

	public void decreaseSpeedMod() {
		if (targetSpeedMod > 0.25) {
			targetSpeedMod = targetSpeedMod - (float) 0.25;
		}
	}
	
	public void catchUpToTargetSpeedMod() {
		if (Math.abs(speedMod - targetSpeedMod) < 0.02) {
			speedMod = targetSpeedMod;
		}
		else if(speedMod < targetSpeedMod) {
			speedMod += 0.025;
		} else if (speedMod > targetSpeedMod) {
			speedMod -= 0.025;
		}
	}

	public float getSpeedMod() {
		return targetSpeedMod;
	}

	public Lane getLaneForButton(int button) {
		for(Lane lane : lanes){
			if(lane.getKey() == button) {
				return lane;
			}
		}
		return null;
	}
	
	private Lane getLaneForLaneNumber(int targetLaneNumber) {
		for(Lane lane : lanes) {
			if(lane.getLaneNumber() == targetLaneNumber) {
				return lane;
			}
		}
		return null;
	}

	public void drawNotes(ArrayList<Note> notes, float currentBeat, int bpm) {
		for (Note note : notes) {
			int yPos = getYPositionForNote(currentBeat, note.getTargetBeat(), bpm);
			if (yPos > 30 && yPos < 600) {
				int xPos = getXPositionForSublane(note.getLane());
				Lane lane = getLaneForLaneNumber(note.getLane());
				int width = lane.getWidth();
				lane.getNoteImage().draw(xPos, yPos, width, 6);
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
				return true;
			}
		}
		return false;
	}

	private int getStartPositionForButton(int button) {
		Lane targetLane = getLaneForButton(button);
		int targetLaneNumber = targetLane.getLaneNumber();
		int xPos = 50;
		for(Lane lane : lanes) {
			if(lane.getLaneNumber() < targetLaneNumber) {
				xPos += lane.getWidth() + laneSeparator.getWidth();
			}
		}
		return xPos;
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
