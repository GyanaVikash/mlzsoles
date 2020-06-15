package com.mlzs.mlzsoles.webview;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.utils.WebAppInterface;

public class HtmlActivity extends AppCompatActivity {
    private WebView mWebView;
    public SwipeRefreshLayout swipe;
    public static  String VIDEO_ID;
    String url;
    @Override
    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_html);
        /*swipe = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                WebAction();
            }
        });

         */
        WebAction();
    }
    public void WebAction(){

        mWebView = findViewById(R.id.webview);

        mWebView.setWebViewClient(new WebViewClient());
        mWebView.setWebChromeClient(new WebChromeClient());
 VIDEO_ID=getVideoURL(getIntent().getStringExtra("url"));
       url ="https://app.gvhssb.in.net/youtube.html?id="+VIDEO_ID;
        mWebView.loadUrl(url);

        WebSettings webSettings = mWebView.getSettings();

       mWebView.getSettings().setAppCacheEnabled(true);

       webSettings.setJavaScriptEnabled(true);

        mWebView.addJavascriptInterface(new WebAppInterface( this), "Android");

        mWebView.setWebViewClient(new WebViewClient(){
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
            {
               // mWebView.loadUrl("file:///android_asset/error.html");
               // onBackPressed();
            }
            public void onPageFinished(WebView view, String url) {
                // do your stuff here
                swipe.setRefreshing(false);
            }

        });

       mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri myUri = Uri.parse(url);
                Intent superIntent = new Intent(Intent.ACTION_VIEW);
                superIntent.setData(myUri);
                startActivity(superIntent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Exit Lectures");
            dialog.setMessage("Are you sure you want to exit ?");
            dialog.setNeutralButton("DOWNLOAD THIS VIDEO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent=new Intent(getApplicationContext(), DownloadActivity.class);
                    intent.putExtra("url", VIDEO_ID);
                    startActivity(intent);
                }
            });
            dialog.setCancelable(false);
            dialog.setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            dialog.setCancelable(false);
            dialog.setNegativeButton("KEEP WATCHING", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();

        }
    }
    public String getVideoURL(String str){
        int findIndex=str.indexOf("embed/");
        int firstIndex=findIndex+6;
        str=str.substring(firstIndex);
        int secondIndex=str.indexOf('"');
        str=str.substring(0,secondIndex);
        return str;
    }

}