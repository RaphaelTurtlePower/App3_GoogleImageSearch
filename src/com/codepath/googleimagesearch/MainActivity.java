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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.googleimagesearch.R;
import com.codepath.googleimagesearch.model.Image;
import com.codepath.googleimagesearch.model.Settings;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MainActivity extends Activity {
	public static int RESULT_SIZE=8;
	EditText etQuery;
	TextView btnSearch;
	GridView gvResults;
	ArrayList<Image> results = new ArrayList<Image>();
	ImageArrayAdapter imageAdapter;
	Settings appSettings;
	int currentPage;
	
	public void setupViews(){
		etQuery = (EditText)findViewById(R.id.etQuery);
		btnSearch = (TextView)findViewById(R.id.btnSearch);
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
				startActivity(i);
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
		btnSearch.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				imageSearch(v);
			}
		});
		
		gvResults.setOnScrollListener(new EndlessScrollListener() {
		    @Override
		    	public void onLoadMore(int page, int totalItemsCount) {
		    		currentPage += RESULT_SIZE;
	            	String query = etQuery.getText().toString();
	            	executeApiCall(query);
		    }
	        });
		
	}
	
	public void imageSearch(View v){
		currentPage = 0;
		String query = etQuery.getText().toString();
		Toast.makeText(this, "Looking for:" + query, Toast.LENGTH_LONG).show();
		executeApiCall(query);
	}
	
	public void executeApiCall(String query){
		AsyncHttpClient client = new AsyncHttpClient();
		String url = "https://ajax.googleapis.com/ajax/services/search/images?rsz="+ RESULT_SIZE+ "&v=1.0&start=" + currentPage + "&q=" + Uri.encode(query) + appSettings.generateQueryString(); 
		System.out.println(url);
		Log.d("DEBUG", url);		
		client.get(url,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject response){
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
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_actions, menu);
	  
	  /*  MenuItem searchItem = menu.findItem(R.id.asearch);
	    SearchView searchView = (SearchView) searchItem.getActionView();
	    searchView.setOnQueryTextListener(new OnQueryTextListener(){

			@Override
			public boolean onQueryTextSubmit(String query) {
				executeApiCall(query);
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
	    	
	    });
	    */
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
	    	}
	    }
	
}
