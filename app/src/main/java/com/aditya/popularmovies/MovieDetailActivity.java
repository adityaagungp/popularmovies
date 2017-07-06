package com.aditya.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.aditya.popularmovies.object.Movie;
import com.aditya.popularmovies.util.Constants;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {

	private TextView movieTitle;
	private ImageView moviePoster;
	private TextView releaseDate;
	private TextView userRating;
	private TextView synopsis;

	private Movie movie;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_detail);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		movieTitle = (TextView) findViewById(R.id.movieTitle);
		moviePoster = (ImageView) findViewById(R.id.moviePoster);
		releaseDate = (TextView) findViewById(R.id.releaseDate);
		userRating = (TextView) findViewById(R.id.userRating);
		synopsis = (TextView) findViewById(R.id.synopsis);

		Intent intent = getIntent();
		if (intent.hasExtra(Constants.Param.MOVIE)){
			movie = intent.getParcelableExtra(Constants.Param.MOVIE);
			setDetailView();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		int id = item.getItemId();
		if (id == android.R.id.home){
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}

	private void setDetailView(){
		movieTitle.setText(movie.getTitle());
		String imageUrl = Constants.Url.IMAGE_BASE + movie.getPosterPath();
		Picasso.with(this).load(imageUrl).into(moviePoster);
		StringBuilder releaseBuilder = new StringBuilder();
		releaseBuilder.append(getString(R.string.release_date)).append(": ").append(movie.getReleaseDate());
		releaseDate.setText(new String(releaseBuilder));
		StringBuilder ratingBuilder = new StringBuilder();
		ratingBuilder.append(movie.getVoteAverage()).append("/10");
		userRating.setText(new String(ratingBuilder));
		synopsis.setText(movie.getOverview());
	}
}
