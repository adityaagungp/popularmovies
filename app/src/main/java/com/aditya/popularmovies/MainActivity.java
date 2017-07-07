package com.aditya.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aditya.popularmovies.object.Movie;
import com.aditya.popularmovies.util.Constants;
import com.aditya.popularmovies.util.GetMovieTask;
import com.aditya.popularmovies.util.GetMovieTaskListener;
import com.aditya.popularmovies.util.NetworkUtils;
import com.aditya.popularmovies.view.MovieAdapter;
import com.aditya.popularmovies.view.MovieClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MovieClickListener, GetMovieTaskListener<String> {

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
			fetchMovies(Constants.Url.API_BASE + Constants.Route.POPULAR, BuildConfig.API_KEY);
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
			fetchMovies(Constants.Url.API_BASE + Constants.Route.POPULAR, BuildConfig.API_KEY);
		} else if (id == R.id.action_top){
			fetchMovies(Constants.Url.API_BASE + Constants.Route.TOP, BuildConfig.API_KEY);
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
			new GetMovieTask(this, this).execute(popularUrl);
		} else {
			Snackbar.make(getCurrentFocus(), R.string.connection_problem, Snackbar.LENGTH_LONG);
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
		Snackbar.make(getCurrentFocus(), R.string.connection_problem, Snackbar.LENGTH_LONG);
	}

	@Override
	public void onItemClick(int index){
		Movie movie = movies.get(index);
		goToMovieDetails(movie);
	}

	@Override
	public void onPreExecute(){
		movieGrid.setVisibility(View.INVISIBLE);
		progressBar.setVisibility(View.VISIBLE);
		emptyText.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onPostExecute(String result){
		progressBar.setVisibility(View.INVISIBLE);
		if (result != null && !result.equals("")){
			parseResponse(result);
		} else {
			showConnectionErrorMessage();
		}
		showMovies();
	}
}
