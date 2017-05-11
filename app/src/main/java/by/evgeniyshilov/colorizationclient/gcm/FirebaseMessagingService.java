package by.evgeniyshilov.colorizationclient.gcm;

import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(FirebaseMessagingService.class.getSimpleName(), remoteMessage.getData().toString());
    }
}
