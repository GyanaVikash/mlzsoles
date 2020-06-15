package com.mlzs.mlzsoles.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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

public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener{

    private Toolbar toolbar ;
    TextView tvTitle;
    ImageView imgBack,imgOldPassword,imgNewPassword,imgConfirmPassword;
    EditText edOldPassword,edNewPassword,edConfirmPassword;

    int oldCount=0,newCount=0,confirmCount=0;
    Button btnUpdatePassword;

    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        imgBack = (ImageView)findViewById(R.id.img_toolbar_back);
        tvTitle = (TextView)findViewById(R.id.tv_toolbar_title);
        edOldPassword = (EditText)findViewById(R.id.ed_cp_old);
        edNewPassword = (EditText)findViewById(R.id.ed_cp_new);
        edConfirmPassword = (EditText)findViewById(R.id.ed_cp_cnew);
        imgOldPassword = (ImageView)findViewById(R.id.img_cp_old);
        imgNewPassword = (ImageView)findViewById(R.id.img_cp_new);
        imgConfirmPassword = (ImageView)findViewById(R.id.img_cp_cnew);
        btnUpdatePassword = (Button)findViewById(R.id.btn_update_pwd);

        tvTitle.setText(getString(R.string.change_pwd));

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        imgNewPassword.setOnClickListener(this);
        imgConfirmPassword.setOnClickListener(this);
        imgOldPassword.setOnClickListener(this);
        btnUpdatePassword.setOnClickListener(this);

        userID = getUserID();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.img_cp_old:

                oldCount++;
                if (oldCount % 2 == 0) {
                    edOldPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgOldPassword.setImageResource(R.drawable.eye_on);
                } else {
                    edOldPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imgOldPassword.setImageResource(R.drawable.eye_off);
                }

                break;
            case R.id.img_cp_new:

                newCount ++ ;

                if(newCount%2==0){
                    edNewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgNewPassword.setImageResource(R.drawable.eye_on);
                }else {
                    edNewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imgNewPassword.setImageResource(R.drawable.eye_off);
                }
                break;

            case R.id.img_cp_cnew:

                confirmCount++;
                if(confirmCount%2==0){
                    edConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgConfirmPassword.setImageResource(R.drawable.eye_on);
                }else {
                    edConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imgConfirmPassword.setImageResource(R.drawable.eye_off);
                }
                break;
            case R.id.btn_update_pwd:

                if(edOldPassword.getText().toString().equals("")){
                    edOldPassword.setError(getString(R.string.plz_enter_old_pwd));
                }
                else if(edNewPassword.getText().toString().equals("")){
                    edNewPassword.setError(getString(R.string.plz_enter_new_pwd));
                }
                else if(edNewPassword.getText().toString().contains(" ")){
                    edNewPassword.setError(getString(R.string.pwd_does_not_allow_spaces));
                }
                else if(edNewPassword.getText().toString().length()<6){
                    edNewPassword.setError(getString(R.string.pwd_min_chars));
                }
                else if(edConfirmPassword.getText().toString().equals("")){
                    edConfirmPassword.setError(getString(R.string.plz_enter_confirm_pwd));
                }
                else if(!edNewPassword.getText().toString().equals(edConfirmPassword.getText().toString())){
                    edConfirmPassword.setError(getString(R.string.both_pwds_doent_match));
                }
                else {

                    if(appState.getNetworkCheck()){

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("user_id",userID);
                            jsonObject.put("old_password",edOldPassword.getText().toString().trim());
                            jsonObject.put("password",edNewPassword.getText().toString().trim());
                            jsonObject.put("password_confirmation",edConfirmPassword.getText().toString().trim());

                            updatePassword(jsonObject);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }else {
                        Toast.makeText(ChangePasswordActivity.this, getResources().getString(R.string.plz_check_network_connection), Toast.LENGTH_SHORT).show();
                    }

                }

                break;
        }

    }

    public void updatePassword(JSONObject jsonObject){

        Utils.showProgressDialog(this,"");
        Utils.showProgress();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Utils.CHANGE_PASSWORD, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {

                            Utils.dissmisProgress();
                            String mssg;
                            int status;
                            try {
                                status = response.getInt("status");
                                mssg = response.getString("message");
                                Toast.makeText(ChangePasswordActivity.this, mssg, Toast.LENGTH_SHORT).show();
                                if(status==1){
                                    Intent intent = new Intent(ChangePasswordActivity.this,LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    appState.logout();
                                    finish();
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
