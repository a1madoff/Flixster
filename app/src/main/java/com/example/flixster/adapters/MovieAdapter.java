package com.example.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixster.MovieDetailsActivity;
import com.example.flixster.R;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    // Inflates the layout from XML and returns the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        Log.d("MovieAdapter", "onCreateViewHolder");
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    // Populates data into the item through the holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Log.d("MovieAdapter", "onBindViewHolder " + position);

        // Gets the movie at the passed in position
        Movie movie = movies.get(position);

        // Binds the movie data into the ViewHolder
        holder.bind(movie);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);

            // Adds this as the itemView's OnClickListener
            itemView.setOnClickListener(this);
        }

        // When the user clicks on a row, shows MovieDetailsActivity for the selected movie
        @Override
        public void onClick(View view) {
            // Gets the item's position
            int position = getAdapterPosition();
            // Makes sure the position is valid, i.e. actually exists in the view
            if (position != RecyclerView.NO_POSITION) {
                // Gets the movie at the position
                Movie movie = movies.get(position);
                // Creates the intent for the new activity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                // Serializes the movie using Parceler using its short name as the key
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // Shows the activity
                context.startActivity(intent);
            }
        }

        public void bind(Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());

            String imageUrl;
            // If the phone is in landscape mode
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageUrl = movie.getBackdropPath();
                Glide.with(context)
                        .load(imageUrl)
                        .placeholder(R.drawable.flicks_landscape_placeholder)
                        .into(ivPoster);
            } else { // If the phone is in portrait mode
                imageUrl = movie.getPosterPath();
                Glide.with(context)
                        .load(imageUrl)
                        .placeholder(R.drawable.flicks_portrait_placeholder)
                        .into(ivPoster);
            }
        }
    }
}
