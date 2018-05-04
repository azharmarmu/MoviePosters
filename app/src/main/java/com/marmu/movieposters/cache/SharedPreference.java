package com.marmu.movieposters.cache;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.marmu.movieposters.R;

public class SharedPreference {

    public static String getMovieList(Activity activity) {
        SharedPreferences preferences = activity
                .getSharedPreferences(activity.getString(R.string.app_name), Context.MODE_PRIVATE);
        return preferences.getString("movieList", "");
    }

    public static void setMovieList(Activity activity, String _movieList) {
        SharedPreferences.Editor editor = activity
                .getSharedPreferences(activity.getString(R.string.app_name), Context.MODE_PRIVATE).edit();
        editor.putString("movieList", _movieList);
        editor.apply();
    }

    public static String getMovie(Activity activity, String movieID) {
        SharedPreferences preferences = activity
                .getSharedPreferences(activity.getString(R.string.app_name), Context.MODE_PRIVATE);
        return preferences.getString(movieID, "");
    }

    public static void setMovie(Activity activity, String movieID, String _movie) {
        SharedPreferences.Editor editor = activity
                .getSharedPreferences(activity.getString(R.string.app_name), Context.MODE_PRIVATE).edit();
        editor.putString(movieID, _movie);
        editor.apply();
    }

    public static boolean getFav(Activity activity, String movieID) {
        SharedPreferences preferences = activity
                .getPreferences(Context.MODE_PRIVATE);
        return preferences.getBoolean(movieID, false);
    }

    public static void setFav(Activity activity, String movieID, boolean _fav) {
        SharedPreferences.Editor editor = activity
                .getPreferences(Context.MODE_PRIVATE).edit();
        editor.putBoolean(movieID, _fav);
        editor.apply();
    }
}
