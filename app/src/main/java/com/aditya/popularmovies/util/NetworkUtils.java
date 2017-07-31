package com.aditya.popularmovies.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.aditya.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class NetworkUtils {

	public static boolean isOnline(Context context){
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();
	}

	public static Map<String, String> buildKeyMap(){
		Map<String, String> map = new HashMap<>();
		map.put(Constants.Param.API_KEY, BuildConfig.API_KEY);
		return map;
	}
}
