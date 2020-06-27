package com.example.flixster;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.models.Movie;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity {
    public static final String TAG = "MovieDetailsActivity";

    // The movie to display
    Movie movie;

    // The view objects
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    ImageView thumbnail;
    ImageButton playButton;
    ProgressBar popularity;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // Resolves the view objects
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        thumbnail = (ImageView) findViewById(R.id.thumbnail);
        playButton = (ImageButton) findViewById(R.id.playButton);
        popularity = (ProgressBar) findViewById(R.id.popularity);
        context = this;

        // Unwraps the movie passed in via intent, using its simple name as the key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d(TAG, String.format("Showing details for '%s'", movie.getTitle()));

        AsyncHttpClient client = new AsyncHttpClient();
        String getVideosURL = "https://api.themoviedb.org/3/movie/" + movie.getId() + "/videos?api_key=" + getString(R.string.tmdb_key);
        client.get(getVideosURL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;

                try {
                    final String youtubeKey = jsonObject.getJSONArray("results").getJSONObject(0).getString("key");
                    Log.i(TAG, "Key: " + youtubeKey);

                    playButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Intent to go to the MovieTrailerActivity

                            Intent intent = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                            intent.putExtra("youtubeKey", youtubeKey);
                            startActivity(intent);
                        }
                    });

                    thumbnail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Intent to go to the MovieTrailerActivity

                            Intent intent = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                            intent.putExtra("youtubeKey", youtubeKey);
                            startActivity(intent);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });

        // Sets the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        // Vote average is 0-10, converts to 0-5
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);

        int popularityScore = movie.getPopularity().intValue();
        popularity.setProgress(popularityScore);


        String imageUrl = movie.getBackdropPath();
        Log.d(TAG, imageUrl);

        Glide.with(context)
                .load(imageUrl)
                .transform(new RoundedCornersTransformation(30, 10))
                .placeholder(R.drawable.flicks_landscape_placeholder)
                .into(thumbnail);
    }
}
