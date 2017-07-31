package com.aditya.popularmovies.object;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Video implements Parcelable {

	@SerializedName("id")
	@Expose
	private String id;

	@SerializedName("key")
	@Expose
	private String key;

	@SerializedName("name")
	@Expose
	private String name;

	@SerializedName("site")
	@Expose
	private String site;

	@SerializedName("size")
	@Expose
	private int size;

	@SerializedName("type")
	@Expose
	private String type;

	public Video(){
	}

	public String getId(){
		return id;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getKey(){
		return key;
	}

	public void setKey(String key){
		this.key = key;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getSite(){
		return site;
	}

	public void setSite(String site){
		this.site = site;
	}

	public int getSize(){
		return size;
	}

	public void setSize(int size){
		this.size = size;
	}

	public String getType(){
		return type;
	}

	public void setType(String type){
		this.type = type;
	}

	protected Video(Parcel in){
		id = in.readString();
		key = in.readString();
		name = in.readString();
		site = in.readString();
		size = in.readInt();
		type = in.readString();
	}

	public static final Creator<Video> CREATOR = new Creator<Video>() {
		@Override
		public Video createFromParcel(Parcel in){
			return new Video(in);
		}

		@Override
		public Video[] newArray(int size){
			return new Video[size];
		}
	};

	@Override
	public int describeContents(){
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags){
		dest.writeString(id);
		dest.writeString(key);
		dest.writeString(name);
		dest.writeString(site);
		dest.writeInt(size);
		dest.writeString(type);
	}
}
