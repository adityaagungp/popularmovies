package com.aditya.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aditya.popularmovies.adapter.MovieDetailsAdapter;
import com.aditya.popularmovies.object.Movie;
import com.aditya.popularmovies.object.Video;
import com.aditya.popularmovies.presenter.MovieDetailPresenter;
import com.aditya.popularmovies.presenter.ReviewsPresenter;
import com.aditya.popularmovies.presenter.TrailersPresenter;
import com.aditya.popularmovies.util.Constants;
import com.aditya.popularmovies.view.ItemClickListener;
import com.aditya.popularmovies.view.MovieDetailView;
import com.aditya.popularmovies.view.ReviewsView;
import com.aditya.popularmovies.view.TrailersView;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity implements ItemClickListener, MovieDetailView,
                                                                      TrailersView, ReviewsView {

	private RecyclerView list;
	private TextView movieTitle;
	private ImageView moviePoster;
	private TextView releaseDate;
	private TextView userRating;
	private ImageButton btnFavorite;
	private TextView synopsis;

	private MovieDetailPresenter detailPresenter;
	private TrailersPresenter trailersPresenter;
	private ReviewsPresenter reviewsPresenter;
	private MovieDetailsAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_detail);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		list = (RecyclerView) findViewById(R.id.list);
		LinearLayoutManager llm = new LinearLayoutManager(this);
		list.setLayoutManager(llm);
		mAdapter = new MovieDetailsAdapter(this, this);
		list.setAdapter(mAdapter);
		DividerItemDecoration divider = new DividerItemDecoration(list.getContext(), llm.getOrientation());
		list.addItemDecoration(divider);

		View view = LayoutInflater.from(this).inflate(R.layout.header_movie_details, null, false);
		movieTitle = (TextView) view.findViewById(R.id.movieTitle);
		moviePoster = (ImageView) view.findViewById(R.id.moviePoster);
		releaseDate = (TextView) view.findViewById(R.id.releaseDate);
		userRating = (TextView) view.findViewById(R.id.userRating);
		btnFavorite = (ImageButton) view.findViewById(R.id.btnFavorite);
		synopsis = (TextView) view.findViewById(R.id.synopsis);

		mAdapter.setHeader(view);
		detailPresenter = new MovieDetailPresenter(this, this);

		Intent intent = getIntent();
		if (intent.hasExtra(Constants.Param.MOVIE)){
			Movie movie = intent.getParcelableExtra(Constants.Param.MOVIE);
			detailPresenter.setMovie(movie);
			trailersPresenter = new TrailersPresenter(this, this, movie.getId());
			trailersPresenter.getTrailers();
			reviewsPresenter = new ReviewsPresenter(this, this, movie.getId());
			reviewsPresenter.fetchReviews();
		}

		btnFavorite.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v){
				detailPresenter.changeFavoriteMovie();
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		int id = item.getItemId();
		if (id == android.R.id.home){
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(int index){
		Video video = trailersPresenter.getVideos().get(index);
		Uri videoPage = Uri.parse("http://www.youtube.com/watch?v=" + video.getKey());
		Intent intent = new Intent(Intent.ACTION_VIEW, videoPage);
		if (intent.resolveActivity(getPackageManager()) != null){
			startActivity(intent);
		}
	}

	@Override
	public void onSuccessGetTrailer(){
		mAdapter.setTrailers(trailersPresenter.getVideos());
	}

	@Override
	public void onFailGetTrailer(){
		showErrorMessage(getString(R.string.connection_problem));
	}

	@Override
	public void onSuccessGetReviews(){
		mAdapter.setReviews(reviewsPresenter.getReviews());
	}

	@Override
	public void onFailGetReviews(){
		showErrorMessage(getString(R.string.connection_problem));
	}

	@Override
	public void onMovieUpdated(Movie movie){
		setDetailView(movie);
	}

	@Override
	public void onFailMakeFavorite(){
		showErrorMessage(getString(R.string.fail_favorite));
	}

	@Override
	public void onFailMakeUnfavorite(){
		showErrorMessage(getString(R.string.fail_unfavorite));
	}

	private void setDetailView(Movie movie){
		getSupportActionBar().setTitle(movie.getTitle());
		movieTitle.setText(movie.getTitle());
		String imageUrl = BuildConfig.API_IMAGE + movie.getPosterPath();
		Picasso.with(this).load(imageUrl).into(moviePoster);
		StringBuilder releaseBuilder = new StringBuilder();
		releaseBuilder.append(getString(R.string.release_date)).append(": ").append(movie.getReleaseDate());
		releaseDate.setText(new String(releaseBuilder));
		StringBuilder ratingBuilder = new StringBuilder();
		ratingBuilder.append(movie.getVoteAverage()).append("/10");
		userRating.setText(new String(ratingBuilder));
		synopsis.setText(movie.getOverview());
		setButtonFavorite(movie.isFavorite() ? R.drawable.ic_favorite_full : R.drawable.ic_favorite);
	}

	private void setButtonFavorite(int id){
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN){
			btnFavorite.setBackgroundDrawable(ContextCompat.getDrawable(this, id));
		} else {
			btnFavorite.setBackground(ContextCompat.getDrawable(this, id));
		}
	}

	private void showErrorMessage(String message){
		Snackbar.make(getWindow().getDecorView().getRootView(), message, Snackbar.LENGTH_LONG);
	}
}
