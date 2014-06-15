package com.codepath.googleimagesearch;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.codepath.googleimagesearch.model.Image;
import com.loopj.android.image.SmartImageView;

public class ImageArrayAdapter extends ArrayAdapter<Image> {
	public ImageArrayAdapter(Context context, List<Image> images){
		super(context, R.layout.image_item, images);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		Image imageInfo = this.getItem(position);
		SmartImageView ivImage;
		ViewHolder holder;
		if(convertView == null){
			LayoutInflater inflator = LayoutInflater.from(getContext());
			holder = new ViewHolder();
			convertView = (SmartImageView) inflator.inflate(R.layout.image_item,  parent, false);
			holder.view = (SmartImageView) convertView;
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
			holder.view.setImageResource(android.R.color.transparent);
		}
		holder.view.setImageUrl(imageInfo.getThumbUrl());
		return convertView;
	}
	
	private static class ViewHolder{
		public SmartImageView view;
	}
}



