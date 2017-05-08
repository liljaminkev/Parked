package com.parked.parked;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.parked.parked.R.string.findMyLocation;

/**
 * Created by kevin on 5/7/17.
 */

public class NavFragment extends SupportMapFragment {

    private GoogleMap mMap;
    private Location mCurrentLocation;
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
        setHasOptionsMenu(true);

        mClient = new GoogleApiClient.Builder(getActivity()).addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        getActivity().invalidateOptionsMenu();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                }).build();

        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                updateUI();
            }

        });

    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().invalidateOptionsMenu();
        mClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mClient.disconnect();
    }



    //
    //Menu Items
    //
    //
    //
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_nav, menu);

        MenuItem findLocation = menu.findItem(R.id.action_locate);
        findLocation.setEnabled(mClient.isConnected());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_locate:
                if (hasLocationPermission())
                    findLocation();
                else
                    requestPermissions(LOCATION_PERMISSIONS, REQUEST_LOCATION_PERMISSIONS);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    //
    //Device permissions
    //
    //
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantedResults) {
        if (hasLocationPermission()) {
            findLocation();
        } else
            super.onRequestPermissionsResult(requestCode, permissions, grantedResults);
    }

    private boolean hasLocationPermission(){
        int result = ContextCompat.checkSelfPermission(getActivity(), LOCATION_PERMISSIONS[0]);

        return result == PackageManager.PERMISSION_GRANTED;
    }


    private void findLocation() {
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        request.setInterval(0);

        LocationServices.FusedLocationApi.requestLocationUpdates(mClient, request, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i(TAG, "Got a fix: " + location);
                new LocationTask().execute(location);
            }
        });
    }



    private void updateUI(){
        if (mMap == null){
            return;
        }


        /*
        LatLng myLocation = new LatLng(mCurrentLocation.getLatitude(),
                mCurrentLocation.getLongitude());

        LatLngBounds bounds = new LatLngBounds.Builder().include(myLocation).build();

        int margin = getResources().getDimensionPixelSize(R.dimen.map_inset_margin);

        */

        LatLng sanFran = new LatLng(38.009270, -121.962937);

        mMap.addMarker(new MarkerOptions().position(sanFran).title("here"));
        CameraUpdate update = CameraUpdateFactory.newLatLng(sanFran);
        mMap.animateCamera(update);

    }



    private class LocationTask extends AsyncTask<Location, Void, Void>{

        private Location mLocation;

        @Override
        protected Void doInBackground(Location... params)
        {
            mLocation = params[0];

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            mCurrentLocation = mLocation;

            updateUI();
        }
    }
}
