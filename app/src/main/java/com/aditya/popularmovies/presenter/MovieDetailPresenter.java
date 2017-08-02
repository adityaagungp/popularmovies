package com.aditya.popularmovies.presenter;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.aditya.popularmovies.data.MovieContract;
import com.aditya.popularmovies.object.Movie;
import com.aditya.popularmovies.view.MovieDetailView;

public class MovieDetailPresenter extends AsyncQueryHandler {

	private Context context;
	private MovieDetailView view;
	private Movie movie;

	public MovieDetailPresenter(MovieDetailView view, Context context){
		super(context.getContentResolver());
		this.view = view;
		this.context = context;
	}

	public Movie getMovie(){
		return movie;
	}

	public void setMovie(Movie movie){
		this.movie = movie;
		if (view != null){
			view.onMovieUpdated(movie);
		}
	}

	public void changeFavoriteMovie(){
		if (movie.isFavorite()){
			deleteFavoriteMovie();
		} else {
			insertFavoriteMovie();
		}
	}

	private void insertFavoriteMovie(){
		ContentValues cv = new ContentValues();
		cv.put(MovieContract.MovieEntry._ID, movie.getId());
		cv.put(MovieContract.MovieEntry.TITLE, movie.getTitle());
		cv.put(MovieContract.MovieEntry.POSTER, movie.getPosterPath());
		cv.put(MovieContract.MovieEntry.OVERVIEW, movie.getOverview());
		cv.put(MovieContract.MovieEntry.RATING, movie.getVoteAverage());
		cv.put(MovieContract.MovieEntry.RELEASE, movie.getReleaseDate());
		startInsert(1, null, MovieContract.MovieEntry.CONTENT_URI, cv);
	}

	private void deleteFavoriteMovie(){
		String selectionClause = MovieContract.MovieEntry._ID + " = ? ";
		String[] selectionArgs = {String.valueOf(movie.getId())};
		startDelete(2, null, MovieContract.MovieEntry.CONTENT_URI, selectionClause, selectionArgs);
	}

	@Override
	protected void onInsertComplete(int token, Object cookie, Uri uri){
		if (uri != null){
			movie.setFavorite(true);
			view.onMovieUpdated(movie);
		} else {
			view.onFailMakeFavorite();
		}
	}

	@Override
	protected void onDeleteComplete(int token, Object cookie, int result){
		if (result > 0) {
			movie.setFavorite(false);
			view.onMovieUpdated(movie);
		} else {
			view.onFailMakeUnfavorite();
		}
	}
}
