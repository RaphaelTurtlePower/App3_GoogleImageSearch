package com.codepath.googleimagesearch;

import com.codepath.googleimagesearch.model.Image;
import com.loopj.android.image.SmartImageView;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ImageDisplayActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_display);
		Image img = getIntent().getParcelableExtra("image");
		SmartImageView ivImage = (SmartImageView) findViewById(R.id.ivResult);
		ivImage.setImageUrl(img.getUrl());
		Drawable bkg = getResources().getDrawable(R.drawable.green_blue_background);
		bkg.setAlpha(200);
		LinearLayout r = (LinearLayout) findViewById(R.id.image_display_container);
		r.setBackgroundDrawable(bkg);
	}	
}
