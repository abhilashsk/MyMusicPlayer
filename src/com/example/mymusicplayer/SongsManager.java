package com.example.mymusicplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
public class SongsManager extends Activity {
//SD card path
	DatabaseHandler db = new DatabaseHandler(this);
	
	MediaMetadataRetriever retriever = new MediaMetadataRetriever(); 
	final String MEDIA_PATH = Environment.getExternalStorageDirectory().getParent()+'/';
	//private ArrayList<HashMap<String,String>> songsList = new ArrayList<HashMap<String,String>>();
	// Constructor
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.songsmanager);
		/**
	     * Function to read all mp3 files from sdcard
	     * and store the details in ArrayList
	     * */
		if(MEDIA_PATH != null){
			File home = new File(MEDIA_PATH);
			File[] listFiles = home.listFiles();
			if (listFiles != null && listFiles.length > 0){
				for(File file : listFiles){
					System.out.println(file.getAbsolutePath());
					if(file.isDirectory()){
						scanDirectory(file);
					}else{
						//addSongToList(file);
						try{
							retriever.setDataSource(file.getAbsolutePath());
							}
							catch(NullPointerException e){
								Log.e("abdef","fuck");}
							catch(RuntimeException e){
								Log.e("a", "fuck");
							}
							finally{
								String songTitle =retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
								if (songTitle=="")
								{
									songTitle=file.getName().substring(0, file.getName().toString().length()-4);
									System.out.println(songTitle);
								}
								db.addSong(new SongList(songTitle,file.getAbsolutePath()));}
					}
						
				}
			}
				
			}
		 List<SongList> songs = db.getAllSongs();    
	     for (SongList cn : songs) {
	         String log = "id:"+cn.getID()+" Path: "+cn.getPath()+" ,Title: " + cn.getTitle();
	             // Writing Contacts to log
	     Log.d("Name: ", log);
	     }
	     Intent in1= new Intent(this,PlaylistActivity.class);
	     in1.putExtra("updated","updated");
         finish();
	}
	private void scanDirectory(File directory){
		if(directory != null){
			File[] listFiles = directory.listFiles();
			if(listFiles != null && listFiles.length > 0){
				for(File file : listFiles){
					if (file.isDirectory()){
						scanDirectory(file);
					}else {
						try{
							retriever.setDataSource(file.getAbsolutePath());
							}
							catch(NullPointerException e){
								Log.e("abdef","fuck");}
							catch(RuntimeException e){
								Log.e("a", "fuck");
							}
							finally{
							String songTitle =" "+retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
							if (songTitle==" ")
							{
								songTitle=file.getName().substring(0, file.getName().toString().length()-4);
							    System.out.println(songTitle);
							}
							db.addSong(new SongList(songTitle,file.getAbsolutePath()));}
					}
				}
			}
		}
	}
}
	
