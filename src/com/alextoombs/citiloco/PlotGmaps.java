package com.alextoombs.citiloco;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.alextoombs.cityplan.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

/**This class plots lat/long data onto a google map to track where photos were taken. 
 * 
 * @author Alex Toombs
 * @since June 5th, 2012
 * @date April 6th, 2013
 */
public class PlotGmaps extends MapActivity {
	private ArrayList<GeoPoint> plotPoints = new ArrayList<GeoPoint>();
	private ArrayList<String> plotStamps = new ArrayList<String>();
	private static final boolean DEBUG = SplashScreen.DEBUG;
	private static final String TAG = SplashScreen.TAG;
	private int plotCount = 0;
	private Intent thisIntent;
	
	Context mContext;
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.plot);
        mContext = getApplicationContext();
        
        thisIntent = this.getIntent();
        
        /**Allows zooming. */
        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.mapmarker);
        if(DEBUG) {
        	if(drawable==null)
        		Log.i(TAG, "Drawable is null.  NPE inc");
        }
        DatabaseOverlay itemizedoverlay = new DatabaseOverlay(drawable, mContext);
        getPoints();
        
        /**Makes Mexico City point. */
       // GeoPoint point = new GeoPoint(19240000,-99120000);
        //OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!", "I'm in Mexico City!");
        
        Log.i(TAG, "Plots Count: " + plotCount);
        /**Plot plots on map. */
        for(int j = 0; j < plotCount; j++) {
        	OverlayItem overlayitem = new OverlayItem(plotPoints.get(j),"Timestamp:",plotStamps.get(j));
        	itemizedoverlay.addOverlay(overlayitem);
        	if(DEBUG)
        		Log.i(TAG, "Plotting geopoint: " + plotPoints.get(j).toString());
        }
        mapOverlays.add(itemizedoverlay);
  
    }
    
    /**Get points from ArrayList and add them to overlayitem to plot. */
    private void getPoints() {		
		plotCount = thisIntent.getIntExtra("tot", 0);
		if(DEBUG)
			Log.i(TAG,"totalCounts: " + plotCount);
		
		for(int j = 0; j < plotCount; j++) {
			plotStamps.add(thisIntent.getStringExtra(j + "name"));
			plotPoints.add(getPoint(thisIntent.getDoubleExtra(j+"lat",0),  thisIntent.getDoubleExtra(j+"lng", 0)));
			
			if(DEBUG) {
				Log.i(TAG, "plotStamp added at " + j + " pass: " + plotStamps.get(j).toString());
				Log.i(TAG, "plotPoints added at " + j + " pass: " + plotPoints.get(j).toString());
			}
		}
    }
    
    /**Return a geopoint from a coordinate. */
    private GeoPoint getPoint(Double lat, Double lon) {
        return(new GeoPoint((int)(lat*1000000.0), (int)(lon*1000000.0)));
      }
    
    /**Convert a string latitude or longitude to float degrees. */
    private Float convertToDegree(String stringDMS){
		 Float result = null;
		 if(DEBUG)
			 Log.i(TAG, "StringDMS: " + stringDMS);
		 String[] DMS = stringDMS.split(",", 3);
		 if(DEBUG)
			 Log.i(TAG, "STringDMS[]: " + DMS);
		
		 String[] stringD = DMS[0].split("/", 2);
		 Double D0 = Double.valueOf(stringD[0]);
		 Double D1 = Double.valueOf(stringD[1]);
		 Double FloatD = D0/D1;
		
		 String[] stringM = DMS[1].split("/", 2);
		 Double M0 = Double.valueOf(stringM[0]);
		 Double M1 = Double.valueOf(stringM[1]);
		 Double FloatM = M0/M1;
		  
		 String[] stringS = DMS[2].split("/", 2);
		 Double S0 = Double.valueOf(stringS[0]);
		 Double S1 = Double.valueOf(stringS[1]);
		 Double FloatS = S0/S1;
		  
		 result = new Float(FloatD + (FloatM/60) + (FloatS/3600));
		  
		 return result;
    };
    
    /**Required to implement from parent. */
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
}
