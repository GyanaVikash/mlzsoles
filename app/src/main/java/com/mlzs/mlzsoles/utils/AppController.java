package com.mlzs.mlzsoles.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;


public class AppController extends Application {

    private static AppController mInstance;
    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    private boolean networkCheck;

    private String email;
    private String userID;
    private String phone;
    private String userName;
    private String FCM_Id;
    private String profilePic;

    private boolean loadActivity ;

    private boolean settingSelected ;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Utils.overrideFont(this, "DEFAULT", "fonts/Sumana-Regular.ttf");
        sharedpreferences = getSharedPreferences(Utils.MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        loadData();


    }
    public static synchronized AppController getInstance() {
        return mInstance;
    }


    public void loadData(){
        userID = sharedpreferences.getString("user_id", "");
        userName=sharedpreferences.getString("user_name","");
        profilePic = sharedpreferences.getString("profilePic", "");
        networkCheck = sharedpreferences.getBoolean("networkCheck", true);
    }

    public void saveData(){

        editor.putString("user_id",userID);
        editor.putString("user_name",userName);
        editor.putString("profilePic",profilePic);
        editor.commit();
    }

    public boolean getSettingSelected() {
        return settingSelected;
    }

    public void setSettingSelected(boolean settingSelected) {
        this.settingSelected = settingSelected;
    }

    public boolean getLoadActivity() {
        return loadActivity;
    }

    public void setLoadActivity(boolean loadActivity) {
        this.loadActivity = loadActivity;
    }

    public boolean getNetworkCheck() {
        return networkCheck;
    }

    public void setNetworkCheck(boolean networkCheck) {
        this.networkCheck = networkCheck;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getFCM_Id() {
        return FCM_Id;
    }

    public void setFCM_Id(String FCM_Id) {
        this.FCM_Id = FCM_Id;
    }

    public void logout() {

        userID = null;
        userName=null;
        profilePic=null;

        editor.clear();
        editor.commit();
    }
}
