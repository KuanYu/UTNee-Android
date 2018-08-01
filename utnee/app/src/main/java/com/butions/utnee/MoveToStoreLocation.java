package com.butions.utnee;

/**
 * Created by Chalitta Khampachua on 11-Oct-17.
 */

public class MoveToStoreLocation {
    private String TAG = "MoveToFriesndLocation";
    private static MoveToStoreLocation mInstance = null;
    private double latitude;
    private double longitude;
    private boolean flag = false;

    protected MoveToStoreLocation(){

    }

    public static synchronized MoveToStoreLocation getInstance(){
        if(null == mInstance){
            mInstance = new MoveToStoreLocation();
        }
        return mInstance;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
