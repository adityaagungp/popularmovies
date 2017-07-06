package com.aditya.popularmovies.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

	public static boolean isOnline(Context context){
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();
	}

	public static URL buildUrl(String root, String key){
		Uri builtUri = Uri.parse(root).buildUpon().appendQueryParameter(Constants.Param.API_KEY, key).build();
		URL url = null;
		try {
			url = new URL(builtUri.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return url;
	}

	public static String getResponseFromUrl(URL url) throws IOException {
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		try {
			InputStream in = urlConnection.getInputStream();
			Scanner scanner = new Scanner(in);
			scanner.useDelimiter("\\A");
			boolean hasInput = scanner.hasNext();
			if (hasInput) {
				return scanner.next();
			} else {
				return null;
			}
		} finally {
			urlConnection.disconnect();
		}
	}
}
