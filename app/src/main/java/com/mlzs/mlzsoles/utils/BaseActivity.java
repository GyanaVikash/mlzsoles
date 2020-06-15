package com.mlzs.mlzsoles.utils;



import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.LinearGradient;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class BaseActivity extends AppCompatActivity {

    IntentFilter filter ;

    public AppController appState;
    Window window;
    static long fileSizeInMB ;
    public Response.ErrorListener errorListener;

    public SharedPreferences sharedPreferences ;
    public SharedPreferences.Editor editor ;
    private Activity activity;
    private int color;

    @Override
    protected void onCreate( Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        filter  = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkStateReceiver, filter);

        appState = ((AppController) getApplicationContext());

        errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";

                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                Utils.dissmisProgress();

                Toast.makeText(BaseActivity.this, message + "", Toast.LENGTH_SHORT).show();
            }
        };

        sharedPreferences = getApplicationContext().getSharedPreferences(Utils.MyPREFERENCES, Context.MODE_MULTI_PROCESS);
        editor = sharedPreferences.edit();
    }
    /*==========drawable image color=============*/
    public void setDrawableColor(Button textView, int color) {
        for (Drawable drawable : textView.getCompoundDrawables()) {
            if (drawable != null) {
                drawable.setColorFilter(new PorterDuffColorFilter(ContextCompat.getColor(this,color), PorterDuff.Mode.SRC_IN));
            }
        }
    }

    /*==========Status bar color=============*/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public  void setStatusBarColor(Activity activity, int color){
        this.activity = activity;
        this.color = color;
        window  = getWindow();
        View view = window.getDecorView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            activity.getWindow().setStatusBarColor(activity.getResources().getColor(color));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            {

                Window window = activity.getWindow();
                // clear FLAG_TRANSLUCENT_STATUS flag:
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                // finally change the color
                window.setStatusBarColor(ContextCompat.getColor(activity,color));
            }
        }
    }


    /*==========Toolbar background color=============*/
    public void setToolBarBackgroundColor(Toolbar toolBar,Activity activity,int color){

        toolBar.setBackgroundColor(ContextCompat.getColor(activity, color));

    }

    /*==========Textview gradient color=============*/

  public void setTextColorGradient(TextView textView,int color1,int color2){

      Shader myShader = new LinearGradient(0, 0,100, 0, color1, color2, Shader.TileMode.REPEAT );
      textView.getPaint().setShader( myShader );
  }


    /*==========Image size=============*/

    String strPath ;

    public long getImageSize(Uri uri){

        String[] filePathColumn = {MediaStore.Audio.Media.DATA};


        Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
        assert cursor != null;

        if(cursor!=null&&cursor.moveToFirst()){

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            strPath = cursor.getString(columnIndex);
        }

        if(strPath!=null){
            File file = new File(strPath);
            long fileSizeInBytes = file.length();
            long fileSizeInKB = fileSizeInBytes / 1024;   // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
            fileSizeInMB = fileSizeInKB / 1024; // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        }

        return  fileSizeInMB ;
    }
    String userID;
    public String getUserID(){


        if(sharedPreferences.contains("user_id")){
            userID = sharedPreferences.getString("user_id",userID);
        }
        return userID;
    }

    public void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /*==========Date Format=============*/

    String formatedDate ;

    public String dateFormat(String strdate){

        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy");

        Date date = null;
        try {
            date = inputFormat.parse(strdate);

            formatedDate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return formatedDate ;
    }
    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(networkStateReceiver, filter);

    }

    @Override
    protected void onPause() {
        super.onPause();

        appState.saveData();

    }

      public BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (isOnline(context)) {
                //  dialog(true);
                appState.setNetworkCheck(true);

            } else {
                //dialog(false);
                appState.setNetworkCheck(false);
                // onConnectionLost();
            }
        }
    };

    private boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        unregisterReceiver(networkStateReceiver);
    }

}
