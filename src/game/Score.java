package game;

public class Score {
	int ok;
	int bad;
	int miss;
	int score;
	int moneyScore;
	int combo;
	
	public Score() {
		ok = 0;
		bad = 0;
		miss = 0;
		score = 0;
	}
	
	public void ok() {
		ok ++;
		score += 3;
		combo ++;
	}
	
	public void bad() {
		bad ++;
	}
	
	public void miss() {
		miss ++;
		score -= 2;
		combo = 0;
	}
	
	public int getCombo() {
		return combo;
	}
	
	public String toString(){
		return "  OK: " + ok + "\nMiss: " + miss + "\n Bad: " + bad + "\nTotal score: " + score;
	}
	
	public void reset() {
		ok = 0;
		bad = 0;
		miss = 0;
		score = 0;
		combo = 0;
	}
}
