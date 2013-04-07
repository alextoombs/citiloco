package com.alextoombs.citiloco;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.alextoombs.cityplan.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Display the schedule generated from the arraylist sent back via XML from the algorithm.
 * @author Alex Toombs
 * @date 4/6/2013
 * @version 1.0
 */
public class ScheduleScreen extends Activity {
	private static final boolean DEBUG = SplashScreen.DEBUG;
	private static final String TAG = SplashScreen.TAG;

	// initialize variables for extras from intent
	String cityName = null;
	double lat = 0;
	double lng = 0;
	String xmlResponse = null;
	
	// instantiate a new Schedule for all activities
	private Schedule sched;
	private ListView lv;
	private ArrayList<Option> options;
	private Button gmapsButton;
	
	// on create function of activity
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_screen);
		if(DEBUG)
			Log.i(TAG, "Schedule Screen Activity created");
		
		lv = (ListView)findViewById(R.id.schedView);
		gmapsButton = (Button)findViewById(R.id.gmapsButton);
		
		// extras from last activity
		cityName = getIntent().getStringExtra("cityname");
		lat = Double.parseDouble(getIntent().getStringExtra("lat"));
		lng = Double.parseDouble(getIntent().getStringExtra("lng"));
		xmlResponse = getIntent().getStringExtra("xml");
		
		if(DEBUG) 
			Log.i(TAG, "Xml Response: " + xmlResponse);	
		
		// Make schedule from XML response, get option arraylist out of that
		sched = new Schedule(xmlResponse);
		options = sched.getOptions();
		
		// create array adapter and populate with list
		ArrayAdapter<String> adapter;
		
		// put into other list
		ArrayList<String> arrStr = new ArrayList<String>();
		for(Option opt : options) {
			arrStr.add("Destination:  " + opt.getLocations().get(0).getName() + ", costs about: $" + opt.getLocations().get(0).getPrice());
		}
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arrStr);
        lv.setAdapter(adapter);
        
        if(DEBUG) {
        	Log.i(TAG,"ArrayList items:");
        	for(Option opt : options) {
        		Log.i(TAG, opt.getLocations().get(0).getName());
        	}
        }
        
        // onClick listener for gmaps plot button
        gmapsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){ 
            	Intent i = new Intent(ScheduleScreen.this, PlotGmaps.class);
            	
            	int j = 0;
            	// iterate through all options, key up and iterate along the arraylist
            	for(Option opt : options) {
            		i.putExtra(j + "name",opt.getLocations().get(0).getName());
            		i.putExtra(j + "lat", opt.getLocations().get(0).getLatitude());
            		i.putExtra(j + "lng", opt.getLocations().get(0).getLongtitude());
            		i.putExtra(j + "price", opt.getLocations().get(0).getPrice());
            		j = j+1;
            	}
            	i.putExtra("tot", j);
            	
			    i.putExtra("cityname", cityName);
			    i.putExtra("lat", String.valueOf(lat));
			    i.putExtra("lng", String.valueOf(lng));
            	
            	// start next activity
            	startActivity(i);
            }
        });
        
        // if no google play on phone, hide button for gmaps plot.
        if(GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext())!=ConnectionResult.SUCCESS) {
        	gmapsButton.setVisibility(View.GONE);
        	if(DEBUG)
        		Log.i(TAG, "Google Play services not on phone.");
        }
	}
	
	/**
	 * Accessor for arraylist of options
	 * @return ArrayList of options
	 */
	public ArrayList<Option> getOptions() {
		return options;
	}
}
