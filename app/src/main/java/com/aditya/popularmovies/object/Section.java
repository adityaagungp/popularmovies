package com.aditya.popularmovies.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

public class Section implements Parcelable{

	private int position;
	private int sectionedPosition;
	private String title;

	public Section(int position, String title){
		this.position = position;
		this.title = title;
	}

	public int getPosition(){
		return position;
	}

	public void setPosition(int position){
		this.position = position;
	}

	public int getSectionedPosition(){
		return sectionedPosition;
	}

	public void setSectionedPosition(int sectionedPosition){
		this.sectionedPosition = sectionedPosition;
	}

	public String getTitle(){
		return title;
	}

	public void setTitle(String title){
		this.title = title;
	}

	protected Section(Parcel in){
		position = in.readInt();
		sectionedPosition = in.readInt();
		title = in.readString();
	}

	public static final Creator<Section> CREATOR = new Creator<Section>() {
		@Override
		public Section createFromParcel(Parcel in){
			return new Section(in);
		}

		@Override
		public Section[] newArray(int size){
			return new Section[size];
		}
	};

	@Override
	public int describeContents(){
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags){
		dest.writeInt(position);
		dest.writeInt(sectionedPosition);
		dest.writeString(title);
	}

	public static Comparator<Section> comparator = new Comparator<Section>() {
		@Override
		public int compare(Section o1, Section o2){
			return o1.getPosition() - o2.getPosition();
		}
	};
}
