package com.butions.utnee;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Chalitta Khampachua on 07-Aug-17.
 */

public class AboutPage extends AppCompatActivity implements View.OnClickListener {

    private ImageView btnBack;
    private TextView followUs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        ActivityManagerName managerName = new ActivityManagerName();
        managerName.setProcessNameClass(this);
        managerName.setNameClassInProcessString("About");

        TextView toolbar = (TextView) findViewById(R.id.toolbar);
        toolbar.setTypeface(Config.getInstance().getDefaultFont(this), Typeface.BOLD);

        btnBack = (ImageView) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);

        TextView text_about = (TextView) findViewById(R.id.text_about);
        text_about.setTypeface(Config.getInstance().getDefaultFont(this));

        TextView text_follow = (TextView) findViewById(R.id.text_follow);
        text_follow.setTypeface(Config.getInstance().getDefaultFont(this));

        followUs = (TextView) findViewById(R.id.followUs);
        followUs.setTypeface(Config.getInstance().getDefaultFont(this));
        followUs.setOnClickListener(this);
        followUs.setBackgroundResource(R.color.transparent);
    }

    @Override
    public void onClick(View view) {
        if(view == btnBack){
            finish();
        }else if(view == followUs){
            followUs.setBackgroundResource(R.color.white);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.followUs)));
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        followUs.setBackgroundResource(R.color.transparent);
    }
}
