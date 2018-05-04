package com.marmu.movieposters.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.marmu.movieposters.R;
import com.marmu.movieposters.adapter.MovieAdapter;
import com.marmu.movieposters.cache.SharedPreference;
import com.marmu.movieposters.model.Movie;
import com.marmu.movieposters.model.PopularMovie;
import com.marmu.movieposters.retrofit.MovieApiClient;
import com.marmu.movieposters.retrofit.MovieApiService;
import com.marmu.movieposters.utils.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // loads from local
        if (!SharedPreference.getMovieList(MainActivity.this).isEmpty()) {
            setContentView(R.layout.activity_main);
            String movieListString = SharedPreference.getMovieList(MainActivity.this);
            Gson gson = new Gson();
            Type type = new TypeToken<List<PopularMovie.Result>>() {
            }.getType();
            List<PopularMovie.Result> movieList = gson.fromJson(movieListString, type);
            populateView(movieList);
        }

        // loads from network
        if (new NetworkUtils().isNetworkConnected(this)) {
            setContentView(R.layout.activity_main);
            getPopularMovies();
        } else {
            // You can show custom layout
            Log.d(Movie.class.getName(), "No network");
        }

    }

    private void getPopularMovies() {
        MovieApiService apiService = MovieApiClient.getClient(this).create(MovieApiService.class);

        Call<PopularMovie> call = apiService.getPopularMovies(getString(R.string.apiKey));
        call.enqueue(new Callback<PopularMovie>() {
            @Override
            public void onResponse(@NonNull Call<PopularMovie> call, @NonNull Response<PopularMovie> response) {
                if (response.isSuccessful()) {
                    List<PopularMovie.Result> movieList = Objects.requireNonNull(response.body()).getResults();
                    populateView(movieList);

                    Gson gson = new Gson();
                    String movieListString = gson.toJson(movieList);
                    SharedPreference.setMovieList(MainActivity.this, movieListString);
                } else {
                    try {
                        Toast.makeText(MainActivity.this,
                                new JSONObject(Objects.requireNonNull(response.errorBody()).string())
                                        .getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<PopularMovie> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void populateView(List<PopularMovie.Result> movieList) {

        RecyclerView rvMovies = findViewById(R.id.rvMovies);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(
                this,
                2,
                GridLayoutManager.VERTICAL,
                false
        );
        rvMovies.setLayoutManager(gridLayoutManager);
        rvMovies.setItemAnimator(new DefaultItemAnimator());

        MovieAdapter movieAdapter = new MovieAdapter(movieList, this);
        rvMovies.setAdapter(movieAdapter);
    }
}
