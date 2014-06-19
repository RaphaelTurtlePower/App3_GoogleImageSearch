package com.codepath.googleimagesearch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.codepath.googleimagesearch.R;
import com.codepath.googleimagesearch.model.Image;
import com.codepath.googleimagesearch.model.Settings;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MainActivity extends SherlockActivity {
	public static int RESULT_SIZE=8;
	String activityQuery = "";
	GridView gvResults;
	ArrayList<Image> results = new ArrayList<Image>();
	ImageArrayAdapter imageAdapter;
	Settings appSettings;
	int currentPage;
	
	public void setupViews(){
		gvResults = (GridView)findViewById(R.id.gvResults);
		results = new ArrayList<Image>();
		imageAdapter = new ImageArrayAdapter(this, results);
		gvResults.setAdapter(imageAdapter);
		gvResults.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> adapter, View parent, int position, long v){
				Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
				Image img = results.get(position);
				i.putExtra("image", img);
				startActivityForResult(i, 6);
			}
		});
		currentPage = 0;	//initialize page
		Drawable bkg = getResources().getDrawable(R.drawable.light_blue_background);
		bkg.setAlpha(200);
		RelativeLayout r = (RelativeLayout) findViewById(R.id.main_container);
		r.setBackgroundDrawable(bkg);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		appSettings = new Settings();
		setupViews();
	
		gvResults.setOnScrollListener(new EndlessScrollListener() {
		    @Override
		    	public void onLoadMore(int page, int totalItemsCount) {
		    		if(activityQuery.length() > 0){
		    			currentPage += RESULT_SIZE;
	            		executeApiCall();
		    		}
		    }
	        });
		
	}
	
	public void executeApiCall(){
		AsyncHttpClient client = new AsyncHttpClient();
		String url = "https://ajax.googleapis.com/ajax/services/search/images?rsz="+ RESULT_SIZE+ "&v=1.0&start=" + currentPage + "&q=" + Uri.encode(activityQuery) + appSettings.generateQueryString(); 
		System.out.println(url);
		Log.d("DEBUG", url);		
		client.get(url,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject response){
						Log.d("DEBUG", response.toString());
						JSONArray imageJsonResults = null;
						try{
							imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
							if(currentPage == 0){
								imageAdapter.clear();
							}
							imageAdapter.addAll(Image.fromJSONArray(imageJsonResults));
						}catch(JSONException e){
							e.printStackTrace();
						}
					}
				}
				);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getSupportMenuInflater();
	    inflater.inflate(R.menu.main_activity_actions, menu);
		
	    SearchView searchView = new SearchView(getSupportActionBar().getThemedContext());
	    searchView.setOnQueryTextListener(new OnQueryTextListener(){

			@Override
			public boolean onQueryTextSubmit(String query) {
				currentPage = 0;
				activityQuery = query;
				executeApiCall();
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
	    	
	    });
	    
	    menu.add(0, 0, 1, "Search").setActionView(searchView)
    	.setIcon(R.drawable.ac_action_search).setShowAsAction(
    			MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
	    
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    if(item.getItemId() == R.id.gear) {
	          //handle setup activity here  openSettingsActivity();
	    	editSettingsActivity();
	    }
	    return true;
	}
	
	  public void editSettingsActivity(){
	    	Intent editIntent = new Intent();
	    	editIntent.setClass(this, EditSettingsActivity.class);
	    	editIntent.putExtra("settings", appSettings);
	    	startActivityForResult(editIntent, 5);
	  }
	  
	  @Override
	  protected void onActivityResult(int requestCode, int resultCode, Intent data ){
	    	if(requestCode == 5){
	    		appSettings = data.getParcelableExtra("settings");
	    	}else if(requestCode == 6){
	    		//awesome hope this works
	    	}
	    }
	
}
