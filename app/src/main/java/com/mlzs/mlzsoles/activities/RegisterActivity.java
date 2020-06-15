package com.mlzs.mlzsoles.activities;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
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
import com.mlzs.mlzsoles.utils.AppController;
import com.mlzs.mlzsoles.utils.BaseActivity;
import com.mlzs.mlzsoles.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    LinearLayout llAccount,helpme;
    EditText edName,edUserName,edEmail,edPassword,edConfirmPassword;
    TextView tvRegisterNow;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    ImageView imgShowPassword,imgShowCPassword;

    int pCount=0,cCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        llAccount = (LinearLayout)findViewById(R.id.ll_have_account);
        helpme = (LinearLayout)findViewById(R.id.help_me);
        tvRegisterNow = (TextView)findViewById(R.id.tv_register_now);
        edName = (EditText)findViewById(R.id.ed_register_name);
        edUserName = (EditText)findViewById(R.id.ed_register_mobile);
        edEmail = (EditText)findViewById(R.id.ed_register_email);
        edPassword = (EditText)findViewById(R.id.ed_register_password);
        edConfirmPassword = (EditText)findViewById(R.id.ed_register_pwd_confirm);
        imgShowCPassword = (ImageView)findViewById(R.id.img_register_show_cpwd);
        imgShowPassword = (ImageView)findViewById(R.id.img_register_show_pwd);




        llAccount.setOnClickListener(this);
        helpme.setOnClickListener(this);

        imgShowPassword.setOnClickListener(this);
        imgShowCPassword.setOnClickListener(this);
        tvRegisterNow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent ;
        switch (v.getId()){

            case R.id.ll_have_account:
                intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.help_me:
                intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:help@gvhssb.in.net?cc=madhabapatra@gmail.com&subject=" + "MLZS-Register Problem" + "&body=" + "Hii, I am facing problem in ");
                intent.setData(data);
                startActivity(Intent.createChooser(intent, "Send Email"));
                break;
            case R.id.tv_register_now:

                if(edName.getText().toString().equals("")){
                    Toast.makeText(this, getResources().getString(R.string.plz_enter_name), Toast.LENGTH_SHORT).show();
                }
                else if(edUserName.getText().toString().equals("")){
                    Toast.makeText(this, getResources().getString(R.string.plz_enter_user_name), Toast.LENGTH_SHORT).show();
                }
                else if(edEmail.getText().toString().equals("")){
                    Toast.makeText(this, getResources().getString(R.string.plz_enter_email), Toast.LENGTH_SHORT).show();
                }
                else if(!edEmail.getText().toString().matches(emailPattern)){
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.invalid_email_pattern), Toast.LENGTH_SHORT).show();
                }
                else if(edPassword.getText().toString().equals("")){
                    Toast.makeText(this, getResources().getString(R.string.plz_enter_pwd), Toast.LENGTH_SHORT).show();
                }
                else if(edConfirmPassword.getText().toString().equals("")){
                    Toast.makeText(this, getResources().getString(R.string.plz_enter_confirm_pwd), Toast.LENGTH_SHORT).show();
                }
                else if(!edPassword.getText().toString().equals(edConfirmPassword.getText().toString())){
                    Toast.makeText(this, getResources().getString(R.string.both_pwds_doent_match), Toast.LENGTH_SHORT).show();
                }
                else {

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("name",edName.getText().toString().trim());
                        jsonObject.put("email",edEmail.getText().toString().trim());
                        jsonObject.put("username",edUserName.getText().toString().trim());
                        jsonObject.put("password",edPassword.getText().toString().trim());
                        jsonObject.put("password_confirmation",edConfirmPassword.getText().toString().trim());
                        jsonObject.put("device_id",appState.getFCM_Id());

                        if(appState.getNetworkCheck()){
                            registerUser(jsonObject);
                        }
                        else {
                            Toast.makeText(RegisterActivity.this,getResources().getString(R.string.plz_check_network_connection), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                break;

            case R.id.img_register_show_pwd:
                pCount++;
                if (pCount % 2 == 0) {
                    edPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgShowPassword.setImageResource(R.drawable.eye_on);
                } else {
                    edPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imgShowPassword.setImageResource(R.drawable.eye_off);
                }
                break;
            case R.id.img_register_show_cpwd:
                cCount++;
                if (cCount % 2 == 0) {
                    edConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgShowCPassword.setImageResource(R.drawable.eye_on);
                } else {
                    edConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imgShowCPassword.setImageResource(R.drawable.eye_off);
                }
                break;
        }

    }

    public void registerUser(JSONObject jsonObject){

        Utils.showProgressDialog(this,"Loggin/Registering...");
        Utils.showProgress();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Utils.REGISTER, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (response != null) {

                            Utils.dissmisProgress();
                            String mssg,userName=null,email=null;
                            int status;
                            JSONObject errorObject = null ;
                            JSONArray jsonArray ;
                            try {
                                status = response.getInt("status");
                                mssg = response.getString("message");

                                if(response.has("errors")){
                                    errorObject = response.getJSONObject("errors");
                                }

                                if(status==1){
//                                    Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
//                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                    startActivity(i);
//                                    finish();
                                    userLogin();
                                    Toast.makeText(RegisterActivity.this, mssg, Toast.LENGTH_SHORT).show();
                                    Log.d("mytagerror",mssg);
                                }else {

                                    if(errorObject.has("username")){
                                        jsonArray = errorObject.getJSONArray("username");
                                        for(int i=0 ;i<jsonArray.length();i++){
                                            userName = jsonArray.getString(i);
                                        }
                                        Toast.makeText(RegisterActivity.this, userName, Toast.LENGTH_SHORT).show();
                                        Log.d("mytagerror",userName );
                                    }else if(errorObject.has("email")) {
                                        jsonArray = errorObject.getJSONArray("email");
                                        for(int i=0 ;i<jsonArray.length();i++){
                                            email = jsonArray.getString(i);
                                        }
                                        Toast.makeText(RegisterActivity.this, email, Toast.LENGTH_SHORT).show();
                                        Log.d("mytagerror",email);
                                    }

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

    public void userLogin() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", edUserName.getText().toString().trim());
            jsonObject.put("password", edPassword.getText().toString().trim());
            jsonObject.put("device_id", appState.getFCM_Id());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(this);
        Utils.showProgressDialog(this, "Logging/Registering...");
        Utils.showProgress();
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Utils.LOGIN, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Utils.dissmisProgress();
                        JSONObject object;
                        String mssg;
                        int status;

                        try {
                            status = response.getInt("status");
                            mssg = response.getString("message");

                            if (status == 1) {

                                object = response.getJSONObject("user");

                                appState.setEmail(object.getString("email"));
                                appState.setPhone(object.getString("phone"));
                                appState.setUserID(object.getString("id"));
                                appState.setUserName(object.getString("name"));
                                appState.setProfilePic(object.getString("image"));

                                AppController.getInstance().setUserName(object.getString("name"));
                                AppController.getInstance().setEmail(object.getString("email"));

                                editor.putString("user_id", object.getString("id"));
                                editor.putString("user_name", object.getString("name"));
                                editor.putString("profilePic", object.getString("image"));
                                editor.commit();

                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(RegisterActivity.this, mssg, Toast.LENGTH_SHORT).show();
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
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(i);
        finish();
    }
}
