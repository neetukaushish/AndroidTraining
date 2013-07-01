package com.addvalsolutions.map;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Dialog;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
 
public class MainActivity extends FragmentActivity implements LocationListener {
 
    GoogleMap googleMap;
    MarkerOptions markerOptions;
    LatLng latLng;
    public static String searchResponse;
	public static List<LatLng> point;
	TextView  lblfrom;
	TextView  lblto;
	private EditText to, from;
	int requestCode = 2;
	 Location location;
	 AutoCompleteTextView atvPlaces;
	 DownloadTask placesDownloadTask;
	    DownloadTask placeDetailsDownloadTask;
	    ParserTask placesParserTask;
	    ParserTask placeDetailsParserTask;
	    final int PLACES=0;
	    final int PLACES_DETAILS=1;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Getting a reference to the AutoCompleteTextView
        atvPlaces = (AutoCompleteTextView) findViewById(R.id.atv_places);
        atvPlaces.setThreshold(1);
     // Adding textchange listener
        atvPlaces.addTextChangedListener(new TextWatcher() {
 
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Creating a DownloadTask to download Google Places matching "s"
                placesDownloadTask = new DownloadTask(PLACES);
 
                // Getting url to the Google Places Autocomplete api
                String url = getAutoCompleteUrl(s.toString());
 
               // Start downloading Google Places
                // This causes to execute doInBackground() of DownloadTask class
                placesDownloadTask.execute(url);
            }
 
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
            int after) {
                // TODO Auto-generated method stub
            }
 
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
     // Setting an item click listener for the AutoCompleteTextView dropdown list
        atvPlaces.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int index,
            long id) {
 
                ListView lv = (ListView) arg0;
                SimpleAdapter adapter = (SimpleAdapter) arg0.getAdapter();
 
                HashMap<String, String> hm = (HashMap<String, String>) adapter.getItem(index);
 
                // Creating a DownloadTask to download Places details of the selected place
                placeDetailsDownloadTask = new DownloadTask(PLACES_DETAILS);
 
                // Getting url to the Google Places details api
                String url = getPlaceDetailsUrl(hm.get("reference"));
 
                // Start downloading Google Place Details
                // This causes to execute doInBackground() of DownloadTask class
                placeDetailsDownloadTask.execute(url);
 
            }
        });
 int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        
        
        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available
 
            
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
        }else{
        SupportMapFragment supportMapFragment = (SupportMapFragment)
        getSupportFragmentManager().findFragmentById(R.id.map);
 
        // Getting a reference to the map
        googleMap = supportMapFragment.getMap();
        googleMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        location = locationManager.getLastKnownLocation(provider);
        if(location!=null){
        	drawMarker(location);
        	
        }
        
            onLocationChanged(location);
            locationManager.requestLocationUpdates(provider, 20000, 0, this);
            
        }   
        // Getting reference to btn_find of the layout activity_main
      //  Button btn_find = (Button) findViewById(R.id.btn_find);
 
        // Defining button click event listener for the find button
        OnClickListener findClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
               // Getting reference to EditText to get the user input location
           //     EditText etLocation = (EditText) findViewById(R.id.et_location);
 
                // Getting user input location
           //    String location = etLocation.getText().toString();
  
                if(location!=null && !location.equals("")){
                 //  new GeocoderTask().execute(location);
                }
            }
        };
 
        // Setting button click event listener for the find button
  //      btn_find.setOnClickListener(findClickListener);
    }
    private String getAutoCompleteUrl(String place){
    	 
        // Obtain browser key from https://code.google.com/apis/console
        String key = "key=AIzaSyAf28q6kNqs0jPjPnVfMoMCTJJB7qFBQ0";
 
        // place to be be searched
        String input = "input="+place;
 
        // place type to be searched
        String types = "types=geocode";
 
        // Sensor enabled
        String sensor = "sensor=false";
 
        // Building the parameters to the web service
        String parameters = input+"&"+types+"&"+sensor+"&"+key;
 
        // Output format
        String output = "json";
 
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"+output+"?"+parameters;
 
        return url;
    }
    private String getPlaceDetailsUrl(String ref){
    	 
        // Obtain browser key from https://code.google.com/apis/console
        String key = "key=AIzaSyAf28q6kNqs0jPjPnVfMoMCTJJB7qFBQ0";
 
        // reference of place
        String reference = "reference="+ref;
 
        // Sensor enabled
        String sensor = "sensor=false";
 
        // Building the parameters to the web service
        String parameters = reference+"&"+sensor+"&"+key;
 
        // Output format
        String output = "json";
 
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/place/details/"+output+"?"+parameters;
 
        return url;
    }
    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
 
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
 
            // Connecting to url
            urlConnection.connect();
 
            // Reading data from url
            iStream = urlConnection.getInputStream();
 
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
 
            StringBuffer sb  = new StringBuffer();
 
            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }
 
            data = sb.toString();
 
            br.close();
 
        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
 
    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String>{
 
        private int downloadType=0;
 
        // Constructor
        public DownloadTask(int type){
            this.downloadType = type;
        }
 
        @Override
        protected String doInBackground(String... url) {
 
            // For storing data from web service
            String data = "";
 
            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }
 
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
 
            switch(downloadType){
            case PLACES:
                // Creating ParserTask for parsing Google Places
                placesParserTask = new ParserTask(PLACES);
 
                // Start parsing google places json data
                // This causes to execute doInBackground() of ParserTask class
                placesParserTask.execute(result);
 
                break;
 
            case PLACES_DETAILS :
                // Creating ParserTask for parsing Google Places
                placeDetailsParserTask = new ParserTask(PLACES_DETAILS);
 
                // Starting Parsing the JSON string
                // This causes to execute doInBackground() of ParserTask class
                placeDetailsParserTask.execute(result);
            }
        }
    }
 
    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String,String>>>{
 
        int parserType = 0;
 
        public ParserTask(int type){
            this.parserType = type;
        }
 
        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {
 
            JSONObject jObject;
            List<HashMap<String, String>> list = null;
 
            try{
                jObject = new JSONObject(jsonData[0]);
 
                switch(parserType){
                case PLACES :
                    PlaceJSONParser placeJsonParser = new PlaceJSONParser();
                    // Getting the parsed data as a List construct
                    list = placeJsonParser.parse(jObject);
                    break;
                case PLACES_DETAILS :
                    PlaceDetailsJSONParser placeDetailsJsonParser = new PlaceDetailsJSONParser();
                    // Getting the parsed data as a List construct
                    list = placeDetailsJsonParser.parse(jObject);
                }
 
            }catch(Exception e){
                Log.d("Exception",e.toString());
            }
            return list;
        }
 
        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {
 
            switch(parserType){
            case PLACES :
                String[] from = new String[] { "description"};
                int[] to = new int[] { android.R.id.text1 };
 
                // Creating a SimpleAdapter for the AutoCompleteTextView
                SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);
 
                // Setting the adapter
                atvPlaces.setAdapter(adapter);
                break;
            case PLACES_DETAILS :
                HashMap<String, String> hm = result.get(0);
 
                // Getting latitude from the parsed data
                double latitude = Double.parseDouble(hm.get("lat"));
 
                // Getting longitude from the parsed data
                double longitude = Double.parseDouble(hm.get("lng"));
 
                // Getting reference to the SupportMapFragment of the activity_main.xml
                SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
 
                // Getting GoogleMap from SupportMapFragment
                googleMap = fm.getMap();
 
                LatLng point = new LatLng(latitude, longitude);
 
                CameraUpdate cameraPosition = CameraUpdateFactory.newLatLng(point);
                CameraUpdate cameraZoom = CameraUpdateFactory.zoomBy(5);
 
                // Showing the user input location in the Google Map
                googleMap.moveCamera(cameraPosition);
                googleMap.animateCamera(cameraZoom);
 
                MarkerOptions options = new MarkerOptions();
                options.position(point);
                options.title("Position");
                options.snippet("Latitude:"+latitude+",Longitude:"+longitude);
 
                // Adding the marker in the Google Map
                googleMap.addMarker(options);
 
                break;
            }
        }
    }
 
    private void drawMarker(Location location){
    	googleMap.clear();
    	LatLng currentPosition = new LatLng(location.getLatitude(),
    	location.getLongitude());
    	googleMap.addMarker(new MarkerOptions().position(currentPosition).title("hii, I am here"));
    	googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    	}
    
    
    public void  onLocationChanged(Location location) {
 
     //   TextView tvLocation = (TextView) findViewById(R.id.tv_location);
 
     //   Getting latitude of the current location
        double latitude = location.getLatitude();
 
        // Getting longitude of the current location
        double longitude = location.getLongitude();
 
        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);
 
        // Showing the current location in Google Map
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
 
        // Zoom in the Google Map
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
 
        // Setting latitude and longitude in the TextView tv_location
     //   tvLocation.setText("Latitude:" +  latitude  + ", Longitude:"+ longitude );
 
    }
 
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
 
    // An AsyncTask class for accessing the GeoCoding Web Service
   /* private class GeocoderTask extends AsyncTask<String, Void, List<Address>>{
 
        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;
 
            try {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }
 
        @Override
        protected void onPostExecute(List<Address> addresses) {
 
            if(addresses==null || addresses.size()==0){
                Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
            }
 
            // Clears all the existing markers on the map
            googleMap.clear();
 
            // Adding Markers on Google Map for each matching address
            for(int i=0;i<addresses.size();i++){
 
                Address address = (Address) addresses.get(i);
 
                // Creating an instance of GeoPoint, to display in Google Map
                latLng = new LatLng(address.getLatitude(), address.getLongitude());
 
                String addressText = String.format("%s, %s",
                address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                address.getCountryName());
 
                markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(addressText);
 
                googleMap.addMarker(markerOptions);
 
                // Locate the first location
                if(i==0)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                
            }
        }
    }
*/
	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
}