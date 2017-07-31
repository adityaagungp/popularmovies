package com.aditya.popularmovies.api;

import com.aditya.popularmovies.BuildConfig;
import com.aditya.popularmovies.object.response.MoviesResponse;
import com.aditya.popularmovies.object.response.ReviewsResponse;
import com.aditya.popularmovies.object.response.TrailersResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIService {

	private static APIService instance = new APIService();

	private OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(3, TimeUnit.SECONDS).build();

	private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

	private Retrofit retrofit = new Retrofit.Builder()
		.client(httpClient)
		.baseUrl(BuildConfig.API_BASE)
		.addConverterFactory(GsonConverterFactory.create(gson))
		.build();

	private IEndPoints service = retrofit.create(IEndPoints.class);

	APIService(){

	}

	public static APIService getInstance(){
		return instance;
	}

	public void getPopularMovies(Map<String, String> params, Callback<MoviesResponse> callback){
		service.getPopularMovies(params).enqueue(callback);
	}

	public void getTopMovies(Map<String, String> params, Callback<MoviesResponse> callback){
		service.getTopMovies(params).enqueue(callback);
	}

	public void getTrailers(long id, Map<String, String> params, Callback<TrailersResponse> callback){
		service.getTrailers(String.valueOf(id), params).enqueue(callback);
	}

	public void getReviews(long id, Map<String, String> params, Callback<ReviewsResponse> callback){
		service.getReviews(String.valueOf(id), params).enqueue(callback);
	}
}
