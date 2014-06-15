package com.codepath.googleimagesearch;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.codepath.googleimagesearch.model.Settings;

public class EditSettingsActivity extends Activity {
	Settings settings;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_settings);
		settings = getIntent().getParcelableExtra("settings");
		Drawable bkg = getResources().getDrawable(R.drawable.android_background_one);
		bkg.setAlpha(200);
		RelativeLayout r = (RelativeLayout) findViewById(R.id.edit_settings_container);
		r.setBackgroundDrawable(bkg);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.edit_activity_actions, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    if(item.getItemId() == R.id.check) {
	    	Spinner color = (Spinner) findViewById(R.id.color_spinner);
	    	Spinner type = (Spinner) findViewById(R.id.image_type_spinner);
	    	Spinner size = (Spinner) findViewById(R.id.image_size_spinner);
	    	EditText search = (EditText) findViewById(R.id.site_filter);
	    	settings.setColor(color.getSelectedItem().toString());
	    	settings.setType(type.getSelectedItem().toString());
	    	settings.setSize(size.getSelectedItem().toString());
	    	settings.setSearch(search.getText().toString());
	    }else if(item.getItemId() == R.id.cancel){
	
	    }   	
    	Intent i = new Intent();
    	i.putExtra("settings", settings);
    	setResult(RESULT_OK, i); 	
	    finish();
	    return true;
	}
	
	@Override
	public void onBackPressed() {
	    Intent intent = new Intent();
	    intent.putExtra("settings", settings);
	    setResult(RESULT_OK, intent);
	    finish();
	}
	
}
