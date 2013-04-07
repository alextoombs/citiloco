package com.alextoombs.citiloco;

import java.util.ArrayList;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.alextoombs.cityplan.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

/**This class plots lat/long data onto a google map to track where photos were taken. 
 * 
 * @author Alex Toombs
 * @since June 5th, 2012
 * @date April 6th, 2013
 */
public class PlotGmaps extends Activity implements OnMapClickListener {
	private ArrayList<LatLng> plotPoints = new ArrayList<LatLng>();
	private ArrayList<String> plotStamps = new ArrayList<String>();
	private ArrayList<Double> plotPrices = new ArrayList<Double>();
	
	private static final boolean DEBUG = SplashScreen.DEBUG;
	private static final String TAG = SplashScreen.TAG;
	private int plotCount = 0;
	
	private GoogleMap myMap;
	private Intent thisIntent;
	
	// info grabbed from extras
    private String cityname = null;
    private double tlat = 0;
    private double tlng = 0;
    
    // point where user is located
    private LatLng userPoint;
	
	Context mContext;
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.plot);
        mContext = getApplicationContext();
        // fragment magic
        FragmentManager fm = getFragmentManager();
        MapFragment mapView = (MapFragment)fm.findFragmentById(R.id.map);
        myMap = mapView.getMap();
        
        // settings for the GoogleMap object
        myMap.setMyLocationEnabled(true);
        myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        myMap.setOnMapClickListener(this);
        
        thisIntent = this.getIntent();
        
        // get current loc info
		cityname = getIntent().getStringExtra("cityname");
		tlat = Double.parseDouble(getIntent().getStringExtra("lat"));
		tlng = Double.parseDouble(getIntent().getStringExtra("lng"));
		
		userPoint = new LatLng(tlat,tlng);
		
		// Move the camera instantly to user's location with a zoom of 15.
		myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userPoint, 15));

		// Zoom in, animating the camera.
		myMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
		
		// toast for user action
		Toast.makeText(getApplicationContext(), "Tap points to see details about your route.", Toast.LENGTH_LONG).show();
        getPoints();
        
        // add mapmarker with special icon for user's current location
        myMap.addMarker(new MarkerOptions().position(userPoint).title("You Are Here")
        		.snippet("This is you.  Try tapping on other plots.  Follow the blue line.")
        		.icon(BitmapDescriptorFactory.fromResource(R.drawable.greenpin)));
        
        // setup for drawing blue lines through route
        ArrayList<LatLng> allPts = new ArrayList<LatLng>();
        allPts.add(userPoint); // adds initial point
        
        // iterate through arraylist of latlngs and drop mapmarkers at eah
        int k = 0;
        for(LatLng laln : plotPoints) {
        	allPts.add(plotPoints.get(k));
        	
        	myMap.addMarker(new MarkerOptions()
        			.position(laln).title(plotStamps.get(k))
        			.snippet("Price: $" + plotPrices.get(k)).icon(BitmapDescriptorFactory
                    .fromResource(R.drawable.redpin)));
        	k = k + 1;
        }
        
        // draw blue lines between points along route
        final int numPts = allPts.size();
        for(int q = 0; q < numPts-1; q++) {
        	myMap.addPolyline(new PolylineOptions()
        							.add(allPts.get(q), allPts.get(q+1))
        							.width(5)
        							.color(0x7F0000FF));
        }
        
        // debugging stuff
        if(DEBUG) {
        	Log.i(TAG, "Plots Count: " + plotCount);
        	for(int j = 0; j < plotCount; j++) {
        		Log.i(TAG, "Plotting geopoint: " + plotPoints.get(j).toString() + 
        				", location: " + plotStamps.get(j).toString() + 
        				", price: " + plotPrices.get(j).toString());
        	}
        }  
    }
    
    /**Adds values to each ArrayList from the extras populating the intent that launches the activity. */
    private void getPoints() {		
		plotCount = thisIntent.getIntExtra("tot", 0);
		if(DEBUG)
			Log.i(TAG,"totalCounts: " + plotCount);
		
		// iterate through all plots
		for(int j = 0; j < plotCount; j++) {
			// add to all arraylists
			plotStamps.add(thisIntent.getStringExtra(j + "name"));
			double lat = thisIntent.getDoubleExtra(j+"lat",0);
			double lng = thisIntent.getDoubleExtra(j+"lng", 0);
			plotPoints.add(new LatLng(lat,lng));
			plotPrices.add(thisIntent.getDoubleExtra(j + "price", 0));
			
			if(DEBUG) {
				Log.i(TAG, "plotStamp added at " + j + " pass: " + plotStamps.get(j).toString());
				Log.i(TAG, "plotPoints added at " + j + " pass: " + plotPoints.get(j).toString());
				Log.i(TAG, "plotPrices added at " + j + " price: " + plotPrices.get(j).toString());
			} 
		}
    }
    
    @Override
    public void onMapClick(LatLng point) {
    	if(DEBUG)
    		Log.i(TAG, "Point string: " + point.toString());
    	myMap.animateCamera(CameraUpdateFactory.newLatLng(point));
    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();

    	int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
     
    	// toasts with city name
    	if (resultCode == ConnectionResult.SUCCESS){
    		Toast.makeText(getApplicationContext(), 
    			 "Bringing you to " + cityname, 
    			 Toast.LENGTH_SHORT).show();
    	} else{
    		Log.e(TAG, "No play services somehow");
    	}
    }
}
