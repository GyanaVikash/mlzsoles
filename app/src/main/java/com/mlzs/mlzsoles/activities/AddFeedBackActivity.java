package com.mlzs.mlzsoles.activities;

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

public class AddFeedBackActivity extends BaseActivity {

    TextView tvTitle;
    ImageView imgBack;
    Toolbar toolbar ;

    EditText edTitle,edSubject,edDescription;

    Button btnAddFeedBack;

    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_feed_back);

        imgBack = (ImageView)findViewById(R.id.img_toolbar_back);
        tvTitle = (TextView)findViewById(R.id.tv_toolbar_title);
        toolbar = (Toolbar)findViewById(R.id.toolbar);

        edTitle = (EditText)findViewById(R.id.ed_feed_back_title);
        edSubject = (EditText)findViewById(R.id.ed_feed_back_subject);
        edDescription = (EditText)findViewById(R.id.ed_feed_back_description);

        btnAddFeedBack = (Button)findViewById(R.id.btn_feed_back_send);



        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvTitle.setText(getString(R.string.give_feed_back));


        userID = getUserID();

        btnAddFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edTitle.getText().toString().equals("")){
                    edTitle.setError(getString(R.string.plz_enter_title));
                }
                else if(edSubject.getText().toString().equals("")){
                    edSubject.setError(getString(R.string.plz_add_subject));
                }
                else if(edDescription.getText().toString().equals("")){
                    edDescription.setError(getString(R.string.plz_add_description));
                }
                else {

                    JSONObject jsonObject = new JSONObject();

                    try {
                        jsonObject.put("user_id",userID);
                        jsonObject.put("title",edTitle.getText().toString().trim());
                        jsonObject.put("subject",edSubject.getText().toString().trim());
                        jsonObject.put("description",edDescription.getText().toString().trim());

                        if(appState.getNetworkCheck()){
                            addFeedBack(jsonObject);
                        }else {
                            Toast.makeText(AddFeedBackActivity.this,getResources().getString(R.string.plz_check_network_connection), Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        });

    }

    public void addFeedBack(JSONObject jsonObject){

        Utils.showProgressDialog(this,"");
        Utils.showProgress();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Utils.ADD_FEED_BACK, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {

                            Utils.dissmisProgress();

                            String message;
                            int status ;

                            try {
                                message = response.getString("message");
                                status = response.getInt("status");

                                if(status==1){
                                    Toast.makeText(AddFeedBackActivity.this, message, Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }
                                else {
                                    Toast.makeText(AddFeedBackActivity.this, message, Toast.LENGTH_SHORT).show();
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
