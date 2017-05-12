package by.evgeniyshilov.colorizationclient.gcm;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String INTENT_ACTION = "PUSH_MESSAGE_RECEIVED";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Intent intent = new Intent(INTENT_ACTION);
        intent.putExtra("id", remoteMessage.getData().get("id"));
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
