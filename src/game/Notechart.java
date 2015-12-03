package game;

import java.util.ArrayList;
import java.util.Random;



public class Notechart {

	public class Note {
		private int lane;
		private float targetBeat;
		
		public Note(int lane, float targetBeat) {
			this.lane = lane;
			this.targetBeat = targetBeat;
		}
		
		public int getPos(float currentBeat, float bpm) {
			return (int) ((currentBeat - targetBeat)*bpm);
		}
	}
	
	private int bpm;
	private ArrayList<Note> chart;
	
	public Notechart(int bpm) {
		this.bpm = bpm;
		this.chart = createNoteChart(dummyMinute());
	}

	public ArrayList<Note> getChart() {
		return this.chart;
	}
	
	private ArrayList<Note> createNoteChart(boolean[][] sixteenths){
		chart = new ArrayList<Note>();
		chart.add(new Note(0, (float)0.25));
		chart.add(new Note(4, (float)0.5));
		chart.add(new Note(3, (float)0.75));
		chart.add(new Note(7, (float)0.75));
		chart.add(new Note(2, (float)1.0));
		return chart;
	}
	
	private boolean[][] dummyMinute() {
		int totalSixteenthNotes = bpm * 16;
		boolean[][] notechart = new boolean[8][totalSixteenthNotes];
		for (int sixteenth = 0; sixteenth < totalSixteenthNotes; sixteenth++) {
			for (int key = 0; key < 8; key++) {
				notechart[key][sixteenth] = random();
			}
		}
		return notechart;
	}

	private boolean random() {
		Random r = new Random();
		double value = r.nextDouble();
		double threshold = 0.8;
		return value > threshold;
	}

	public String toString() {
		return chart.toString();
		
	}
}
