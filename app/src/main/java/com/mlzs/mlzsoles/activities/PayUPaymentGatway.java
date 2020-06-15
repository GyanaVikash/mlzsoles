package com.mlzs.mlzsoles.activities;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.utils.BaseActivity;
import com.mlzs.mlzsoles.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class PayUPaymentGatway extends BaseActivity {


    WebView webView;
    final Activity activity = this;
    private String tag = "PayUPaymentGatway";
    private String hash;
    ProgressDialog progressDialog;

    String merchant_key = "do3vAdBt"; // test
    String salt = "O0nqoiMiY7"; // test


    String action1 = "";
    String base_url = "https://test.payu.in";  // test
    // String base_url="https://secure.payu.in"; // live
    int error = 0;
    String hashString = "";
    Map<String, String> params;
    String txnid = "";

    String SUCCESS_URL = "https://test.payumoney.com/mobileapp/payumoney/success.php";  // for testing
    String FAILED_URL = "https://test.payumoney.com/mobileapp/payumoney/failure.php";

    // String SUCCESS_URL ="https://www.payumoney.com/mobileapp/payumoney/success.php" ;  // live
    // String FAILED_URL = "https://www.payumoney.com/mobileapp/payumoney/failure.php"

    Handler mHandler = new Handler();


    static String id, getRechargeAmt, subjectName, current_date, end_date, type, coupon_id, discount, amountPay, examType;


    int coupon_apply = 0;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_u);
        setStatusBarColor(this, R.color.pay_u);


        if (getIntent().getStringExtra("cost") != null) {

            getRechargeAmt = getIntent().getStringExtra("cost");
            id = getIntent().getStringExtra("id");
            subjectName = getIntent().getStringExtra("item_name");
            current_date = getIntent().getStringExtra("start_date");
            end_date = getIntent().getStringExtra("end_date");
            type = getIntent().getStringExtra("plan_type");
            coupon_apply = getIntent().getIntExtra("coupon_applied", coupon_apply);
            coupon_id = getIntent().getStringExtra("coupon_id");
            discount = getIntent().getStringExtra("discount_amount");
            amountPay = getIntent().getStringExtra("after_discount");
            examType = getIntent().getStringExtra("exam_type");

        }

        webView = (WebView) findViewById(R.id.webview_pay_u);


        progressDialog = new ProgressDialog(activity);


        params = new HashMap<String, String>();
        params.put("key", merchant_key);

        params.put("amount", getRechargeAmt);
        params.put("firstname", "Test");
        params.put("email", "test@gmail.com");
        params.put("phone", "9999999999");
        params.put("productinfo", "Recharge Wallet");
        params.put("surl", SUCCESS_URL);
        params.put("furl", FAILED_URL);
        params.put("service_provider", "payu_paisa");
        params.put("lastname", "");
        params.put("address1", "");
        params.put("address2", "");
        params.put("city", "");
        params.put("state", "");
        params.put("country", "");
        params.put("zipcode", "");
        params.put("udf1", "");
        params.put("udf2", "");
        params.put("udf3", "");
        params.put("udf4", "");
        params.put("udf5", "");
        params.put("pg", "");


        if (empty(params.get("txnid"))) {
            Random rand = new Random();
            String rndm = Integer.toString(rand.nextInt()) + (System.currentTimeMillis() / 1000L);
            txnid = hashCal("SHA-256", rndm).substring(0, 20);
            params.put("txnid", txnid);
        } else
            txnid = params.get("txnid");
        //String udf2 = txnid;
        String txn = "abcd";
        hash = "";
        String hashSequence = "key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5|udf6|udf7|udf8|udf9|udf10";
        if (empty(params.get("hash")) && params.size() > 0) {
            if (empty(params.get("key"))
                    || empty(params.get("txnid"))
                    || empty(params.get("amount"))
                    || empty(params.get("firstname"))
                    || empty(params.get("email"))
                    || empty(params.get("phone"))
                    || empty(params.get("productinfo"))
                    || empty(params.get("surl"))
                    || empty(params.get("furl"))
                    || empty(params.get("service_provider"))

                    ) {
                error = 1;
            } else {
                String[] hashVarSeq = hashSequence.split("\\|");

                for (String part : hashVarSeq) {
                    hashString = (empty(params.get(part))) ? hashString.concat("") : hashString.concat(params.get(part));
                    hashString = hashString.concat("|");
                }
                hashString = hashString.concat(salt);

                hash = hashCal("SHA-512", hashString);
                action1 = base_url.concat("/_payment");
            }
        } else if (!empty(params.get("hash"))) {
            hash = params.get("hash");
            action1 = base_url.concat("/_payment");
        }

        webView.setWebViewClient(new MyWebViewClient() {

            public void onPageFinished(WebView view, final String url) {
                progressDialog.dismiss();
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //make sure dialog is showing
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
            }


        });


        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setDomStorageEnabled(true);
        webView.clearHistory();
        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setUseWideViewPort(false);
        webView.getSettings().setLoadWithOverviewMode(false);


        webView.addJavascriptInterface(new PayUJavaScriptInterface(), "PayUMoney");
        Map<String, String> mapParams = new HashMap<String, String>();
        mapParams.put("key", merchant_key);
        mapParams.put("hash", PayUPaymentGatway.this.hash);
        mapParams.put("txnid", (empty(PayUPaymentGatway.this.params.get("txnid"))) ? "" : PayUPaymentGatway.this.params.get("txnid"));
        mapParams.put("service_provider", "payu_paisa");

        mapParams.put("amount", (empty(PayUPaymentGatway.this.params.get("amount"))) ? "" : PayUPaymentGatway.this.params.get("amount"));
        mapParams.put("firstname", (empty(PayUPaymentGatway.this.params.get("firstname"))) ? "" : PayUPaymentGatway.this.params.get("firstname"));
        mapParams.put("email", (empty(PayUPaymentGatway.this.params.get("email"))) ? "" : PayUPaymentGatway.this.params.get("email"));
        mapParams.put("phone", (empty(PayUPaymentGatway.this.params.get("phone"))) ? "" : PayUPaymentGatway.this.params.get("phone"));

        mapParams.put("productinfo", (empty(PayUPaymentGatway.this.params.get("productinfo"))) ? "" : PayUPaymentGatway.this.params.get("productinfo"));
        mapParams.put("surl", (empty(PayUPaymentGatway.this.params.get("surl"))) ? "" : PayUPaymentGatway.this.params.get("surl"));
        mapParams.put("furl", (empty(PayUPaymentGatway.this.params.get("furl"))) ? "" : PayUPaymentGatway.this.params.get("furl"));
        mapParams.put("lastname", (empty(PayUPaymentGatway.this.params.get("lastname"))) ? "" : PayUPaymentGatway.this.params.get("lastname"));

        mapParams.put("address1", (empty(PayUPaymentGatway.this.params.get("address1"))) ? "" : PayUPaymentGatway.this.params.get("address1"));
        mapParams.put("address2", (empty(PayUPaymentGatway.this.params.get("address2"))) ? "" : PayUPaymentGatway.this.params.get("address2"));
        mapParams.put("city", (empty(PayUPaymentGatway.this.params.get("city"))) ? "" : PayUPaymentGatway.this.params.get("city"));
        mapParams.put("state", (empty(PayUPaymentGatway.this.params.get("state"))) ? "" : PayUPaymentGatway.this.params.get("state"));

        mapParams.put("country", (empty(PayUPaymentGatway.this.params.get("country"))) ? "" : PayUPaymentGatway.this.params.get("country"));
        mapParams.put("zipcode", (empty(PayUPaymentGatway.this.params.get("zipcode"))) ? "" : PayUPaymentGatway.this.params.get("zipcode"));
        mapParams.put("udf1", (empty(PayUPaymentGatway.this.params.get("udf1"))) ? "" : PayUPaymentGatway.this.params.get("udf1"));
        mapParams.put("udf2", (empty(PayUPaymentGatway.this.params.get("udf2"))) ? "" : PayUPaymentGatway.this.params.get("udf2"));

        mapParams.put("udf3", (empty(PayUPaymentGatway.this.params.get("udf3"))) ? "" : PayUPaymentGatway.this.params.get("udf3"));
        mapParams.put("udf4", (empty(PayUPaymentGatway.this.params.get("udf4"))) ? "" : PayUPaymentGatway.this.params.get("udf4"));
        mapParams.put("udf5", (empty(PayUPaymentGatway.this.params.get("udf5"))) ? "" : PayUPaymentGatway.this.params.get("udf5"));
        mapParams.put("pg", (empty(PayUPaymentGatway.this.params.get("pg"))) ? "" : PayUPaymentGatway.this.params.get("pg"));
        webview_ClientPost(webView, action1, mapParams.entrySet());

    }

    private final class PayUJavaScriptInterface {

        PayUJavaScriptInterface() {
        }

        @JavascriptInterface
        public void success(long id, final String paymentId) {

            mHandler.post(new Runnable() {
                public void run() {
                    mHandler = null;

                    verifyPaymentOnServer(paymentId, "success", "payu");

                    Toast.makeText(getApplicationContext(), "Successfully payment", Toast.LENGTH_LONG).show();

                }
            });
        }

        @JavascriptInterface
        public void failure(final String id, String error) {

            mHandler.post(new Runnable() {
                @Override
                public void run() {

                    // verifyPaymentOnServer("","cancelled ","payu");
                    onBackPressed();
                    Toast.makeText(PayUPaymentGatway.this, "Payment is cancelled", Toast.LENGTH_SHORT).show();

                }
            });
        }

        @JavascriptInterface
        public void failure() {
            failure("");
        }

        @JavascriptInterface
        public void failure(final String params) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(getApplicationContext(), "Failed payment", Toast.LENGTH_LONG).show();

                }
            });
        }

    }


    public void webview_ClientPost(WebView webView, String url, Collection<Map.Entry<String, String>> postData) {

        StringBuilder sb = new StringBuilder();

        sb.append("<html><head></head>");
        sb.append("<body onload='form1.submit()'>");
        sb.append(String.format("<form id='form1' action='%s' method='%s'>", url, "post"));
        for (Map.Entry<String, String> item : postData) {
            sb.append(String.format("<input name='%s' type='hidden' value='%s' />", item.getKey(), item.getValue()));
        }
        sb.append("</form></body></html>");


        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading. Please wait...");
        webView.loadData(sb.toString(), "text/html", "utf-8");

    }


    public void success(long id, final String paymentId) {

        mHandler.post(new Runnable() {
            public void run() {
                mHandler = null;

                Toast.makeText(getApplicationContext(), "Successfully payment\n redirect from Success Function", Toast.LENGTH_LONG).show();

            }
        });
    }


    public boolean empty(String s) {
        if (s == null || s.trim().equals(""))
            return true;
        else
            return false;
    }

    public String hashCal(String type, String str) {
        byte[] hashseq = str.getBytes();
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest algorithm = MessageDigest.getInstance(type);
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();


            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1) hexString.append("0");
                hexString.append(hex);
            }

        } catch (NoSuchAlgorithmException nsae) {
        }

        return hexString.toString();


    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.startsWith("http")) {
                progressDialog.show();
                view.loadUrl(url);
                System.out.println("myresult " + url);
            } else {
                return false;
            }

            return true;
        }
    }


    private void verifyPaymentOnServer(final String paymentId, String payment_status, String payment_gateway) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", getUserID());
            jsonObject.put("item_id", id);
            jsonObject.put("item_name", subjectName);
            jsonObject.put("start_date", current_date);
            jsonObject.put("end_date", end_date);
            jsonObject.put("plan_type", type);
            jsonObject.put("payment_gateway", payment_gateway);
            jsonObject.put("transaction_id", paymentId);
            jsonObject.put("paid_by", "");
            jsonObject.put("actual_cost", getRechargeAmt);
            jsonObject.put("coupon_applied", coupon_apply);

            if (coupon_apply == 1) {
                jsonObject.put("coupon_id", coupon_id);
                jsonObject.put("discount_amount", discount);
                jsonObject.put("cost", getRechargeAmt);
                jsonObject.put("after_discount", amountPay);
                jsonObject.put("paid_amount", amountPay);
            } else {
                jsonObject.put("coupon_id", "");
                jsonObject.put("discount_amount", "");
                jsonObject.put("cost", getRechargeAmt);
                jsonObject.put("after_discount", getRechargeAmt);
                jsonObject.put("paid_amount", getRechargeAmt);
            }

            jsonObject.put("payment_status", payment_status);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Utils.showProgressDialog(this, "Loading....");
        Utils.showProgress();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Utils.SAVE_TRANSACTION_STATUS, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {

                            Utils.dissmisProgress();
                            String payment_status, slug;
                            JSONObject object;
                            try {
                                object = response.getJSONObject("record");
                                slug = object.getString("slug");
                                payment_status = object.getString("payment_status");

                                if (payment_status.equals("success")) {
                                    if (examType.equals("exam_series")) {
                                        Intent intent = new Intent(PayUPaymentGatway.this, ExamsSeriesActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else if (examType.equals("lms")) {

                                        onBackPressed();

                                    } else if (examType.equals("lms_series")) {
                                        Intent intent = new Intent(PayUPaymentGatway.this, LMSSeriesActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else if (examType.equals("popular_lms")) {
                                        Intent intent = new Intent(PayUPaymentGatway.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {

                                        onBackPressed();
                                    }

                                } else {

                                    Toast.makeText(PayUPaymentGatway.this, "Payment failed please try again", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }, errorListener) {
        };
        queue.add(jsonObjReq);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}