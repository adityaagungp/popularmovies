package com.aditya.popularmovies.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aditya.popularmovies.R;
import com.aditya.popularmovies.object.Video;

import java.util.ArrayList;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

	private Context context;
	private ArrayList<Video> videos;
	private ItemClickListener listener;

	public TrailerAdapter(Context context, ItemClickListener listener){
		this.context = context;
		this.listener = listener;
	}

	@Override
	public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.item_trailer, parent, false);
		return new TrailerAdapter.TrailerViewHolder(view);
	}

	@Override
	public void onBindViewHolder(TrailerViewHolder holder, int position){
		Video video = videos.get(position);
		holder.bind(video);
	}

	@Override
	public int getItemCount(){
		return videos.size();
	}

	public void setVideos(ArrayList<Video> videos){
		this.videos = videos;
		notifyDataSetChanged();
	}

	class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		TextView title;

		TrailerViewHolder(View itemView){
			super(itemView);
			title = (TextView) itemView.findViewById(R.id.title);
			itemView.setOnClickListener(this);
		}

		void bind(Video video){
			title.setText(video.getName());
		}

		@Override
		public void onClick(View v){
			int clickedPosition = getAdapterPosition();
			listener.onItemClick(clickedPosition);
		}
	}
}
