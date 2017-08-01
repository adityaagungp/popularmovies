package com.aditya.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

	public static final String CONTENT_AUTHORITY = "com.aditya.popularmovies.app";
	public static final Uri BASE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

	public static final class MovieEntry implements BaseColumns {

		public static final String TABLE_MOVIE = "movie";
		public static final String _ID = "_id";
		public static final String TITLE = "title";

		//Content Uri
		public static final Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(TABLE_MOVIE).build();
		public static final String CONTENT_DIR_TYPE =
			ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_MOVIE;
		public static final String CONTENT_ITEM_TYPE =
			ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_MOVIE;

		public static Uri buildMovieUri(long id){
			return ContentUris.withAppendedId(CONTENT_URI, id);
		}
	}
}
