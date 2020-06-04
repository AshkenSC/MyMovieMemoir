package com.example.assignment3.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment3.HomeActivity;
import com.example.assignment3.R;
import com.example.assignment3.localDB.entity.WatchlistEntry;

import java.util.List;

public class WatchlistRecyclerViewAdapter extends RecyclerView.Adapter
        <WatchlistRecyclerViewAdapter.ViewHolder> {

    // used for button functions (view/delete)
    Context currentContext;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // ViewHolder should contain variables for all the views in each row of the list
        public TextView movieNameTextView;
        public TextView releaseDateTextView;
        public TextView addDateTextView;
        public TextView imdbIdView;


        // a constructor that accepts the entire View (itemView)
        // provides a reference and access to all the views in each row
        public ViewHolder(View itemView) {
            super(itemView);
            movieNameTextView = itemView.findViewById(R.id.watchlist_movie_name);
            releaseDateTextView = itemView.findViewById(R.id.watchlist_release_date);
            addDateTextView = itemView.findViewById(R.id.watchlist_add_date);
            imdbIdView = itemView.findViewById(R.id.watchlist_imdb_id);
        }
    }


    @Override
    public int getItemCount() {
        return watchlist.size();
    }
    private List<WatchlistEntry> watchlist;
    // Pass in the contact array into the constructor
    public WatchlistRecyclerViewAdapter(List<WatchlistEntry> units) {
        watchlist = units;
    }
    public void addUnits(List<WatchlistEntry> units) {
        watchlist = units;
        notifyDataSetChanged();
    }


    //This method creates a new view holder that is constructed with a new View, inflate from a layout
    @Override
    public WatchlistRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                             int viewType) {
        Context context = parent.getContext();
        currentContext = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the view from an XML layout file
        View unitsView = inflater.inflate(R.layout.rv_layout_watch, parent, false);
        // construct the viewholder with the new view
        ViewHolder viewHolder = new ViewHolder(unitsView);
        return viewHolder;
    }


    // this method binds the view holder created with data that will be displayed
    @Override
    public void onBindViewHolder(@NonNull WatchlistRecyclerViewAdapter.ViewHolder viewHolder,
                                 int position) {
        final WatchlistEntry entry = watchlist.get(position);
        // viewholder binding with its data at the specified position
        TextView tvMovieName = viewHolder.movieNameTextView;
        tvMovieName.setText(entry.getMovieName());

        TextView tvReleaseDate = viewHolder.releaseDateTextView;
        tvReleaseDate.setText("Released on: " + entry.getReleaseDate());

        TextView tvAddDate = viewHolder.addDateTextView;
        tvAddDate.setText("Added in: " + entry.getAddDate());

        TextView tvImdbId = viewHolder.imdbIdView;
        tvImdbId.setText("imdb id:" + entry.getImdbId());

        // buttons setting
//        Button btnView = viewHolder.viewDetailButton;
//        btnView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//
//        Button btnDelete = viewHolder.deleteButton;
//        btnDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DeleteWatchlist deleteWatchlist = new DeleteWatchlist();
//                deleteWatchlist.execute(entry.getImdbId());
//            }
//        });
    }

    public void removeEntry(int position) {
        watchlist.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
}
