package com.mlzs.mlzsoles.notifications;

import android.content.SharedPreferences;


import com.mlzs.mlzsoles.utils.AppController;
import com.mlzs.mlzsoles.utils.Utils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {


    private static final String TAG = MyFirebaseInstanceIdService.class.getSimpleName();

    AppController appState;
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        appState = ((AppController)MyFirebaseInstanceIdService.this.getApplicationContext());

        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);
        appState.setFCM_Id(refreshedToken);

        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);

    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Utils.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
    }

}
