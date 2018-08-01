package com.butions.utnee;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ShopPage extends Fragment implements View.OnClickListener, ShopsFirebase.ShopsCallbacks {

    private String TAG = "MyFriend";
    private ListView my_list_shop;
    private CustomListViewShop adapter;
    private String mUserID;
    private Context context;
    private DatabaseReference mRootRef;
    private String mLat, mLng;
    private Loading objMyLoading;
    private String mShopsID;
    private ShopsFirebase.ShopsListener shopsListener;
    private ArrayList<String> mRetailShopID;
    private java.util.Map<Integer, String> mShopName;
    private java.util.Map<Integer, String> mShopType;
    private java.util.Map<Integer, String> mShopRate;
    private java.util.Map<Integer, String> mShopAddress;
    private java.util.Map<Integer, String> mShopOpenTime;
    private java.util.Map<Integer, String> mShopCloseTime;
    private java.util.Map<Integer, String> mShopCode;
    private java.util.Map<Integer, String> mShopImage1;
    private java.util.Map<Integer, String> mShopImage2;
    private java.util.Map<Integer, String> mShopImage3;
    private java.util.Map<Integer, String> mShopImage4;
    private java.util.Map<Integer, String> mShopImage5;
    private java.util.Map<Integer, String> mShopTelephone;
    private java.util.Map<Integer, String> mShopDescription;
    private java.util.Map<Integer, String> mShopLink;
    private java.util.Map<Integer, String> mShopLatitude;
    private java.util.Map<Integer, String> mShopLongitude;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "*** onCreateView ***");
        View v = inflater.inflate(R.layout.shop, container, false);
        ActivityManagerName managerName = new ActivityManagerName();
        managerName.setProcessNameClass(context);
        managerName.setNameClassInProcessString("ShopPage");

        Translater.getInstance().setLanguages(context);

        objMyLoading = new Loading(context);
        mRootRef = FirebaseDatabase.getInstance().getReference();

        TextView textShop = (TextView) v.findViewById(R.id.textShop);
        textShop.setText(StringManager.getsInstance().getString("MyShop"));
        textShop.setTypeface(Config.getInstance().getDefaultFont(context), Typeface.BOLD);

        mUserID = getArguments().getString("MyUserID");
        SharedPreferences sp = context.getSharedPreferences("MYPROFILE", Context.MODE_PRIVATE);
        mLat = sp.getString("Latitude", "-1");
        mLng = sp.getString("Longitude", "-1");

        adapter = new CustomListViewShop(context);
        my_list_shop = (ListView) v.findViewById(R.id.my_list_shops);
        my_list_shop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, ShopDetailPage.class);
                intent.putExtra("UserID", mUserID);
                intent.putExtra("mRetailShopID", mRetailShopID.get(position));
                intent.putExtra("mShopName", mShopName.get(position));
                intent.putExtra("mShopType", mShopType.get(position));
                intent.putExtra("mShopRate", mShopRate.get(position));
                intent.putExtra("mShopAddress", mShopAddress.get(position));
                intent.putExtra("mShopOpenTime", mShopOpenTime.get(position));
                intent.putExtra("mShopCloseTime", mShopCloseTime.get(position));
                intent.putExtra("mShopCode", mShopCode.get(position));
                intent.putExtra("mShopImage1", mShopImage1.get(position));
                intent.putExtra("mShopImage2", mShopImage2.get(position));
                intent.putExtra("mShopImage3", mShopImage3.get(position));
                intent.putExtra("mShopImage4", mShopImage4.get(position));
                intent.putExtra("mShopImage5", mShopImage5.get(position));
                intent.putExtra("mShopTelephone", mShopTelephone.get(position));
                intent.putExtra("mShopDescription", mShopDescription.get(position));
                intent.putExtra("mShopLink", mShopLink.get(position));
                intent.putExtra("mShopLatitude", mShopLatitude.get(position));
                intent.putExtra("mShopLongitude", mShopLongitude.get(position));
                startActivity(intent);
            }
        });

        //call my shops
        if(!mUserID.equals("null")) {
            shopsListener = ShopsFirebase.addShopsListener(mUserID, this, context);
        }

        return v;
    }

    @Override
    public void onShopsChange(ShopsValue shops) {
//        shopsListener
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
            mShopDescription = shops.getDescription();
            mShopLink = shops.getLink();
            mShopLatitude = shops.getLatitude();
            mShopLongitude = shops.getLongitude();

            adapter.notifyDataSetChanged();
            my_list_shop.setAdapter(adapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "*** onResume ***");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "*** onStop ***");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ShopsFirebase.stop(shopsListener);
        Log.d(TAG, "*** onDestroyView ***");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "*** onPause ***");
    }

    @Override
    public void onClick(View view) {

    }

    private class CustomListViewShop extends BaseAdapter {

        public Context mContext;
        public LayoutInflater mInflater;
        public CustomListViewShop(Context context) {
            mContext = context;
            mInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return mRetailShopID.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if(convertView == null){
                convertView = mInflater.inflate(R.layout.listview_shops_layout, null);
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.title);
                holder.image = (ImageView) convertView.findViewById(R.id.image);
                holder.rate_count = (TextView) convertView.findViewById(R.id.rate_count);
                holder.star1 = (ImageView) convertView.findViewById(R.id.star1);
                holder.star2 = (ImageView) convertView.findViewById(R.id.star2);
                holder.star3 = (ImageView) convertView.findViewById(R.id.star3);
                holder.star4 = (ImageView) convertView.findViewById(R.id.star4);
                holder.star5 = (ImageView) convertView.findViewById(R.id.star5);
                holder.type = (TextView) convertView.findViewById(R.id.type);
                holder.location = (TextView) convertView.findViewById(R.id.location);
                holder.open_time = (TextView) convertView.findViewById(R.id.open_time);
                holder.code_id = (TextView) convertView.findViewById(R.id.code_id);
                holder.edit = (TextView) convertView.findViewById(R.id.edit);


                holder.title.setTypeface(Config.getInstance().getDefaultFont(mContext), Typeface.BOLD);
                holder.rate_count.setTypeface(Config.getInstance().getDefaultFont(mContext));
                holder.type.setTypeface(Config.getInstance().getDefaultFont(mContext));
                holder.location.setTypeface(Config.getInstance().getDefaultFont(mContext));
                holder.open_time.setTypeface(Config.getInstance().getDefaultFont(mContext));
                holder.code_id.setTypeface(Config.getInstance().getDefaultFont(mContext));
                holder.edit.setTypeface(Config.getInstance().getDefaultFont(mContext));
                holder.edit.setText(StringManager.getsInstance().getString("Edit"));

                convertView.setTag(holder);
            }
            else{
                holder = (ViewHolder) convertView.getTag();
            }

            holder.title.setText(mShopName.get(i));
            holder.rate_count.setText(mShopRate.get(i));
            holder.type.setText(mShopType.get(i));
            holder.location.setText(mShopAddress.get(i));
            holder.open_time.setText(String.valueOf(mShopOpenTime.get(i)+ " - " + mShopCloseTime.get(i)));
            holder.code_id.setText(mShopCode.get(i));

            if(mShopRate.get(i) != null) {
                if (mShopRate.get(i).equals("1.0")) {
                    holder.star1.setImageResource(R.drawable.ic_star);
                    holder.star2.setImageResource(R.drawable.ic_star_grey);
                    holder.star3.setImageResource(R.drawable.ic_star_grey);
                    holder.star4.setImageResource(R.drawable.ic_star_grey);
                    holder.star5.setImageResource(R.drawable.ic_star_grey);
                } else if (mShopRate.get(i).equals("2.0")) {
                    holder.star1.setImageResource(R.drawable.ic_star);
                    holder.star2.setImageResource(R.drawable.ic_star);
                    holder.star3.setImageResource(R.drawable.ic_star_grey);
                    holder.star4.setImageResource(R.drawable.ic_star_grey);
                    holder.star5.setImageResource(R.drawable.ic_star_grey);
                } else if (mShopRate.get(i).equals("3.0")) {
                    holder.star1.setImageResource(R.drawable.ic_star);
                    holder.star2.setImageResource(R.drawable.ic_star);
                    holder.star3.setImageResource(R.drawable.ic_star);
                    holder.star4.setImageResource(R.drawable.ic_star_grey);
                    holder.star5.setImageResource(R.drawable.ic_star_grey);
                } else if (mShopRate.get(i).equals("4.0")) {
                    holder.star1.setImageResource(R.drawable.ic_star);
                    holder.star2.setImageResource(R.drawable.ic_star);
                    holder.star3.setImageResource(R.drawable.ic_star);
                    holder.star4.setImageResource(R.drawable.ic_star);
                    holder.star5.setImageResource(R.drawable.ic_star_grey);
                } else if (mShopRate.get(i).equals("5.0")) {
                    holder.star1.setImageResource(R.drawable.ic_star);
                    holder.star2.setImageResource(R.drawable.ic_star);
                    holder.star3.setImageResource(R.drawable.ic_star);
                    holder.star4.setImageResource(R.drawable.ic_star);
                    holder.star5.setImageResource(R.drawable.ic_star);
                }
            }

            Picasso.with(mContext)
                    .load(mShopImage1.get(i))
                    .centerCrop()
                    .fit()
                    .placeholder(R.drawable.bg_circle_white)
                    .error(R.drawable.ic_account_circle)
                    .transform(new CircleTransform())
                    .into(holder.image);

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Edit Data For Shop
                    Intent intent = new Intent(context, EditShopPage.class);
                    intent.putExtra("UserID", mUserID);
                    intent.putExtra("mRetailShopID", mRetailShopID.get(i));
                    intent.putExtra("mShopName", mShopName.get(i));
                    intent.putExtra("mShopType", mShopType.get(i));
                    intent.putExtra("mShopRate", mShopRate.get(i));
                    intent.putExtra("mShopAddress", mShopAddress.get(i));
                    intent.putExtra("mShopOpenTime", mShopOpenTime.get(i));
                    intent.putExtra("mShopCloseTime", mShopCloseTime.get(i));
                    intent.putExtra("mShopCode", mShopCode.get(i));
                    intent.putExtra("mShopImage1", mShopImage1.get(i));
                    intent.putExtra("mShopImage2", mShopImage2.get(i));
                    intent.putExtra("mShopImage3", mShopImage3.get(i));
                    intent.putExtra("mShopImage4", mShopImage4.get(i));
                    intent.putExtra("mShopImage5", mShopImage5.get(i));
                    intent.putExtra("mShopTelephone", mShopTelephone.get(i));
                    intent.putExtra("mShopDescription", mShopDescription.get(i));
                    intent.putExtra("mShopLink", mShopLink.get(i));
                    startActivity(intent);
                }
            });

            holder.code_id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //to move position on map
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            MoveToNavigateWithCode.getInstance().setFlagNavigate(true);
                            MoveToNavigateWithCode.getInstance().setCode(mShopCode.get(i));
                            Intent intent = new Intent(context, MapPage.class);
                            intent.putExtra("Latitude", Double.parseDouble(mLat));
                            intent.putExtra("Longitude", Double.parseDouble(mLng));
                            intent.putExtra("UserID", mUserID);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    });
                }
            });

            return convertView;
        }
    }

    private class ViewHolder{
        TextView title;
        ImageView image;
        TextView rate_count;
        ImageView star1;
        ImageView star2;
        ImageView star3;
        ImageView star4;
        ImageView star5;
        TextView type;
        TextView location;
        TextView open_time;
        TextView code_id;
        TextView edit;

    }

}
