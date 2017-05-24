package com.parked.parked;

/**
 * Created by kevin on 5/23/17.
 */

public class ParkingSpot {

    private double mLatitude;
    private double mLongitude;
    private boolean mTaken;

    public ParkingSpot(double latitude, double longitude, boolean status){
        mLatitude = latitude;
        mLongitude = longitude;
        mTaken = status;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public boolean ismTaken() {
        return mTaken;
    }

    public void setmStatus(boolean mStatus) {
        this.mTaken = mStatus;
    }

}
