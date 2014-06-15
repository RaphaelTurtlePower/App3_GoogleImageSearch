package com.codepath.googleimagesearch.model;

import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Image implements Parcelable{
	private String url;
	private String thumbUrl;
	private String title;
	
	public Image(JSONObject json){
		try{
			this.url = json.getString("url");
			this.thumbUrl = json.getString("tbUrl");
			this.title = json.getString("title");
		}catch(JSONException e){
			this.url = null;
			this.thumbUrl = null;
			this.title = null;
		}
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getThumbUrl() {
		return thumbUrl;
	}
	public void setThumbUrl(String thumbUrl) {
		this.thumbUrl = thumbUrl;
	}
	
	public String toString(){
		return this.title;
	}
	public static ArrayList<Image> fromJSONArray(
			JSONArray imageJsonResults) {
		ArrayList<Image> images = new ArrayList<Image>();
		for(int i=0; i<imageJsonResults.length(); i++){
			try{
				images.add(new Image(imageJsonResults.getJSONObject(i)));
			}catch(JSONException e){
				e.printStackTrace();
			}
		}
		return images;
	}
	
	public static final Parcelable.Creator<Image> CREATOR = 
			new Parcelable.Creator<Image>(){

				@Override
				public Image createFromParcel(Parcel source) {
					return new Image(source);
				}

				@Override
				public Image[] newArray(int size) {
					return new Image[size];
				}
		
		
	};
	
	public Image(Parcel source){
		readFromParcel(source);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.url);
		dest.writeString(this.thumbUrl);
		dest.writeString(this.title);
	}
	
	public void readFromParcel(Parcel source){
		this.url = source.readString();
		this.thumbUrl = source.readString();
		this.title = source.readString();
	}
	
}
