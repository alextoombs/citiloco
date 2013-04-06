package com.alextoombs.citiloco;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alextoombs.cityplan.R;

/**
 * Activity to choose city or plan a trip ahead
 * 
 * @author Alex Toombs
 * @date 4/6/13
 * @version 1.0
 *
 */
public class CityChoiceScreen extends Activity {
	private static final boolean DEBUG = SplashScreen.DEBUG;
	private static final String TAG = SplashScreen.TAG;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_choice_screen);
		if(DEBUG)
			Log.i(TAG, "Choice Screen Activity created");
		
		// get bcity name from intent from last activity
		final String cityName = getIntent().getStringExtra("cityname");
		final double lat = Double.parseDouble(getIntent().getStringExtra("lat"));
		final double lng = Double.parseDouble(getIntent().getStringExtra("lng"));
		
		// objects
		final Button thisCity = (Button)findViewById(R.id.thisCityButton);
		final Button planCity = (Button)findViewById(R.id.planCityButton);
		TextView currLocTv = (TextView)findViewById(R.id.ccLocTv);
		
		if(DEBUG)
			Log.i(TAG, "CityName: " + cityName);
		currLocTv.setText(cityName);
		
		// Button to stay in this city
        thisCity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){ 
            	if(DEBUG)
            		Log.i(TAG,"This city button pressed");
            	
            	// launches Intent for parameter entry activity
            	Intent goToEntry = new Intent(CityChoiceScreen.this, ParameterScreen.class);
        		// Add extra info to intent
        		goToEntry.putExtra("cityname", cityName);
        		goToEntry.putExtra("lat", String.valueOf(lat));
        		goToEntry.putExtra("lng", String.valueOf(lng));
            	startActivity(goToEntry);
            }
        });
        
		// Button for future planning.  Coming soon!
        planCity.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){ 
            	if(DEBUG)
            		Log.i(TAG,"The planning button pressed");
            	final String soonTxt = "This feature is coming soon!";
            	int duration = Toast.LENGTH_SHORT;
            	
            	Toast toast = Toast.makeText(getApplicationContext(), soonTxt, duration);
            	toast.show();
            }
        });
	}
	
}
