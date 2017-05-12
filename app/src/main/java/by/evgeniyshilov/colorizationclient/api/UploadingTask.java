package by.evgeniyshilov.colorizationclient.api;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

import by.evgeniyshilov.colorizationclient.utils.ImageProcessor;

public abstract class UploadingTask extends AsyncTask<Object, Object, Throwable> {

    private Bitmap bitmap;

    protected UploadingTask(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    protected final Throwable doInBackground(Object... params) {
        try {
            String base64 = ImageProcessor.getBase64(bitmap);
            String id = "123";
            API.getInterface().loadImage(FirebaseInstanceId.getInstance().getToken(), id, base64)
                    .execute();
            return null;
        } catch (IOException e) {
            return e;
        }
    }
}
