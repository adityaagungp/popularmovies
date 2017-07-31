package com.aditya.popularmovies.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aditya.popularmovies.R;
import com.aditya.popularmovies.object.Review;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

	private Context context;
	private ArrayList<Review> reviews;
	private ItemClickListener listener;

	public ReviewAdapter(Context context, ItemClickListener listener){
		this.context = context;
		this.listener = listener;
	}

	@Override
	public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.item_review, parent, false);
		return new ReviewViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ReviewViewHolder holder, int position){
		Review review = reviews.get(position);
		holder.bind(review);
	}

	@Override
	public int getItemCount(){
		return reviews.size();
	}

	public void setReviews(ArrayList<Review> reviews){
		this.reviews = reviews;
		notifyDataSetChanged();
	}

	class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		TextView content;
		TextView author;

		ReviewViewHolder(View itemView){
			super(itemView);
			content = (TextView) itemView.findViewById(R.id.content);
			author = (TextView) itemView.findViewById(R.id.author);
			itemView.setOnClickListener(this);
		}

		void bind(Review review){
			content.setText(review.getContent());
			author.setText(review.getAuthor());
		}

		@Override
		public void onClick(View v){
			int clickedPosition = getAdapterPosition();
			listener.onItemClick(clickedPosition);
		}
	}
}
