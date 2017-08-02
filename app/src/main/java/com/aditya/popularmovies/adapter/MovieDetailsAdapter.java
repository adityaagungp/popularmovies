package com.aditya.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aditya.popularmovies.R;
import com.aditya.popularmovies.object.Review;
import com.aditya.popularmovies.object.Section;
import com.aditya.popularmovies.object.Video;
import com.aditya.popularmovies.view.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final int VIEW_HEADER = 0;
	private static final int VIEW_SECTION = 1;
	private static final int VIEW_TRAILER = 2;
	private static final int VIEW_REVIEW = 3;

	private Context context;
	private View header;
	private List<Section> sections = new ArrayList<>();
	private ArrayList<Video> trailers = new ArrayList<>();
	private ArrayList<Review> reviews = new ArrayList<>();
	private ItemClickListener videoListener;

	public MovieDetailsAdapter(Context context, ItemClickListener videoListener){
		this.context = context;
		this.videoListener = videoListener;
	}

	public void setHeader(View v){
		header = v;
		notifyDataSetChanged();
	}

	public void setVideoListener(ItemClickListener videoListener){
		this.videoListener = videoListener;
		notifyDataSetChanged();
	}

	public void setTrailers(ArrayList<Video> trailers){
		this.trailers = trailers;
		resetSections();
		notifyDataSetChanged();
	}

	public void setReviews(ArrayList<Review> reviews){
		this.reviews = reviews;
		resetSections();
		notifyDataSetChanged();
	}

	private void resetSections(){
		sections.clear();
		int offset = header == null ? 0 : 1;
		if (trailers.size() > 0){
			Section section = new Section(offset, context.getString(R.string.trailers));
			sections.add(section);
		}
		if (reviews.size() > 0){
			Section section = new Section(offset + sections.size() + trailers.size(), context.getString(R.string
				.reviews));
			sections.add(section);
		}
	}

	private int getMinSectionPosition(){
		int min = Integer.MAX_VALUE;
		for (Section section : sections){
			if (section.getPosition() < min){
				min = section.getPosition();
			}
		}
		return min;
	}

	private int getMaxSectionPosition(){
		int max = Integer.MIN_VALUE;
		for (Section section : sections){
			if (section.getPosition() > max){
				max = section.getPosition();
			}
		}
		return max;
	}

	private int getDataVideoPosition(int position){
		if (trailers.size() == 0){
			return -1;
		} else {
			return position - 1 - (header == null ? 0 : 1);
		}
	}

	private int getDataReviewPosition(int position){
		if (reviews.size() == 0){
			return -1;
		} else {
			return position - sections.size() - trailers.size() - (header == null ? 0 : 1);
		}
	}

	private int getSectionIndex(int position){
		int index = -1;
		for (int i = 0; i < sections.size(); i++){
			if (sections.get(i).getPosition() == position) {
				index = i;
				break;
			}
		}
		return index;
	}

	@Override
	public int getItemViewType(int position){
		if (header != null && position == 0)
			return VIEW_HEADER;
		else if (isSectionPosition(position)){
			return VIEW_SECTION;
		} else {
			if (trailers.size() > 0 && reviews.size() > 0){
				if (position < getMaxSectionPosition())
					return VIEW_TRAILER;
				else return VIEW_REVIEW;
			} else if (trailers.size() > 0){
				return VIEW_TRAILER;
			} else {
				return VIEW_REVIEW;
			}
		}
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
		if (viewType == VIEW_HEADER){
			return new HeaderHolder(header);
		} else {
			LayoutInflater inflater = LayoutInflater.from(context);
			if (viewType == VIEW_SECTION){
				View view = inflater.inflate(R.layout.item_section, parent, false);
				return new SectionViewHolder(view);
			} else if (viewType == VIEW_TRAILER){
				View view = inflater.inflate(R.layout.item_trailer, parent, false);
				return new VideoViewHolder(view);
			} else {
				View view = inflater.inflate(R.layout.item_review, parent, false);
				return new ReviewViewHolder(view);
			}
		}
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
		if (header == null || position != 0){
			if (isSectionPosition(position)){
				((SectionViewHolder) holder).bind(sections.get(getSectionIndex(position)));
			} else {
				if (trailers.size() > 0 && reviews.size() > 0){
					if (position < getMaxSectionPosition()){
						Video video = trailers.get(getDataVideoPosition(position));
						((VideoViewHolder) holder).bind(video);
					} else {
						Review review = reviews.get(getDataReviewPosition(position));
						((ReviewViewHolder) holder).bind(review);
					}
				} else if (trailers.size() > 0){
					Video video = trailers.get(getDataVideoPosition(position));
					((VideoViewHolder) holder).bind(video);
				} else {
					Review review = reviews.get(getDataReviewPosition(position));
					((ReviewViewHolder) holder).bind(review);
				}
			}
		}
	}

	@Override
	public int getItemCount(){
		return ((header == null) ? 0 : 1) + sections.size() + trailers.size() + reviews.size();
	}

	private boolean isSectionPosition(int position){
		for (Section section : sections){
			if (section.getPosition() == position){
				return true;
			}
		}
		return false;
	}

	class HeaderHolder extends RecyclerView.ViewHolder {
		HeaderHolder(View itemView){
			super(itemView);
		}
	}

	class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		TextView title;

		VideoViewHolder(View itemView){
			super(itemView);
			title = (TextView) itemView.findViewById(R.id.title);
			itemView.setOnClickListener(this);
		}

		void bind(Video video){
			title.setText(video.getName());
		}

		@Override
		public void onClick(View v){
			int clickedPosition = getDataVideoPosition(getAdapterPosition());
			videoListener.onItemClick(clickedPosition);
		}
	}

	class ReviewViewHolder extends RecyclerView.ViewHolder {

		TextView content;
		TextView author;

		ReviewViewHolder(View itemView){
			super(itemView);
			content = (TextView) itemView.findViewById(R.id.content);
			author = (TextView) itemView.findViewById(R.id.author);
		}

		void bind(Review review){
			content.setText(review.getContent());
			author.setText(review.getAuthor());
		}
	}

	class SectionViewHolder extends RecyclerView.ViewHolder {

		TextView title;

		SectionViewHolder(View itemView){
			super(itemView);
			title = (TextView) itemView.findViewById(R.id.title);
		}

		void bind(Section section){
			title.setText(section.getTitle());
		}
	}
}
