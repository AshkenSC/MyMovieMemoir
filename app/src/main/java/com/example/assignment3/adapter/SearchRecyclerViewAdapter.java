package com.example.assignment3.adapter;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3.R;
import com.example.assignment3.model.SearchResult;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class SearchRecyclerViewAdapter extends RecyclerView.Adapter
        <SearchRecyclerViewAdapter.ViewHolder> {

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
        public Button detailButton;
        public ImageView movieImageView;

        // a constructor that accepts the entire View (itemView)
        // provides a reference and access to all the views in each row
        public ViewHolder(View itemView) {
            super(itemView);
            movieNameTextView = itemView.findViewById(R.id.search_name);
            releaseDateTextView = itemView.findViewById(R.id.search_release_date);
            imdbIdTextView = itemView.findViewById(R.id.search_imdb_id);
            detailButton = itemView.findViewById(R.id.search_detail_button);
            movieImageView = itemView.findViewById(R.id.search_image);
        }
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    private List<SearchResult> searchResults;

    // Pass in the contact array into the constructor
    public SearchRecyclerViewAdapter(List<SearchResult> units) {
        searchResults = units;
    }

    public void addUnits(List<SearchResult> units) {
        searchResults = units;
        notifyDataSetChanged();
    }

    //This method creates a new view holder that is constructed with a new View, inflate from a layout
    @Override
    public SearchRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                   int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the view from an XML layout file
        View unitsView = inflater.inflate(R.layout.rv_layout_search, parent, false);

        // construct the viewholder with the new view
        ViewHolder viewHolder = new ViewHolder(unitsView);
        return viewHolder;
    }

    // this method binds the view holder created with data that will be displayed
    @Override
    public void onBindViewHolder(@NonNull SearchRecyclerViewAdapter.ViewHolder viewHolder,
                                 final int position) {
        final SearchResult entry = searchResults.get(position);
        // viewholder binding with its data at the specified position
        TextView tvMovieName = viewHolder.movieNameTextView;
        tvMovieName.setText(entry.getMovieName());

        TextView tvReleaseDate = viewHolder.releaseDateTextView;
        tvReleaseDate.setText(entry.getReleaseDate());

        TextView tvImdbId = viewHolder.imdbIdTextView;
        tvImdbId.setText(entry.getMovieIMDBid());

        ImageView imageView = viewHolder.movieImageView;
        Picasso.get().load(entry.getImageURL()).into(imageView);

    }

}
