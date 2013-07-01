package com.addvalsolutions.mymap;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

public class FindRoute extends AsyncTask<Void, Void, Void>{
	String path;
	private String from;
	private String dest;
	private String temp = null;
	private JSONArray jsonArray;
	private JSONObject jsonObject;
	private Object mMap;
	public FindRoute(Context context,String org, String dest)
	{
		this.from = org;
		this.dest = dest;
		jsonArray = new JSONArray();
		jsonObject = new JSONObject();
	
	}
	
	public FindRoute(){}
	
		
	public List<LatLng>  getRouteData(String from, String to)
	{
		List<LatLng> point= null;
		try
		{
			path = "http://maps.googleapis.com/maps/api/directions/json?origin=" + from + "&destination=" + to + "&mode=driving&sensor=true";
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(new HttpGet(path));
			jsonObject = new JSONObject(EntityUtils.toString(response.getEntity()));
	       String encodedString = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONObject("overview_polyline").getString("points");

            PolylineDecode decodePoly = new PolylineDecode();
            
            point=decodePoly.decodePoly(encodedString);
            
            
/*
	       Log.d("json", jsonObject.toString());
	       int len = jsonArray.length();
	       MainActivity.steps = new Route[len];
	       for(int i=0; i < len; i++)
	       {
	    	   JSONObject object = jsonArray.getJSONObject(i);
	    	   Route ret = new Route();
	    	   start_lat = object.getJSONObject("start_location").getDouble("lat");
			   start_lng = object.getJSONObject("start_location").getDouble("lng");
			   end_lat = object.getJSONObject("end_location").getDouble("lat");
			   end_lng = object.getJSONObject("end_location").getDouble("lng");
			   strtpoint = new LatLng(start_lat,start_lng);
				endpoint = new LatLng(end_lat,end_lng);
				ret.setStrtpoint(strtpoint);
				ret.setEndpoint(endpoint);
	    	  
				ret.setDuration(object.getJSONObject("duration").getString("text"));
				ret.setDistance(object.getJSONObject("distance").getString("value"));

	    	   MainActivity.steps[i] = ret;
	    	  
	    	   
           }*/
		}
		catch(Exception e)
		{
			e.printStackTrace();
			MainActivity.searchResponse = "Network error";
		}
        return point;
	}

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		return null;
	}

	
}