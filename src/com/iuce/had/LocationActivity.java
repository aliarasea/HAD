package com.iuce.had;

import java.io.IOException;
import java.util.List;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class LocationActivity extends MapActivity implements LocationListener {
	
	private static final String TAG = "LocationActivity";

	LocationManager locationManager;
	Geocoder geocoder;
	TextView locationText;
	MapView map;	
	MapController mapController;
	String result;
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        
        locationText = (TextView)this.findViewById(R.id.lblLocationInfo);
        map = (MapView)this.findViewById(R.id.mapview);
        map.setBuiltInZoomControls(true);
        
        mapController = map.getController();
        mapController.setZoom(16);
        
        locationManager = (LocationManager)this.getSystemService(LOCATION_SERVICE);

        geocoder = new Geocoder(this);
        
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) 
        {
        	Log.d(TAG, location.toString());
        	this.onLocationChanged(location);	
        }
    }

    @Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		locationManager.removeUpdates(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, this);
	}
    
	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLocationChanged(Location location) 
	{

		Log.d(TAG, "onLocationChanged with location " + location.toString());

		String text = String.format("Lat:\t %f\nLong:\t %f\nAlt:\t %f\nBearing:\t %f", location.getLatitude(), location.getLongitude(), location.getAltitude(), location.getBearing());
		this.locationText.setText(text);
		StringBuilder sb = new StringBuilder();
		
		try 
		{

			List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 10);
			for (Address address : addresses) 
			{
				this.locationText.append("\n" + address.getAddressLine(0));
				sb.append("\n"+ address.getAddressLine(0));
			}
			
			result = sb.toString();
			
			int latitude = (int)(location.getLatitude() * 1000000);
			int longitude = (int)(location.getLongitude() * 1000000);

			GeoPoint point = new GeoPoint(latitude,longitude);
			mapController.animateTo(point);

        	if(!ServiceConstant.p.isEmpty())
        	{ 
        		sendSMS();
        		super.onPause();
        	}
		}
		catch (IOException e) 
		{
			Log.e("LocateMe", "Could not get Geocoder data", e);
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
	public void sendSMS() 
	{
		try 
		{
			for (int i = 0; i <ServiceConstant.p.size(); i++)
			{		
				SmsManager smsManager = SmsManager.getDefault();
				smsManager.sendTextMessage(ServiceConstant.p.get(i).toString(), null,
				"ACÝL DURUM!!!\n" + "Konum:\n"+result,	null, null);
			}

		} 
		catch (Exception e) 
		{
			Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_LONG).show();
		}


	}
}