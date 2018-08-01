package com.butions.utnee;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chalitta Khampachua on 09-Nov-16.
 */
public class ShopsFirebase {
    private static final DatabaseReference sRef = FirebaseDatabase.getInstance().getReference();
    private static final String TAG = "ShopsFirebase";
    private static Context mContext;
    private static String MY_SHOPS;

    public static ShopsListener addShopsListener(String userID, final ShopsCallbacks callbacks, Context context){
        mContext = context;
        ShopsListener listener = new ShopsListener(callbacks);
        MY_SHOPS = "Shops/" + userID;
        sRef.child(MY_SHOPS).addValueEventListener(listener);
        return listener;
    }

    public static void stop(ShopsListener listener){
        sRef.child(MY_SHOPS).removeEventListener(listener);
    }


    public static class ShopsListener implements ValueEventListener {
        private ShopsCallbacks callbacks;
        private ArrayList<String> mRetailShopID = new ArrayList<>();
        private Map<Integer, String> mShopName = new HashMap<>();
        private Map<Integer, String> mShopType = new HashMap<>();
        private Map<Integer, String> mShopRate = new HashMap<>();
        private Map<Integer, String> mShopAddress = new HashMap<>();
        private Map<Integer, String> mShopOpenTime = new HashMap<>();
        private Map<Integer, String> mShopCloseTime = new HashMap<>();
        private Map<Integer, String> mShopCode = new HashMap<>();
        private Map<Integer, String> mShopImage1 = new HashMap<>();
        private Map<Integer, String> mShopImage2 = new HashMap<>();
        private Map<Integer, String> mShopImage3 = new HashMap<>();
        private Map<Integer, String> mShopImage4 = new HashMap<>();
        private Map<Integer, String> mShopImage5 = new HashMap<>();
        private Map<Integer, String> mShopTelephone = new HashMap<>();
        private Map<Integer, String> mShopDescription = new HashMap<>();
        private Map<Integer, String> mShopLink = new HashMap<>();
        private Map<Integer, String> mShopLatitude = new HashMap<>();
        private Map<Integer, String> mShopLongitude = new HashMap<>();

        ShopsListener(ShopsCallbacks callbacks){
            this.callbacks = callbacks;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.getChildrenCount() > 0) {
                clearAll();
                int count = -1;
                for (DataSnapshot objDataSnapshotPost : dataSnapshot.getChildren()) {
                    count++;
                    mRetailShopID.add(objDataSnapshotPost.getKey());

                    Map<String, Object> mapObjShop = (Map<String, Object>) objDataSnapshotPost.getValue();
                    if (mapObjShop.containsKey("Name")) mShopName.put(count, mapObjShop.get("Name").toString());
                    if (mapObjShop.containsKey("Type")) mShopType.put(count, mapObjShop.get("Type").toString());
                    if (mapObjShop.containsKey("Image1")) mShopImage1.put(count, mapObjShop.get("Image1").toString());
                    if (mapObjShop.containsKey("Image2")) mShopImage2.put(count, mapObjShop.get("Image2").toString());
                    if (mapObjShop.containsKey("Image3")) mShopImage3.put(count, mapObjShop.get("Image3").toString());
                    if (mapObjShop.containsKey("Image4")) mShopImage4.put(count, mapObjShop.get("Image4").toString());
                    if (mapObjShop.containsKey("Image5")) mShopImage5.put(count, mapObjShop.get("Image5").toString());
                    if (mapObjShop.containsKey("Code")) mShopCode.put(count, mapObjShop.get("Code").toString());
                    if (mapObjShop.containsKey("Rate")) mShopRate.put(count, mapObjShop.get("Rate").toString());
                    if (mapObjShop.containsKey("Address")) mShopAddress.put(count, mapObjShop.get("Address").toString());
                    if (mapObjShop.containsKey("TimeOpen")) mShopOpenTime.put(count, mapObjShop.get("TimeOpen").toString());
                    if (mapObjShop.containsKey("TimeClose")) mShopCloseTime.put(count, mapObjShop.get("TimeClose").toString());
                    if (mapObjShop.containsKey("Tel")) mShopTelephone.put(count, mapObjShop.get("Tel").toString());
                    if (mapObjShop.containsKey("Descriptions")) mShopDescription.put(count, mapObjShop.get("Descriptions").toString());
                    if (mapObjShop.containsKey("Link")) mShopLink.put(count, mapObjShop.get("Link").toString());
                    if(mapObjShop.containsKey("LatLng")){
                        String latlng = mapObjShop.get("LatLng").toString();
                        String[] location = latlng.split(",");
                        mShopLatitude.put(count, location[0]);
                        mShopLongitude.put(count, location[1]);
                    }
                }

                ShopsValue shops = new ShopsValue();
                shops.setRetailShopID(mRetailShopID);
                shops.setName(mShopName);
                shops.setType(mShopType);
                shops.setImage1(mShopImage1);
                shops.setImage2(mShopImage2);
                shops.setImage3(mShopImage3);
                shops.setImage4(mShopImage4);
                shops.setImage5(mShopImage5);
                shops.setRate(mShopRate);
                shops.setOpenTime(mShopOpenTime);
                shops.setCloseTime(mShopCloseTime);
                shops.setAddress(mShopAddress);
                shops.setCode(mShopCode);
                shops.setTelephone(mShopTelephone);
                shops.setDescription(mShopDescription);
                shops.setLink(mShopLink);
                shops.setLatitude(mShopLatitude);
                shops.setLongitude(mShopLongitude);

                if (callbacks != null) {
                    callbacks.onShopsChange(shops);
                }

            }else{
                //is not value at database
                if(callbacks != null){
                    callbacks.onShopsChange(null);
                }
            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

        private void clearAll() {
            mRetailShopID.clear();
            mShopName.clear();
            mShopType.clear();
            mShopRate.clear();
            mShopCode.clear();
            mShopAddress.clear();
            mShopOpenTime.clear();
            mShopImage1.clear();
            mShopImage2.clear();
            mShopImage3.clear();
            mShopImage4.clear();
            mShopImage5.clear();
            mShopTelephone.clear();
            mShopDescription.clear();
            mShopLink.clear();
            mShopLatitude.clear();
            mShopLongitude.clear();
        }
    }

    public interface ShopsCallbacks{
        public void onShopsChange(ShopsValue shops);
    }

}
