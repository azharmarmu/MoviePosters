package com.marmu.movieposters.retrofit;

import com.marmu.movieposters.model.Movie;
import com.marmu.movieposters.model.PopularMovie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApiService {

    @GET("popular")
    Call<PopularMovie> getPopularMovies(@Query("api_key") String apiKey);

    @GET("top_rated")
    Call<PopularMovie> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("{movie_id}")
    Call<Movie> getMovie(@Path("movie_id")String string, @Query("api_key") String apiKey);
}
