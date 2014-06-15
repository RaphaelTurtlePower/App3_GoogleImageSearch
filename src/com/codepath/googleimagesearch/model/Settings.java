package com.codepath.googleimagesearch.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Settings implements Parcelable {
	private String color;
	private String size;
	private String type;
	private String search;
	
	public Settings(){
		this.color = null;
		this.size = null;
		this.type = null;
		this.search = null;
	}
	
	public Settings(Parcel source){
		this.readFromParcel(source);
	}
	public void setParams(String color, String size, String type, String search){
		this.color = color;
		this.size = size;
		this.type = type;
		this.search = search;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.getColor());
		dest.writeString(this.getSize());
		dest.writeString(this.getType());
		dest.writeString(this.getSearch());
	}
	
	public void readFromParcel(Parcel source){
		this.color = source.readString();
		this.size = source.readString();
		this.type = source.readString();
		this.search = source.readString();
	}
	
	public String generateQueryString(){
		String query = "";
		if(this.color != null){
			query = "&imgcolor=" + this.color;
		}
		if(this.size != null){
			query += "&imgsz=" + this.size;
		}
		if(this.type != null){
			query += "&imgtype=" + this.type;
		}
		if(this.search != null){
			query += "&as_sitesearch=" + this.search;
		}
		return query;
	}
	
	public static final Parcelable.Creator<Settings> CREATOR = 
			new Parcelable.Creator<Settings>(){

				@Override
				public Settings createFromParcel(Parcel source) {
					return new Settings(source);
				}

				@Override
				public Settings[] newArray(int size) {
					return new Settings[size];
				}
	};
	
	
}
