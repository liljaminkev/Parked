package com.parked.parked;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.core.GeoHashQuery;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by kevin on 5/29/17.
 */
/*
public class ParkingQuery {

    private final Map<GeoHashQuery, Query> firebaseQueries = new HashMap<GeoHashQuery, Query>();

    private static class SpotInfo {
        final GeoLocation location;
        final boolean inParkingQuery;

        public SpotInfo(GeoLocation location, boolean inParkingQuery) {
            this.location = location;
            this.inParkingQuery = inParkingQuery;
        }
    }

    private final ChildEventListener childEventLister = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            synchronized (ParkingQuery.this) {
                ParkingQuery.this.childAdded(dataSnapshot);
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            synchronized (ParkingQuery.this) {
                ParkingQuery.this.childChanged(dataSnapshot);
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public synchronized void onChildMoved(DataSnapshot dataSnapshot, String s) {
            // ignore, this should be handled by onChildChanged
        }

        @Override
        public synchronized void onCancelled(DatabaseError databaseError) {
            // ignore, our API does not support onCancelled
        }
    };

    private void childAdded(DataSnapshot dataSnapshot) {
        GeoLocation location = GeoFire.getLocationValue(dataSnapshot);
        if (location != null) {
            this.updateLocationInfo(dataSnapshot.getKey(), location);
        } else {
            // throw an error in future?
        }
    }

    private void childChanged(DataSnapshot dataSnapshot) {
        GeoLocation location = GeoFire.getLocationValue(dataSnapshot);
        if (location != null) {
            this.updateLocationInfo(dataSnapshot.getKey(), location);
        } else {
            // throw an error in future?
        }
    }

    private void setupQueries() {

        Set<GeoHashQuery> oldQueries = (this.queries == null) ? new HashSet<GeoHashQuery>() : this.queries;
        Set<GeoHashQuery> newQueries = GeoHashQuery.queriesAtLocation(center, radius);
        this.queries = newQueries;
        for (GeoHashQuery query: oldQueries) {
            if (!newQueries.contains(query)) {
                firebaseQueries.get(query).removeEventListener(this.childEventLister);
                firebaseQueries.remove(query);
                outstandingQueries.remove(query);
            }
        }
        for (final GeoHashQuery query: newQueries) {
            if (!oldQueries.contains(query)) {
                outstandingQueries.add(query);
                DatabaseReference databaseReference = this.geoFire.getDatabaseReference();
                Query firebaseQuery = databaseReference.orderByChild("g").startAt(query.getStartValue()).endAt(query.getEndValue());
                firebaseQuery.addChildEventListener(this.childEventLister);
                addValueToReadyListener(firebaseQuery, query);
                firebaseQueries.put(query, firebaseQuery);
            }
        }
        for(Map.Entry<String, GeoQuery.LocationInfo> info: this.locationInfos.entrySet()) {
            GeoQuery.LocationInfo oldLocationInfo = info.getValue();
            this.updateLocationInfo(info.getKey(), oldLocationInfo.location);
        }
        // remove locations that are not part of the geo query anymore
        Iterator<Map.Entry<String, GeoQuery.LocationInfo>> it = this.locationInfos.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, GeoQuery.LocationInfo> entry = it.next();
            if (!this.geoHashQueriesContainGeoHash(entry.getValue().geoHash)) {
                it.remove();
            }
        }

        checkAndFireReady();
    }
}
*/