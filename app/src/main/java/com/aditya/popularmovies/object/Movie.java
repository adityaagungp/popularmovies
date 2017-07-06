package com.aditya.popularmovies.object;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Movie implements Parcelable{

	private long id;
	private double voteAverage;
	private String title;
	private String posterPath;
	private String overview;
	private String releaseDate;

	public Movie(){

	}

	public long getId(){
		return id;
	}

	public void setId(long id){
		this.id = id;
	}

	public double getVoteAverage(){
		return voteAverage;
	}

	public void setVoteAverage(double voteAverage){
		this.voteAverage = voteAverage;
	}

	public String getTitle(){
		return title;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getPosterPath(){
		return posterPath;
	}

	public void setPosterPath(String posterPath){
		this.posterPath = posterPath;
	}

	public String getOverview(){
		return overview;
	}

	public void setOverview(String overview){
		this.overview = overview;
	}

	public String getReleaseDate(){
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate){
		this.releaseDate = releaseDate;
	}

	public void setValueFromJson(JSONObject object) throws JSONException {
		setId(object.getLong("id"));
		setVoteAverage(object.getDouble("vote_average"));
		setTitle(object.getString("original_title"));
		setPosterPath(object.getString("poster_path"));
		setOverview(object.getString("overview"));
		setReleaseDate(object.getString("release_date"));
	}

	protected Movie(Parcel in){
		id = in.readLong();
		voteAverage = in.readDouble();
		title = in.readString();
		posterPath = in.readString();
		overview = in.readString();
		releaseDate = in.readString();
	}

	public static final Creator<Movie> CREATOR = new Creator<Movie>() {
		@Override
		public Movie createFromParcel(Parcel in){
			return new Movie(in);
		}

		@Override
		public Movie[] newArray(int size){
			return new Movie[size];
		}
	};

	@Override
	public int describeContents(){
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags){
		dest.writeLong(id);
		dest.writeDouble(voteAverage);
		dest.writeString(title);
		dest.writeString(posterPath);
		dest.writeString(overview);
		dest.writeString(releaseDate);
	}
}
