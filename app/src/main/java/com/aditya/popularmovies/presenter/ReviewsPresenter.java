package com.aditya.popularmovies.presenter;

import android.content.Context;

import com.aditya.popularmovies.api.APIService;
import com.aditya.popularmovies.object.Review;
import com.aditya.popularmovies.object.response.ReviewsResponse;
import com.aditya.popularmovies.util.NetworkUtils;
import com.aditya.popularmovies.view.ReviewsView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewsPresenter implements Callback<ReviewsResponse> {

	private Context context;
	private ReviewsView view;
	private long movieId;
	private ArrayList<Review> reviews;

	public ReviewsPresenter(ReviewsView view, Context context, long movieId){
		this.view = view;
		this.context = context;
		this.movieId = movieId;
		reviews = new ArrayList<>();
	}

	public void setReviews(ArrayList<Review> reviews){
		this.reviews = reviews;
	}

	public ArrayList<Review> getReviews(){
		return reviews;
	}

	public void fetchReviews(){
		if (NetworkUtils.isOnline(context)){
			APIService.getInstance().getReviews(movieId, NetworkUtils.buildKeyMap(), this);
		} else {
			view.onFailGetReviews();
		}
	}

	@Override
	public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response){
		if (response.isSuccessful()){
			reviews.clear();
			reviews.addAll(response.body().getResults());
			view.onSuccessGetReviews();
		} else {
			view.onFailGetReviews();
		}
	}

	@Override
	public void onFailure(Call<ReviewsResponse> call, Throwable t){
		view.onFailGetReviews();
	}
}
