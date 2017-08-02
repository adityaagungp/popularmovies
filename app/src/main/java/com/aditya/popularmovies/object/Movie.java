package com.aditya.popularmovies.object;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

public class Movie implements Parcelable{

	@SerializedName("id")
	@Expose
	private long id;

	@SerializedName("vote_average")
	@Expose
	private double voteAverage;

	@SerializedName("title")
	@Expose
	private String title;

	@SerializedName("poster_path")
	@Expose
	private String posterPath;

	@SerializedName("overview")
	@Expose
	private String overview;

	@SerializedName("release_date")
	@Expose
	private String releaseDate;

	private boolean favorite;

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

	public boolean isFavorite(){
		return favorite;
	}

	public void setFavorite(boolean isfavorite){
		this.favorite = isfavorite;
	}

	protected Movie(Parcel in){
		id = in.readLong();
		voteAverage = in.readDouble();
		title = in.readString();
		posterPath = in.readString();
		overview = in.readString();
		releaseDate = in.readString();
		favorite = in.readInt() == 1;
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
		dest.writeInt(favorite ? 1 : 0);
	}

	public static Comparator<Movie> comparator = new Comparator<Movie>() {
		@Override
		public int compare(Movie o1, Movie o2){
			if (o1.isFavorite() == o2.isFavorite()){
				return 0;
			} else if (o1.isFavorite() && !o2.isFavorite()){
				return -1;
			} else {
				return 1;
			}
		}
	};
}
