package com.mlzs.mlzsoles.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.mlzs.mlzsoles.R;
import com.mlzs.mlzsoles.model.MyProfile;
import com.mlzs.mlzsoles.utils.AppController;
import com.mlzs.mlzsoles.utils.BaseActivity;
import com.mlzs.mlzsoles.utils.Utils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;


public class MyProfileActivity extends BaseActivity {

    TextView tvTitle,tvUserName;
    ImageView imgBack,imgMenu;
    Toolbar toolbar;

    public GoogleSignInClient mGoogleSignInClient;

    RelativeLayout ediProfilePic,rlImage;
    private Bitmap bitmap;
    Snackbar snackbar;
    private static final int MY_CAMERA_REQUEST_CODE = 100;


    EditText edName,edUserName,edPhone,edAddress,edEmail;
    CircleImageView userPic;

    Button btnUpdateProfile;

    FloatingActionButton fabProfile ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_new);

        hideKeyboard();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, MY_CAMERA_REQUEST_CODE);
        }

      //  imgBack = (ImageView)findViewById(R.id.img_toolbar_back);
       // tvTitle = (TextView)findViewById(R.id.tv_toolbar_title);
       // toolbar = (Toolbar)findViewById(R.id.toolbar);
      //  setSupportActionBar(toolbar);


     //   tvUserName = (TextView)findViewById(R.id.tv_myprofile_username);
      //  ediProfilePic = (RelativeLayout)findViewById(R.id.rl_edit_profile_pic);
        edName = (EditText)findViewById(R.id.ed_profile_name);
        edUserName = (EditText)findViewById(R.id.ed_profile_user_name);
        edEmail = (EditText)findViewById(R.id.ed_profile_email);
        edPhone=(EditText)findViewById(R.id.ed_profile_phone);
        edAddress = (EditText)findViewById(R.id.ed_profile_address);
       // userPic = (CircleImageView)findViewById(R.id.img_profile_pic);
        btnUpdateProfile = (Button)findViewById(R.id.btn_profile_update);

        rlImage = findViewById(R.id.rl_top);


        fabProfile = findViewById(R.id.fab_profile);

        imgMenu = findViewById(R.id.img_profile_menu);

        imgBack = findViewById(R.id.img_profile_back);


       // tvTitle.setText(getString(R.string.my_profile));
        //setTextColorGradient(tvUserName,getResources().getColor(R.color.white_color),getResources().getColor(R.color.white_color));


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

