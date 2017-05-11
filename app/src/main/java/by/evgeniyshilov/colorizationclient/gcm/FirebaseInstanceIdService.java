package by.evgeniyshilov.colorizationclient.gcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

public class FirebaseInstanceIdService extends com.google.firebase.iid.FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(FirebaseInstanceIdService.class.getSimpleName(), "Refreshed token: " + refreshedToken);
    }
}
