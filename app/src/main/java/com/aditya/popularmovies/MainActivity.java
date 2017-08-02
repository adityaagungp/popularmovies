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
import com.aditya.popularmovies.presenter.MoviesPresenter;
import com.aditya.popularmovies.util.Constants;
import com.aditya.popularmovies.view.ItemClickListener;
import com.aditya.popularmovies.adapter.MovieAdapter;
import com.aditya.popularmovies.view.MoviesView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ItemClickListener, MoviesView {

	private static final int MOVIE_LOADER_ID = 1;

	private RecyclerView movieGrid;
	private ProgressBar progressBar;
	private TextView emptyText;

	private MoviesPresenter moviesPresenter;
	private MovieAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		movieGrid = (RecyclerView) findViewById(R.id.movieGrid);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		emptyText = (TextView) findViewById(R.id.emptyText);

		moviesPresenter = new MoviesPresenter(this, this);

		if (savedInstanceState != null && savedInstanceState.containsKey(Constants.Param.MOVIES)){
			ArrayList<Movie> movies = savedInstanceState.getParcelableArrayList(Constants.Param.MOVIES);
			moviesPresenter.setMovies(movies);
		}
		if (moviesPresenter.getMovies() == null || moviesPresenter.getMovies().size() == 0){
			fetchPopularMovies();
		}

		adapter = new MovieAdapter(this, this);
		adapter.setMovies(moviesPresenter.getMovies());
		GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
		movieGrid.setLayoutManager(layoutManager);
		movieGrid.setAdapter(adapter);

		getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, moviesPresenter);
	}

	@Override
	protected void onResume(){
		super.onResume();
		getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, null, moviesPresenter);
	}

	@Override
	public void onSaveInstanceState(Bundle outState){
		outState.putParcelableArrayList(Constants.Param.MOVIES, moviesPresenter.getMovies());
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
			fetchPopularMovies();
			moviesPresenter.setMovieSortedByFavorite(false);
		} else if (id == R.id.action_top){
			fetchTopMovies();
			moviesPresenter.setMovieSortedByFavorite(false);
		} else if (id == R.id.action_favorites){
			moviesPresenter.setMovieSortedByFavorite(true);
			moviesPresenter.sortByFavorite();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(int index){
		Movie movie = moviesPresenter.getMovies().get(index);
		goToMovieDetails(movie);
	}

	@Override
	public void onPreGetMovies(){
		movieGrid.setVisibility(View.INVISIBLE);
		progressBar.setVisibility(View.VISIBLE);
		emptyText.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onSuccessGetMovies(){
		progressBar.setVisibility(View.INVISIBLE);
		adapter.setMovies(moviesPresenter.getMovies());
		showMovies();
	}

	@Override
	public void onFailGetMovies(){
		progressBar.setVisibility(View.INVISIBLE);
		showConnectionErrorMessage();
		showMovies();
	}

	private void goToMovieDetails(Movie movie){
		Intent intent = new Intent(this, MovieDetailActivity.class);
		intent.putExtra(Constants.Param.MOVIE, movie);
		startActivity(intent);
	}

	private void fetchPopularMovies(){
		moviesPresenter.getPopularMovies();
	}

	private void fetchTopMovies(){
		moviesPresenter.getTopMovies();
	}

	private void showMovies(){
		if (moviesPresenter.getMovies().size() > 0){
			emptyText.setVisibility(View.INVISIBLE);
			movieGrid.setVisibility(View.VISIBLE);
		} else {
			emptyText.setVisibility(View.VISIBLE);
			movieGrid.setVisibility(View.INVISIBLE);
		}
	}

	private void showConnectionErrorMessage(){
		Snackbar.make(getWindow().getDecorView().getRootView(), R.string.connection_problem, Snackbar.LENGTH_LONG);
	}
}
