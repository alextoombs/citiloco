package com.alextoombs.citiloco;

import java.util.ArrayList;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

/**
 * Extends ItemizedOverlay for plotting stuff on a google map.
 * @author Alex Toombs
 * @date 4/6/2013
 * @version 1.0
 */
public class DatabaseOverlay extends ItemizedOverlay {
	private static final boolean DEBUG = SplashScreen.DEBUG;
	private static final String TAG = SplashScreen.TAG;
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Context mContext;
	
	public DatabaseOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		mContext = context;
		if(DEBUG) {
			if(defaultMarker==null)
				Log.i(TAG, "That marker is null.");
		}
	}
	
	/**Creates an item in the overlay. */
	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return mOverlays.get(i);
	}

	/**Returns the size of the overlay. */
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return mOverlays.size();
	}

	/**Add an overlay to the private arraylist mOverlays. */
	public void addOverlay(OverlayItem overlay) {
	    mOverlays.add(overlay);
	    populate();
	}
	
	/**Handles when the user taps the mapview. */
	@Override
	protected boolean onTap(int index) {
	  OverlayItem item = mOverlays.get(index);
	  AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
	  dialog.setTitle(item.getTitle());
	  dialog.setMessage(item.getSnippet());
	  dialog.show();
	  return true;
	}
}
