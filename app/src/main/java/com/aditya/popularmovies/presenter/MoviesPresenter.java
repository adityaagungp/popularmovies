package com.aditya.popularmovies.presenter;

import android.content.Context;

import com.aditya.popularmovies.api.APIService;
import com.aditya.popularmovies.object.Movie;
import com.aditya.popularmovies.object.response.MoviesResponse;
import com.aditya.popularmovies.util.NetworkUtils;
import com.aditya.popularmovies.view.MoviesView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesPresenter implements Callback<MoviesResponse> {

	private Context context;
	private MoviesView view;
	private ArrayList<Movie> movies = new ArrayList<>();

	public MoviesPresenter(MoviesView view, Context context){
		this.view = view;
		this.context = context;
	}

	public ArrayList<Movie> getMovies(){
		return movies;
	}

	public void setMovies(ArrayList<Movie> movies){
		this.movies = movies;
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

	@Override
	public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response){
		if (response.isSuccessful()){
			movies.clear();
			movies.addAll(response.body().getResults());
			view.onSuccessGetMovies();
		} else {
			view.onFailGetMovies();
		}
	}

	@Override
	public void onFailure(Call<MoviesResponse> call, Throwable t){
		view.onFailGetMovies();
	}
}
