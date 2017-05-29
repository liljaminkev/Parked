package com.parked.parked;


import android.Manifest;

import android.content.pm.PackageManager;
import android.location.Location;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * Created by kevin on 5/7/17.
 */

public class NavFragment extends SupportMapFragment implements LocationListener {

    private GoogleMap mMap;
    private Location mCurrentLocation;
    private Boolean mLocationPermissionGranted;
    private GoogleApiClient mClient;
    private static final String TAG = "NavFragment";
    private static final int REQUEST_LOCATION_PERMISSIONS = 0;

    private static final String[] LOCATION_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};



    public static NavFragment newInstance() {
        return new NavFragment();
    }

    //
    //Basic calls
    //
    //
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mClient = new GoogleApiClient.Builder(getActivity()).addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        startLocationUpdates();

                        if (hasLocationPermission()) {
                            findLocation();
                            updateUI();
                        } else
                            requestPermissions(LOCATION_PERMISSIONS, REQUEST_LOCATION_PERMISSIONS);

                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                }).build();

        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setMyLocationEnabled(true);
            }

        });

        /*
        /setup GeoFire
        /
        */
        //this.geoFire = new GeoFire(FirebaseDatabase.getInstance(app).getReferenceFromUrl(GEO_FIRE_REF));
        // radius in km
        //this.geoQuery = this.geoFire.queryAtLocation(INITIAL_CENTER, 1);

    }

    @Override
    public void onStart() {
        super.onStart();
        mClient.connect();

    }

    @Override
    public void onStop() {
        super.onStop();
        mClient.disconnect();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mClient.isConnected()) {
            startLocationUpdates();
        }
    }

/*


    /**Menu Items
    //
    //
    */

    /*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //inflater.inflate(R.menu.menu, menu);

        //MenuItem findLocation = menu.findItem(R.id.action_locate);
        //findLocation.setEnabled(mClient.isConnected());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_locate:
                if (hasLocationPermission()){
                    findLocation();
                    updateUI();
                }
                else
                    requestPermissions(LOCATION_PERMISSIONS, REQUEST_LOCATION_PERMISSIONS);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/


    //
    //Device permissions
    //
    //
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantedResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSIONS:
                if (hasLocationPermission()) {
                    findLocation();
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantedResults);
        }
    }

    private boolean hasLocationPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), LOCATION_PERMISSIONS[0]);

        return result == PackageManager.PERMISSION_GRANTED;
    }


    private void findLocation() {

        if (ContextCompat.checkSelfPermission(getActivity(), LOCATION_PERMISSIONS[0])
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            requestPermissions(LOCATION_PERMISSIONS, REQUEST_LOCATION_PERMISSIONS);
        }

        if (mLocationPermissionGranted) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mClient);
            updateUI();
        }
    }

    private void updateUI() {
        if (mMap == null) {
            return;
        }

        LatLng myLocation = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());


        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(myLocation, 18);
        mMap.moveCamera(update);

    }

    //start getting gps updates
    protected void startLocationUpdates() {
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(1000);

        LocationServices.FusedLocationApi.requestLocationUpdates(mClient, request, this);
    }

    //stop getting gps updates
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mClient, this);
    }

    //when location has changed update var
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        updateUI();
    }




}
