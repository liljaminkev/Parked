package com.parked.parked;

/**
 * Created by kevin on 5/23/17.
 */

public class ParkingSpot {

    private double mLatitude;
    private double mLongitude;
    private boolean mTaken;
    private int mTime;

    public ParkingSpot(){};

    public ParkingSpot(double latitude, double longitude, boolean status, int time){
        mLatitude = latitude;
        mLongitude = longitude;
        mTaken = status;
        mTime = time;
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

    public void setmTaken(boolean mTaken) {
        this.mTaken = mTaken;
    }

    public int getmTime() {
        return mTime;
    }

    public void setmTime(int mTime) {
        this.mTime = mTime;
    }


}
