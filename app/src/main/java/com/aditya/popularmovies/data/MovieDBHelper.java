package com.aditya.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDBHelper extends SQLiteOpenHelper{

	private static final String DATABASE_NAME = "movies.db";
	private static final int DATABASE_VERSION = 2;

	private static final String CREATE_MOVIE_TABLE = "CREATE TABLE " +
		MovieContract.MovieEntry.TABLE_MOVIE + "(" +
		MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY, " +
		MovieContract.MovieEntry.TITLE + " TEXT NOT NULL, " +
		MovieContract.MovieEntry.POSTER + " TEXT NOT NULL, " +
		MovieContract.MovieEntry.OVERVIEW + " TEXT NOT NULL, " +
		MovieContract.MovieEntry.RATING + " DOUBLE NOT NULL, " +
		MovieContract.MovieEntry.RELEASE + " TEXT NOT NULL);";

	private static final String ALTER_MOVIE_TABLE = "ALTER TABLE " + MovieContract.MovieEntry.TABLE_MOVIE +
		" ADD COLUMN " + MovieContract.MovieEntry.POSTER + " TEXT NOT NULL," +
		MovieContract.MovieEntry.OVERVIEW + " TEXT NOT NULL, " +
		MovieContract.MovieEntry.RATING + " DOUBLE NOT NULL, " +
		MovieContract.MovieEntry.RELEASE + " TEXT NOT NULL;";

	public MovieDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db){
		db.execSQL(CREATE_MOVIE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		if (oldVersion < 2) {
			db.execSQL(ALTER_MOVIE_TABLE);
		}
	}
}
