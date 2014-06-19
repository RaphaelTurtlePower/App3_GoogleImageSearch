package com.codepath.googleimagesearch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.codepath.googleimagesearch.model.Image;
import com.loopj.android.image.SmartImageTask;
import com.loopj.android.image.SmartImageTask.OnCompleteHandler;
import com.loopj.android.image.SmartImageTask.OnCompleteListener;
import com.loopj.android.image.SmartImageView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ShareActionProvider;
import android.widget.Spinner;

public class ImageDisplayActivity extends Activity {

	ShareActionProvider shareActionProvider;
	Image img;
	SmartImageView ivImage;
	Intent shareIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_display);
		img = getIntent().getParcelableExtra("image");
		ivImage = (SmartImageView) findViewById(R.id.ivResult);
		ivImage.setImageUrl(img.getUrl());
		ivImage.setImageUrl(img.getUrl(), new OnCompleteListener(){
			@Override
			public void onComplete() {
				setupShareIntent();
			}	
		});
		Drawable bkg = getResources().getDrawable(R.drawable.green_blue_background);
		bkg.setAlpha(200);
		LinearLayout r = (LinearLayout) findViewById(R.id.image_display_container);
		r.setBackgroundDrawable(bkg);
	}	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.display_activity_actions, menu);
	    // Locate MenuItem with ShareActionProvider
	    MenuItem item = menu.findItem(R.id.menu_item_share);
	    // Fetch and store ShareActionProvider
	    shareActionProvider = (ShareActionProvider) item.getActionProvider();
	    shareActionProvider.setShareIntent(shareIntent);
	    // Return true to display menu
	    return super.onCreateOptionsMenu(menu);
	}
	
	public void setupShareIntent(){
	    Uri bmpUri = getImageUri();
	    shareIntent = new Intent();
	    if(bmpUri != null){
	        shareIntent.setAction(Intent.ACTION_SEND);
	        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
	        shareIntent.setType("image/*");
	    }
	}
	
	public Uri getImageUri(){
		SmartImageView siv = (SmartImageView) findViewById(R.id.ivResult);
		Drawable mDrawable = siv.getDrawable();
		if(mDrawable != null){
			Bitmap mBitmap = ((BitmapDrawable)mDrawable).getBitmap();
			if(mBitmap != null){
			String path = Images.Media.insertImage(getContentResolver(), 
			    mBitmap, "Image Description", null);
			Uri uri = Uri.parse(path);
			return uri;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
		if(item.getItemId() == R.id.menu_item_share){
			shareItem();
		}else if(item.getItemId() == R.id.back){
			Intent i = new Intent();
	    	setResult(RESULT_OK, i); 
	    	finish();
		}
	    return true;
	}
	
	
	// Can be triggered by a view event such as a button press
	public void shareItem() {
	   startActivity(Intent.createChooser(shareIntent, "Share Image"));       
	}

}
