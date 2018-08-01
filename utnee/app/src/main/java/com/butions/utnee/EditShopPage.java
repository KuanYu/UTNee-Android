package com.butions.utnee;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightGridView;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Chalitta Khampachua on 22-Dec-17.
 */

public class EditShopPage extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "EditShopPage";
    private String mShopName;
    private String mShopType;
    private String mShopRate;
    private String mShopAddress;
    private String mShopOpenTime;
    private String mShopCloseTime;
    private String mShopCode;
    private String mShopTelephone;
    private String mShopDescription;
    private String mShopLink;
    private String[] arrayImage;
    private ImageView[] arrayImageView;
    private DatabaseReference mRootRef;
    private String shopID;
    private ArrayList<String> listTypes = new ArrayList<>();
    private ImageView btnBack;
    private TextView textCode;
    private TextView textInputCode;
    private TextView textShopName;
    private TextView textInputShopName;
    private TextView textAddress;
    private TextView textInputAddress;
    private TextView textTel;
    private EditText textInputTel;
    private TextView textOpenTime;
    private TextView textInputOpen;
    private TextView textCloseTime;
    private TextView textInputClose;
    private TextView textType;
    private TextView textInputType;
    private TextView textRate;
    private ImageView star1;
    private ImageView star2;
    private ImageView star3;
    private ImageView star4;
    private ImageView star5;
    private TextView rate_count;
    private TextView textImage;
    private ImageView image1;
    private ImageView image2;
    private ImageView image3;
    private ImageView image4;
    private ImageView image5;
    private TextView btnConfirm;
    private TextView textShopDescription;
    private EditText textInputDescription;
    private TextView textShopLink;
    private TextView textInputShopLink;
    private Loading objMyLoading;
    private static int PLACE_CHANGE_REQUEST_CODE = 99;
    private String userID;
    private ImageAdapter adapter_gallery;
    private ArrayList<String> images;
    private int w;
    private int h;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_shop);

        objMyLoading = new Loading(this);
        mRootRef = FirebaseDatabase.getInstance().getReference();

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        userID = bundle.getString("UserID");
        shopID = bundle.getString("mRetailShopID");
        mShopName = bundle.getString("mShopName");
        mShopType = bundle.getString("mShopType");
        mShopRate = bundle.getString("mShopRate");
        mShopAddress = bundle.getString("mShopAddress");
        mShopOpenTime = bundle.getString("mShopOpenTime");
        mShopCloseTime = bundle.getString("mShopCloseTime");
        mShopCode = bundle.getString("mShopCode");
        mShopTelephone = bundle.getString("mShopTelephone");
        mShopDescription = bundle.getString("mShopDescription");
        mShopLink = bundle.getString("mShopLink");
        String mShopImage1 = bundle.getString("mShopImage1");
        String mShopImage2 = bundle.getString("mShopImage2");
        String mShopImage3 = bundle.getString("mShopImage3");
        String mShopImage4 = bundle.getString("mShopImage4");
        String mShopImage5 = bundle.getString("mShopImage5");

        arrayImage = new String[]{mShopImage1, mShopImage2, mShopImage3, mShopImage4, mShopImage5};

        CallListCategorys();
        InitializeEdit();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter_gallery = new ImageAdapter((Activity) this);
        float m =  Config.getInstance().getDisplayDensity(this);
        w = (int) (210 * m);
        h = (int) (210 * m);

    }

    private void InitializeEdit() {
        TextView textCreate = (TextView) findViewById(R.id.textEdit);
        textCreate.setText(StringManager.getsInstance().getString("EditShop"));
        textCreate.setTypeface(Config.getInstance().getDefaultFont(this), Typeface.BOLD);

        btnBack = (ImageView) findViewById(R.id.btnBack);
        textCode = (TextView) findViewById(R.id.textCode);
        textInputCode = (TextView) findViewById(R.id.textInputCode);
        textShopName = (TextView) findViewById(R.id.textShopName);
        textInputShopName = (EditText) findViewById(R.id.textInputShopName);
        textAddress = (TextView) findViewById(R.id.textAddress);
        textInputAddress = (TextView) findViewById(R.id.textInputAddress);
        textTel = (TextView) findViewById(R.id.textTel);
        textInputTel = (EditText) findViewById(R.id.textInputTel);
        textOpenTime = (TextView) findViewById(R.id.textOpenTime);
        textInputOpen = (TextView) findViewById(R.id.textInputOpen);
        textCloseTime = (TextView) findViewById(R.id.textCloseTime);
        textInputClose = (TextView) findViewById(R.id.textInputClose);
        textType = (TextView) findViewById(R.id.textType);
        textInputType = (TextView) findViewById(R.id.textInputType);
        textRate = (TextView) findViewById(R.id.textRate);
        star1 = (ImageView) findViewById(R.id.star1);
        star2 = (ImageView) findViewById(R.id.star2);
        star3 = (ImageView) findViewById(R.id.star3);
        star4 = (ImageView) findViewById(R.id.star4);
        star5 = (ImageView) findViewById(R.id.star5);
        rate_count = (TextView) findViewById(R.id.rate_count);
        textImage = (TextView) findViewById(R.id.textImage);
        image1 = (ImageView) findViewById(R.id.image1);
        image2 = (ImageView) findViewById(R.id.image2);
        image3 = (ImageView) findViewById(R.id.image3);
        image4 = (ImageView) findViewById(R.id.image4);
        image5 = (ImageView) findViewById(R.id.image5);
        btnConfirm = (TextView) findViewById(R.id.btnConfirm);
        textShopDescription = (TextView) findViewById(R.id.textShopDescription);
        textInputDescription = (EditText) findViewById(R.id.textInputDescription);
        textShopLink = (TextView) findViewById(R.id.textShopLink);
        textInputShopLink = (EditText) findViewById(R.id.textInputShopLink);

        arrayImageView = new ImageView[]{image1, image2, image3, image4, image5};

        textCode.setTypeface(Config.getInstance().getDefaultFont(this));
        textInputCode.setTypeface(Config.getInstance().getDefaultFont(this));
        textShopName.setTypeface(Config.getInstance().getDefaultFont(this));
        textInputShopName.setTypeface(Config.getInstance().getDefaultFont(this));
        textAddress.setTypeface(Config.getInstance().getDefaultFont(this));
        textInputAddress.setTypeface(Config.getInstance().getDefaultFont(this));
        textTel.setTypeface(Config.getInstance().getDefaultFont(this));
        textInputTel.setTypeface(Config.getInstance().getDefaultFont(this));
        textOpenTime.setTypeface(Config.getInstance().getDefaultFont(this));
        textInputOpen.setTypeface(Config.getInstance().getDefaultFont(this));
        textCloseTime.setTypeface(Config.getInstance().getDefaultFont(this));
        textInputClose.setTypeface(Config.getInstance().getDefaultFont(this));
        textType.setTypeface(Config.getInstance().getDefaultFont(this));
        textInputType.setTypeface(Config.getInstance().getDefaultFont(this));
        textRate.setTypeface(Config.getInstance().getDefaultFont(this));
        rate_count.setTypeface(Config.getInstance().getDefaultFont(this));
        textImage.setTypeface(Config.getInstance().getDefaultFont(this));
        btnConfirm.setTypeface(Config.getInstance().getDefaultFont(this));
        textShopDescription.setTypeface(Config.getInstance().getDefaultFont(this));
        textInputDescription.setTypeface(Config.getInstance().getDefaultFont(this));
        textShopLink.setTypeface(Config.getInstance().getDefaultFont(this));
        textInputShopLink.setTypeface(Config.getInstance().getDefaultFont(this));

        setFonts();
        setData();

        btnBack.setOnClickListener(this);
        textInputOpen.setOnClickListener(this);
        textInputClose.setOnClickListener(this);
        textInputType.setOnClickListener(this);
        textInputTel.setOnClickListener(this);
        textInputShopName.setOnClickListener(this);
        star1.setOnClickListener(this);
        star2.setOnClickListener(this);
        star3.setOnClickListener(this);
        star4.setOnClickListener(this);
        star5.setOnClickListener(this);
        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        image3.setOnClickListener(this);
        image4.setOnClickListener(this);
        image5.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        textInputAddress.setOnClickListener(this);
    }

    private void setData() {
        textInputCode.setText(mShopCode);
        textInputShopName.setText(mShopName);
        textInputAddress.setText(mShopAddress);
        textInputTel.setText(mShopTelephone);
        textInputOpen.setText(mShopOpenTime);
        textInputClose.setText(mShopCloseTime);
        textInputType.setText(mShopType);
        textInputDescription.setText(mShopDescription);
        textInputShopLink.setText(mShopLink);
        rate_count.setText(mShopRate);
        checkRate();
        setImage();
    }

    private void CheckIndex(String mShopType) {
        for(int i=0; i<listTypes.size(); i++){
            if(StringManager.getsInstance().getString(listTypes.get(i)).equals(mShopType)){
                textInputType.setTag(i);
            }
        }
    }

    private void setImage() {
        for(int i=0; i<arrayImage.length; i++){
            arrayImageView[i].setTag(arrayImage[i]);
            Picasso.with(this)
                    .load(arrayImage[i])
                    .centerCrop()
                    .fit()
                    .noFade()
                    .error(R.color.black)
                    .placeholder(R.color.white)
                    .into(arrayImageView[i]);
        }
    }

    private void setFonts(){
        textCode.setText(StringManager.getsInstance().getString("Code"));
        textShopName.setText(StringManager.getsInstance().getString("Name"));
        textAddress.setText(StringManager.getsInstance().getString("Address"));
        textTel.setText(StringManager.getsInstance().getString("Tel"));
        textOpenTime.setText(StringManager.getsInstance().getString("OpenTime"));
        textCloseTime.setText(StringManager.getsInstance().getString("CloseTime"));
        textType.setText(StringManager.getsInstance().getString("Types"));
        textRate.setText(StringManager.getsInstance().getString("Rate"));
        textImage.setText(StringManager.getsInstance().getString("Image"));
        btnConfirm.setText(StringManager.getsInstance().getString("Confirm"));
        textShopDescription.setText(StringManager.getsInstance().getString("Descriptions"));
        textShopLink.setText(StringManager.getsInstance().getString("Link"));
    }

    private void checkRate() {
        if (mShopRate.equals("1.0")) {
            star1.setImageResource(R.drawable.ic_star);
            star2.setImageResource(R.drawable.ic_star_grey);
            star3.setImageResource(R.drawable.ic_star_grey);
            star4.setImageResource(R.drawable.ic_star_grey);
            star5.setImageResource(R.drawable.ic_star_grey);
        } else if (mShopRate.equals("2.0")) {
            star1.setImageResource(R.drawable.ic_star);
            star2.setImageResource(R.drawable.ic_star);
            star3.setImageResource(R.drawable.ic_star_grey);
            star4.setImageResource(R.drawable.ic_star_grey);
            star5.setImageResource(R.drawable.ic_star_grey);
        } else if (mShopRate.equals("3.0")) {
            star1.setImageResource(R.drawable.ic_star);
            star2.setImageResource(R.drawable.ic_star);
            star3.setImageResource(R.drawable.ic_star);
            star4.setImageResource(R.drawable.ic_star_grey);
            star5.setImageResource(R.drawable.ic_star_grey);
        } else if (mShopRate.equals("4.0")) {
            star1.setImageResource(R.drawable.ic_star);
            star2.setImageResource(R.drawable.ic_star);
            star3.setImageResource(R.drawable.ic_star);
            star4.setImageResource(R.drawable.ic_star);
            star5.setImageResource(R.drawable.ic_star_grey);
        } else if (mShopRate.equals("5.0")) {
            star1.setImageResource(R.drawable.ic_star);
            star2.setImageResource(R.drawable.ic_star);
            star3.setImageResource(R.drawable.ic_star);
            star4.setImageResource(R.drawable.ic_star);
            star5.setImageResource(R.drawable.ic_star);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == btnBack){
            dialogConfirmExit();
        }
        else if(v == textInputShopName){
            textInputTel.clearFocus();
            textInputShopName.setFocusable(true);
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.showSoftInput(textInputShopName, InputMethodManager.SHOW_IMPLICIT);
        }
        else if(v == textInputAddress){
            // change location
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    objMyLoading.loading(true);
                    onPickButtonClick(PLACE_CHANGE_REQUEST_CODE);
                }
            });
        }
        else if(v == textInputTel){
            textInputShopName.clearFocus();
            textInputTel.setFocusable(true);
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.showSoftInput(textInputTel, InputMethodManager.SHOW_IMPLICIT);
        }
        else if(v == textInputOpen){
            dialogTimePicker(textInputOpen);
        }
        else if(v == textInputClose){
            dialogTimePicker(textInputClose);
        }
        else if(v == textInputType){
            dialogListType(textInputType.getTag());
        }
        else if(v == star1){
            star1.setImageResource(R.drawable.ic_star);
            star2.setImageResource(R.drawable.ic_star_grey);
            star3.setImageResource(R.drawable.ic_star_grey);
            star4.setImageResource(R.drawable.ic_star_grey);
            star5.setImageResource(R.drawable.ic_star_grey);
            rate_count.setText("1.0");
        }else if(v == star2){
            star1.setImageResource(R.drawable.ic_star);
            star2.setImageResource(R.drawable.ic_star);
            star3.setImageResource(R.drawable.ic_star_grey);
            star4.setImageResource(R.drawable.ic_star_grey);
            star5.setImageResource(R.drawable.ic_star_grey);
            rate_count.setText("2.0");
        }else if(v == star3){
            star1.setImageResource(R.drawable.ic_star);
            star2.setImageResource(R.drawable.ic_star);
            star3.setImageResource(R.drawable.ic_star);
            star4.setImageResource(R.drawable.ic_star_grey);
            star5.setImageResource(R.drawable.ic_star_grey);
            rate_count.setText("3.0");
        }else if(v == star4){
            star1.setImageResource(R.drawable.ic_star);
            star2.setImageResource(R.drawable.ic_star);
            star3.setImageResource(R.drawable.ic_star);
            star4.setImageResource(R.drawable.ic_star);
            star5.setImageResource(R.drawable.ic_star_grey);
            rate_count.setText("4.0");
        }else if(v == star5){
            star1.setImageResource(R.drawable.ic_star);
            star2.setImageResource(R.drawable.ic_star);
            star3.setImageResource(R.drawable.ic_star);
            star4.setImageResource(R.drawable.ic_star);
            star5.setImageResource(R.drawable.ic_star);
            rate_count.setText("5.0");
        }else if(v == image1){
            dialogGallery(image1);

        }else if(v == image2){
            dialogGallery(image2);

        }else if(v == image3){
            dialogGallery(image3);

        }else if(v == image4){
            dialogGallery(image4);

        }else if(v == image5){
            dialogGallery(image5);

        }else if(v == btnConfirm){
            Log.d(TAG, "Click btn confirm");
            //add data to firebase database [Shops]
            final DatabaseReference mUsersRef = mRootRef.child("Shops").child(userID).child(shopID);
            if(!textInputShopName.getText().toString().trim().isEmpty() && !textInputShopName.getText().toString().equals(mShopName)) mUsersRef.child("Name").setValue(textInputShopName.getText().toString());
            if(!textInputAddress.getText().toString().trim().isEmpty() && !textInputAddress.getText().toString().equals(mShopAddress)) mUsersRef.child("Address").setValue(textInputAddress.getText().toString());
            if(!textInputTel.getText().toString().trim().isEmpty() && !textInputTel.getText().toString().equals(mShopTelephone)) mUsersRef.child("Tel").setValue(textInputTel.getText().toString());
            if(!textInputOpen.getText().toString().trim().isEmpty() && !textOpenTime.getText().toString().equals(mShopOpenTime)) mUsersRef.child("TimeOpen").setValue(textInputOpen.getText().toString());
            if(!textCloseTime.getText().toString().trim().isEmpty() && !textCloseTime.getText().toString().equals(mShopCloseTime))mUsersRef.child("TimeClose").setValue(textInputClose.getText().toString());
            if(!textInputType.getText().toString().trim().isEmpty() && !textInputType.getText().toString().equals(mShopType)) mUsersRef.child("Type").setValue(textInputType.getText().toString());
            if(!rate_count.getText().toString().trim().isEmpty() && !rate_count.getText().toString().equals(rate_count.toString())) mUsersRef.child("Rate").setValue(rate_count.getText().toString());
            Log.d(TAG, "Description : " + textInputDescription.getText().toString());
            Log.d(TAG, "Trim Description : " + !textInputDescription.getText().toString().trim().isEmpty());
            if(!textInputDescription.getText().toString().trim().isEmpty()) mUsersRef.child("Descriptions").setValue(textInputDescription.getText().toString());
            if(!textInputShopLink.getText().toString().trim().isEmpty()) mUsersRef.child("Link").setValue(textInputShopLink.getText().toString());


            String[] imageName = {"Image1", "Image2", "Image3", "Image4", "Image5"};
            String[] imagePath = {
                    image1.getTag().toString(),
                    image2.getTag().toString(),
                    image3.getTag().toString(),
                    image4.getTag().toString(),
                    image5.getTag().toString(),
            };
            for(int i=0; i<imageName.length; i++){
                if(!imagePath[i].equals(arrayImage[i])) {
                    uploadImage(imagePath[i], imageName[i]);
                }
            }
            finish();
        }
    }

    private void CallListCategorys(){
        mRootRef.child("Category").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot objDataSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "Category :" + objDataSnapshot.getKey());
                    String key = objDataSnapshot.getKey();
                    listTypes.add(key);
                }

                CheckIndex(mShopType);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void uploadImage(String path, final String imageName) {
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpg")
                .build();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference folderRef = storageRef.child("Shops").child(userID).child(shopID);
        Uri file = Uri.fromFile(new File(path));
        StorageReference imageRef = folderRef.child(file.getLastPathSegment());


        final UploadTask mUploadTask = imageRef.putFile(file , metadata);

        mUploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, String.format("Failure: %s", exception.getMessage()));
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "Upload Image Success");
                //add data to database
                final DatabaseReference mUsersRef = mRootRef.child("Shops").child(userID).child(shopID);
                mUsersRef.child(imageName).setValue(String.valueOf(taskSnapshot.getDownloadUrl()));
                if(imageName.equals("Image5")){
                    objMyLoading.loading(false);
                    //move to location at created
                    finish();
                }

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                int progress = (int) ((100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "OnPausedListener");
            }
        });
    }

    private void dialogListType(Object tag){
        final Dialog dialog_data = new Dialog(this);
        dialog_data.getWindow();
        dialog_data.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_data.getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        dialog_data.setContentView(R.layout.dialog_spinner);
        dialog_data.getWindow().setLayout((int) (getScreenWidth(this)*0.9), (int)(getScreenHeight(this)*0.9));

        TextView dialog_title = (TextView) dialog_data.findViewById(R.id.dialog_title);
        dialog_title.setTypeface(Config.getInstance().getDefaultFont(this));
        dialog_title.setText(StringManager.getsInstance().getString("Category"));

        final ListView dialog_list_item = (ListView) dialog_data.findViewById(R.id.dialog_list_item);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listview_spinner, R.id.textView, listTypes){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View  view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(R.id.textView);
                text.setText(StringManager.getsInstance().getString(listTypes.get(position)));
                text.setTypeface(Config.getInstance().getDefaultFont(getContext()));
                return view;
            }
        };
        dialog_list_item.setAdapter(adapter);
        if(tag != null) {
            dialog_list_item.setSelectionFromTop((Integer) tag, 10);
            dialog_list_item.setItemChecked((Integer) tag,true);
        }

        TextView buttonCancel = (TextView) dialog_data.findViewById(R.id.buttonCancel);
        buttonCancel.setText(StringManager.getsInstance().getString("Cancel"));
        buttonCancel.setTypeface(Config.getInstance().getDefaultFont(this));
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_data.cancel();
            }
        });

        TextView buttonOK = (TextView) dialog_data.findViewById(R.id.buttonOK);
        buttonOK.setText(StringManager.getsInstance().getString("Ok"));
        buttonOK.setTypeface(Config.getInstance().getDefaultFont(this));
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = listTypes.get(dialog_list_item.getCheckedItemPosition());
                textInputType.setText(StringManager.getsInstance().getString(key));
                textInputType.setTag(dialog_list_item.getCheckedItemPosition());
                dialog_data.cancel();
            }
        });
        dialog_data.show();
    }

    private void dialogTimePicker(final TextView textView){
        String time = textView.getText().toString();
        String[] part_time = time.split(":");
        int mHour = Integer.parseInt(part_time[0]);
        int mMinute = Integer.parseInt(part_time[1]);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String hr, min;
                        if(hourOfDay == 0){
                            hr = "00";
                        }else{
                            if(hourOfDay < 10){
                                hr = String.valueOf("0" + hourOfDay);
                            }else {
                                hr = String.valueOf(hourOfDay);
                            }
                        }

                        if(minute == 0){
                            min = "00";
                        }else{
                            if(minute < 10){
                                min = String.valueOf("0" + minute);
                            }else {
                                min = String.valueOf(minute);
                            }
                        }
                        textView.setText(String.valueOf(hr + ":" + min));
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    private void dialogGallery(final ImageView imageView){
        final Dialog dialog_select = new Dialog(this);
        dialog_select.setCanceledOnTouchOutside(false);
        dialog_select.getWindow();
        dialog_select.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_select.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog_select.getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
        dialog_select.setContentView(R.layout.dialog_gallery);
        dialog_select.getWindow().setLayout((int) (getScreenWidth(this)*0.96), (int)(getScreenHeight(this)*0.96));

        ExpandableHeightGridView grid_gallery = (ExpandableHeightGridView) dialog_select.findViewById(R.id.gallery_gridView);
        grid_gallery.setExpanded(true);
        grid_gallery.setAdapter(adapter_gallery);
        grid_gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                dialog_select.cancel();
                imageView.setImageURI(Uri.parse(images.get(i)));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setTag(Uri.parse(images.get(i)).toString());
            }
        });

        ImageView btnClose = (ImageView) dialog_select.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_select.cancel();
            }
        });


        dialog_select.show();
    }

    public static int getScreenWidth(Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return size.x;
    }

    public static int getScreenHeight(Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return size.y;
    }

    public void onPickButtonClick(int choose) {
        // Construct an intent for the place picker
        try {
            objMyLoading.loading(false);
            PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(this);
            startActivityForResult(intent, choose);

        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            Log.d(TAG, "Error check in : " + e.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == PLACE_CHANGE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            // The user has selected a place. Extract the name and address.
            final Place place = PlacePicker.getPlace(data, this);

            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
            final LatLng latlng = place.getLatLng();

            textInputAddress.setBackgroundResource(R.drawable.bg_stoke_grey_square);
            textInputAddress.setText(address);

            ChangeLatLng(latlng.latitude, latlng.longitude);

        } else {
            objMyLoading.loading(false);
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void ChangeLatLng(double lat, double lng) {
        //[Code]
        String isCode = textInputCode.getText().toString();
        if(!isCode.trim().isEmpty()) {
            //add data to firebase database [Shops]
            final DatabaseReference mUsersRef = mRootRef.child("Shops").child(userID).child(shopID);
            mUsersRef.child("LatLng").setValue(lat +"," +lng);

            final DatabaseReference mUsersRef2 = mRootRef.child("Codes").child(isCode);
            if (lat != 0.0 && lng != 0.0) mUsersRef2.child("LatLng").setValue(lat + "," + lng);
        }else{
            Toast.makeText(this, "Change Address Fail!", Toast.LENGTH_SHORT).show();
        }
    }

    private void dialogConfirmExit(){
        final Dialog dialog_exit = new Dialog(this);
        dialog_exit.getWindow();
        dialog_exit.setCanceledOnTouchOutside(false);
        dialog_exit.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_exit.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        if(Build.VERSION.SDK_INT > 25) {
            dialog_exit.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        }else if(Build.VERSION.SDK_INT < 25 && Build.VERSION.SDK_INT != 25){
            dialog_exit.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
        }
        dialog_exit.setContentView(R.layout.dialog_choose);
        dialog_exit.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog_exit.show();
            }
        });

        TextView dialog_title = (TextView) dialog_exit.findViewById(R.id.dialog_title);
        dialog_title.setTypeface(Config.getInstance().getDefaultFont(this));
        dialog_title.setText(StringManager.getsInstance().getString("Exit"));

        TextView dialog_detail = (TextView) dialog_exit.findViewById(R.id.dialog_detail);
        dialog_detail.setTypeface(Config.getInstance().getDefaultFont(this));
        dialog_detail.setText(StringManager.getsInstance().getString("Ignored"));

        TextView buttonCancel = (TextView) dialog_exit.findViewById(R.id.btnCancel);
        buttonCancel.setTypeface(Config.getInstance().getDefaultFont(this));
        buttonCancel.setText(StringManager.getsInstance().getString("Cancel"));
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_exit.dismiss();
            }
        });

        TextView buttonOK = (TextView) dialog_exit.findViewById(R.id.btnOk);
        buttonOK.setTypeface(Config.getInstance().getDefaultFont(this));
        buttonOK.setText(StringManager.getsInstance().getString("Ok"));
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_exit.dismiss();
                finish();
            }
        });

        if (!((Activity) this).isFinishing()) {
            dialog_exit.show();
        }
    }

    private ArrayList<String> getAllShownImagesPath(Activity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = new String[] {
                MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        cursor = activity.getContentResolver().query(uri, projection, null, null, MediaStore.Images.Media._ID +" DESC");

        assert cursor != null;
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(absolutePathOfImage);
        }
        return listOfAllImages;
    }

    private class ImageAdapter extends BaseAdapter {

        private LayoutInflater mLayoutInflater;
        private Activity context;
        public ImageAdapter(Activity localContext) {
            context = localContext;
            mLayoutInflater = LayoutInflater.from(context);
            images = getAllShownImagesPath((Activity) context);
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolderGridView holder = null;
            if (convertView == null) {
                convertView =  mLayoutInflater.inflate(R.layout.gridview, parent, false);
                holder = new ViewHolderGridView();

                holder.picturesView = (ImageView) convertView.findViewById(R.id.image);
                ViewGroup.LayoutParams params = holder.picturesView.getLayoutParams();
                params.width = w;
                params.height = h;
                holder.picturesView.setLayoutParams(params);
                holder.picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolderGridView) convertView.getTag();
            }

            Glide.with(context)
                    .load(images.get(position))
                    .placeholder(R.color.black)
                    .centerCrop()
                    .into(holder.picturesView);

            return convertView;
        }
    }
    private class ViewHolderGridView{
        ImageView picturesView;
    }
}
