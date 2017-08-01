package com.aditya.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDBHelper extends SQLiteOpenHelper{

	private static final String DATABASE_NAME = "movies.db";
	private static final int DATABASE_VERSION = 1;

	public MovieDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db){
		final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
			MovieContract.MovieEntry.TABLE_MOVIE + "(" + MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY, " +
			MovieContract.MovieEntry.TITLE + " TEXT NOT NULL);";
		db.execSQL(SQL_CREATE_MOVIE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		// Drop the table
		db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_MOVIE);

		// re-create database
		onCreate(db);
	}
}
