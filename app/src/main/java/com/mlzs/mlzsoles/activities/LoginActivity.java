package com.mlzs.mlzsoles.activities;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.mlzs.mlzsoles.utils.AppController;
import com.mlzs.mlzsoles.utils.BaseActivity;
import com.mlzs.mlzsoles.utils.Utils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class LoginActivity extends BaseActivity implements View.OnClickListener {

    TextView llCreateAccount;
    TextView tvLoginNow, tvPrivacyPolicy, tvForgotPassword;
    EditText edUserName, edPassword;
    ImageView imgShowPwd;
    Button btnFacebook, btnGoogle,mail_me;

    /*==============Google=====================*/
    //a constant for detecting the login intent result
    private static final int RC_SIGN_IN = 234;
    //Tag for the logs optional
    private static final String TAG = "loginactivity";
    //creating a GoogleSignInClient object
    public GoogleSignInClient mGoogleSignInClient;
    //And also a Firebase Auth object
    FirebaseAuth mAuth;

    CallbackManager callbackManager;

    private String facebook_id, f_name, m_name, l_name, profile_image, full_name, email_id;

    int count = 0;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getApplicationContext().getSharedPreferences(Utils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        llCreateAccount = (TextView) findViewById(R.id.ll_create_account);

        tvLoginNow = (TextView) findViewById(R.id.tv_login_now);
        edUserName = (EditText) findViewById(R.id.ed_login_username);
        edPassword = (EditText) findViewById(R.id.ed_login_password);

     //   btnFacebook = (Button) findViewById(R.id.btn_facebook);
     //   btnGoogle = (Button) findViewById(R.id.btn_google);

        imgShowPwd = (ImageView) findViewById(R.id.img_login_show_pwd);

        tvForgotPassword = (TextView) findViewById(R.id.tv_login_forgot_pwd);
        mail_me  = (Button) findViewById(R.id.mail_me);

        tvPrivacyPolicy = (TextView) findViewById(R.id.tv_privacy_policy);

     //   setDrawableColor(btnFacebook, R.color.white_color);
      //  setDrawableColor(btnGoogle, R.color.white_color);


        llCreateAccount.setOnClickListener(this);
        tvLoginNow.setOnClickListener(this);

        tvPrivacyPolicy.setOnClickListener(this);
        mail_me.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);

      //  btnGoogle.setOnClickListener(this);
       // btnFacebook.setOnClickListener(this);


        callbackManager = CallbackManager.Factory.create();

        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        FacebookSdk.sdkInitialize(getApplicationContext());
        printHashKey();

        imgShowPwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent;
        switch (v.getId()) {

            case R.id.ll_create_account:

                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;

            case R.id.mail_me:
              intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:help@gvhssb.in.net?cc=madhabapatra@gmail.com&subject=" + "MLZS OLES Support" + "&body=" + "I Visited MLZS OLES App, And i");
                intent.setData(data);
                startActivity(Intent.createChooser(intent, "Send Email"));
                break;

            case R.id.tv_login_now:

                if (edUserName.getText().toString().equals("")) {

                    edUserName.setError(getResources().getString(R.string.plz_enter_email));

                } else if (edPassword.getText().toString().equals("")) {

                    edPassword.setError(getResources().getString(R.string.plz_enter_pwd));

                } else {
                    if (appState.getNetworkCheck()) {
                        userLogin();
                    } else {
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.plz_check_network_connection), Toast.LENGTH_SHORT).show();
                    }

                }

                break;

            case R.id.tv_privacy_policy:

                intent = new Intent(this, PrivacyPolicyActivity.class);
                startActivity(intent);

                break;
