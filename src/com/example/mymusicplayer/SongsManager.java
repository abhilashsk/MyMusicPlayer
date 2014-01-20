package com.example.mymusicplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

public class SongsManager {
//SD card path
	final String MEDIA_PATH = Environment.getExternalStorageDirectory().getParent()+'/';
	private ArrayList<HashMap<String,String>> songsList = new ArrayList<HashMap<String,String>>();
	// Constructor
	public SongsManager(){
		
	}
	 /**
     * Function to read all mp3 files from sdcard
     * and store the details in ArrayList
     * */
	public ArrayList<HashMap<String,String>> getPlayList(){
		System.out.println(MEDIA_PATH);
		if(MEDIA_PATH != null){
		File home = new File(MEDIA_PATH);
		File[] listFiles = home.listFiles();
		if (listFiles != null && listFiles.length > 0){
			for(File file : listFiles){
				System.out.println(file.getAbsolutePath());
				if(file.isDirectory()){
					scanDirectory(file);
				}else{
					addSongToList(file);
				}
					
			}
		}
			
		}
		
		/*if (home.listFiles(new FileExtensionFilter()).length>0){
			for (File file : home.listFiles(new FileExtensionFilter())){
				HashMap<String,String> song = new HashMap<String,String>();
				song.put("songTitle",file.getName().substring(0,(file.getName().length()-4)));
				song.put("songPath", file.getPath());
				//Adding each song to the list
				songsList.add(song);
			}
		}*/
		//return songs list array
		return songsList;
	}
	private void scanDirectory(File directory){
		if(directory != null){
			File[] listFiles = directory.listFiles();
			if(listFiles != null && listFiles.length > 0){
				for(File file : listFiles){
					if (file.isDirectory()){
						scanDirectory(file);
					}else {
						addSongToList(file);
					}
				}
			}
		}
	}
	@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
	private void addSongToList(File file){
		if (file.getName().endsWith("mp3")){
			HashMap<String,String> song = new HashMap<String,String>();
			MediaMetadataRetriever retriever = new MediaMetadataRetriever(); 
			try {
			retriever.setDataSource(file.getPath());
			
			}
			catch (RuntimeException e){
				e.printStackTrace();
				e.getMessage();
				Log.e("abc","fuck");
			}
			finally{
			String songTitle = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
			
			
			song.put("songTitle",songTitle);
			song.put("songPath", file.getPath());
			//Adding each song to the list
			songsList.add(song);
			}
		}
	}
}
	/**
     * Class to filter files which are having .mp3 extension
     * */
	

