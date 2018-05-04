package com.marmu.movieposters.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

import java.util.Objects;

public class NetworkUtils {
    public boolean isNetworkConnected(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return Objects.requireNonNull(cm).getActiveNetworkInfo() != null;
    }
}
