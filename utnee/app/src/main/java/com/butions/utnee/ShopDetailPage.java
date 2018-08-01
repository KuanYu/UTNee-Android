package com.butions.utnee;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

/**
 * Created by Chalitta Khampachua on 19-Dec-17.
 */

public class ShopDetailPage extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "ShopDetailPage";
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
    private String mShopLatitude;
    private String mShopLongitude;
    private String[] arrayImage;
    private ViewPager viewPager;
    private int current_position;
    private ImageView point1, point2, point3, point4, point5;
    private ImageView star1, star2, star3, star4, star5;
    private ImageView btnBack;
    private TextView textLink;
    private TextView textTel;
    private TextView textCode;
    private String mLat, mLng;
    private String mUserID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_detail);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        mUserID = bundle.getString("UserID");
        String mRetailShopID = bundle.getString("mRetailShopID");
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
        mShopLatitude = bundle.getString("mShopLatitude");
        mShopLongitude = bundle.getString("mShopLongitude");
        String mShopImage1 = bundle.getString("mShopImage1");
        String mShopImage2 = bundle.getString("mShopImage2");
        String mShopImage3 = bundle.getString("mShopImage3");
        String mShopImage4 = bundle.getString("mShopImage4");
        String mShopImage5 = bundle.getString("mShopImage5");

        arrayImage = new String[]{mShopImage1, mShopImage2, mShopImage3, mShopImage4, mShopImage5};

        InitializeShopDetail();

        CustomSwipeAdapter adapter = new CustomSwipeAdapter(this);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                current_position = position;
                viewPaper_prev_next(position);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        viewPager.setCurrentItem(current_position, true);
    }

    private void InitializeShopDetail() {

        SharedPreferences sp = getSharedPreferences("MYPROFILE", Context.MODE_PRIVATE);
        mLat = sp.getString("Latitude", "-1");
        mLng = sp.getString("Longitude", "-1");

        viewPager = (ViewPager) findViewById(R.id.view_pager);
        point1 = (ImageView) findViewById(R.id.point1);
        point2 = (ImageView) findViewById(R.id.point2);
        point3 = (ImageView) findViewById(R.id.point3);
        point4 = (ImageView) findViewById(R.id.point4);
        point5 = (ImageView) findViewById(R.id.point5);

        TextView textNameShop = (TextView) findViewById(R.id.textNameShop);
        TextView rate_count = (TextView) findViewById(R.id.rate_count);
        star1 = (ImageView) findViewById(R.id.star1);
        star2 = (ImageView) findViewById(R.id.star2);
        star3 = (ImageView) findViewById(R.id.star3);
        star4 = (ImageView) findViewById(R.id.star4);
        star5 = (ImageView) findViewById(R.id.star5);
        textCode = (TextView) findViewById(R.id.textCode);
        TextView textType = (TextView) findViewById(R.id.textType);
        TextView textAddress = (TextView) findViewById(R.id.textAddress);
        TextView textTime = (TextView) findViewById(R.id.textTime);
        TextView textDescription = (TextView) findViewById(R.id.textDescription);
        textLink = (TextView) findViewById(R.id.textLink);
        textTel = (TextView) findViewById(R.id.textTel);
        btnBack = (ImageView) findViewById(R.id.btnBack);

        //setFonts
        textNameShop.setTypeface(Config.getInstance().getDefaultFont(this));
        rate_count.setTypeface(Config.getInstance().getDefaultFont(this));
        textCode.setTypeface(Config.getInstance().getDefaultFont(this));
        textType.setTypeface(Config.getInstance().getDefaultFont(this));
        textAddress.setTypeface(Config.getInstance().getDefaultFont(this));
        textTime.setTypeface(Config.getInstance().getDefaultFont(this));
        textTel.setTypeface(Config.getInstance().getDefaultFont(this));
        textDescription.setTypeface(Config.getInstance().getDefaultFont(this));
        textLink.setTypeface(Config.getInstance().getDefaultFont(this));

        //check rate
        checkRate();
        //setData
        textNameShop.setText(mShopName);
        rate_count.setText(mShopRate);
        textCode.setText(mShopCode);
        textType.setText(mShopType);
        textAddress.setText(mShopAddress);
        textTime.setText(String.valueOf(mShopOpenTime + " - " + mShopCloseTime));
        textTel.setText(mShopTelephone);
        textDescription.setText(mShopDescription);
        textLink.setText(mShopLink);

        btnBack.setOnClickListener(this);
        textTel.setOnClickListener(this);
        textLink.setOnClickListener(this);
        textCode.setOnClickListener(this);
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

    private void viewPaper_prev_next(int position) {
        if (position == 0) {
            point1.setImageResource(R.drawable.ic_dot_blue);
            point2.setImageResource(R.drawable.ic_dot);
            point3.setImageResource(R.drawable.ic_dot);
            point4.setImageResource(R.drawable.ic_dot);
            point5.setImageResource(R.drawable.ic_dot);
        } else if (position == 1) {
            point1.setImageResource(R.drawable.ic_dot_blue);
            point2.setImageResource(R.drawable.ic_dot_blue);
            point3.setImageResource(R.drawable.ic_dot);
            point4.setImageResource(R.drawable.ic_dot);
            point5.setImageResource(R.drawable.ic_dot);
        } else if (position == 2) {
            point1.setImageResource(R.drawable.ic_dot_blue);
            point2.setImageResource(R.drawable.ic_dot_blue);
            point3.setImageResource(R.drawable.ic_dot_blue);
            point4.setImageResource(R.drawable.ic_dot);
            point5.setImageResource(R.drawable.ic_dot);
        } else if (position == 3) {
            point1.setImageResource(R.drawable.ic_dot_blue);
            point2.setImageResource(R.drawable.ic_dot_blue);
            point3.setImageResource(R.drawable.ic_dot_blue);
            point4.setImageResource(R.drawable.ic_dot_blue);
            point5.setImageResource(R.drawable.ic_dot);
        } else if (position == 4) {
            point1.setImageResource(R.drawable.ic_dot_blue);
            point2.setImageResource(R.drawable.ic_dot_blue);
            point3.setImageResource(R.drawable.ic_dot_blue);
            point4.setImageResource(R.drawable.ic_dot_blue);
            point5.setImageResource(R.drawable.ic_dot_blue);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            finish();
        } else if (v == textTel) {
            String phone = textTel.getText().toString();
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
            startActivity(intent);

        }else if(v == textLink){
            if(!textLink.getText().toString().trim().isEmpty()){
                String url = textLink.getText().toString();
                if(Patterns.WEB_URL.matcher(url.toLowerCase()).matches()) {
                    try {
                        String www_url, full_url;
                        if(url.contains("www")){
                            www_url = url;
                        }else{
                            www_url = "www."+url;
                        }

                        if (!url.startsWith("https://") && !url.startsWith("http://")){
                            full_url = "http://" + www_url;
                        }else{
                            full_url = www_url;
                        }

                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(full_url));
                        startActivity(i);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Invalid url!", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "Intent web view error : " + e.getMessage());
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Invalid url! Please change.", Toast.LENGTH_LONG).show();
                }
            }
        }else if(v == textCode){
            //to move position on map
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    MoveToNavigateWithCode.getInstance().setFlagNavigate(true);
                    MoveToNavigateWithCode.getInstance().setCode(mShopCode);
                    Intent intent = new Intent(ShopDetailPage.this, MapPage.class);
                    intent.putExtra("Latitude", Double.parseDouble(mLat));
                    intent.putExtra("Longitude", Double.parseDouble(mLng));
                    intent.putExtra("UserID", mUserID);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }


    public class CustomSwipeAdapter extends PagerAdapter {

        private String TAG = "CustomSwipeAdapter";
        Context context;
        LayoutInflater layoutInflater;

        public CustomSwipeAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return arrayImage.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == (LinearLayout)object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.pager_list, container , false);

            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

            Picasso.with(context)
                    .load(arrayImage[position])
                    .centerInside()
                    .fit()
                    .noFade()
                    .error(R.color.black)
                    .placeholder(R.color.white)
                    .into(imageView);

            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout)object);
        }


        public int getItemPosition(Object object){
            return POSITION_NONE;
        }
    }

}
