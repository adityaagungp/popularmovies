package com.aditya.popularmovies.object.response;

import com.aditya.popularmovies.object.Review;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewsResponse {

	@SerializedName("results")
	@Expose
	private List<Review> results;

	public ReviewsResponse(){
	}

	public List<Review> getResults(){
		return results;
	}

	public void setResults(List<Review> results){
		this.results = results;
	}
}
