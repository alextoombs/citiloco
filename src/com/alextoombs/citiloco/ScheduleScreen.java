package com.alextoombs.citiloco;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.alextoombs.cityplan.R;

/**
 * Display the schedule generated from the arraylist sent back via XML from the algorithm.
 * @author Alex Toombs
 * @date 4/6/2013
 * @version 1.0
 *
 */
public class ScheduleScreen extends Activity {
	private static final boolean DEBUG = SplashScreen.DEBUG;
	private static final String TAG = SplashScreen.TAG;
	
	String cityName = null;
	double lat = 0;
	double lng = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule_screen);
		if(DEBUG)
			Log.i(TAG, "Schedule Screen Activity created");
		
		// extras from last activity
		cityName = getIntent().getStringExtra("cityname");
		lat = Double.parseDouble(getIntent().getStringExtra("lat"));
		lng = Double.parseDouble(getIntent().getStringExtra("lng"));
	}
}
