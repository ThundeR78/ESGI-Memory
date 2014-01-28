package fr.esgi.android.project.esgi_memory.business;

import android.os.Parcel;
import android.os.Parcelable;

public class Card implements Parcelable {

	public boolean isReturned;
	public int imageId;
	
	public Card() {
		isReturned = false;
	}
	
	public Card(int resId) {
		super();
		imageId = resId;
	}
	
	public void toggleSide() {
		isReturned = !isReturned;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	public static final Parcelable.Creator<Card> CREATOR = new Parcelable.Creator<Card>() {
		@Override
		public Card createFromParcel(Parcel in) {
			return new Card(in);
		}

		@Override
		public Card[] newArray(int size) {
			return new Card[size];
		}
	};

	private Card(Parcel in) {
		readFromParcel(in);
	}

	public void writeToParcel(Parcel dest, int flags) {
		boolean[] bool = { isReturned };
		dest.writeBooleanArray(bool);
		dest.writeInt(imageId);
	}

	public void readFromParcel(Parcel in) {
		boolean[] bool = new boolean[1];
		in.readBooleanArray(bool);
		isReturned = bool[0];
		imageId = in.readInt();
	}
}
