package com.aditya.popularmovies.presenter;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.aditya.popularmovies.api.APIService;
import com.aditya.popularmovies.data.MovieContract;
import com.aditya.popularmovies.object.Movie;
import com.aditya.popularmovies.object.response.MoviesResponse;
import com.aditya.popularmovies.util.NetworkUtils;
import com.aditya.popularmovies.view.MoviesView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesPresenter implements Callback<MoviesResponse>, LoaderManager.LoaderCallbacks<Cursor> {

	private Context context;
	private MoviesView view;
	private ArrayList<Movie> movies = new ArrayList<>();
	private Set<Long> favoriteIds = new HashSet<>();
	private boolean movieSortedByFavorite;

	public MoviesPresenter(MoviesView view, Context context){
		this.view = view;
		this.context = context;
		movieSortedByFavorite = false;
	}

	public ArrayList<Movie> getMovies(){
		return movies;
	}

	public void setMovies(ArrayList<Movie> movies){
		this.movies = movies;
	}

	public boolean isMovieSortedByFavorite(){
		return movieSortedByFavorite;
	}

	public void setMovieSortedByFavorite(boolean movieSortedByFavorite){
		this.movieSortedByFavorite = movieSortedByFavorite;
	}

	public void getPopularMovies(){
		if (NetworkUtils.isOnline(context)){
			view.onPreGetMovies();
			APIService.getInstance().getPopularMovies(NetworkUtils.buildKeyMap(), this);
		} else {
			view.onFailGetMovies();
		}
	}

	public void getTopMovies(){
		if (NetworkUtils.isOnline(context)){
			view.onPreGetMovies();
			APIService.getInstance().getTopMovies(NetworkUtils.buildKeyMap(), this);
		} else {
			view.onFailGetMovies();
		}
	}

	private void fetchFavoriteIds(Cursor data){
		favoriteIds.clear();
		int idIndex = data.getColumnIndex(MovieContract.MovieEntry._ID);
		for (int i = 0; i < data.getCount(); i++){
			data.moveToPosition(i);
			favoriteIds.add(data.getLong(idIndex));
		}
		if (movies.size() > 0){
			favoriteMovies();
		}
	}

	private void favoriteMovies(){
		for (Movie movie : movies){
			movie.setIsFavorite(favoriteIds.contains(movie.getId()));
		}
		if (movieSortedByFavorite){
			sortByFavorite();
		}
		view.onSuccessGetMovies();
	}

	public void sortByFavorite(){
		ArrayList<Movie> favoriteMovies = new ArrayList<>();
		ArrayList<Movie> unfavoriteMovies = new ArrayList<>();
		for (Movie movie : movies){
			if (movie.isFavorite()){
				favoriteMovies.add(movie);
			} else {
				unfavoriteMovies.add(movie);
			}
		}
		movies.clear();
		movies.addAll(favoriteMovies);
		movies.addAll(unfavoriteMovies);
		favoriteMovies.clear();
		unfavoriteMovies.clear();
		view.onSuccessGetMovies();
	}

	@Override
	public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response){
		if (response.isSuccessful()){
			movies.clear();
			movies.addAll(response.body().getResults());
			if (favoriteIds.size() > 0){
				favoriteMovies();
			}
			view.onSuccessGetMovies();
		} else {
			view.onFailGetMovies();
		}
	}

	@Override
	public void onFailure(Call<MoviesResponse> call, Throwable t){
		view.onFailGetMovies();
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args){
		return new AsyncTaskLoader<Cursor>(context) {

			Cursor favoriteData = null;

			@Override
			protected void onStartLoading(){
				if (favoriteData != null){
					deliverResult(favoriteData);
				} else {
					forceLoad();
				}
			}

			@Override
			public Cursor loadInBackground(){
				try {
					return context.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
						new String[] {MovieContract.MovieEntry._ID},
						null,
						null,
						null);
				} catch (Exception e) {
					return null;
				}
			}

			public void deliverResult(Cursor data){
				favoriteData = data;
				super.deliverResult(data);
			}
		};
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data){
		fetchFavoriteIds(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader){

	}
}
