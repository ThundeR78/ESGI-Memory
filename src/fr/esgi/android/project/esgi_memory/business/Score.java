package fr.esgi.android.project.esgi_memory.business;

import java.util.Date;

public class Score {

	private int id;
	private String username = "";
	private Date date;
	private boolean win = false;
	private int level = 2;
	private long time = 0;
	private int move = 0;
	private int bonus = 0;
	private int point = 0;
	
	//Constructors
	public Score() {
	
	}
	
	public Score(String username, Date date, boolean win, int level, long time, int move, int bonus, int point) {
		this.username = username;
		this.date = date;
		this.win = win;
		this.level = level;
		this.time = time;
		this.move = move;
		this.bonus = bonus;
		this.point = point;
	}
	
	public Score(int id, String username, Date date, boolean win, int level, long time, int move, int bonus, int point) {
		this.id = id;
		this.username = username;
		this.date = date;
		this.win = win;
		this.level = level;
		this.time = time;
		this.move = move;
		this.bonus = bonus;
		this.point = point;
	}
	
	//Getters/Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isWin() {
		return win;
	}

	public void setWin(boolean win) {
		this.win = win;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getMove() {
		return move;
	}

	public void setMove(int move) {
		this.move = move;
	}

	public int getBonus() {
		return bonus;
	}

	public void setBonus(int bonus) {
		this.bonus = bonus;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}
}
