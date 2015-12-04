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
		
		public float getTargetBeat() {
			return targetBeat;
		}
		
		public int getLane() {
			return lane;
		}
		
		public String toString() {
			return "(Lane: " + lane + ", Beat: " + targetBeat + ")";
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
		chart.add(new Note(1, (float)16.0));
		chart.add(new Note(2, (float)16.5));
		chart.add(new Note(1, (float)17.0));
		chart.add(new Note(5, (float)18.0));
		chart.add(new Note(6, (float)19.0));
		chart.add(new Note(1, (float)20.0));
		chart.add(new Note(2, (float)21.5));
		chart.add(new Note(1, (float)22.0));
		chart.add(new Note(5, (float)23.0));
		chart.add(new Note(6, (float)24.0));
		chart.add(new Note(1, (float)25.0));
		chart.add(new Note(2, (float)26.5));
		chart.add(new Note(1, (float)27.0));
		chart.add(new Note(5, (float)28.0));
		chart.add(new Note(6, (float)29.0));
		chart.add(new Note(1, (float)30.0));
		chart.add(new Note(2, (float)30.5));
		chart.add(new Note(1, (float)31.0));
		chart.add(new Note(5, (float)31.5));
		chart.add(new Note(6, (float)32.0));
		chart.add(new Note(1, (float)33.0));
		chart.add(new Note(3, (float)34.0));
		chart.add(new Note(2, (float)35.0));
		chart.add(new Note(1, (float)35.0));
		chart.add(new Note(5, (float)36.0));
		chart.add(new Note(6, (float)36.0));
		chart.add(new Note(6, (float)37.0));
		chart.add(new Note(1, (float)37.0));
		chart.add(new Note(0, (float)38.0));
		chart.add(new Note(7, (float)38.0));
		// Vocals begin here
		chart.add(new Note(7, (float)40.0));
		chart.add(new Note(2, (float)40.5));
		chart.add(new Note(5, (float)41.0));
		chart.add(new Note(1, (float)41.5));
		chart.add(new Note(6, (float)42.0));
		chart.add(new Note(3, (float)42.5));
		chart.add(new Note(7, (float)43.0));
		chart.add(new Note(0, (float)43.5));
		chart.add(new Note(1, (float)44.0));
		chart.add(new Note(0, (float)44.5));
		chart.add(new Note(1, (float)45.0));
		chart.add(new Note(0, (float)45.5));
		chart.add(new Note(1, (float)46.0));
		chart.add(new Note(3, (float)47.0));
		chart.add(new Note(4, (float)47.0));
		
		chart.add(new Note(1, (float)48.0));
		chart.add(new Note(4, (float)48.5));
		chart.add(new Note(4, (float)49.0));
		chart.add(new Note(6, (float)49.5));
		chart.add(new Note(7, (float)50.0));
		chart.add(new Note(3, (float)50.5));
		chart.add(new Note(4, (float)51.0));
		chart.add(new Note(2, (float)51.5));
		chart.add(new Note(4, (float)52.0));
		chart.add(new Note(0, (float)52.5));
		chart.add(new Note(5, (float)53.0));
		chart.add(new Note(5, (float)53.5));
		
		chart.add(new Note(0, (float)54.0)); chart.add(new Note(1, (float)54.0));
		chart.add(new Note(1, (float)55.0)); chart.add(new Note(2, (float)55.0));
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
