package game;

public class Score {
	int great;
	int ok;
	int bad;
	int miss;
	int score;
	int moneyScore;
	int combo;
	
	public Score() {
		great = 0;
		ok = 0;
		bad = 0;
		miss = 0;
		score = 0;
	}
	
	public void great() {
		great ++;
		combo ++;
		score += 4;
	}
	
	public void ok() {
		ok ++;
		score += 2;
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
		return "Great: " + great + "\n   OK: " + ok + "\n Miss: " + miss + "\n  Bad: " + bad + "\nTotal score: " + score;
	}
	
	public void reset() {
		great = 0;
		ok = 0;
		bad = 0;
		miss = 0;
		score = 0;
		combo = 0;
	}
}
