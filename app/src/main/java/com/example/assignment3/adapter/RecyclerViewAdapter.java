package com.example.assignment3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3.R;
import com.example.assignment3.model.HomeMemoirEntry;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter
<RecyclerViewAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        // ViewHolder should contain variables for all the views in each row of the list
        public TextView movieNameTextView;
        public TextView releaseDateTextView;
        public RatingBar ratingBarView;

        // a constructor that accepts the entire View (itemView)
        // provides a reference and access to all the views in each row
        public ViewHolder(View itemView) {
            super(itemView);
            movieNameTextView = itemView.findViewById(R.id.home_movie_name);
            releaseDateTextView = itemView.findViewById(R.id.home_release_date);
            ratingBarView =itemView.findViewById(R.id.home_rating_bar);
        }
    }


    @Override
    public int getItemCount() {
        return homeMemoirEntries.size();
    }
    private List<HomeMemoirEntry> homeMemoirEntries;
    // Pass in the contact array into the constructor
    public RecyclerViewAdapter(List<HomeMemoirEntry> units) {
        homeMemoirEntries = units;
    }
    public void addUnits(List<HomeMemoirEntry> units) {
        homeMemoirEntries = units;
        notifyDataSetChanged();
    }


    //This method creates a new view holder that is constructed with a new View, inflate from a layout
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                             int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the view from an XML layout file
        View unitsView = inflater.inflate(R.layout.rv_layout_home, parent, false);
        // construct the viewholder with the new view
        ViewHolder viewHolder = new ViewHolder(unitsView);
        return viewHolder;
    }


    // this method binds the view holder created with data that will be displayed
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder viewHolder,
                                 int position) {
        final HomeMemoirEntry entry = homeMemoirEntries.get(position);
        // viewholder binding with its data at the specified position
        TextView tvMovieName= viewHolder.movieNameTextView;
        tvMovieName.setText(entry.getMovieName());

        TextView tvReleaseDate = viewHolder.releaseDateTextView;
        tvReleaseDate.setText("Released on: " + entry.getReleaseDate());

        RatingBar ratingBar = viewHolder.ratingBarView;
        ratingBar.setRating((float)entry.getRating());
    }

}
