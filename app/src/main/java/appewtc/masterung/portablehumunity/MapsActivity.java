package appewtc.masterung.portablehumunity;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //Explicit
    private GoogleMap mMap;
    private double myLatADouble, myLngADouble;
    private LatLng latLng;
    private LocationManager locationManager;
    private Criteria criteria;
    private boolean gpsABoolean, networkABoolean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




        //Setup Location
        setupLocation();

    }   // Main Method

    @Override
    protected void onResume() {
        super.onResume();

        locationManager.removeUpdates(locationListener);
        myLatADouble = 13.66748421;
        myLngADouble = 100.62178373;

        Location networkLocation = myFindLocation(LocationManager
                .NETWORK_PROVIDER, "No Internet");
        if (networkLocation != null) {
            myLatADouble = networkLocation.getLatitude();
            myLngADouble = networkLocation.getLongitude();
        }

        Location gpsLocation = myFindLocation(LocationManager.GPS_PROVIDER,
                "NO Card GPS");
        if (gpsLocation != null) {
            myLatADouble = gpsLocation.getLatitude();
            myLngADouble = gpsLocation.getLongitude();
        }

    }   // onResume

    @Override
    protected void onStop() {
        super.onStop();

        locationManager.removeUpdates(locationListener);

    }   // onStop

    public Location myFindLocation(String strProvider, String strLogError) {

        Location location = null;

        if (locationManager.isProviderEnabled(strProvider)) {

            locationManager.requestLocationUpdates(strProvider,
                    1000, 10, locationListener);
            location = locationManager.getLastKnownLocation(strProvider);

        } else {
            Log.d("test", strLogError);
        }

        return location;
    }


    // Create Class find Location
    public LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            myLatADouble = location.getLatitude();
            myLngADouble = location.getLongitude();

        }   // onLocationChange

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };


    private void setupLocation() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);

    }   // setup


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Start At Center
        startAtCenter();

        //Manage Marker
        manageMarker();

        //Loop for marker
        loopForMarker();


    }   // onMapReady

    private void loopForMarker() {

        mMap.clear();
        manageMarker();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               loopForMarker();
            }
        }, 1000);


    }   // loopForMarker

    private void manageMarker() {

        //for User
        latLng = new LatLng(myLatADouble, myLngADouble);

        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_cow)));

    }   // manageMarker

    private void startAtCenter() {

        latLng = new LatLng(myLatADouble, myLngADouble); // กำหนดจุดเริ่มกลางแผนที่
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));


    }   // startAtCenter


}   // Main Class
