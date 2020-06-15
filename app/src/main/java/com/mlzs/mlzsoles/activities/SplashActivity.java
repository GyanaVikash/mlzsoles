package com.mlzs.mlzsoles.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.utils.BaseActivity;
import com.mlzs.mlzsoles.utils.Utils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;


public class SplashActivity extends BaseActivity {

    private static int SPLASH_TIME_OUT = 2500;

    SharedPreferences.Editor editor;
    SharedPreferences sharedpreferences;

    String userID,fcmToken;
    ImageView imageView ;
    TextView tvAppTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        imageView = (ImageView)findViewById(R.id.img_splash);
        tvAppTitle = (TextView)findViewById(R.id.tv_splash_title);


        sharedpreferences = getSharedPreferences(Utils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        FirebaseMessaging.getInstance().subscribeToTopic("topic");
        fcmToken= FirebaseInstanceId.getInstance().getToken();
        if(fcmToken!=null&&!fcmToken.equals("")){
            appState.setFCM_Id(fcmToken);
        }


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                if(sharedpreferences.contains("user_id")) {
                    userID = sharedpreferences.getString("user_id",userID);


                    if(!userID.equals("")&&userID!=null){

                        appState.setUserID(userID);
                        Intent i =new Intent(SplashActivity.this,MainActivity.class);
                        startActivity(i);
                        finish();

                    }else {
                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();

                    }
                }else {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);
    }
}
