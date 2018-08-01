package com.butions.utnee;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Map;

/**
 * Created by Chalitta Khampachua on 19-Jul-17.
 */

public class ProfilePage extends Fragment implements View.OnClickListener, ProfileFirebase.ProfileCallbacks {

    private String TAG = "MyProfile";
    private ImageView imageProfile;
    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private static String mUserID;
    private TextView btnName, btnBirthDay, btnCity, btnEmail, btnPhone, btnGender;
    private EditText btnEditPhone;
    private ImageView btnSave;
    private boolean isFrist;
    private int isYear, isMonth, isDay;
    private String birthDate;
    private ProfileFirebase.ProfileListener profileListener;
    private Map<String, String > myProfile;
    private Context context;
    private View v;
    private ImageView btnSetting;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.profile, container, false);
        context = getActivity();
        ActivityManagerName managerName = new ActivityManagerName();
        managerName.setProcessNameClass(context);
        managerName.setNameClassInProcessString("Profile");

        Translater.getInstance().setLanguages(context);

        isFrist = true;
        mUserID =  getArguments().getString("mUserID");

        //call profile
        profileListener = ProfileFirebase.addProfileListener(mUserID, this, context);

        InitializeMyProfile();

        return v;

    }

    @Override
    public void onProfileChange(ProfileValue profile) {
        myProfile = profile.getMyProfile();
        setDataProfile();
    }

    private void InitializeMyProfile() {
        TextView textProfile = (TextView) v.findViewById(R.id.textProfile);
        imageProfile = (ImageView) v.findViewById(R.id.imageProfile);
        btnName = (TextView) v.findViewById(R.id.btnName);
        btnBirthDay = (TextView) v.findViewById(R.id.btnBirthDay);
        btnCity = (TextView) v.findViewById(R.id.btnCity);
        btnEmail = (TextView) v.findViewById(R.id.btnEmail);
        btnPhone = (TextView) v.findViewById(R.id.btnPhone);
        btnGender = (TextView) v.findViewById(R.id.btnGender);
        btnSave = (ImageView) v.findViewById(R.id.btnSave);
        btnSetting = (ImageView) v.findViewById(R.id.btnSetting);
        btnEditPhone = (EditText) v.findViewById(R.id.btnEditPhone);
        btnEditPhone.setVisibility(View.GONE);

        //setFont
        textProfile.setText(StringManager.getsInstance().getString("Profile"));
        textProfile.setTypeface(Config.getInstance().getDefaultFont(context), Typeface.BOLD);
        btnName.setTypeface(Config.getInstance().getDefaultFont(context));
        btnBirthDay.setTypeface(Config.getInstance().getDefaultFont(context));
        btnCity.setTypeface(Config.getInstance().getDefaultFont(context));
        btnEmail.setTypeface(Config.getInstance().getDefaultFont(context));
        btnPhone.setTypeface(Config.getInstance().getDefaultFont(context));
        btnGender.setTypeface(Config.getInstance().getDefaultFont(context));
        btnEditPhone.setTypeface(Config.getInstance().getDefaultFont(context));

        btnPhone.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnBirthDay.setOnClickListener(this);
        btnSetting.setOnClickListener(this);
    }


    private void setDataProfile() {
        Picasso.with(context)
                .load(myProfile.get("Picture"))
                .centerCrop()
                .fit()
                .placeholder(R.drawable.bg_circle_white)
                .error(R.drawable.ic_account_circle)
                .transform(new CircleTransform())
                .into(imageProfile);

        btnName.setText(myProfile.get("Name"));
        if (myProfile.containsKey("Birthday") && !myProfile.get("Birthday").trim().isEmpty()) {
            convertDate(myProfile.get("Birthday"));
            btnBirthDay.setText(myProfile.get("Birthday"));
            btnBirthDay.setTextColor(getResources().getColor(R.color.black));
        } else {
            btnBirthDay.setText(StringManager.getsInstance().getString("HintBirthday"));
            btnBirthDay.setTextColor(getResources().getColor(R.color.grey));
        }
        btnCity.setText(myProfile.get("Address"));
        btnEmail.setText(myProfile.get("Email"));
        if(myProfile.containsKey("Phone") && !myProfile.get("Phone").trim().isEmpty()){
            btnEditPhone.setVisibility(View.GONE);
            btnPhone.setVisibility(View.VISIBLE);
            btnPhone.setText(myProfile.get("Phone"));
        }else {
            btnPhone.setVisibility(View.GONE);
            btnEditPhone.setVisibility(View.VISIBLE);
            btnEditPhone.setHint(StringManager.getsInstance().getString("HintPhone"));
        }
        btnGender.setText(myProfile.get("Gender"));
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "***onStop***");
        ProfileFirebase.stop(profileListener);
    }

    @Override
    public void onClick(View view) {
        if(view == btnPhone){
            btnPhone.setVisibility(View.GONE);
            btnEditPhone.setVisibility(View.VISIBLE);
            btnEditPhone.clearFocus();
            btnEditPhone.setCursorVisible(true);
        }else if(view == btnSave){
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnSave.setBackgroundResource(R.color.transparent);
                }
            },2000);
            String dataPhone = btnEditPhone.getText().toString();
            DatabaseReference mUsersRef = mRootRef.child("Users").child(mUserID);
            if(!dataPhone.equals("")) {
                mUsersRef.child("Phone").setValue(dataPhone);
            }
            if(!birthDate.equals("")) {
                mUsersRef.child("Birthday").setValue(birthDate);
            }
            btnEditPhone.setVisibility(View.GONE);
            btnPhone.setVisibility(View.VISIBLE);
            btnPhone.setText(dataPhone);
        }else if(view == btnBirthDay){
            DialogdatePicker();
        }else if(view == btnSetting){
            Intent intent = new Intent(context, SettingPage.class);
            intent.putExtra("mUserID", mUserID);
            intent.putExtra("mDeviceID", myProfile.get("DeviceID"));
            startActivity(intent);
        }
    }

    private void DialogdatePicker(){
        if(isFrist){
            Log.d(TAG, "*** isMonth-1: " + (isMonth-1) + "***");
            isMonth = isMonth-1;
            isFrist = false;
        }
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, R.style.Theme_DatePickerDialog, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                showDate(year, monthOfYear+1, dayOfMonth);
                isMonth = monthOfYear;
                isYear = year;
                isDay = dayOfMonth;
            }
        },isYear,isMonth,isDay);
        datePickerDialog.show();
    }

    private void showDate(int year, int month, int day) {
        btnBirthDay.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));

        birthDate =  day + "/" + month + "/" + year;
        Log.d(TAG,"BirthDate : " + birthDate);
    }

    private boolean convertDate(String birthDate) {
        if(!birthDate.equals("null") && !birthDate.equals("")) {
            String part[] = birthDate.split("/");
            isDay = Integer.parseInt(part[0]);
            isMonth = Integer.parseInt(part[1]);
            isYear = Integer.parseInt(part[2]);
            showDate(isYear, isMonth, isDay);
            return true;
        }
        else{
            java.util.Calendar now = java.util.Calendar.getInstance();
            isYear = now.get(java.util.Calendar.YEAR);
            isMonth = now.get(java.util.Calendar.MONTH) + 1; // Note: zero based!
            isDay = now.get(java.util.Calendar.DAY_OF_MONTH);
            showDate(isYear, isMonth, isDay);
            return false;
        }
    }
}
