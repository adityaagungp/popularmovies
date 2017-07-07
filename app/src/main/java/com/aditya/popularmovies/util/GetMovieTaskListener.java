package com.aditya.popularmovies.util;

public interface GetMovieTaskListener<T> {

	void onPreExecute();

	void onPostExecute(T result);
}
