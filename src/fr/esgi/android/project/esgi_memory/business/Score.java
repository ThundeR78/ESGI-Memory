package fr.esgi.android.project.esgi_memory.business;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class Score implements Parcelable {

	private int id;
	private String username = "";
	private Date date;
	private boolean win = false;
	private boolean timer = false;
	private int level = 2;
	private long time = 0;
	private int move = 0;
	private int bonus = 0;
	private int point = 0;
	
	//Constructors
	public Score() {
	
	}
	
	public Score(String username, Date date, boolean win, boolean timer, int level, long time, int move, int bonus, int point) {
		this.username = username;
		this.date = date;
		this.win = win;
		this.timer = timer;
		this.level = level;
		this.time = time;
		this.move = move;
		this.bonus = bonus;
		this.point = point;
	}
	
	public Score(int id, String username, Date date, boolean win, boolean timer, int level, long time, int move, int bonus, int point) {
		this.id = id;
		this.username = username;
		this.date = date;
		this.win = win;
		this.timer = timer;
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
	
	public boolean hasTimer() {
		return timer;
	}

	public void setTimer(boolean timer) {
		this.timer = timer;
	}

	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<Score> CREATOR = new Parcelable.Creator<Score>() {
		@Override
		public Score createFromParcel(Parcel in) {
			return new Score(in);
		}

		@Override
		public Score[] newArray(int size) {
			return new Score[size];
		}
	};

	private Score(Parcel in) {
		readFromParcel(in);
	}

	public void writeToParcel(Parcel dest, int flags) {
		boolean[] bool = { win, timer };
		dest.writeInt(id);
		dest.writeString(username);
		dest.writeLong(date.getTime());
		dest.writeInt(level);
		dest.writeLong(time);
		dest.writeInt(move);
		dest.writeInt(bonus);
		dest.writeInt(point);
		dest.writeBooleanArray(bool);
	}

	public void readFromParcel(Parcel in) {
		boolean[] bool = new boolean[2];
		id = in.readInt();
		username = in.readString();
		date = new Date(in.readLong());
		level = in.readInt();
		time = in.readLong();
		move = in.readInt();
		bonus = in.readInt();
		point = in.readInt();
		in.readBooleanArray(bool);
		win = bool[0];
		timer = bool[1];
	}

}
