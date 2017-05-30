package com.parked.parked;


import android.Manifest;

import android.content.pm.PackageManager;
import android.location.Location;

import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by kevin on 5/7/17.
 */

public class NavFragment extends SupportMapFragment implements LocationListener, GeoQueryEventListener {

    //Google Maps assets
    private GoogleMap mMap;
    private GoogleApiClient mClient;

    //User location Assets
    private Location mCurrentLocation;

    //User permission asets
    private static final int REQUEST_LOCATION_PERMISSIONS = 0;
    private static final String[] LOCATION_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    private static final String TAG = "NavFragment";


    //Firebase assets
    private DatabaseReference mGeoFireRef;
    private DatabaseReference mFirebaseSpotRef;

    //geofire assets
    private static final int INITIAL_ZOOM_LEVEL = 17;
    private Circle mSearchCircle;
    private GeoFire mGeoFire;
    private GeoQuery mGeoQuery;

    private Map<String,Marker> markers; //holds markers found at current location
    private Map<String, ValueEventListener> mSpotListeners;
    private Map<String,ParkingSpotInformation> mParkingSpots;
    final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();



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

        /**
         * Set up firebase and crate geofire object
         */
        mGeoFireRef = mDatabase.getReference().child("geoFire");
        mFirebaseSpotRef = mDatabase.getReference().child("_spots");
        mGeoFire = new GeoFire(mGeoFireRef);

        /**
         * setup markers database
         */

        markers = new HashMap<String, Marker>();

        if(mClient == null)
        {
            mClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                            /**
                             * Check or request permissions
                             */
                            if (ContextCompat.checkSelfPermission(getActivity(), LOCATION_PERMISSIONS[0])
                                    == PackageManager.PERMISSION_GRANTED) {
                                startLocationUpdates();


                                /**
                                 /setup google look at last location
                                 /
                                 */

                                mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mClient);

                                if (mCurrentLocation != null) {

                                    /**
                                     * setup a GeoQuery and listener at c
                                     */
                                    mGeoQuery = mGeoFire.queryAtLocation(new GeoLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), 0.1);
                                    mGeoQuery.addGeoQueryEventListener(NavFragment.this);
                                    updateUI();
                                }
                            } else {
                                requestPermissions(LOCATION_PERMISSIONS, REQUEST_LOCATION_PERMISSIONS);
                            }


                        }

                        @Override
                        public void onConnectionSuspended(int i) {

                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                            Log.d(TAG, "Failed to connect to Google Api Client");
                        }
                    })
                    .build();
        }


        getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                mMap.setMyLocationEnabled(true);
            }

        });


    }

    public void setupGeoFire(){

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


    /**
     * OnRequestPermissionsResult
     * returns if permissions have bee
     * @param requestCode
     * @param permissions
     * @param grantedResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantedResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSIONS:
                if (grantedResults.length > 0  && grantedResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantedResults);
        }
    }



    private void updateUI() {
        if (mMap == null) {
            return;
        }
        LatLng latlng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latlng, INITIAL_ZOOM_LEVEL);
        mMap.moveCamera(update);
    }

    //start getting gps updates
    protected void startLocationUpdates() {
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(500);

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
        mGeoQuery.setCenter(new GeoLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
        updateUI();
    }




    /**
     *
     *
     * GEOQUERY Assets
     *
     */

    /**
     *
     *
     */


    @Override
    public void onKeyEntered(final String key, final GeoLocation location) {

        ValueEventListener listener;

        mFirebaseSpotRef.child(key).addValueEventListener(listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ParkingSpotInformation spot = dataSnapshot.getValue(ParkingSpotInformation.class);
                Marker marker;
                if(spot.getStatus() == 1){
                    marker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(location.latitude, location.longitude))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                }
                else if(spot.getStatus() == 2){
                    marker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(location.latitude, location.longitude))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                }
                else{
                    marker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(location.latitude, location.longitude))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                }
                markers.put(key, marker);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //mSpotListeners.put(key, listener);


    }

    @Override
    public void onKeyExited(String key) {
        Marker marker = markers.get(key);
        if (marker != null) {
            marker.remove();
            markers.remove(key);
            //mFirebaseSpotRef.child(key).removeEventListener(mSpotListeners.get(key));
        }
    }

    @Override
    public void onKeyMoved(String key, GeoLocation location) {

    }

    @Override
    public void onGeoQueryReady() {

    }

    @Override
    public void onGeoQueryError(DatabaseError error) {

    }



}
