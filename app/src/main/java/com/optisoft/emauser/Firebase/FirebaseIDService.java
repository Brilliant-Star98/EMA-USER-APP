package com.optisoft.emauser.Firebase;

import android.util.Log;

import com.optisoft.emauser.HelperClasses.CommonPrefrence;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by OptiSoft_A on 1/20/2018.
 */

public class FirebaseIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";
    CommonPrefrence commonPrefrence = new CommonPrefrence();

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        commonPrefrence.setFbTockenPref(this, refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
    }
}
