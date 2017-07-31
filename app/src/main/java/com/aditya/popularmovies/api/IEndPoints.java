package com.aditya.popularmovies.api;

import com.aditya.popularmovies.object.response.MoviesResponse;
import com.aditya.popularmovies.object.response.ReviewsResponse;
import com.aditya.popularmovies.object.response.TrailersResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface IEndPoints {

	//Get popular movies
	@GET("movie/popular")
	Call<MoviesResponse> getPopularMovies(@QueryMap(encoded = true) Map<String, String> params);

	//Get top movies
	@GET("movie/top_rated")
	Call<MoviesResponse> getTopMovies(@QueryMap(encoded = true) Map<String, String> params);

	//Get trailers
	@GET("movie/{movieId}/videos")
	Call<TrailersResponse> getTrailers(@Path("movieId") String movieId,
	                                   @QueryMap(encoded = true) Map<String, String> params);

	//Get reviews
	@GET("movie/{movieId}/reviews")
	Call<ReviewsResponse> getReviews(@Path("movieId") String movieId,
	                                 @QueryMap(encoded = true) Map<String, String> params);
}
