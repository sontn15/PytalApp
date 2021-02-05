package com.sh.pytalapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;

import java.util.Objects;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class NetworkUtils {

    public static boolean haveNetwork(Context mContext) {
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(CONNECTIVITY_SERVICE);
        boolean is3g = Objects.requireNonNull(manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).isConnectedOrConnecting();
        boolean isWifi = Objects.requireNonNull(manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).isConnectedOrConnecting();
        return is3g || isWifi;
    }

}
