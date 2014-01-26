package com.example.mymusicplayer;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MusicPlayerActivity extends Activity implements OnCompletionListener,SeekBar.OnSeekBarChangeListener {
	
 private ImageButton bPlay;
 private ImageButton bForward;
 private ImageButton bBackward;
 private ImageButton bNext;
 private ImageButton bPrevious;
 private ImageButton bPlaylist;
 private ImageButton bRepeat;
 private ImageButton bShuffle;
 private SeekBar songProgressBar;
 private TextView songTitleLabel;
 private TextView songCurrentDuration;
 private TextView songTotalDuration;
 private ImageView albumArt;
 // Media Player
 private  MediaPlayer mp;
 // Handler to update UI timer, progress bar etc,.
 private Handler mHandler = new Handler();;
 private Utilities utils;
 private int seekForwardTime = 5000; // 5000 milliseconds
 private int seekBackwardTime = 5000; // 5000 milliseconds
 private int currentSongIndex = 0;
 private boolean isShuffle = false;
 private boolean isRepeat = false;
 private  List<SongList> songsList;
 private DatabaseHandler db ;

 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music_player);
		 bPlay = (ImageButton) findViewById(R.id.bPlay);
	        bForward = (ImageButton) findViewById(R.id.bForward);
	        bBackward = (ImageButton) findViewById(R.id.bBackward);
	        bNext = (ImageButton) findViewById(R.id.bNext);
	        bPrevious = (ImageButton) findViewById(R.id.bPrevious);
	        bPlaylist = (ImageButton) findViewById(R.id.bPlaylist);
	        bRepeat = (ImageButton) findViewById(R.id.bRepeat);
	        bShuffle = (ImageButton) findViewById(R.id.bShuffle);
	        songProgressBar = (SeekBar) findViewById(R.id.SongProgress);
	        songTitleLabel = (TextView) findViewById(R.id.SongTitle);
	        songCurrentDuration = (TextView) findViewById(R.id.currentDuration);
	        songTotalDuration = (TextView) findViewById(R.id.totalDuration);
	        albumArt = (ImageView) findViewById(R.id.albumArt);
	        //media player
	        mp = new MediaPlayer();
	       //songManager = new SongsManager();
	        utils = new Utilities();
	        db = new DatabaseHandler(this); 
	        //Listeners
	        songProgressBar.setOnSeekBarChangeListener(this);//Important
	        mp.setOnCompletionListener(this);//Important
	        //getting all songs list
	       
	        //setting initial values for song title and album art
	        songTitleLabel.setText("Song Title");
	        albumArt.setImageResource(R.drawable.adele);
	        albumArt.setScaleType(ScaleType.FIT_XY);
	        /**
	         * Button Click event for Play list click event
	         * Launches list activity which displays list of songs
	         * */
	        bPlaylist.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 Intent i = new Intent(getApplicationContext(), PlaylistActivity.class);
					//Intent i = new Intent(getApplicationContext(), SongsManager.class);
					 Log.d("abc", ""+DatabaseHandler.DATABASE_VERSION); 
					 //db.DATABASE_VERSION=3;
					 startActivityForResult(i, 100);
				}
			});
	        bPlay.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// check for already playing
					if(mp.isPlaying()){
						if(mp!=null){
							mp.pause();
							// Changing button image to play button
							bPlay.setImageResource(R.drawable.img_bplay);
						}
					}else{
						// Resume song
						if(mp!=null){
							mp.start();
							// Changing button image to pause button
							bPlay.setImageResource(R.drawable.img_bpause);
						}
					}
					
				}
			});
	        /**
	         * Button Click event for Repeat button
	         * Enables repeat flag to true
	         * */
	    	 bRepeat.setOnClickListener(new View.OnClickListener(){
	    		 public void onClick(View arg0)
	    		 {
	    			 if(isRepeat){
	    				 isRepeat = false;
	    				 Toast.makeText(getApplicationContext(),"Repeat is OFF",Toast.LENGTH_SHORT).show();
	    				 bRepeat.setImageResource(R.drawable.img_btn_repeat);
	    			 }
	    			 else{
	    				 //make repeat to true
	    				 isRepeat = true;
	    				 Toast.makeText(getApplicationContext(),"Repeat is ON",Toast.LENGTH_SHORT).show();
	    	            //make shuffle to false
	    	            isShuffle=false;
	    	            bRepeat.setImageResource(R.drawable.img_btn_repeat_pressed);
	    	            bShuffle.setImageResource(R.drawable.img_btn_shuffle);
	    			 }	 
	    			 }
	    		 
	    	 });
	    	
	    	

	    bShuffle.setOnClickListener(new View.OnClickListener(){
	    	public void onClick(View arg0){
	    		if(isShuffle){
	    			isShuffle=false;
	    			Toast.makeText(getApplicationContext(), "Shuffle is OFF",Toast.LENGTH_SHORT).show();
	    		    bShuffle.setImageResource(R.drawable.img_btn_shuffle);
	    		}else
	    		{
	    			isShuffle=true;
	    			isRepeat =false;
	    		Toast.makeText(getApplicationContext(),"Shuffle is ON",Toast.LENGTH_SHORT).show();
	    			bShuffle.setImageResource(R.drawable.img_btn_shuffle_pressed);
	    		    bRepeat.setImageResource(R.drawable.img_btn_repeat);
	    		}
	    	}
	    		
	    	});
	    /**
	             * Forward button click event
	             * Forwards song specified seconds
	             * */
	    bForward.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//get current song position
				int currentPosition = mp.getCurrentPosition();
				//check if seekForward time is less than song duration
				if(currentPosition+seekForwardTime <= mp.getDuration()){
					//forward Sing
					mp.seekTo(currentPosition+seekForwardTime);
				}else{
					// forward to end
					mp.seekTo(mp.getDuration());
				}
			}
		});
	    /**
         * Backward button click event
         * Backward song to specified seconds
         * */
        bBackward.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View arg0) {
                // get current song position
                int currentPosition = mp.getCurrentPosition();
                // check if seekBackward time is greater than 0 sec
                if(currentPosition - seekBackwardTime >= 0){
                    // forward song
                    mp.seekTo(currentPosition - seekBackwardTime);
                }else{
                    // backward to starting position
                    mp.seekTo(0);
                }
 
            }
        });
        /**
         * Next button click event
         * Plays next song by taking currentSongIndex + 1
         * */
        bNext.setOnClickListener(new View.OnClickListener() {
        	
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			//get current song position
				int currentPosition = mp.getCurrentPosition();
				//check if next song is present
				if(currentSongIndex<(db.getSongsCount())-1){
					playSong(currentSongIndex + 1);
					currentSongIndex = currentSongIndex + 1;
				}else {
					playSong(0);
					currentSongIndex=(0);
				}
			}
		});
        /**
         * Back button click event
         * Plays previous song by currentSongIndex - 1
         * */
        bPrevious.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View arg0) {
                if(currentSongIndex > 0){
                    playSong(currentSongIndex - 1);
                    currentSongIndex = currentSongIndex - 1;
                }else{
                    // play last song
                    playSong(db.getSongsCount() - 1);
                    currentSongIndex = db.getSongsCount() - 1;
                }
 
            }
        });    
	        
	}
	/**
     * Receiving song index from playlist view
     * and play the song
     * */
	
	@Override
	protected void onActivityResult(int requestCode,int resultCode,Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==100)
		{
			currentSongIndex = data.getExtras().getInt("songIndex1");
			//play selected song
			playSong(currentSongIndex);
		}
	}
	/**
     * Function to play a song
     * @param songIndex - index of song
     * */
	@SuppressWarnings("unused")
	@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
	public void playSong(int songIndex){
		// play song
		try {
			mp.reset();
			//mp.setDataSource(songsList.get(songIndex).get("songPath"));
			System.out.println(db.getsong(songIndex).getPath());
			mp.setDataSource(db.getsong(songIndex).getPath());
			mp.prepare();
			mp.start();
			
			//Displaying Song Title
			String songTitle = (db.getsong(songIndex).getTitle());
			MediaMetadataRetriever retriever = new MediaMetadataRetriever(); 
			retriever.setDataSource(db.getsong(songIndex).getPath());
			byte [] data = retriever.getEmbeddedPicture();
			Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			try {
			songTitleLabel.setText(" " + songTitle);
			if(data!=null){
			albumArt.setImageBitmap(bitmap);
			albumArt.setScaleType(ScaleType.FIT_XY);
			}
			else{
			albumArt.setImageResource(R.drawable.adele);
			albumArt.setScaleType(ScaleType.FIT_XY);
			}
			
			}
			catch (NullPointerException e) {
				e.getMessage();
				Log.e("abcd","wtf");
				e.printStackTrace();
			}
			// Changing Button image to pause
			bPlay.setImageResource(R.drawable.img_bpause);
			
			//setProgress Bar Values
			songProgressBar.setProgress(0);
			songProgressBar.setMax(100);
			// Updating progress bar
            updateProgressBar();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
		} catch(NullPointerException e){
			e.getMessage();
        	Log.e("banana","This is where the shit is");
			e.printStackTrace();
		} catch (RuntimeException e){
			e.getMessage();
			Log.e("banana1","fuck");
			e.printStackTrace();
		}
	}
	/**
     * Update timer on seekbar
     * */
	public void updateProgressBar(){
		mHandler.postDelayed(mUpdateTimeTask, 100);
	}
	/**
     * Background Runnable thread
     * */
	private Runnable mUpdateTimeTask= new Runnable(){
		public void run(){
			long totalDuration = mp.getDuration();
			long currentDuration = mp.getCurrentPosition();
			// Displaying total time
			songTotalDuration.setText(""+utils.millisecondsToTimer(totalDuration));
			//displaying cuurent time
			songCurrentDuration.setText(""+utils.millisecondsToTimer(currentDuration));
			
			//Updating Progress Bar
			int progress = (int)utils.getProgressPercentage(currentDuration, totalDuration);
			Log.d("Progress", ""+progress);
            songProgressBar.setProgress(progress);
         // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
		}
		
	};
	public void onProgressChanged(SeekBar seekBar,int progress,boolean formTouch){
		
	}
	 /**
     * When user starts moving the progress handler
     * */
	public void onStartTrackingTouch(SeekBar seekBar){
		// remove message Handler from updating Progress Handler
		mHandler.removeCallbacks(mUpdateTimeTask);
	}
	/**
     * When user stops moving the progress hanlder
     * */
	public void onStopTrackingTouch(SeekBar seekBar){
		mHandler.removeCallbacks(mUpdateTimeTask);
		int totalDuration = mp.getDuration();
		int currentPosition = (seekBar.getProgress())*totalDuration/100;
		//forward or backward to certain seconds
		System.out.println(seekBar.getProgress());
		System.out.println(currentPosition);
		mp.seekTo(currentPosition);
		//update timer
		updateProgressBar();
		}
	/**
     * On Song Playing completed
     * if repeat is ON play same song again
     * if shuffle is ON play random song
     * */
	public void onCompletion(MediaPlayer arg0)
	{
		// check for repeat on or off
		if(isRepeat){
			//repeat is on play same song again
			playSong(currentSongIndex);
		}
		else if (isShuffle){
			//shuffle is on--play random song
			Random rand = new Random();
			currentSongIndex = rand.nextInt((db.getSongsCount()-1)- 0 + 1)+0;
			playSong(currentSongIndex);
		}else {
			//neither repeat nor shuffle is on
			if(currentSongIndex < (db.getSongsCount()-1)){
				playSong(currentSongIndex+1);
				currentSongIndex = currentSongIndex + 1;
			}else
			{
				playSong(0);
				currentSongIndex=0;
			}
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.music_player, menu);
		return true;
	}

}
