package com.marmu.movieposters.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.marmu.movieposters.R;
import com.marmu.movieposters.activity.MovieDetailsActivity;
import com.marmu.movieposters.model.PopularMovie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MoviesViewHolder> {

    private List<PopularMovie.Result> movieList;
    private Activity activity;

    public MovieAdapter(List<PopularMovie.Result> movieList, Activity activity) {
        this.movieList = movieList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(
                        parent.getContext()
                ).inflate(
                        R.layout.list_movies,
                        parent,
                        false
                );
        return new MoviesViewHolder(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder holder, int position) {
        final PopularMovie.Result movie = movieList.get(position);

        RequestOptions options = new RequestOptions();
        options.fitCenter();

        Glide.with(activity)
                .load(activity.getString(R.string.imagePath) + movie.getPosterPath())
                .apply(options)
                .into(holder.poster);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, MovieDetailsActivity.class);
                intent.putExtra(activity.getString(R.string.intentKey), movie.getId());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (movieList == null) ? 0 : movieList.size();
    }

    static class MoviesViewHolder extends RecyclerView.ViewHolder {

        ImageView poster;

        MoviesViewHolder(View v) {
            super(v);
            poster = v.findViewById(R.id.poster);
        }
    }
}

