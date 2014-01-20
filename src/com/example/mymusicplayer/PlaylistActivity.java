package com.example.mymusicplayer;

import java.util.ArrayList;
import java.util.HashMap;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;


public class PlaylistActivity extends ListActivity {
     //Songs List
	public ArrayList<HashMap<String,String>> songsList = new ArrayList<HashMap<String,String>>(); 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playlist);
	    ArrayList<HashMap<String,String>> songsListData = new ArrayList<HashMap<String,String>>();
	    SongsManager plm = new SongsManager();
	    //get all songs from sd card
	    this.songsList = plm.getPlayList();
	    
	    //looping through play list
	    for(int i = 0;i < songsList.size();i++)
	    {
	    	// creating new HashMap
	    	HashMap<String,String> song = songsList.get(i);
	    	//adding Hash List to Array List
	    	songsListData.add(song);
	    }
	    // adding Menu Items to list View
	    ListAdapter adapter = new SimpleAdapter (this,songsListData,R.layout.playlist_item,new String[]{"songTitle"},new int[]{R.id.songTitle});
	    setListAdapter(adapter);
	    //selecting Single ListView item
	    ListView lv = getListView();
	    //listening to single listitem click
	    lv.setOnItemClickListener(new OnItemClickListener() {
	    	 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                // getting listitem index
                int songIndex = position;
                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        MusicPlayerActivity.class);
                
                // Sending songIndex to PlayerActivity
               try{ in.putExtra("songIndex1", songIndex);
                setResult(100, in);
                
    			
                }
                catch (NullPointerException e){
                	
                }
                // Closing PlayListView
                
            }
        });
	    	
		
	}
	
		
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
	}


	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.playlist, menu);
		return true;
	}

}
