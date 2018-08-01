package com.butions.utnee;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chalitta Khampachua on 03-Oct-17.
 */

public class ProfileValue {

    private String TAG = "ProfileValue";
    private Map<String, String> myProfile = new HashMap<>();

    public Map<String, String> getMyProfile() {
        return myProfile;
    }

    public void setMyProfile(Map<String, String> myProfile , Context context) {
        this.myProfile = myProfile;

        SharedPreferences sp = context.getSharedPreferences("MYPROFILE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("Birthday", myProfile.get("Birthday"));
        editor.putString("Gender", myProfile.get("Gender"));
        editor.putString("Link", myProfile.get("Link"));
        editor.putString("Address", myProfile.get("Address"));
        editor.putString("Name", myProfile.get("Name"));
        editor.putString("Phone", myProfile.get("Phone"));
        editor.putString("Picture", myProfile.get("Picture"));
        editor.putString("Cover", myProfile.get("Cover"));
        editor.putString("Email", myProfile.get("Email"));
        editor.putString("FacebookID", myProfile.get("FacebookID"));
        editor.putString("DeviceID", myProfile.get("DeviceID"));
        editor.putString("DeviceName", myProfile.get("DeviceName"));
        editor.putString("Token", myProfile.get("Token"));
        editor.putString("OnMap", myProfile.get("OnMap"));
        editor.putString("Latitude", myProfile.get("Latitude"));
        editor.putString("Longitude", myProfile.get("Longitude"));
        editor.putString("Code", myProfile.get("Code"));
        editor.putString("ShopsID", myProfile.get("ShopsID"));
        editor.apply();
    }
}
