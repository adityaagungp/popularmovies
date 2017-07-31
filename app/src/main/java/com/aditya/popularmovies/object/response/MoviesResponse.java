package com.aditya.popularmovies.object.response;

import com.aditya.popularmovies.object.Movie;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MoviesResponse {

	@SerializedName("results")
	@Expose
	private List<Movie> results;

	public MoviesResponse(){
	}

	public List<Movie> getResults(){
		return results;
	}

	public void setResults(List<Movie> results){
		this.results = results;
	}
}
