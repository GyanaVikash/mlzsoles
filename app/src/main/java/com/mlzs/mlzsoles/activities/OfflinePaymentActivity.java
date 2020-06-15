package com.mlzs.mlzsoles.activities;

import android.graphics.drawable.GradientDrawable;
import androidx.core.content.ContextCompat;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import org.json.JSONException;
import org.json.JSONObject;

public class OfflinePaymentActivity extends BaseActivity {


    EditText edDetails;
    Button btnSubmit;
    Toolbar toolbar;
    ImageView imgBack;
    TextView tvTitle ;

    String item_id,item_name,plan_type,start_date,end_date,actual_cost,coupon_id,discount_amount,after_discount,examType;

    int coupon_applied;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_payment);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        imgBack = (ImageView) findViewById(R.id.img_toolbar_back);
        tvTitle = (TextView) findViewById(R.id.tv_toolbar_title);
        tvTitle.setText(getString(R.string.off_line_payment_form));

        edDetails = (EditText)findViewById(R.id.ed_offline_pay);
        btnSubmit = (Button)findViewById(R.id.btn_offline_pay);

        GradientDrawable title = (GradientDrawable) edDetails.getBackground();
        title.setStroke(2, ContextCompat.getColor(this, R.color.exams_series_bg));

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if(getIntent().getStringExtra("item_id")!=null){

            item_id = getIntent().getStringExtra("item_id");
            item_name = getIntent().getStringExtra("item_name");
            plan_type = getIntent().getStringExtra("plan_type");
            start_date = getIntent().getStringExtra("start_date");
            end_date = getIntent().getStringExtra("end_date");
            actual_cost = getIntent().getStringExtra("actual_cost");
            coupon_id = getIntent().getStringExtra("coupon_id");
            discount_amount = getIntent().getStringExtra("discount_amount");
            after_discount = getIntent().getStringExtra("after_discount");
            examType = getIntent().getStringExtra("exam_type");
            coupon_applied = getIntent().getIntExtra("coupon_applied",coupon_applied);
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edDetails.getText().toString().equals("")){
                    Toast.makeText(OfflinePaymentActivity.this, getResources().getString(R.string.enter_payment_details), Toast.LENGTH_SHORT).show();
                }
                else {

                    payOffline();
                }
            }
        });
    }

    public void payOffline(){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", getUserID());
            jsonObject.put("item_id", item_id);
            jsonObject.put("item_name", item_name);
            jsonObject.put("start_date", start_date);
            jsonObject.put("end_date", end_date);
            jsonObject.put("plan_type", plan_type);
            jsonObject.put("actual_cost", actual_cost);
            jsonObject.put("coupon_applied", coupon_applied);
            if (coupon_applied==1) {
                jsonObject.put("coupon_id", coupon_id);
                jsonObject.put("discount_amount", discount_amount);
                jsonObject.put("cost",actual_cost );
                jsonObject.put("after_discount", after_discount);
            } else {
                jsonObject.put("coupon_id", "");
                jsonObject.put("discount_amount", "");
                jsonObject.put("cost", "");
                jsonObject.put("after_discount", actual_cost);
            }
            jsonObject.put("payment_details", edDetails.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Utils.showProgressDialog(this, "Loading....");
        Utils.showProgress();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Utils.OFFLINE_PAYMENT, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {

                            Utils.dissmisProgress();
                            String mssg;
                            int status;
                            try {
                                mssg = response.getString("message");
                                status = response.getInt("status");

                                Toast.makeText(OfflinePaymentActivity.this, mssg, Toast.LENGTH_SHORT).show();
                                if(status==1){

                                    onBackPressed();

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
