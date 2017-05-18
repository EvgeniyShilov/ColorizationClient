package by.evgeniyshilov.colorizationclient.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.IOException;

public abstract class DownloadingTask extends AsyncTask<Object, Object, Throwable> {

    private String id;
    private Bitmap bitmap;

    protected DownloadingTask(String id) {
        this.id = id;
    }

    @Override
    protected final Throwable doInBackground(Object... params) {
        try {
            String base64 = API.getInterface().getImage(id).execute().body();
            byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            return null;
        } catch (IOException e) {
            return e;
        }
    }

    protected final Bitmap getBitmap() {
        return bitmap;
    }
}
