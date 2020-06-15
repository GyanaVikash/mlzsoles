package com.mlzs.mlzsoles.activities;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.utils.BaseActivity;
import com.mlzs.mlzsoles.utils.Utils;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;


public class CheckOutActivity extends BaseActivity implements View.OnClickListener, PaymentResultListener {

    Toolbar toolbar;
    ImageView imgBack;
    TextView tvTitle, tvCost, tvApply, tvSubTotal, tvDiscount, tvFinalTotal, tvSubjectName, tvValidity, tvCancel;
    EditText edPromocode;
    TextView tvCurrencyCost,tvCurrencySubtotal,tvCurrencyDiscount,tvCurrencyOrderTotal;

    Button btnPay;
    BottomSheetDialog mBottomSheetDialog;

    LinearLayout llPromoCode, llPaypal, llPayU, llRazorPay,llOffline;

    String subjectName, validity, cost, subSlug, type, id, examType;

    public static final int PAYPAL_REQUEST_CODE = 123;

    int coupon_apply=0;
    String amountPay, discount, transactionID;
    private static final String TAG = PaymentActivity.class.getSimpleName();


    String end_date, current_date, coupon_id, amount_toPay,randomNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imgBack = (ImageView) findViewById(R.id.img_toolbar_back);
        tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        tvTitle.setText(getString(R.string.checkout));

        btnPay = (Button) findViewById(R.id.btn_checkout_pay);
        llPromoCode = (LinearLayout) findViewById(R.id.ll_checkout_promo_code);
        tvCost = (TextView) findViewById(R.id.tv_checkout_cost);
        tvApply = (TextView) findViewById(R.id.tv_checkout_apply);
        tvSubTotal = (TextView) findViewById(R.id.tv_checkout_sub_total);
        tvDiscount = (TextView) findViewById(R.id.tv_checkout_discount);
        tvFinalTotal = (TextView) findViewById(R.id.tv_checkout_final_total);
        edPromocode = (EditText) findViewById(R.id.ed_checkout_promocode);
        tvSubjectName = (TextView) findViewById(R.id.tv_checkout_sub_name);
        tvValidity = (TextView) findViewById(R.id.tv_checkout_validity);

        tvCurrencyCost = (TextView)findViewById(R.id.tv_checkout_currency_cost);
        tvCurrencySubtotal = (TextView)findViewById(R.id.tv_checkout_currency_subtotal);
        tvCurrencyDiscount = (TextView)findViewById(R.id.tv_checkout_currency_discount);
        tvCurrencyOrderTotal = (TextView)findViewById(R.id.tv_checkout_currency_order_total);


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        GradientDrawable title = (GradientDrawable) llPromoCode.getBackground();
        title.setStroke(2, ContextCompat.getColor(this, R.color.exams_series_bg));


        mBottomSheetDialog = new BottomSheetDialog(this);
        View sheetView = getLayoutInflater().inflate(R.layout.payment_options_sheet, null);
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.setCancelable(false);

        tvCancel = (TextView) sheetView.findViewById(R.id.tv_cancel);

        llPaypal = (LinearLayout) sheetView.findViewById(R.id.ll_pay_pal);
        llPayU = (LinearLayout) sheetView.findViewById(R.id.ll_pay_u);
        llRazorPay = (LinearLayout) sheetView.findViewById(R.id.ll_razor_pay);
        llOffline = (LinearLayout)sheetView.findViewById(R.id.ll_offline_pay);


        if (getIntent().getStringExtra("sub_name") != null) {

            id = getIntent().getStringExtra("id");
            subjectName = getIntent().getStringExtra("sub_name");
            validity = getIntent().getStringExtra("validity");
            cost = getIntent().getStringExtra("cost");
            subSlug = getIntent().getStringExtra("slug");


            type = getIntent().getStringExtra("type");
            examType = getIntent().getStringExtra("exam_type");


        }

        tvSubjectName.setText(subjectName);
        tvValidity.setText(getString(R.string.valid_for) + " " + validity + " " + getString(R.string.days));
        tvCost.setText(cost);
        tvSubTotal.setText(cost);
        tvDiscount.setText("0");
        tvFinalTotal.setText(cost);


        tvApply.setOnClickListener(this);
        btnPay.setOnClickListener(this);
        tvCancel.setOnClickListener(this);

        llPaypal.setOnClickListener(this);
        llPayU.setOnClickListener(this);
        llRazorPay.setOnClickListener(this);
        llOffline.setOnClickListener(this);

        Checkout.preload(getApplicationContext());

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss");
        current_date = df.format(c.getTime());


        c.add(Calendar.DATE, Integer.parseInt(validity));  // number of days to add  Integer.parseInt(number)
        end_date = df.format(c.getTime());

        if(appState.getNetworkCheck()){
            getCurrencyCode();

        }else {
            Toast.makeText(this, getResources().getString(R.string.plz_check_network_connection), Toast.LENGTH_SHORT).show();
        }

