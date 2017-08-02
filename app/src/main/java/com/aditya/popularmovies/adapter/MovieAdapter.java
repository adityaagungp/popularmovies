package com.aditya.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aditya.popularmovies.BuildConfig;
import com.aditya.popularmovies.R;
import com.aditya.popularmovies.object.Movie;
import com.aditya.popularmovies.view.ItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

	private Context context;
	private ArrayList<Movie> movies;
	private ItemClickListener listener;

	public MovieAdapter(Context context, ItemClickListener listener){
		this.context = context;
		this.listener = listener;
	}

	@Override
	public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.item_movie, parent, false);
		return new MovieViewHolder(view);
	}

	@Override
	public void onBindViewHolder(MovieViewHolder holder, int position){
		Movie movie = movies.get(position);
		holder.bind(movie);
	}

	@Override
	public int getItemCount(){
		return movies.size();
	}

	public void setMovies(ArrayList<Movie> movies){
		this.movies = movies;
		notifyDataSetChanged();
	}

	class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		ImageView thumbnail;

		MovieViewHolder(View itemView){
			super(itemView);
			thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
			thumbnail.setOnClickListener(this);
		}

		void bind(Movie movie){
			String imageUrl = BuildConfig.API_IMAGE + movie.getPosterPath();
			Picasso.with(context).load(imageUrl).into(thumbnail);
		}

		@Override
		public void onClick(View v){
			int clickedPosition = getAdapterPosition();
			listener.onItemClick(clickedPosition);
		}
	}
}
