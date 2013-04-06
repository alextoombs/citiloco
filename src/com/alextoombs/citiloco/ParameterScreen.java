package com.alextoombs.citiloco;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.alextoombs.cityplan.R;

/**
 * Activity to enter parameters to guide recommended itinerary
 * 
 * @author Alex Toombs
 * @date 4/6/13
 * @version 1.0
 *
 */
public class ParameterScreen extends Activity {
	private static final boolean DEBUG = SplashScreen.DEBUG;
	private static final String TAG = SplashScreen.TAG;
	private int costProgress = 0;
	private int amHour = 0;
	private int amMinute = 0;
	private int pmHour = 0;
	private int pmMinute = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.parameter_screen);
		if(DEBUG)
			Log.i(TAG, "Parameter Entry Screen Activity created");
		
		// objects
		final SeekBar costSlider = (SeekBar)findViewById(R.id.costSlider);
		final TimePicker amTimePicker = (TimePicker)findViewById(R.id.amTimePicker);
		final TimePicker pmTimePicker = (TimePicker)findViewById(R.id.pmTimePicker);
		final Button sendButton = (Button)findViewById(R.id.sendButton);
		
		// listener for costSlider to obtain total cost
		costSlider.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
		    @Override
		    public void onStopTrackingTouch(SeekBar seekBar) {
		        // TODO Auto-generated method stub
		    	   // toast with value
		        Toast.makeText(getApplicationContext(), "Your Selected Cost:  $" + costProgress, Toast.LENGTH_SHORT).show();
		    }

		    @Override
		    public void onStartTrackingTouch(SeekBar seekBar) {
		        // TODO Auto-generated method stub
		    }

		    @Override
		    public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
		        // TODO Auto-generated method stub
		        costProgress = progress;
		        
		        if(DEBUG)
		        	Log.i(TAG, "Current progress: " + costProgress);
		    }
		});
		        
		// timepicker listener for am
		amTimePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
			public void onTimeChanged (TimePicker view, int hourOfDay, int minute) {
				view.setCurrentHour(hourOfDay);
				view.setCurrentMinute(minute);
			}
		});
		
		// timepicker listener for pm
		pmTimePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
			public void onTimeChanged (TimePicker view, int hourOfDay, int minute) {
				view.setCurrentHour(hourOfDay);
				view.setCurrentMinute(minute);
			}
		});
		
		// initialize times for TimePicker objects
		amTimePicker.setIs24HourView(true);
		pmTimePicker.setIs24HourView(true);
		amTimePicker.setCurrentHour(8);
		amTimePicker.setCurrentMinute(8);
		pmTimePicker.setCurrentHour(17);
		pmTimePicker.setCurrentMinute(17);
		
		// get times for time picker
		amHour = amTimePicker.getCurrentHour();
		amMinute = amTimePicker.getCurrentMinute();
		pmHour = pmTimePicker.getCurrentHour();
		pmMinute = pmTimePicker.getCurrentMinute();
		
		if(DEBUG) {
			Log.i(TAG, "Current cost: $" + costProgress);
			Log.i(TAG, "Current AM time: " + amHour + ":" + amMinute + "AM");
			Log.i(TAG, "Current PM time: " + pmHour + ":" + pmMinute + "PM");
		}
		
		// Button to send values to server
        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){ 
            	if(DEBUG)
            		Log.i(TAG,"Sending value to server for calculations");
            	
            	// magic
            }
        });
	}
}
