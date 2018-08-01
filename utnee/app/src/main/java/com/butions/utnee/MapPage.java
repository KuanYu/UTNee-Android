package com.butions.utnee;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MapPage extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, ProfileFirebase.ProfileCallbacks, ShopsFirebase.ShopsCallbacks, CodeFirebase.CodesCallbacks {

    private String TAG = "MapPage";
    private String mUserID;
    private String myName;
    private String myPicture;
    private DatabaseReference mRootRef;
    private MapFragment mapFragment;
    private static GoogleMap mGoogleMap;
    private double Lat, Lng;
    private Marker marker;
    private Marker lastMarker = null;
    private ViewGroup infoWindow;
    private RelativeLayout rectangle_bubble;
    private TextView title;
    private TextView code;
    private static Marker my_marker;
    private static Bitmap my_icon_marker;
    private String deviceID;
    private String deviceName;
    private String myOnMap;
    private LatLng my_position;
    private MapWrapperLayout mapWrapperLayout;
    private static final int DEFAULT_MARGIN_BOTTOM = 2;
    private MapOnInfoWindowElemTouchListener infoButtonListenerLoadMore;
    private List<Marker> ListMarker = new ArrayList<>();
    private ImageView btnProfile, btnHome, btnShop;
    private FragmentManager transaction;
    private RelativeLayout MapRelative;
    private TextView text_utnai;
    @SuppressLint("StaticFieldLeak")
    private RelativeLayout toolbarMap;

    @SuppressLint("StaticFieldLeak")
    private RelativeLayout space_frame;
    private boolean toolbarShow;
    private LinearLayout toolbarNavigation;
    private ImageView createShop;
    private EditText searchCode;
    private ImageView btnNavigate;
    private TextView btnHideNavigate;
    private boolean showNavigate;
    private Loading objLoading;
    private RelativeLayout.LayoutParams params;
    private ActivityManagerName managerName;
    private FloatingActionButton icon_fab;
    private ProfileFirebase.ProfileListener profileListener;
    private ShopsFirebase.ShopsListener shopsListener;

    private ArrayList<String> mRetailShopID;
    private Map<Integer, String> mShopName;
    private Map<Integer, String> mShopType;
    private Map<Integer, String> mShopRate;
    private Map<Integer, String> mShopAddress;
    private Map<Integer, String> mShopOpenTime;
    private Map<Integer, String> mShopCloseTime;
    private Map<Integer, String> mShopCode;
    private Map<Integer, String> mShopImage1;
    private Map<Integer, String> mShopLatitude;
    private Map<Integer, String> mShopLongitude;
    private Map<Integer, String> mShopImage2;
    private Map<Integer, String> mShopImage3;
    private Map<Integer, String> mShopImage4;
    private Map<Integer, String> mShopImage5;
    private Map<Integer, String> mShopTelephone;
    private Map<Integer, String> mShopDescription;
    private Map<Integer, String> mShopLink;
    private TextView rate_count;
    private ImageView star1, star2, star3, star4, star5;
    private TextView type;
    private TextView location;
    private TextView time;
    private ImageView image1;
    private ProgressBar image_loading;
    private String isCode;
    private CodeFirebase.CodesListener codeListener;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        managerName = new ActivityManagerName();
        managerName.setProcessNameClass(this);
        managerName.setNameClassInProcessString("Map");

        objLoading = new Loading(this);

        Translater.getInstance().setLanguages(this);

        toolbarShow = true;
        mRootRef = FirebaseDatabase.getInstance().getReference();

        deviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        deviceName = Config.getInstance().getDeviceName();

        toolbarMap = (RelativeLayout) findViewById(R.id.toolbarMap);
        toolbarMap.setVisibility(View.VISIBLE);

        space_frame = (RelativeLayout) findViewById(R.id.space_frame);
        LinearLayout footbarMap = (LinearLayout) findViewById(R.id.footbarMap);

        text_utnai = (TextView) findViewById(R.id.text_utnai);
        text_utnai.setText(StringManager.getsInstance().getString("Utnee"));
        text_utnai.setTypeface(Config.getInstance().getDefaultFont(this), Typeface.BOLD);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        Lat = bundle.getDouble("Latitude");
        Lng = bundle.getDouble("Longitude");
        mUserID = bundle.getString("UserID");
        String mUserPhoto = bundle.getString("UserPhoto");
        int mNotification = bundle.getInt("NotificationID");

        //call profile
        profileListener = ProfileFirebase.addProfileListener(mUserID, this, this);

        //call my shop
        shopsListener = ShopsFirebase.addShopsListener(mUserID, this, this);

        MapRelative = (RelativeLayout) findViewById(R.id.Map);
        transaction = getSupportFragmentManager();

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_framelayout);
                if (fragment != null) transaction.beginTransaction().remove(fragment).commit();
                try {
                    mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.content_map);
                    mapWrapperLayout = (MapWrapperLayout) findViewById(R.id.map_relative_layout);
                    mapFragment.getMapAsync(MapPage.this);
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        });

        Initialize();
        if (mNotification != 0) {
            Log.d(TAG, "NotificationID:" + mNotification);
            checkPage();
        }


        SharedPreferences sp = getSharedPreferences("MYPROFILE", Context.MODE_PRIVATE);
        String error = "-1";
        myName = sp.getString("Name", error);
        myPicture = sp.getString("Picture", error);

    }

    private void checkPage() {
        Log.d(TAG, "checkPage:currentPage:" + managerName.getCurrentPage());
        if (managerName.getCurrentPage().equals("ProfilePage")) {
            profilePage();
        } else {
            homePage();
        }
    }

    private void Initialize() {
        showNavigate = true;
        toolbarNavigation = (LinearLayout) findViewById(R.id.toolbarNavigation);
        createShop = (ImageView) findViewById(R.id.createShop);
        searchCode = (EditText) findViewById(R.id.searchCode);
        btnNavigate = (ImageView) findViewById(R.id.btnNavigate);
        btnHideNavigate = (TextView) findViewById(R.id.btnHideNavigate);
        icon_fab = (FloatingActionButton) findViewById(R.id.icon_fab);

        btnHideNavigate.setTypeface(Config.getInstance().getDefaultFont(this));
        searchCode.setTypeface(Config.getInstance().getDefaultFont(this));

        btnHideNavigate.setText(StringManager.getsInstance().getString("HideNavigate"));
        searchCode.setHint(StringManager.getsInstance().getString("FriendCode"));
        searchCode.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        btnHideNavigate.setOnClickListener(this);
        btnNavigate.setOnClickListener(this);
        createShop.setOnClickListener(this);

        btnHome = (ImageView) findViewById(R.id.btnHome);
        btnShop = (ImageView) findViewById(R.id.btnShop);
        btnProfile = (ImageView) findViewById(R.id.btnProfile);

        icon_fab.setOnClickListener(this);
        btnHome.setOnClickListener(this);
        btnShop.setOnClickListener(this);
        btnProfile.setOnClickListener(this);
    }


    @SuppressLint("InflateParams")
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mGoogleMap = googleMap;
        //Log.d(TAG, "*** onMapReady ***");
        final UiSettings settings = googleMap.getUiSettings();
        settings.setMapToolbarEnabled(false);      //navigation bar
        settings.setZoomControlsEnabled(false);   //button zoom
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);

        my_position = new LatLng(Lat, Lng);
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        boolean isFlag = MoveToStoreLocation.getInstance().getFlag();
        if(isFlag){
            LatLng friend_position = new LatLng(MoveToStoreLocation.getInstance().getLatitude(), MoveToStoreLocation.getInstance().getLongitude());
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(friend_position, 18));
            MoveToStoreLocation.getInstance().setFlag(false);
        }else {
            boolean isFlagNavigate = MoveToNavigateWithCode.getInstance().getFlagNavigate();
            if(isFlagNavigate){
                //hide keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchCode.getWindowToken(), 0);
                //set code to editText
                isCode = MoveToNavigateWithCode.getInstance().getCode();
                searchCode.setText(isCode);

                if(!isCode.isEmpty()){
                    objLoading.loading(true);
                    codeListener = CodeFirebase.addCodesListener(this , isCode);
                }
                MoveToNavigateWithCode.getInstance().setFlagNavigate(false);
            }else {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(my_position, 18));
            }
        }
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if(marker != null){
                    marker.showInfoWindow();
                }
            }
        });
        InitializeCustomInfo();
        googleMap.setOnMyLocationChangeListener(myLocationChangeListener);
        googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());

    }

    private void InitializeCustomInfo() {
        this.infoWindow = (ViewGroup) getLayoutInflater().inflate(R.layout.custom_info_windows, null);
        this.rectangle_bubble = (RelativeLayout) infoWindow.findViewById(R.id.rectangle_bubble);
        this.title = (TextView) infoWindow.findViewById(R.id.title);
        this.rate_count = (TextView) infoWindow.findViewById(R.id.rate_count);
        this.star1 = (ImageView) infoWindow.findViewById(R.id.star1);
        this.star2 = (ImageView) infoWindow.findViewById(R.id.star2);
        this.star3 = (ImageView) infoWindow.findViewById(R.id.star3);
        this.star4 = (ImageView) infoWindow.findViewById(R.id.star4);
        this.star5 = (ImageView) infoWindow.findViewById(R.id.star5);
        this.type = (TextView) infoWindow.findViewById(R.id.type);
        this.location = (TextView) infoWindow.findViewById(R.id.text_location);
        this.time = (TextView) infoWindow.findViewById(R.id.time);
        this.code = (TextView) infoWindow.findViewById(R.id.code);
        this.image1 = (ImageView) infoWindow.findViewById(R.id.image);
        this.image_loading = (ProgressBar) infoWindow.findViewById(R.id.image_loading);
        LinearLayout infoSpaceBtnMore = (LinearLayout) infoWindow.findViewById(R.id.btnMore);
        ImageButton infoButtonMore = (ImageButton) infoWindow.findViewById(R.id.imageMore);

        //setFont
        this.title.setTypeface(Config.getInstance().getDefaultFont(this));
        this.code.setTypeface(Config.getInstance().getDefaultFont(this));
        this.location.setTypeface(Config.getInstance().getDefaultFont(this));
        this.time.setTypeface(Config.getInstance().getDefaultFont(this));
        this.rate_count.setTypeface(Config.getInstance().getDefaultFont(this));

        this.infoButtonListenerLoadMore = new MapOnInfoWindowElemTouchListener(infoSpaceBtnMore,
                getResources().getDrawable(R.drawable.infowindows3),
                getResources().getDrawable(R.drawable.infowindows4)) {
            @Override
            protected void onClickConfirmed(View v, Marker marker) {
                //load more
                Log.d(TAG, "getData : " + marker.getTitle());
                String shopID = marker.getSnippet();
                String[] getData = marker.getTitle().split("/&");
                String isShopName = getData[0];
                String isShopType = getData[1];
                String isShopRate = getData[2];
                String isShopAddress = getData[3];
                String isShopOpenTime = getData[4];
                String isShopCloseTime = getData[5];
                String isShopCode = getData[6];
                String isShopImage1 = getData[7];
                String isShopLatitude = getData[8];
                String isShopLongitude = getData[9];
                String isShopImage2 = getData[10];
                String isShopImage3 = getData[11];
                String isShopImage4 = getData[12];
                String isShopImage5 = getData[13];
                String isShopTelephone = getData[14];
                String isShopDescription = getData[15];
                String isShopLink = getData[16];


                Intent intent = new Intent(MapPage.this, ShopDetailPage.class);
                intent.putExtra("UserID", mUserID);
                intent.putExtra("mRetailShopID", shopID);
                intent.putExtra("mShopName", isShopName);
                intent.putExtra("mShopType", isShopType);
                intent.putExtra("mShopRate", isShopRate);
                intent.putExtra("mShopAddress", isShopAddress);
                intent.putExtra("mShopOpenTime", isShopOpenTime);
                intent.putExtra("mShopCloseTime", isShopCloseTime);
                intent.putExtra("mShopCode", isShopCode);
                intent.putExtra("mShopImage1", isShopImage1);
                intent.putExtra("mShopImage2", isShopImage2);
                intent.putExtra("mShopImage3", isShopImage3);
                intent.putExtra("mShopImage4", isShopImage4);
                intent.putExtra("mShopImage5", isShopImage5);
                intent.putExtra("mShopTelephone", isShopTelephone);
                intent.putExtra("mShopDescription", isShopDescription);
                intent.putExtra("mShopLink", isShopLink);
                intent.putExtra("mShopLatitude", isShopLongitude);
                intent.putExtra("mShopLongitude", isShopLatitude);
                startActivity(intent);
            }
        };
        infoButtonMore.setOnTouchListener(infoButtonListenerLoadMore);
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(android.location.Location location) {
            my_position = new LatLng(location.getLatitude(), location.getLongitude());
            Log.d(TAG,"latitude :" + location.getLatitude());
            Log.d(TAG,"longitude :" + location.getLongitude());

            SharedPreferences sp = getSharedPreferences("MYPROFILE", Context.MODE_PRIVATE);
            double sp_lat = Double.parseDouble(sp.getString("Latitude", "-1"));
            double sp_lng = Double.parseDouble(sp.getString("Longitude", "-1"));

            if(location.getLatitude() != sp_lat || location.getLongitude() != sp_lng){
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("Latitude", String.valueOf(location.getLatitude()));
                editor.putString("Longitude", String.valueOf(location.getLongitude()));
                editor.apply();

                UpdateLocation(location.getLatitude(), location.getLongitude());
            }
        }
    };

    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        CustomInfoWindowAdapter() {

        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }

        @Override
        public View getInfoWindow(final Marker marker) {
                //Log.d(TAG, "id marker : " + marker.getId());
            if(marker.getSnippet() != null) {
                if (lastMarker == null || !lastMarker.getId().equals(marker.getId())) {
                    lastMarker = marker;

                    //set Data Info
                    String shopID = marker.getSnippet();
                    String[] getData = marker.getTitle().split("/&");
                    String isShopName = getData[0];
                    String isShopType = getData[1];
                    String isShopRate = getData[2];
                    String isShopAddress = getData[3];
                    String isShopOpenTime = getData[4];
                    String isShopCloseTime = getData[5];
                    String isShopCode = getData[6];
                    String isShopImage1 = getData[7];

//                  set data
                    rectangle_bubble.setBackgroundResource(R.drawable.infowindows2);  //White
                    title.setText(isShopName);
                    rate_count.setText(isShopRate);
                    if(isShopRate != null) {
                        if (isShopRate.equals("1.0")) {
                            star1.setImageResource(R.drawable.ic_star);
                            star2.setImageResource(R.drawable.ic_star_grey);
                            star3.setImageResource(R.drawable.ic_star_grey);
                            star4.setImageResource(R.drawable.ic_star_grey);
                            star5.setImageResource(R.drawable.ic_star_grey);
                        } else if (isShopRate.equals("2.0")) {
                            star1.setImageResource(R.drawable.ic_star);
                            star2.setImageResource(R.drawable.ic_star);
                            star3.setImageResource(R.drawable.ic_star_grey);
                            star4.setImageResource(R.drawable.ic_star_grey);
                            star5.setImageResource(R.drawable.ic_star_grey);
                        } else if (isShopRate.equals("3.0")) {
                            star1.setImageResource(R.drawable.ic_star);
                            star2.setImageResource(R.drawable.ic_star);
                            star3.setImageResource(R.drawable.ic_star);
                            star4.setImageResource(R.drawable.ic_star_grey);
                            star5.setImageResource(R.drawable.ic_star_grey);
                        } else if (isShopRate.equals("4.0")) {
                            star1.setImageResource(R.drawable.ic_star);
                            star2.setImageResource(R.drawable.ic_star);
                            star3.setImageResource(R.drawable.ic_star);
                            star4.setImageResource(R.drawable.ic_star);
                            star5.setImageResource(R.drawable.ic_star_grey);
                        } else if (isShopRate.equals("5.0")) {
                            star1.setImageResource(R.drawable.ic_star);
                            star2.setImageResource(R.drawable.ic_star);
                            star3.setImageResource(R.drawable.ic_star);
                            star4.setImageResource(R.drawable.ic_star);
                            star5.setImageResource(R.drawable.ic_star);
                        }else{
                            star1.setImageResource(R.drawable.ic_star_grey);
                            star2.setImageResource(R.drawable.ic_star_grey);
                            star3.setImageResource(R.drawable.ic_star_grey);
                            star4.setImageResource(R.drawable.ic_star_grey);
                            star5.setImageResource(R.drawable.ic_star_grey);
                        }
                    }
                    type.setText(isShopType);
                    location.setText(isShopAddress);
                    time.setText(String.valueOf(isShopOpenTime+ " - " + isShopCloseTime));
                    code.setText(isShopCode);
                    if(isShopImage1 != null) {
                        image1.setVisibility(View.VISIBLE);
                        image_loading.setVisibility(View.VISIBLE);
                        Picasso.with(MapPage.this)
                                .load(isShopImage1)
                                .centerCrop()
                                .fit()
                                .noFade()
                                .placeholder(R.drawable.bg_circle_white)
                                .error(R.drawable.ic_account_circle)
                                .transform(new CircleTransform())
                                .into(image1, new ImageLoadedCallback(image_loading, marker));
                    }else{
                        image1.setVisibility(View.GONE);
                        image_loading.setVisibility(View.GONE);
                    }
                }

                mapWrapperLayout.init(mGoogleMap, getPixelsFromDp(getApplicationContext(), DEFAULT_MARGIN_BOTTOM));
                infoButtonListenerLoadMore.setMarker(marker);
                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
            }else{
                rectangle_bubble.setVisibility(View.GONE);
            }
            return infoWindow;
        }
    }

    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 2.5f);
    }

    private class ImageLoadedCallback implements Callback {
        Marker marker = null;

        ImageLoadedCallback(ProgressBar progBar, Marker marker) {
            this.marker = marker;
            image_loading = progBar;
        }

        @Override
        public void onSuccess() {
            if(image_loading != null){
                image_loading.setVisibility(View.GONE);
            }

            if (marker != null && marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
                marker.showInfoWindow();
            }

        }

        @Override
        public void onError() {

        }
    }

    private void UpdateLocation(Double Latitude, Double Longitude) {
        //update location into Devices
        DatabaseReference mUsersRef = mRootRef.child("Users").child(mUserID).child("Devices").child(deviceID);
        mUsersRef.child("ID").setValue(deviceID);
        mUsersRef.child("DeviceName").setValue(deviceName);
        mUsersRef.child("DeviceType").setValue("Android");
        mUsersRef.child("LatLng").setValue(Latitude +"," +Longitude);

        //update location into main
        DatabaseReference mLocationRef = mRootRef.child("Users").child(mUserID);
        mLocationRef.child("DeviceID").setValue(deviceID);
        mLocationRef.child("DeviceName").setValue(deviceName);
        mLocationRef.child("LatLng").setValue(Latitude +"," +Longitude);
    }

    private void shopPage(){
        managerName.setCurrentPage("ShopPage");
        space_frame.setFocusableInTouchMode(true);
        space_frame.setClickable(true);

        icon_fab.setVisibility(View.GONE);
        toolbarNavigation.setVisibility(View.GONE);
        btnHideNavigate.setVisibility(View.GONE);

        MapRelative.setFocusableInTouchMode(false);
        MapRelative.setClickable(false);
        MapRelative.setVisibility(View.GONE);

        toolbarShow = false;
        toolbarMap.setVisibility(View.GONE);

        btnShop.setImageResource(R.drawable.ic_shop_full);
        clearClickIcon(btnShop);

        Bundle bundle = new Bundle();
        bundle.putString("MyUserID", mUserID);
        ShopPage objShopPage = new ShopPage();
        objShopPage.setArguments(bundle);
        transaction.beginTransaction().replace(R.id.content_framelayout, objShopPage).commit();
    }

    private void homePage(){
        managerName.setCurrentPage("Map");
        space_frame.setFocusableInTouchMode(false);
        space_frame.setClickable(false);

        icon_fab.setVisibility(View.VISIBLE);
        toolbarNavigation.setVisibility(View.VISIBLE);
        btnHideNavigate.setVisibility(View.VISIBLE);

        MapRelative.setFocusableInTouchMode(true);
        MapRelative.setClickable(true);
        MapRelative.setVisibility(View.VISIBLE);

        toolbarShow = true;
        toolbarMap.setVisibility(View.VISIBLE);
        text_utnai.setText(StringManager.getsInstance().getString("Utnee"));
        searchCode.setHint(StringManager.getsInstance().getString("FriendCode"));

        btnHome.setImageResource(R.drawable.ic_home_full);
        clearClickIcon(btnHome);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_framelayout);
        if(fragment != null) transaction.beginTransaction().remove(fragment).commit();
    }

    private void profilePage(){
        managerName.setCurrentPage("ProfilePage");
        space_frame.setFocusableInTouchMode(true);
        space_frame.setClickable(true);

        MapRelative.setFocusableInTouchMode(false);
        MapRelative.setClickable(false);
        MapRelative.setVisibility(View.GONE);

        icon_fab.setVisibility(View.GONE);
        toolbarNavigation.setVisibility(View.GONE);
        btnHideNavigate.setVisibility(View.GONE);

        toolbarShow = false;
        toolbarMap.setVisibility(View.GONE);

        btnProfile.setImageResource(R.drawable.ic_person_full);
        clearClickIcon(btnProfile);

        Bundle bundle = new Bundle();
        bundle.putString("mUserID", mUserID);
        ProfilePage objProfilePage = new ProfilePage();
        objProfilePage.setArguments(bundle);
        transaction.beginTransaction().replace(R.id.content_framelayout, objProfilePage).commit();
    }

    @Override
    public void onClick(final View view) {
        if(view == btnHome){
            homePage();

        }else if(view == btnProfile){
            profilePage();

        }else if(view == btnShop){
            shopPage();

        }else if(view == createShop){
            Intent intent = new Intent(this, CreatePage.class);
            intent.putExtra("UserID", mUserID);
            startActivity(intent);
        }
        else if(view  == btnHideNavigate) {
            if (showNavigate) {
                toolbarNavigation.setVisibility(View.GONE);
                btnHideNavigate.setText(StringManager.getsInstance().getString("ShowNavigate"));
                showNavigate = false;
            } else {
                toolbarNavigation.setVisibility(View.VISIBLE);
                btnHideNavigate.setText(StringManager.getsInstance().getString("HideNavigate"));
                showNavigate = true;
            }
        }
        else if(view == icon_fab){
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(my_position, 18));
        }
        else if(view == btnNavigate){
            //hide keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchCode.getWindowToken(), 0);
            //get code from editText
            isCode = searchCode.getText().toString().toUpperCase().trim();
            if(!isCode.isEmpty()){
                objLoading.loading(true);
                codeListener = CodeFirebase.addCodesListener(this, isCode);
            }
        }
    }

    @Override
    public void onCodesChange(CodesValue codesValue) {
//        codeListener
        Log.d(TAG, "isCode : " + isCode);
        CodeFirebase.stop(codeListener);
        if(codesValue != null){
            String  codesID = codesValue.getCodes();
            String codesLat = codesValue.getCodesLat();
            String codesLng = codesValue.getCodesLng();
            //navigate
            Log.d(TAG, "Navigate Founds!");
            objLoading.loading(false);
            LatLng position_search = new LatLng(Double.parseDouble(codesLat), Double.parseDouble(codesLng));
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position_search, 18));

        }else{
            //dialog show not founds
            objLoading.loading(false);
            Log.d(TAG, "Navigate Not Founds!");
            //show dialog not founds
            final Dialog dialog_waining = new Dialog(this);
            dialog_waining.getWindow();
            dialog_waining.setCanceledOnTouchOutside(false);
            dialog_waining.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog_waining.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog_waining.setContentView(R.layout.dialog_data);

            TextView dialog_title = (TextView) dialog_waining.findViewById(R.id.dialog_title);
            dialog_title.setText(StringManager.getsInstance().getString("NoRouteFound"));

            TextView dialog_detail = (TextView) dialog_waining.findViewById(R.id.dialog_detail);
            dialog_detail.setText(StringManager.getsInstance().getString("CheckCode"));

            Button buttonOK = (Button) dialog_waining.findViewById(R.id.btnOk);
            buttonOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_waining.cancel();
                    searchCode.setText("");
                }
            });

            dialog_waining.show();

        }
    }

    private void clearClickIcon(ImageView view) {
        if(view == btnHome){
            btnProfile.setImageResource(R.drawable.ic_person);
            btnShop.setImageResource(R.drawable.ic_shop);

        }else if(view == btnShop){
            btnProfile.setImageResource(R.drawable.ic_person);
            btnHome.setImageResource(R.drawable.ic_home);

        }else if(view == btnProfile){
            btnShop.setImageResource(R.drawable.ic_shop);
            btnHome.setImageResource(R.drawable.ic_home);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "***onResume***");
        //translate
        Translater.getInstance().setLanguages(this);
        
        ActivityManagerName managerName = new ActivityManagerName();
        managerName.setProcessNameClass(this);
        managerName.setNameClassInProcessString("Map");
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.content_map);
        mapFragment.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "***onPause***");
        ProfileFirebase.stop(profileListener);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.content_map);
        mapFragment.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        ListMarker.clear();
        Log.d(TAG, "***onDestroy***");
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.content_map);
        mapFragment.onLowMemory();
        super.onLowMemory();
    }

    @Override
    public void onProfileChange(ProfileValue profile) {
        //profileListener
        btnShop.setClickable(true);
        Map<String, String> myProfile = profile.getMyProfile();

    }

    @Override
    public void onShopsChange(ShopsValue shops) {
        // shopsListener
        if(shops != null) {
            mRetailShopID = shops.getRetailShopID();
            mShopName = shops.getName();
            mShopType = shops.getType();
            mShopRate = shops.getRate();
            mShopAddress = shops.getAddress();
            mShopOpenTime = shops.getOpenTime();
            mShopCloseTime = shops.getCloseTime();
            mShopCode = shops.getCode();
            mShopImage1 = shops.getImage1();
            mShopImage2 = shops.getImage2();
            mShopImage3 = shops.getImage3();
            mShopImage4 = shops.getImage4();
            mShopImage5 = shops.getImage5();
            mShopTelephone = shops.getTelephone();
            mShopLatitude = shops.getLatitude();
            mShopLongitude = shops.getLongitude();
            mShopDescription = shops.getDescription();
            mShopLink = shops.getLink();

            onMarkerChangeMyFriends();
        }
    }

    private void onMarkerChangeMyFriends() {
        for(int i=0; i<mRetailShopID.size(); i++){
            String data = mRetailShopID.get(i) + "/&" + mShopName.get(i) + "/&" + mShopType.get(i) + "/&" + mShopRate.get(i) + "/&" + mShopAddress.get(i) + "/&" + mShopOpenTime.get(i) + "/&" + mShopCloseTime.get(i) + "/&" + mShopCode.get(i) + "/&" + mShopImage1.get(i) + "/&" + mShopLatitude.get(i) + "/&" + mShopLongitude.get(i) + "/&" + mShopImage2.get(i) + "/&" + mShopImage3.get(i) + "/&" + mShopImage4.get(i) + "/&" + mShopImage5.get(i) + "/&" + mShopTelephone.get(i) + "/&" + mShopDescription.get(i) + "/&" + mShopLink.get(i);
            new BitmapFromUrl().execute(data);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class BitmapFromUrl extends AsyncTask<String, Bitmap, Bitmap> {
        Bitmap bitmap = null;
        InputStream input;
        HttpURLConnection connection = null;
        String isShopID;
        String isShopName;
        String isShopType;
        String isShopRate;
        String isShopAddress;
        String isShopOpenTime;
        String isShopCloseTime;
        String isShopCode;
        String isShopImage1;
        String isShopLatitude;
        String isShopLongitude;
        String isShopImage2;
        String isShopImage3;
        String isShopImage4;
        String isShopImage5;
        String isShopTelephone;
        String isShopDescription;
        String isShopLink;

        @Override
        protected Bitmap doInBackground(String... data) {
            try {
                String isData = data[0];
                String[] getData = isData.split("/&");
                isShopID = getData[0];
                isShopName = getData[1];
                isShopType = getData[2];
                isShopRate = getData[3];
                isShopAddress = getData[4];
                isShopOpenTime = getData[5];
                isShopCloseTime = getData[6];
                isShopCode = getData[7];
                isShopImage1 = getData[8];
                isShopLatitude = getData[9];
                isShopLongitude = getData[10];
                isShopImage2 = getData[11];
                isShopImage3 = getData[12];
                isShopImage4 = getData[13];
                isShopImage5 = getData[14];
                isShopTelephone = getData[15];
                isShopDescription = getData[16];
                isShopLink = getData[17];

                URL url = new URL(isShopImage1);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                input = new BufferedInputStream(connection.getInputStream());

                //read image from url
                bitmap = BitmapFactory.decodeStream(input);
                return DrawableMarker.getInstance().drawableFromUrl(bitmap, MapPage.this);

            }catch (Exception e){
                e.getMessage();
            }finally {
                if(connection != null){
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            BitmapDescriptor bmp = BitmapDescriptorFactory.fromBitmap(bitmap);
            double doulat = Double.parseDouble(isShopLatitude);
            double doulng = Double.parseDouble(isShopLongitude);
            LatLng isPosition = new LatLng(doulat , doulng);
            //check device id before mark lat,lng (case: location change)
            try {
                hideMarkerFriend(isShopID);
                MarkerOptions marker_option = new MarkerOptions()
                        .position(isPosition)
                        .title(isShopName + "/&" + isShopType + "/&" + isShopRate + "/&" + isShopAddress + "/&" + isShopOpenTime + "/&" + isShopCloseTime + "/&" + isShopCode + "/&" + isShopImage1 + "/&" + isShopLatitude + "/&" + isShopLongitude + "/&" + isShopImage2 + "/&" + isShopImage3 + "/&" + isShopImage4 + "/&" + isShopImage5 + "/&" + isShopTelephone + "/&" + isShopDescription + "/&" + isShopLink)
                        .snippet(isShopID)
                        .icon(bmp);
                marker = mGoogleMap.addMarker(marker_option);
                ListMarker.add(marker);

            }catch (Exception e){
                Log.d(TAG, "ERE : " + e.getMessage());
            }
        }
    }

    private void hideMarkerFriend(String shopID) {
        //hide all marker, it had shopID
        for(int i=0; i<ListMarker.size(); i++) {
            Marker isMarker = ListMarker.get(i);
            String[] mSnippet = isMarker.getSnippet().split("/&");
            String getSnippetFacebookId = mSnippet[0];
            if (getSnippetFacebookId.equals(shopID)) {
                isMarker.remove();
            }
        }
    }

}
