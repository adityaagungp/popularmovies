package com.aditya.popularmovies.presenter;

import android.content.Context;

import com.aditya.popularmovies.api.APIService;
import com.aditya.popularmovies.object.Video;
import com.aditya.popularmovies.object.response.TrailersResponse;
import com.aditya.popularmovies.util.NetworkUtils;
import com.aditya.popularmovies.view.TrailersView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrailersPresenter implements Callback<TrailersResponse> {

	private TrailersView view;
	private Context context;
	private long movieId;
	private ArrayList<Video> videos;

	public TrailersPresenter(TrailersView view, Context context, long movieId){
		this.view = view;
		this.context = context;
		this.movieId = movieId;
		videos = new ArrayList<>();
	}

	public ArrayList<Video> getVideos(){
		return videos;
	}

	public void setVideos(ArrayList<Video> videos){
		this.videos = videos;
	}

	public void getTrailers(){
		if (NetworkUtils.isOnline(context)){
			APIService.getInstance().getTrailers(movieId, NetworkUtils.buildKeyMap(), this);
		} else {
			view.onFailGetTrailer();
		}
		APIService.getInstance().getTrailers(movieId, NetworkUtils.buildKeyMap(), this);
	}

	@Override
	public void onResponse(Call<TrailersResponse> call, Response<TrailersResponse> response){
		if (response.isSuccessful()){
			videos.clear();
			videos.addAll(response.body().getResults());
			view.onSuccessGetTrailer();
		} else {
			view.onFailGetTrailer();
		}
	}

	@Override
	public void onFailure(Call<TrailersResponse> call, Throwable t){
		view.onFailGetTrailer();
	}
}