/*
            case R.id.btn_google:
                com.google.android.gms.common.SignInButton gLogin = new SignInButton(this);
                gLogin.performClick();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;

  Temporary disbale facebook
            case R.id.btn_facebook:
                com.facebook.login.widget.LoginButton login = new LoginButton(this);
                login.setReadPermissions(Arrays.asList("email"));
                login.performClick();
                login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        facebook_id = f_name = m_name = l_name = profile_image = full_name = email_id = "";

                        if (AccessToken.getCurrentAccessToken() != null) {
                            RequestData();
                            Profile profile = Profile.getCurrentProfile();
                            if (profile != null) {
                                facebook_id = profile.getId();
                                f_name = profile.getFirstName();
                                m_name = profile.getMiddleName();
                                l_name = profile.getLastName();
                                full_name = profile.getName();
                                profile_image = profile.getProfilePictureUri(400, 400).toString();
                            }

                        }
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException exception) {
                    }
                });
                break;
*/
            case R.id.img_login_show_pwd:

                count++;
                if (count % 2 == 0) {
                    edPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    imgShowPwd.setImageResource(R.drawable.eye_on);
                } else {
                    edPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imgShowPwd.setImageResource(R.drawable.eye_off);
                }
                break;

            case R.id.tv_login_forgot_pwd:
                openDialog();
                break;
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {

            //Getting the GoogleSignIn Task
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                //Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //   authenticating with firebase
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {

            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        //getting the auth credential
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        //Now using firebase we are signing in the user here
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String gEmail, gName;

                            gEmail = acct.getEmail();
                            gName = acct.getDisplayName();

                            if (appState.getNetworkCheck()) {
                                socialAppLogin(gEmail, gName);
                            } else {
                                Toast.makeText(LoginActivity.this, getResources().getString(R.string.plz_check_network_connection), Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    public void printHashKey() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }


    public void RequestData() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                JSONObject json = response.getJSONObject();

                try {
                    if (json != null && response != null) {
                        String fbEmail = "", fbName;
                        if (json.has("email"))

                        fbEmail = response.getJSONObject().getString("email");
                        fbName = json.getString("name");

                        if (appState.getNetworkCheck()) {
                            socialAppLogin(fbEmail, fbName);
                        } else {
                            Toast.makeText(LoginActivity.this, getResources().getString(R.string.plz_check_network_connection), Toast.LENGTH_SHORT).show();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,email,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();


        finish();
    }


    private void openDialog() {
        LayoutInflater inflater = LayoutInflater.from(LoginActivity.this);
        View subView = inflater.inflate(R.layout.alert_forgot_pwd, null);
        final EditText email_id = (EditText) subView.findViewById(R.id.ed_forgot_pwd);
        final Button btnSubmit = (Button) subView.findViewById(R.id.btn_forgot_pwd);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Forgot Password ?");
        builder.setView(subView);
        alertDialog = builder.create();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {

                if (email_id.getText().toString().equals("")) {
                    email_id.setError("Please enter registered email id");
                } else {
                    if (appState.getNetworkCheck()) {
                        btnSubmit.setEnabled(false);
                        btnSubmit.setText("Validating....");
                        btnSubmit.setBackgroundColor(R.color.grey_color);
                        checkEmail(email_id.getText().toString());
                    } else {
                        Toast.makeText(LoginActivity.this, getResources().getString(R.string.plz_check_network_connection), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        alertDialog.show();
        //builder.show();
    }

    public void checkEmail(String strEmail) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", strEmail);
            jsonObject.put("user_type","student");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Utils.CHECK_EMAIL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int status;
                        String mssg;

                        try {
                            status = response.getInt("status");
                            Log.d("mystatus", String.valueOf(status));
                            mssg = response.getString("message");
                            Toast.makeText(LoginActivity.this, mssg, Toast.LENGTH_SHORT).show();
                            Log.d("mystatus", mssg);
                                alertDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
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

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(LoginActivity.this, mssg, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, errorListener) {
        };

        queue.add(jsonObjReq);

    }


    public void socialAppLogin(final String email, String name) {

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("email", email);
            jsonObject.put("name", name);
            jsonObject.put("device_id", appState.getFCM_Id());

            RequestQueue queue = Volley.newRequestQueue(this);
            Utils.showProgressDialog(this, "Loggin/Registering...");
            Utils.showProgress();
            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Utils.SOCIAL_LOGINS, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Utils.dissmisProgress();

                            try {
                                appState.setEmail(response.getString("email"));
                                appState.setUserID(response.getString("id"));
                                appState.setUserName(response.getString("name"));

                                AppController.getInstance().setEmail(response.getString("email"));
                                AppController.getInstance().setUserName(response.getString("name"));

                                if (response.has("image")) {
                                    appState.setProfilePic(response.getString("image"));
                                    editor.putString("profilePic", response.getString("image"));
                                }

                                editor.putString("user_id", response.getString("id"));
                                editor.putString("user_name", response.getString("name"));


                                editor.commit();

                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, errorListener) {

            };
            queue.add(jsonObjReq);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
