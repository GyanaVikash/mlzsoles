package com.mlzs.mlzsoles.webview;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;


import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.activities.MainActivity;
import com.mlzs.mlzsoles.model.MyProfile;
import com.mlzs.mlzsoles.utils.AppController;
import com.mlzs.mlzsoles.utils.BaseActivity;
import com.mlzs.mlzsoles.utils.Utils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;




public class DoubtActivity extends BaseActivity {
    public SwipeRefreshLayout swipe;
public static String CurURL="";
    private WebView mWebView;
    String url;
    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 1;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            if (requestCode == REQUEST_SELECT_FILE)
            {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                uploadMessage = null;
            }
        }
        else if (requestCode == FILECHOOSER_RESULTCODE)
        {
            if (null == mUploadMessage)
                return;
            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
            // Use RESULT_OK only if you're implementing WebView inside an Activity
            Uri result = intent == null || resultCode != MainActivity.RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
        else
            Toast.makeText(this, "Failed to Upload Image", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html);

        if(appState.getNetworkCheck()){
            getMyProfile();

        }else {
            Toast.makeText(this, getResources().getString(R.string.plz_check_network_connection), Toast.LENGTH_SHORT).show();
        }



    }
    public void getMyProfile(){

        Utils.showProgressDialog(this,"");
        Utils.showProgress();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Utils.GET_MY_PROFILE+getUserID(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.dissmisProgress();
                        String mssg;
                        int status;
                        JSONObject jsonObject;
                        try {
                            status = response.getInt("status");
                            if(status==1){

                                jsonObject = response.getJSONObject("user");

                                AppController.getInstance().setUserName(jsonObject.getString("name"));
                                AppController.getInstance().setEmail(jsonObject.getString("email"));

                                editor.putString("profilePic",jsonObject.getString("image"));
                                editor.putString("user_name",jsonObject.getString("name"));

                                editor.commit();

                                Gson gson = new Gson();
                                final MyProfile profileJson = gson.fromJson(jsonObject.toString(), MyProfile.class);
                                url ="https://helpmlzs.gvhssb.in.net/login?id="+profileJson.getEmail()+"&pwd="+profileJson.getPassword();
                                 swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
                                        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                            @Override
                                            public void onRefresh() {
                                                WebAction(getCurrentURL(CurURL));
                                            }
                                        });
                                WebAction(url);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, errorListener) {

        };
        queue.add(jsonObjReq);

    }
    public void WebAction(String U){

        mWebView = findViewById(R.id.webview);

        mWebView.setWebViewClient(new WebViewClient());

        mWebView.loadUrl(U);



        mWebView.setInitialScale(50);
        WebSettings mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setBuiltInZoomControls(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setSupportZoom(true);
        mWebSettings.setAllowFileAccess(true);
        mWebSettings.setAllowFileAccess(true);
        mWebSettings.setAllowContentAccess(true);

        mWebView.setWebChromeClient(new WebChromeClient()
        {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams)
            {
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }

                uploadMessage = filePathCallback;

               Intent intent = fileChooserParams.createIntent();

                try
                {
                    startActivityForResult(intent, REQUEST_SELECT_FILE);

                } catch (ActivityNotFoundException e)
                {
                    uploadMessage = null;
                    Toast.makeText(getApplicationContext(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }
        });
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
            {
                
                mWebView.loadUrl("file:///android_asset/error.html");
                onBackPressed();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                swipe.setRefreshing(false);
                super.onPageFinished(view, url);
                setCurrentURL(url);
            }

        });
    }

    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            if(getCurrentURL(CurURL).equals("https://help.gvhssb.in.net/"))
            {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Exit Doubt Section");
                dialog.setMessage("Are you sure you want to exit ?");
                dialog.setCancelable(false);
                dialog.setPositiveButton("EXIT Doubt Section", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                dialog.setCancelable(false);
                dialog.setNegativeButton("KEEP DOUBT CLEARING", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
            else{
                mWebView.goBack();
            }

        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Exit Doubt Section");
            dialog.setMessage("Are you sure you want to exit ?");
            dialog.setCancelable(false);
            dialog.setPositiveButton("EXIT Doubt Section", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            dialog.setCancelable(false);
            dialog.setNegativeButton("KEEP DOUBT CLEARING", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();

        }
    }
    public void setCurrentURL(String u){
       CurURL= u;
    }
public String getCurrentURL(String u){
        return u;
}
}
