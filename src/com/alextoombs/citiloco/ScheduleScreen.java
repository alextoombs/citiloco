package com.alextoombs.citiloco;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.alextoombs.cityplan.R;

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
	
	// on create function of activity
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_screen);
		if(DEBUG)
			Log.i(TAG, "Schedule Screen Activity created");
		
		lv = (ListView)findViewById(R.id.schedView);
		
		// extras from last activity
		cityName = getIntent().getStringExtra("cityname");
		lat = Double.parseDouble(getIntent().getStringExtra("lat"));
		lng = Double.parseDouble(getIntent().getStringExtra("lng"));
		xmlResponse = getIntent().getStringExtra("xml");
//		// convert response into proper format
//		String modXml = xmlResponse.substring(xmlResponse.indexOf("<"),xmlResponse.length());
//		modXml = "<schedule>\n" + modXml + "\n</schedule>";
		
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
	}
}
