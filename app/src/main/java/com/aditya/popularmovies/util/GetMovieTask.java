package com.aditya.popularmovies.util;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;

public class GetMovieTask extends AsyncTask<URL, Void, String> {

	private Context context;
	private GetMovieTaskListener<String> listener;

	public GetMovieTask(Context context, GetMovieTaskListener<String> listener){
		this.context = context;
		this.listener = listener;
	}

	@Override
	protected void onPreExecute(){
		super.onPreExecute();
		listener.onPreExecute();
	}

	@Override
	protected String doInBackground(URL... params){
		URL url = params[0];
		String movies = null;
		try {
			movies = NetworkUtils.getResponseFromUrl(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return movies;
	}

	@Override
	protected void onPostExecute(String response){
		super.onPostExecute(response);
		listener.onPostExecute(response);
	}
}
