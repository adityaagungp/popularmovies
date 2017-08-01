package com.aditya.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MovieProvider extends ContentProvider{

	private static final UriMatcher sUriMatcher = buildUriMatcher();
	private MovieDBHelper mOpenHelper;

	private static final int MOVIE = 100;
	private static final int MOVIE_WITH_ID = 101;

	@Override
	public boolean onCreate(){
		mOpenHelper = new MovieDBHelper(getContext());
		return true;
	}

	@Nullable
	@Override
	public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable
		String[] selectionArgs, @Nullable String sortOrder){
		Cursor cursor;
		switch (sUriMatcher.match(uri)){
			case MOVIE:
				cursor = mOpenHelper.getReadableDatabase().query(MovieContract.MovieEntry.TABLE_MOVIE,
					projection,
					selection,
					selectionArgs,
					null,
					null,
					sortOrder);
				return cursor;
			case MOVIE_WITH_ID:
				cursor = mOpenHelper.getReadableDatabase().query(MovieContract.MovieEntry.TABLE_MOVIE,
					projection,
					MovieContract.MovieEntry._ID + " =? ",
					new String[] {String.valueOf(ContentUris.parseId(uri))},
					null,
					null,
					sortOrder);
				return cursor;
			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
	}

	@Nullable
	@Override
	public String getType(@NonNull Uri uri){
		int match = sUriMatcher.match(uri);
		switch (match) {
			case MOVIE:
				return MovieContract.MovieEntry.CONTENT_DIR_TYPE;
			case MOVIE_WITH_ID:
				return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
	}

	@Nullable
	@Override
	public Uri insert(@NonNull Uri uri, @Nullable ContentValues values){
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		Uri returnUri;
		switch (sUriMatcher.match(uri)) {
			case MOVIE:
				long id = db.insert(MovieContract.MovieEntry.TABLE_MOVIE, null, values);
				if (id > 0) {
					returnUri = MovieContract.MovieEntry.buildMovieUri(id);
				} else {
					throw new android.database.SQLException("Failed to insert row into: " + uri);
				}
				break;

			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return returnUri;
	}

	@Override
	public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs){
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		final int match = sUriMatcher.match(uri);
		int numDeleted;
		switch(match){
			case MOVIE:
				numDeleted = db.delete(MovieContract.MovieEntry.TABLE_MOVIE, selection, selectionArgs);
				break;
			case MOVIE_WITH_ID:
				numDeleted = db.delete(MovieContract.MovieEntry.TABLE_MOVIE, MovieContract.MovieEntry._ID + " = ?",
					new String[]{String.valueOf(ContentUris.parseId(uri))});
				break;
			default:
				throw new UnsupportedOperationException("Unknown uri: " + uri);
		}
		return numDeleted;
	}

	@Override
	public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[]
		selectionArgs){
		final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int numUpdated = 0;
		if (values == null){
			throw new IllegalArgumentException("Cannot have null content values");
		}
		switch(sUriMatcher.match(uri)){
			case MOVIE :
				numUpdated = db.update(MovieContract.MovieEntry.TABLE_MOVIE, values, selection, selectionArgs);
				break;
			case MOVIE_WITH_ID: {
				numUpdated = db.update(MovieContract.MovieEntry.TABLE_MOVIE, values,
					MovieContract.MovieEntry._ID + " = ?",
					new String[] {String.valueOf(ContentUris.parseId(uri))});
				break;
			}
			default:{
				throw new UnsupportedOperationException("Unknown uri: " + uri);
			}
		}
		if (numUpdated > 0){
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return numUpdated;
	}

	private static UriMatcher buildUriMatcher(){
		final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
		final String authority = MovieContract.CONTENT_AUTHORITY;

		matcher.addURI(authority, MovieContract.MovieEntry.TABLE_MOVIE, MOVIE);
		matcher.addURI(authority, MovieContract.MovieEntry.TABLE_MOVIE + "/#", MOVIE_WITH_ID);
		return matcher;
	}
}
