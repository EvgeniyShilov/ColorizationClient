package by.evgeniyshilov.colorizationclient.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import java.io.IOException;

public abstract class GetBitmapTask extends AsyncTask<Object, Object, Throwable> {

    private static final float DEFAULT_SCALE = 0.25f;

    private Context context;
    private Uri uri;
    private Bitmap bitmap;

    protected GetBitmapTask(Context context, Uri uri) {
        this.context = context;
        this.uri = uri;
    }

    @Override
    protected final Throwable doInBackground(Object[] params) {
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            bitmap = ImageProcessor.resize(bitmap, DEFAULT_SCALE);
            ColorizationEvaluator.setOriginal(bitmap);
            bitmap = ImageProcessor.getGrayScale(bitmap);
            return null;
        } catch (IOException e) {
            return e;
        }
    }

    protected final Bitmap getBitmap() {
        return bitmap;
    }
}
