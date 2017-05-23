package com.parked.parked;

/**
 * Created by kevin on 5/23/17.
 */

public class parkingSpot {

    private double mLatitude;
    private double mLongitude;
    private boolean mStatus;

    public parkingSpot(double latitude, double longitude, boolean status){
        mLatitude = latitude;
        mLongitude = longitude;
        mStatus = status;
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

    public boolean ismStatus() {
        return mStatus;
    }

    public void setmStatus(boolean mStatus) {
        this.mStatus = mStatus;
    }

}
