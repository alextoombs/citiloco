package com.alextoombs.citiloco;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alextoombs.cityplan.R;

/**
 * Splash screen for citiloco app, designed to come up with an itinerary for someone in a new city.
 * 
 * @author Alex Toombs
 * @date 4/6/2013
 * @version 1.0
 * 
 * ND Startup Weekend
 */
public class SplashScreen extends Activity {	
	private LocationManager lmgr = null;
	public static final String TAG = "citiloco";
	public static final boolean DEBUG = true;
	private TextView locTv;
	private Location currentLoc = null;
	private BasicCity bcity;
	
	// Run on create for new activity
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		if(DEBUG)
			Log.i(TAG, "Splash Screen Activity created");
		
		// objects
		final ImageButton goPlanButton = (ImageButton)findViewById(R.id.goplan_button);
		lmgr=(LocationManager)getSystemService(LOCATION_SERVICE);
		locTv = (TextView)findViewById(R.id.locTv);

		// if signal found, continue prompt.  Else, prompt to look for service
		if(!locTv.getText().toString().contains("GPS")) {
			// show toast to prompt user to tap
			final String promptTxt = "Signal Found! Tap Anywhere to Continue";
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(getApplicationContext(), promptTxt, duration);
			toast.show();
		}
		else {
        	final String gpsTxt = "Trying to get your location-- hang on.";
        	int duration = Toast.LENGTH_SHORT;
        	Toast toast = Toast.makeText(getApplicationContext(), gpsTxt, duration);
        	toast.show();
		}
		
		// Button to go to planning activity
        goPlanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){ 
            	Intent goToChoice = new Intent(SplashScreen.this, CityChoiceScreen.class);
            	
            	// If app is still looking for GPS, don't go to next activity.  Instead, prompt user to go outside
            	if(!locTv.getText().toString().contains("GPS")) {
            		// Add extra info to intent
            		goToChoice.putExtra("cityname", bcity.getName());
            		goToChoice.putExtra("lat", String.valueOf(bcity.getLat()));
            		goToChoice.putExtra("lng", String.valueOf(bcity.getLng()));
            		startActivity(goToChoice);
            		
                	if(DEBUG)
                		Log.i(TAG, "Going to City Choice Screen");
            	}
            	else {
            		if(DEBUG)
            			Log.i(TAG, "No city given yet, waiting for GPS signal");
            			
                	final String gpsTxt = "No GPS yet-- try going outside.";
                	int duration = Toast.LENGTH_SHORT;
                	
                	Toast toast = Toast.makeText(getApplicationContext(), gpsTxt, duration);
                	toast.show();
            	}
            }
        });
	}
	
	@Override
	public void onResume() {
	  super.onResume();
	  lmgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,
	                                3600000, 1000,
	                                onLocationChange);
	}
	
	@Override
	public void onPause() {
	  super.onPause();
	  lmgr.removeUpdates(onLocationChange);
	}
	
	// Listen for a change of location, update location on app splash screen if changed
	LocationListener onLocationChange=new LocationListener() {
		public void onLocationChanged(Location location) {
			if(DEBUG)
				Log.i(TAG, "Updating location");
	        updateLoc(location);
	        currentLoc = location;
		}
		    
		public void onProviderDisabled(String provider) {
		  // required for interface, not used
		}
		    
		public void onProviderEnabled(String provider) {
		  // required for interface, not used
		}
		    
		public void onStatusChanged(String provider, int status,
		                              Bundle extras) {
		  // required for interface, not used
		}
	};

	/**
	 * Requests an update to the current LocationManager object
	 * @param location Location object that indicates the current location from GPS
	 */
	private void updateLoc(Location location) {   
	  lmgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,
	                                3600000, 1000,
	                                onLocationChange);
	  
	  locTv.setText(getCityName(location));
	  currentLoc = location;
	  if(DEBUG)
		  Log.i(TAG, "Location updated.");
	}
	
	/**
	 * Get the name of the city from the location manager object
	 * @param location Location object provided by GPS on device
	 * @return cityName String representation of city name for display in TextView
	 */
	private String getCityName(Location location) {
		// get lat and lng from location object
		double lat = location.getLatitude();
		double lng = location.getLongitude();
		if(DEBUG)
			Log.i(TAG, "Lat: " + lat + ", lng: " + lng);
		
		List<Address> addresses = null;
		
		// use Geocoder object gcd to get a Locale from a latitude and longitude
		Geocoder gcd = new Geocoder(this.getBaseContext(), Locale.getDefault());
		try {
			addresses = gcd.getFromLocation(lat, lng, 1);
		}
		catch (IOException e){
			// this operation throws IOException and possibly lat/lng out of bounds exceptions
			Log.e(TAG, "Exception on geocoder pull from location: " + e.getMessage());
		}

		
		// if there's anything in the Addresses list, return that string
		if (addresses.size() > 0) {
			if(DEBUG)
				Log.i(TAG, "City name: " + addresses.get(0).getLocality());
			
			// fill BasicCity object fields
			bcity = new BasicCity(addresses.get(0).getLocality(),lat,lng);
			if(DEBUG) 
				Log.i(TAG, "bcity: " + bcity.getName());
			
			
			return "in " + addresses.get(0).getLocality();
		}
		else {
			if(DEBUG)
				Log.i(TAG, "Nothing in GCD address list");
		}
		// dummy return
		return "Waiting for GPS...";
	}
	
	/**
	 * Accessor for the current location (i.e. South Bend, once you're already there, for other methods)
	 * @return currentLoc Location object of the current city
	 */
	public Location getLoc() {
		return currentLoc;
	}
	
	/**
	 * Class for storing and serving up information about current location
	 * @author Alex Toombs
	 * @date 4/6/2013
	 */
	class BasicCity {
		private String basicName;
		private double lat;
		private double lng;
		
		/**
		 * Base constructor for BasicCity class
		 * @param bName String basic name (i.e South Bend)
		 * @param bLat double latitude
		 * @param bLng double longitude
		 */
		public BasicCity(String bName, double bLat, double bLng) {
			this.basicName = bName;
			this.lat = bLat;
			this.lng = bLng;
		}
		
		/**
		 * Updates fields of the location object
		 */
		public void updateLoc() {
			Location tLoc = getLoc();
			lat = tLoc.getLatitude();
			lng = tLoc.getLongitude();
		}
		
		/**
		 * Update locality name
		 * @param name String locality name
		 */
		public void setName(String name) {
			basicName = name;
		}
		
		/**
		 * Accessor for the city name
		 * @return String basicName the name of the city
		 */
		public String getName() {
			return basicName;
		}
		
		/**
		 * Accessor for latitude
		 * @return double latitude of location
		 */
		public double getLat() {
			return lat;
		}
		
		/**
		 * Accessor for longitude
		 * @return double longitude of location
		 */
		public double getLng() {
			return lng;
		}
	}
}
