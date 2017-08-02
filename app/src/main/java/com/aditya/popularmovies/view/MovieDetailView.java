package com.aditya.popularmovies.view;

import com.aditya.popularmovies.object.Movie;

public interface MovieDetailView {

	void onMovieUpdated(Movie movie);
	void onFailMakeFavorite();
	void onFailMakeUnfavorite();
}