        Random rand = new Random();
        String rndm = Integer.toString(rand.nextInt()) + (System.currentTimeMillis() / 1000L);
        randomNumber = hashCal("SHA-256", rndm).substring(0, 20);

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

    public void getGateways() {

        Utils.showProgressDialog(this, "");
        Utils.showProgress();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Utils.GET_PAYMET_GATEWAYS, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       Utils.dissmisProgress();
                        int status, payu, paypal, razorPay,  offline;
                        JSONObject jsonObject;
                        try {
                            status = response.getInt("status");
                            if (status == 1) {

                                mBottomSheetDialog.show();

                                jsonObject = response.getJSONObject("data");

                                payu = jsonObject.getInt("payu");
                                paypal = jsonObject.getInt("paypal");
                                razorPay = jsonObject.getInt("razorpay");
                                offline = jsonObject.getInt("offline_payment");

                                if (payu == 1) {

                                    llPayU.setVisibility(View.VISIBLE);
                                } else {
                                    llPayU.setVisibility(View.GONE);
                                }

                                if (paypal == 1) {
                                    llPaypal.setVisibility(View.VISIBLE);
                                } else {
                                    llPaypal.setVisibility(View.GONE);
                                }

                                if (offline == 1) {
                                    llOffline.setVisibility(View.VISIBLE);
                                } else {
                                    llOffline.setVisibility(View.GONE);
                                }

                                if(razorPay == 1){

                                    llRazorPay.setVisibility(View.VISIBLE);
                                }else {

                                    llRazorPay.setVisibility(View.GONE);
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, errorListener) {


        };
        queue.add(jsonObjReq);

    }


    public void getCurrencyCode(){

        Utils.showProgressDialog(this,"");
        Utils.showProgress();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Utils.GET_CURRENCY_CODE, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.dissmisProgress();
                        String mssg,currency;
                        int status;
                        try {
                            status = response.getInt("status");
                            if(status==1){


                                currency = response.getString("record");

                                if(currency!=null){

                                    tvCurrencyCost.setText(currency);
                                    tvCurrencySubtotal.setText(currency);
                                    tvCurrencyDiscount.setText(currency);
                                    tvCurrencyOrderTotal.setText(currency);

                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, errorListener) {


        };
        queue.add(jsonObjReq);

    }


    public void applyCouponCode(JSONObject jsonObject) {

        Utils.showProgressDialog(this, "");
        Utils.showProgress();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Utils.APPLY_COUPON_CODE, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.dissmisProgress();

                        int status;
                        String mssg;

                        try {
                            status = response.getInt("status");
                            mssg = response.getString("message");
                            if (status == 1) {

                                coupon_apply = 1;
                                coupon_id = response.getString("coupon_id");
                                edPromocode.setClickable(false);
                                edPromocode.setFocusable(false);
                                tvApply.setBackgroundColor(ContextCompat.getColor(CheckOutActivity.this, R.color.grey_color));
                                tvApply.setClickable(false);

                                amountPay = response.getString("amount_to_pay");
                                discount = response.getString("discount");


                                tvSubTotal.setText(amountPay);
                                tvDiscount.setText(discount);

                                tvFinalTotal.setText(amountPay);

                                if (amountPay.equals("0")) {
                                    verifyPaymentOnServer(randomNumber, "success", "coupon_code_zero_amount");
                                }

                            } else {

                                coupon_apply = 0;
                                coupon_id = null;

                                Toast.makeText(CheckOutActivity.this, mssg, Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, errorListener) {


        };
        queue.add(jsonObjReq);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_checkout_apply:
                if (edPromocode.getText().toString().equals("")) {
                    edPromocode.setError("Please enter coupon code");
                } else {

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("coupon_code", edPromocode.getText().toString().trim());
                        jsonObject.put("item_name", subSlug);
                        jsonObject.put("item_type", type);
                        jsonObject.put("cost", cost);
                        jsonObject.put("student_id", getUserID());

                        applyCouponCode(jsonObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case R.id.btn_checkout_pay:

//                mBottomSheetDialog.show();
                if(appState.getNetworkCheck()){
                  getGateways();

                }else {
                    Toast.makeText(this, getResources().getString(R.string.plz_check_network_connection), Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.tv_cancel:
                mBottomSheetDialog.dismiss();
                break;

            case R.id.ll_pay_pal:

                paypalGateway();
                mBottomSheetDialog.dismiss();
                break;

            case R.id.ll_pay_u:

                Intent intent = new Intent(CheckOutActivity.this, PayUPaymentGatway.class);
                intent.putExtra("cost", amount_toPay);
                intent.putExtra("id", id);
                intent.putExtra("item_name", subjectName);
                intent.putExtra("start_date", current_date);
                intent.putExtra("end_date", end_date);
                intent.putExtra("plan_type", type);
                intent.putExtra("coupon_applied", coupon_apply);
                intent.putExtra("coupon_id", coupon_id);
                intent.putExtra("discount_amount", discount);
                intent.putExtra("after_discount", amountPay);
                intent.putExtra("exam_type", examType);
                startActivity(intent);
                finish();
                mBottomSheetDialog.cancel();
                break;

            case R.id.ll_razor_pay:

                razorpayGateWay();
                mBottomSheetDialog.cancel();
                break;

            case R.id.ll_offline_pay:

                Intent i = new Intent(CheckOutActivity.this, OfflinePaymentActivity.class);
                i.putExtra("item_id", id);
                i.putExtra("item_name", subjectName);
                i.putExtra("start_date", current_date);
                i.putExtra("end_date", end_date);
                i.putExtra("plan_type", type);
                i.putExtra("actual_cost", cost);
                i.putExtra("coupon_applied", coupon_apply);
                i.putExtra("coupon_id", coupon_id);
                i.putExtra("discount_amount", discount);
                i.putExtra("after_discount", amountPay);
                i.putExtra("exam_type", examType);
                startActivity(i);
                finish();
                mBottomSheetDialog.dismiss();
                break;
        }

    }

    /*===================== Pay Pal ====================================*/

    public void paypalGateway() {

        amount_toPay = tvFinalTotal.getText().toString();
        PayPalConfiguration config = new PayPalConfiguration()
                // or live (ENVIRONMENT_PRODUCTION)
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(Utils.PAYPAL_CLIENT_ID);

        mBottomSheetDialog.cancel();

        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(amount_toPay)), "USD", getString(R.string.app_title), PayPalPayment.PAYMENT_INTENT_SALE);
        //Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);
        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //If the result is from paypal
        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);

                        transactionID = confirm.toJSONObject().getJSONObject("response").getString("id");

                        String payment_client = confirm.getPayment().toJSONObject().toString();

                        // Now verify the payment on the server side
                        verifyPaymentOnServer(transactionID, "success", "paypal");

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");

               // verifyPaymentOnServer(transactionID, "cancelled", "paypal");
                onBackPressed();
                Toast.makeText(CheckOutActivity.this, "Payment is cancelled", Toast.LENGTH_SHORT).show();

            }

            else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }


    }

    /*===================== Razor Pay ====================================*/
    public void razorpayGateWay() {

        mBottomSheetDialog.dismiss();
        amount_toPay = tvFinalTotal.getText().toString();
        final Activity activity = this;

        final Checkout co = new Checkout();

        float amountRupees = Float.valueOf(amount_toPay)*100;
        try {
            JSONObject options = new JSONObject();
            options.put("name", getResources().getString(R.string.app_title));
            options.put("description", type + " charges for " + subjectName);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", amountRupees);

            JSONObject preFill = new JSONObject();
            preFill.put("email", appState.getEmail());
            preFill.put("contact", "");

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {

            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();

            verifyPaymentOnServer(razorpayPaymentID, "success", "razorpay");

        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {

            onBackPressed();
            Toast.makeText(CheckOutActivity.this, "Payment is cancelled", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

        }
    }

    private void verifyPaymentOnServer(final String paymentId, String paymentStatus, String payment_gateway) {

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
            jsonObject.put("actual_cost", cost);
            jsonObject.put("coupon_applied", coupon_apply);
            if (coupon_apply == 1) {
                jsonObject.put("coupon_id", coupon_id);
                jsonObject.put("discount_amount", discount);
                jsonObject.put("cost", cost);
                jsonObject.put("after_discount", amountPay);
                jsonObject.put("paid_amount",amount_toPay);
            } else {
                jsonObject.put("coupon_id", "");
                jsonObject.put("discount_amount", "");
                jsonObject.put("cost", cost);
                jsonObject.put("after_discount", cost);
                jsonObject.put("paid_amount",cost);
            }
            jsonObject.put("payment_status", paymentStatus);

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
                            String  payment_status;
                            JSONObject object;
                            try {
                                object = response.getJSONObject("record");
                                payment_status = object.getString("payment_status");

                                if (payment_status.equals("success")) {

                                    if(examType.equals("exam_series")){
                                        Intent   intent = new Intent(CheckOutActivity.this,ExamsSeriesActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
                                        startActivity(intent);
                                        finish();
                                    }
                                    else if (examType.equals("lms")){

                                        onBackPressed();

                                    }
                                    else if (examType.equals("lms_series")){
                                        Intent   intent = new Intent(CheckOutActivity.this,LMSSeriesActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else if(examType.equals("popular_lms")){
                                        Intent   intent = new Intent(CheckOutActivity.this,MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {

                                        onBackPressed();

                                    }

                                } else {

                                    Toast.makeText(CheckOutActivity.this, "Payment failed please try again", Toast.LENGTH_SHORT).show();
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
}
