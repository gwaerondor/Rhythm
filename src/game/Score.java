package game;

public class Score {
	int ok;
	int bad;
	int miss;
	int score;
	
	public Score() {
		ok = 0;
		bad = 0;
		miss = 0;
		score = 0;
	}
	
	public void ok() {
		ok ++;
		score += 3;
	}
	
	public void bad() {
		bad ++;
		score -= 1;
	}
	
	public void miss() {
		miss ++;
		score -= 2;
	}
	
	public String toString(){
		return "  OK: " + ok + "\nMiss: " + miss + "\n Bad: " + bad + "\nTotal score: " + score;
	}
	
	public void reset() {
		ok = 0;
		bad = 0;
		miss = 0;
		score = 0;
	}
}
