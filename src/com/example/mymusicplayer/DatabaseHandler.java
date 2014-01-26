package com.example.mymusicplayer;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	public static int DATABASE_VERSION = 2;

	// Database Name
	private static final String DATABASE_NAME = "songsManager";

	// Contacts table name
	private static final String TABLE_SONGS = "songs";

	// Contacts Table Columns names
	public static final String KEY_ID = "_id";
	public static final String KEY_TITLE = "songTitle";
	public static final String KEY_PATH = "path";

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_SONGS_TABLE = "CREATE TABLE " + TABLE_SONGS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
				+ KEY_PATH + " TEXT" + ")";
		db.execSQL(CREATE_SONGS_TABLE);
		System.out.println("created");
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONGS);
		System.out.println("dropped");
		// Create tables again
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new contact
	void addSong(SongList songList) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, songList.getTitle()); // Contact Name
		values.put(KEY_PATH, songList.getPath()); // Contact Phone

		// Inserting Row
		db.insert(TABLE_SONGS, null, values);
		db.close(); // Closing database connection
		}
		
		
	

	// Getting single contact
	SongList getsong(int id) { 
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_SONGS, new String[] { KEY_ID ,
				KEY_TITLE, KEY_PATH }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		SongList songList = new SongList(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2));
		// return contact
		return songList;
	}
	
	// Getting All Contacts
	public List<SongList> getAllSongs() {
		List<SongList> songList = new ArrayList<SongList>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_SONGS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				SongList song = new SongList();
				song.setID(Integer.parseInt(cursor.getString(0)));
				song.setTitle(cursor.getString(1));
				song.setPath(cursor.getString(2));
				// Adding contact to list
				songList.add(song);
			} while (cursor.moveToNext());
		}

		// return contact list
		return songList;
	}
	public Cursor getValues(){
		           SQLiteDatabase db = this.getReadableDatabase();
		            Cursor mCursor = db.query(true, // isdistinct
		                   TABLE_SONGS, // table name
		                   new String[] { KEY_ID ,KEY_TITLE },// select clause
		                   null, // where cluase
		                   null, // where clause parameters
		                   null, // group by
		                   null, // having
		                   null, // orderby
		                   null);// limit
		    
		          return mCursor;
		       }

	// Updating single contact
	public int updateSong(SongList song) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, song.getTitle());
		values.put(KEY_PATH, song.getPath());

		// updating row
		return db.update(TABLE_SONGS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(song.getID()) });
	}

	// Deleting single contact
	public void deleteSong(SongList song) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_SONGS, KEY_ID + " = ?",
				new String[] { String.valueOf(song.getID()) });
		db.close();
	}


	// Getting contacts Count
	public int getSongsCount() {
		
		String countQuery = "SELECT  * FROM " + TABLE_SONGS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		

		// return count
		return cursor.getCount();
	}

}
