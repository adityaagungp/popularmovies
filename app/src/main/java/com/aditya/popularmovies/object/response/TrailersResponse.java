package com.aditya.popularmovies.object.response;

import com.aditya.popularmovies.object.Video;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailersResponse {

	@SerializedName("results")
	@Expose
	private List<Video> results;

	public TrailersResponse(){
	}

	public List<Video> getResults(){
		return results;
	}

	public void setResults(List<Video> results){
		this.results = results;
	}
}
