package com.aditya.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aditya.popularmovies.object.Movie;
import com.aditya.popularmovies.util.Constants;
import com.aditya.popularmovies.util.NetworkUtils;
import com.aditya.popularmovies.view.MovieAdapter;
import com.aditya.popularmovies.view.MovieClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieClickListener {

	private RecyclerView movieGrid;
	private ProgressBar progressBar;
	private TextView emptyText;

	private ArrayList<Movie> movies;
	private MovieAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		movieGrid = (RecyclerView) findViewById(R.id.movieGrid);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		emptyText = (TextView) findViewById(R.id.emptyText);

		movies = new ArrayList<>();
		if (savedInstanceState != null && savedInstanceState.containsKey(Constants.Param.MOVIES)){
			movies = savedInstanceState.getParcelableArrayList(Constants.Param.MOVIES);
		}
		if (movies == null || movies.size() == 0){
			fetchMovies(Constants.Url.API_BASE + Constants.Route.POPULAR, Constants.Key.MOVIE_KEY);
		}

		adapter = new MovieAdapter(this, this);

		adapter.setMovies(movies);
		GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
		movieGrid.setLayoutManager(layoutManager);
		movieGrid.setAdapter(adapter);
	}

	@Override
	public void onSaveInstanceState(Bundle outState){
		outState.putParcelableArrayList(Constants.Param.MOVIES, movies);
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		int id = item.getItemId();
		if (id == R.id.action_popular){
			fetchMovies(Constants.Url.API_BASE + Constants.Route.POPULAR, Constants.Key.MOVIE_KEY);
		} else if (id == R.id.action_top){
			fetchMovies(Constants.Url.API_BASE + Constants.Route.TOP, Constants.Key.MOVIE_KEY);
		}
		return super.onOptionsItemSelected(item);
	}

	private void goToMovieDetails(Movie movie){
		Intent intent = new Intent(this, MovieDetailActivity.class);
		intent.putExtra(Constants.Param.MOVIE, movie);
		startActivity(intent);
	}

	private void fetchMovies(String url, String key){
		if (NetworkUtils.isOnline(this)){
			URL popularUrl = NetworkUtils.buildUrl(url, key);
			new GetMovieTask().execute(popularUrl);
		} else {
			Toast.makeText(this, R.string.no_internet, Toast.LENGTH_LONG).show();
		}
	}

	private void parseResponse(String json){
		try {
			JSONObject object = new JSONObject(json);
			JSONArray results = object.getJSONArray("results");
			movies.clear();
			for (int i = 0; i < results.length(); i++){
				JSONObject movieJson = results.getJSONObject(i);
				Movie movie = new Movie();
				movie.setValueFromJson(movieJson);
				movies.add(movie);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void showMovies(){
		adapter.setMovies(movies);
		if (movies.size() > 0){
			emptyText.setVisibility(View.INVISIBLE);
			movieGrid.setVisibility(View.VISIBLE);
		} else {
			emptyText.setVisibility(View.VISIBLE);
			movieGrid.setVisibility(View.INVISIBLE);
		}
	}

	private void showConnectionErrorMessage(){
		Toast.makeText(this, R.string.connection_problem, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onItemClick(int index){
		Movie movie = movies.get(index);
		goToMovieDetails(movie);
	}

	private class GetMovieTask extends AsyncTask<URL, Void, String> {

		@Override
		protected void onPreExecute(){
			super.onPreExecute();
			movieGrid.setVisibility(View.INVISIBLE);
			progressBar.setVisibility(View.VISIBLE);
			emptyText.setVisibility(View.INVISIBLE);
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
			progressBar.setVisibility(View.INVISIBLE);
			if (response != null && !response.equals("")){
				parseResponse(response);
			} else {
				showConnectionErrorMessage();
			}
			showMovies();
		}
	}
}
