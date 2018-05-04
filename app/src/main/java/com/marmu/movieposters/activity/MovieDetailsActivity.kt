package com.marmu.movieposters.activity

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.marmu.movieposters.R
import com.marmu.movieposters.cache.SharedPreference
import com.marmu.movieposters.model.Movie
import com.marmu.movieposters.retrofit.MovieApiClient
import com.marmu.movieposters.retrofit.MovieApiService
import com.marmu.movieposters.utils.Constants
import com.marmu.movieposters.utils.NetworkUtils
import okhttp3.ResponseBody

import org.json.JSONException
import org.json.JSONObject

import java.io.IOException
import java.util.Objects

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("CheckResult", "SetTextI18n")
class MovieDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = intent.extras
        if (bundle != null) {

            val movieID = Objects.requireNonNull(bundle.get(getString(R.string.intentKey))).toString()

            // loads from local
            if (!SharedPreference.getMovie(this@MovieDetailsActivity, movieID).isEmpty()) {
                setContentView(R.layout.activity_movie_details)
                val movieString = SharedPreference.getMovie(this@MovieDetailsActivity, movieID)
                val gson = Gson()
                val movie = gson.fromJson(movieString, Movie::class.java)
                populateView(movieID, movie)
            }

            // loads from network
            if (NetworkUtils().isNetworkConnected(this)) {
                setContentView(R.layout.activity_movie_details)
                getMovieDetails(movieID)
            } else {
                // You can show custom layout
                Log.d(Movie::class.java.name, "No network")
            }
        }
    }


    private fun getMovieDetails(movieID: String) {
        val apiService = MovieApiClient.getClient(this).create(MovieApiService::class.java)

        val call = apiService.getMovie(movieID, getString(R.string.apiKey))
        call.enqueue(object : Callback<Movie> {

            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                if (response.isSuccessful) {
                    val movie = response.body()
                    populateView(movieID, movie)

                    val gson = Gson()
                    val movieString = gson.toJson(movie)
                    SharedPreference.setMovie(this@MovieDetailsActivity, movieID, movieString)
                } else {
                    try {
                        Toast.makeText(this@MovieDetailsActivity,
                                JSONObject(Objects.requireNonNull<ResponseBody>(response.errorBody()).string())
                                        .getString("message"), Toast.LENGTH_SHORT).show()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }

            override fun onFailure(call: Call<Movie>, t: Throwable) {
                Toast.makeText(this@MovieDetailsActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun populateView(movieID: String, movie: Movie?) {
        val container = findViewById<LinearLayout>(R.id.container)
        val movieName = findViewById<TextView>(R.id.movieName)
        val moviePoster = findViewById<ImageView>(R.id.moviePoster)
        val movieYear = findViewById<TextView>(R.id.movieYear)
        val movieDuration = findViewById<TextView>(R.id.movieDuration)
        val movieRating = findViewById<TextView>(R.id.movieRating)
        val movieFav = findViewById<TextView>(R.id.movieFav)
        val movieOverview = findViewById<TextView>(R.id.movieOverview)

        movieName.text = Objects.requireNonNull<Movie>(movie).title
        movieYear.text = Objects.requireNonNull<Movie>(movie).releaseDate
        movieDuration.text = Objects.requireNonNull<Movie>(movie).runtime!!.toString() + "min"
        movieRating.text = Objects.requireNonNull<Movie>(movie).voteAverage!!.toString() + "/10"
        movieOverview.text = Objects.requireNonNull<Movie>(movie).overview


        if (SharedPreference.getFav(this@MovieDetailsActivity, movieID)) {
            movieFav.text = Constants.favorite
        } else {
            movieFav.text = Constants.notFavorite
        }

        movieFav.setOnClickListener {
            if (SharedPreference.getFav(this@MovieDetailsActivity, movieID)) {
                SharedPreference.setFav(this@MovieDetailsActivity, movieID, false)
                movieFav.text = Constants.notFavorite
            } else {
                SharedPreference.setFav(this@MovieDetailsActivity, movieID, true)
                movieFav.text = Constants.favorite
            }
        }

        val options = RequestOptions()
        options.fitCenter()

        Glide.with(this@MovieDetailsActivity)
                .load(getString(R.string.imagePath) + movie!!.posterPath)
                .apply(options)
                .into(moviePoster)

        container.visibility = View.VISIBLE
    }
}
