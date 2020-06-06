package com.example.assignment3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3.R;
import com.example.assignment3.model.MemoirEntry;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MemoirRecyclerViewAdapter extends RecyclerView.Adapter
        <MemoirRecyclerViewAdapter.ViewHolder>{

    private List<MemoirEntry> memoirEntries;

    // record click position
    private int mSelectedPosition;

    public void setSelectedPosition(int position) {
        mSelectedPosition = position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // ViewHolder should contain variables for all the views in each row of the list
        public TextView movieNameTextView;
        public TextView releaseDateTextView;
        public TextView imdbIdTextView;
        public TextView watchDateTextView;
        public TextView cinemaPostcodeView;
        public TextView commentTextView;
        public ImageView movieImageView;
        public RatingBar userRatingBar;
        public RatingBar publicRatingBar;

        // a constructor that accepts the entire View (itemView)
        // provides a reference and access to all the views in each row
        public ViewHolder(View itemView) {
            super(itemView);
            movieNameTextView = itemView.findViewById(R.id.memoir_name);
            releaseDateTextView = itemView.findViewById(R.id.memoir_release_date);
            imdbIdTextView = itemView.findViewById(R.id.memoir_imdb_id);
            watchDateTextView = itemView.findViewById(R.id.memoir_user_watch_date);
            cinemaPostcodeView = itemView.findViewById(R.id.memoir_cinema_postcode);
            commentTextView = itemView.findViewById(R.id.memoir_comment);
            movieImageView = itemView.findViewById(R.id.memoir_image);
            userRatingBar = itemView.findViewById(R.id.memoir_user_rating_bar);
            publicRatingBar = itemView.findViewById(R.id.memoir_public_rating_bar);
        }
    }

    @Override
    public int getItemCount() {
        return memoirEntries.size();
    }

    // Pass in the contact array into the constructor
    public MemoirRecyclerViewAdapter(List<MemoirEntry> units) {
        memoirEntries = units;
    }

    // add items from the list units
    public void addUnits(List<MemoirEntry> units) {
        memoirEntries = units;
        notifyDataSetChanged();
    }

    // remove all units from recycler view
    public void removeAllUnits() {
        int size = memoirEntries.size();
        memoirEntries.clear();
        notifyItemRangeRemoved(0, size);
    }

    //This method creates a new view holder that is constructed with a new View, inflate from a layout
    @Override
    public MemoirRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                   int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the view from an XML layout file
        View unitsView = inflater.inflate(R.layout.rv_layout_memoir, parent, false);

        // construct the viewholder with the new view
        MemoirRecyclerViewAdapter.ViewHolder viewHolder = new MemoirRecyclerViewAdapter.ViewHolder(unitsView);
        return viewHolder;
    }

    // this method binds the view holder created with data that will be displayed
    @Override
    public void onBindViewHolder(@NonNull MemoirRecyclerViewAdapter.ViewHolder viewHolder,
                                 final int position) {
        final MemoirEntry entry = memoirEntries.get(position);
        // viewholder binding with its data at the specified position
        TextView tvMovieName = viewHolder.movieNameTextView;
        tvMovieName.setText(entry.getMovieName());

        TextView tvImdbId = viewHolder.imdbIdTextView;
        tvImdbId.setText(entry.getImdbId());

        TextView tvComment = viewHolder.commentTextView;
        tvComment.setText(entry.getComment());

        TextView tvWatchDate = viewHolder.watchDateTextView;
        tvWatchDate.setText("Watched on " + entry.getWatchDate().split("T")[0]);

        TextView tvCinemaPostcode = viewHolder.cinemaPostcodeView;
        tvCinemaPostcode.setText("Cinema postcode: " + entry.getCinemaPostcode());

        TextView tvReleaseDate = viewHolder.releaseDateTextView;
        tvReleaseDate.setText("Released on " + entry.getReleaseDate().split("T")[0]);

        ImageView imageView = viewHolder.movieImageView;
        Picasso.get().load(entry.getImageURL()).into(imageView);

        RatingBar userRatingBar = viewHolder.userRatingBar;
        userRatingBar.setRating((float)(entry.getUserRating() / 20.0));

        RatingBar publicRatingBar = viewHolder.publicRatingBar;
        publicRatingBar.setRating((float)(entry.getImdbRating()));

    }
}
