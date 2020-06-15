package com.mlzs.mlzsoles.activities;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.View;
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

public class PrivacyPolicyActivity extends BaseActivity {

    TextView tvTitle,tvText;
    ImageView imgBack;
    Toolbar toolbar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        tvText = (TextView)findViewById(R.id.tv_privacy_policy);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        imgBack = (ImageView)findViewById(R.id.img_toolbar_back);
        tvTitle = (TextView)findViewById(R.id.tv_toolbar_title);

        tvTitle.setText(getString(R.string.privacy_policy));

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

       getPrivacyPolicy();
    }

    public void getPrivacyPolicy(){
        Utils.showProgressDialog(this,"");
        Utils.showProgress();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Utils.PRIVACY_POLICY, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.dissmisProgress();
                        String mssg,records;
                        int status;

                        try {
                            status = response.getInt("status");
                            mssg = response.getString("message");
                            if(status==1){
                                records=response.getString("records");
                                tvText.setText(Html.fromHtml(records));
                            }else {
                                Toast.makeText(PrivacyPolicyActivity.this, mssg, Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, errorListener) {


        };
        queue.add(jsonObjReq);
    }
}
