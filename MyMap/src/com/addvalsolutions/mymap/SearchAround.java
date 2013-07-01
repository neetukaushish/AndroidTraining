package com.addvalsolutions.mymap;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class SearchAround {
	String path;
	private JSONArray jsonArray;
	private JSONObject jsonObject;
	double lat;
	double log;
		
	public SearchAround()
	{
		
		jsonArray = new JSONArray();
		jsonObject = new JSONObject();
	
	}
	public  Search[] getPlace(String current, int radius, String name)
	{
		Search[] searchs = null;
		try
		{
			String apiKey = "AIzaSyA0Hv8yMsOxTRCX4d5hg5Wud1T7IxKn2Hk";		
			
			
			
			if(name.equalsIgnoreCase("search everything"))
				path = "https://maps.googleapis.com/maps/api/place/search/json?location=" +current+ "&radius=" + radius + "&sensor=true&key=" + apiKey;
			else
				path = "https://maps.googleapis.com/maps/api/place/search/json?location="+ current +"&radius=" + radius +"&name="+name+"&sensor=true&key=" + apiKey;
			DefaultHttpClient client = new DefaultHttpClient();
			HttpPost url;
			try
			{
				url = new HttpPost(path);
			}
			catch(Exception e)
			{
				MainActivity.searchResponse = "Please enter a valid string";
				
			}

			HttpResponse response = client.execute(new HttpGet(path));
			jsonObject = new JSONObject(EntityUtils.toString(response.getEntity()));
			Log.d("Status code", jsonObject.getString("status"));
			if(jsonObject.getString("status").equalsIgnoreCase("OK"))
			{
				jsonArray = jsonObject.getJSONArray("results");
				searchs = new Search[jsonArray.length()];
				searchs = parseJSON(jsonObject, jsonArray);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			MainActivity.searchResponse = "Network Error";

		}
		return searchs;
	
    }


	

	private Search[] parseJSON(JSONObject jsonObject, JSONArray jsonArray) {
		String status = null;
		Search[] results = new Search[jsonArray.length()];
		try
		{
			status = jsonObject.getString("status");
			JSONObject object;
			double lat,lng;
			if(status.equalsIgnoreCase("ok"))
			{
				int len = jsonArray.length();
				MainActivity.response = new Search[len];
				for(int i =0;i<len;i++)
				{
					object = jsonArray.getJSONObject(i);
					Search result = new Search();
					result.setIcon(object.optString("icon","N/A"));
					result.setId(object.optString("id","N/A"));
					result.setReference(object.optString("reference","N/A"));
					result.setName(object.optString("name","N/A"));
					result.setRating(object.optDouble("rating",0));
					result.setType(object.optString("types", "N/A"));
					result.setVicinity(object.optString("vicinity", "N/A"));
					lat = object.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
					lng = object.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
					result.setLatitude(lat);
					result.setLongitude(lng);
					results[i] = result;
				}
				MainActivity.searchResponse = "ok";
		
	         }
			else
				MainActivity.searchResponse = status;
		

          }
		catch(Exception e)
  		{
  			e.printStackTrace();
  			MainActivity.searchResponse = "Unknown Error";
  		}
		return results;
	 }
}	
	