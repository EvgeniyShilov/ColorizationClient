package by.evgeniyshilov.colorizationclient.api;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class UploadingTask extends AsyncTask<Object, Object, Throwable> {

    private Bitmap bitmap;

    protected UploadingTask(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    protected final Throwable doInBackground(Object... params) {
        try {
            String base64 = getBase64(bitmap);
            String id = "123";
            API.getInterface().loadImage(FirebaseInstanceId.getInstance().getToken(), id, base64)
                    .execute();
            return null;
        } catch (IOException e) {
            return e;
        }
    }

    private String getBase64(Bitmap original) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        original.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
