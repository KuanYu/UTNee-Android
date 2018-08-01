package com.butions.utnee;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chalitta Khampachua on 09-Nov-16.
 */
public class ProfileFirebase {
    private static final DatabaseReference sRef = FirebaseDatabase.getInstance().getReference();
    private static final String TAG = "ProfileFirebase";
    private static Context mContext;
    private static String My_PROFILE;

    public static ProfileListener addProfileListener(String userID, final ProfileCallbacks callbacks, Context context){
        mContext = context;
        ProfileListener listener = new ProfileListener(callbacks);
        My_PROFILE = "Users/" + userID;
        sRef.child(My_PROFILE).addValueEventListener(listener);
        return listener;
    }

    public static void stop(ProfileListener listener){
        sRef.child(My_PROFILE).removeEventListener(listener);
    }


    public static class ProfileListener implements ValueEventListener {
        private ProfileCallbacks callbacks;
        private Map<String, String> myProfile = new HashMap<>();

        ProfileListener(ProfileCallbacks callbacks){
            this.callbacks = callbacks;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.getChildrenCount() > 0) {
                clearAll();
                Map<String, Object> mapObj = (Map<String, Object>) dataSnapshot.getValue();
                assert mapObj != null;
                if (mapObj.containsKey("Birthday")) {
                    myProfile.put("Birthday", mapObj.get("Birthday").toString());
                }

                if (mapObj.containsKey("Gender")) {
                    myProfile.put("Gender", mapObj.get("Gender").toString());
                }

                if (mapObj.containsKey("Link")) {
                    myProfile.put("Link", mapObj.get("Link").toString());
                }

                if (mapObj.containsKey("Address")) {
                    myProfile.put("Address", mapObj.get("Address").toString());
                }

                if (mapObj.containsKey("Name")) {
                    myProfile.put("Name", mapObj.get("Name").toString());
                }

                if (mapObj.containsKey("Phone")) {
                    myProfile.put("Phone", mapObj.get("Phone").toString());
                }

                if (mapObj.containsKey("Picture")) {
                    myProfile.put("Picture", mapObj.get("Picture").toString());
                }

                if (mapObj.containsKey("Cover")) {
                    myProfile.put("Cover", mapObj.get("Cover").toString());
                }

                if (mapObj.containsKey("Email")) {
                    myProfile.put("Email", mapObj.get("Email").toString());
                }

                if (mapObj.containsKey("UserID")) {
                    myProfile.put("UserID", mapObj.get("UserID").toString());
                }

                if(mapObj.containsKey("DeviceID")){
                    myProfile.put("DeviceID", mapObj.get("DeviceID").toString());
                }

                if(mapObj.containsKey("DeviceName")){
                    myProfile.put("DeviceName", mapObj.get("DeviceName").toString());
                }

                if(mapObj.containsKey("Token")){
                    myProfile.put("Token", mapObj.get("Token").toString());
                }

                if(mapObj.containsKey("LatLng")){
                    String latlng = mapObj.get("LatLng").toString();
                    String[] location = latlng.split(",");
                    myProfile.put("Latitude", location[0]);
                    myProfile.put("Longitude", location[1]);
                }

                ProfileValue profile = new ProfileValue();
                profile.setMyProfile(myProfile, mContext);

                if(callbacks != null){
                    callbacks.onProfileChange(profile);
                }


            }else{
                //is not value at database
                if(callbacks != null){
                    callbacks.onProfileChange(null);
                }
            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

        private void clearAll() {
            myProfile.clear();
        }
    }

    public interface ProfileCallbacks{
        public void onProfileChange(ProfileValue profileValue);
    }

}
