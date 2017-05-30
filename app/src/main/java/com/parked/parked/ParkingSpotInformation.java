package com.parked.parked;

/**
 * Created by kevin on 5/29/17.
 */

public class ParkingSpotInformation {
    private int status;
    private float rate;

    public ParkingSpotInformation(){}

    public ParkingSpotInformation(int status, float rate){
        this.status = status;
        this.rate = rate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }


}
