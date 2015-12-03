package game;

import java.util.Random;

public class Notechart {

	private int bpm;
	private boolean[][] chart;
	
	public Notechart(int bpm) {
		this.bpm = bpm;
		this.chart = dummyMinute();
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
		String r = "";
		int totalSixteenthNotes = bpm * 16;
		for (int sixteenth = 0; sixteenth < totalSixteenthNotes; sixteenth++) {
			for (int key = 0; key < 8; key++) {
				r = r + (chart[key][sixteenth] ? 1 : 0);
			}
			r += "\n";
		}
		return r;
	}
}