//        ediProfilePic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectImage();
//            }
//        });

        fabProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();
            }
        });


        if(appState.getNetworkCheck()){
            getMyProfile();
        }else {
            Toast.makeText(this, getResources().getString(R.string.plz_check_network_connection), Toast.LENGTH_SHORT).show();
        }



        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edName.getText().toString().equals("")){
                    edName.setError("Please enter name");
                }
                else if(edPhone.getText().toString().equals("")){
                    edPhone.setError("Please enter mobile");
                }
                else if(edAddress.getText().toString().equals("")){
                    edAddress.setError("Please enter address");
                }
                else {

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("name",edName.getText().toString().trim());
                        jsonObject.put("phone",edPhone.getText().toString().trim());
                        jsonObject.put("address",edAddress.getText().toString().trim());
                        jsonObject.put("image","");

                        updateProfile(jsonObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MyProfileActivity.this, imgMenu);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu_myprofile, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        int id = item.getItemId();
                        switch (id){
                            case R.id.change_pwd:

                                Intent intent = new Intent(MyProfileActivity.this,ChangePasswordActivity.class);
                                startActivity(intent);
                                return true;

                            case R.id.logout:
                                showLogoutDialog();
                                return true;

                        }

                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        });

    }


    public void selectImage() {
        final CharSequence[] items = {getString(R.string.take_photo), getString(R.string.choose_from_gallery)};
        AlertDialog.Builder builder = new AlertDialog.Builder(MyProfileActivity.this);
        builder.setTitle(getString(R.string.add_photo));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals(getString(R.string.take_photo))) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 0);
                } else if (items[item].equals(getString(R.string.choose_from_gallery))) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(photoPickerIntent, 1);
                }
            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_myprofile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.change_pwd:

                Intent intent = new Intent(MyProfileActivity.this,ChangePasswordActivity.class);
                startActivity(intent);
                return true;

            case R.id.logout:
               showLogoutDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showLogoutDialog(){
                AlertDialog.Builder alert = new AlertDialog.Builder(MyProfileActivity.this);
                alert.setTitle(R.string.want_to_logout);
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        Toast.makeText(MyProfileActivity.this, getResources().getString(R.string.logged_out_successfully), Toast.LENGTH_SHORT).show();

                         appState.logout();

                        googleSignOut();

                        Intent i = new Intent(MyProfileActivity.this, LoginActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // TODO Auto-generated method stub
                    }
                });
                alert.show();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1){
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                    long imgSize = getImageSize(uri);

                    if(imgSize>5){
                        snackbar = Snackbar.make(ediProfilePic,"File size should be <=5 MB,to reduce size",Snackbar.LENGTH_LONG);
                        snackbar.setAction("Click Here", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.aconvert.com/"));
                                startActivity(browserIntent);
                            }
                        });

                        snackbar.show();

                    }else {

                       ImageUploadToServerFunction(bitmap, Utils.UPLOAD_PROFILE_PIC);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            else if (requestCode == 0){
                bitmap = (Bitmap) data.getExtras().get("data");
                ImageUploadToServerFunction(bitmap,Utils.UPLOAD_PROFILE_PIC);
            }

        }

    }

    public  void googleSignOut(){

        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete( Task<Void> task) {
                        // Toast.makeText(ActivityMain.this, "Google Sign Out done.", Toast.LENGTH_SHORT).show();
                        revokeAccess();
                    }
                });
    }

    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete( Task<Void> task) {
                        // Toast.makeText(ActivityMain.this, "Google access revoked.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }


    public void getMyProfile(){

        Utils.showProgressDialog(this,"");
        Utils.showProgress();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, Utils.GET_MY_PROFILE+getUserID(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.dissmisProgress();
                        String mssg;
                        int status;
                        JSONObject jsonObject;
                        try {
                            status = response.getInt("status");
                            if(status==1){

                                jsonObject = response.getJSONObject("user");

                                AppController.getInstance().setUserName(jsonObject.getString("name"));
                                AppController.getInstance().setEmail(jsonObject.getString("email"));

                                editor.putString("profilePic",jsonObject.getString("image"));
                                editor.putString("user_name",jsonObject.getString("name"));

                                editor.commit();

                                Gson gson = new Gson();
                                MyProfile profileJson = gson.fromJson(jsonObject.toString(), MyProfile.class);
                                setDetails(profileJson);

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, errorListener) {

        };
        queue.add(jsonObjReq);

    }

    public void setDetails(MyProfile profile){

        if(profile.getName()!=null){
            edName.setText(profile.getName());
        }

        if(profile.getUsername()!=null){
            edUserName.setText(profile.getUsername());
        }

        if(profile.getEmail()!=null){
            edEmail.setText(profile.getEmail());
        }

        if(profile.getPhone()!=null){
           edPhone.setText(profile.getPhone());
        }

        if(profile.getAddress()!=null){
            edAddress.setText(profile.getAddress());
        }

        if(profile.getImage()!=null&&!profile.getImage().equals("")){


            Glide.with(this).load(Utils.USER_PIC_BASE_URL+profile.getImage()).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        rlImage.setBackground(resource);
                    }
                }
            });

        }else {

            rlImage.setBackgroundColor(ContextCompat.getColor(this,R.color.green_500));

//            Glide.with(this)
//                 .load(Utils.USER_PIC_BASE_URL+"default.png")
//                  .into(userPic);
        }

    }

    public void updateProfile(JSONObject jsonObject){

        Utils.showProgressDialog(this,"");
        Utils.showProgress();
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, Utils.UPDATE_PROFILE+getUserID(), jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Utils.dissmisProgress();
                        String mssg;
                        int status;
                        try {
                            status = response.getInt("status");
                            mssg = response.getString("message");
                            Toast.makeText(MyProfileActivity.this, mssg, Toast.LENGTH_SHORT).show();

                            getMyProfile();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, errorListener) {


        };
        queue.add(jsonObjReq);
    }

    public void ImageUploadToServerFunction(final Bitmap bitmap, final String URL) {

        ByteArrayOutputStream byteArrayOutputStreamObject;
        byteArrayOutputStreamObject = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStreamObject);
        byte[] byteArrayVar = byteArrayOutputStreamObject.toByteArray();

        final String ConvertImage = Base64.encodeToString(byteArrayVar, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void, Void, String> {
            ProgressDialog    progressDialog ;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                progressDialog   = ProgressDialog.show(MyProfileActivity.this, "Image is Uploading", "Please Wait", false, false);
            }

            @Override
            protected String doInBackground(Void... params) {
                ImageProcessClass imageProcessClass = new ImageProcessClass();

                JSONObject jsonObject = new JSONObject();
                try {
                 jsonObject.put("user_id",getUserID());
                    jsonObject.put("image", ConvertImage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                String FinalData = imageProcessClass.ImageHttpRequest(URL, jsonObject.toString());

                return FinalData;
            }

            @Override
            protected void onPostExecute(String res) {
                super.onPostExecute(res);

                progressDialog.dismiss();

                String mssg,imageName;
                int status;

                try {
                    JSONObject obj = new JSONObject(res);
                    mssg = obj.getString("message");
                    status = obj.getInt("status");

                    if(status==1){

                        imageName = obj.getString("image_name");
                        AppController.getInstance().setProfilePic(imageName);

                        editor.putString("profilePic",imageName);
                        editor.commit();

                        Drawable image = new BitmapDrawable(getResources(), bitmap);

                        rlImage.setBackground(image);

                        Toast.makeText(MyProfileActivity.this, mssg, Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(MyProfileActivity.this, mssg+"", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }

        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();

    }

    public class ImageProcessClass {

        public String ImageHttpRequest(String requestURL, String s) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                URL url;
                HttpURLConnection httpURLConnectionObject;
                OutputStream OutPutStream;
                BufferedWriter bufferedWriterObject;
                BufferedReader bufferedReaderObject;
                int RC;
                url = new URL(requestURL);
                httpURLConnectionObject = (HttpURLConnection) url.openConnection();
                httpURLConnectionObject.setReadTimeout(19000);
                httpURLConnectionObject.setConnectTimeout(19000);
                httpURLConnectionObject.setRequestMethod("POST");
                httpURLConnectionObject.setRequestProperty("Content-Type", "application/json");
                httpURLConnectionObject.setDoInput(true);
                httpURLConnectionObject.setDoOutput(true);
                OutPutStream = httpURLConnectionObject.getOutputStream();
                bufferedWriterObject = new BufferedWriter(
                new OutputStreamWriter(OutPutStream, "UTF-8"));
                bufferedWriterObject.write(s);
                bufferedWriterObject.flush();
                bufferedWriterObject.close();
                OutPutStream.close();
                RC = httpURLConnectionObject.getResponseCode();
                if (RC == HttpsURLConnection.HTTP_OK) {
                    bufferedReaderObject = new BufferedReader(new InputStreamReader(httpURLConnectionObject.getInputStream()));
                    stringBuilder = new StringBuilder();
                    String RC2;
                    while ((RC2 = bufferedReaderObject.readLine()) != null) {
                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {
            StringBuilder stringBuilderObject;
            stringBuilderObject = new StringBuilder();
            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
                stringBuilderObject.append("&");
                stringBuilderObject.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));
                stringBuilderObject.append("=");
                stringBuilderObject.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }
            return stringBuilderObject.toString();
        }

    }
}
