package com.mlzs.mlzsoles.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;



public class MathJaxWebView extends WebView {

    public MathJaxWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        clearCache(true);
        getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        setBackgroundColor(Color.TRANSPARENT);
        getSettings().setJavaScriptEnabled(true);
        getSettings().setBuiltInZoomControls(false);
        setHorizontalScrollBarEnabled(false);

    }

    public void setText(final String text)
    {

        loadDataWithBaseURL("http://bar",
                "<script type=\"text/x-mathjax-config\">" +
                        "  MathJax.Hub.Config({" +
                        "extensions: [\"tex2jax.js\"],messageStyle:\"none\"," +
                        "jax: [\"input/TeX\",\"output/HTML-CSS\"]," +
                        "tex2jax: {inlineMath: [['$','$'],['\\\\(','\\\\)']]}" +
                        "});" +
                        "</script>" +
                        "<script type=\"text/javascript\" async src=\"file:///android_asset/MathJax/MathJax.js?config=TeX-AMS-MML_HTMLorMML\"></script>" +
                        "" +
                        "</head>" +
                        "" +
                        "<body>" +
                        text +
                        "</body>" +
                        "</html>", "text/html", "utf-8", "");

    }

}
