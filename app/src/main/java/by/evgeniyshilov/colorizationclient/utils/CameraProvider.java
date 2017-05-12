package by.evgeniyshilov.colorizationclient.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Pair;

public class CameraProvider {

    public static final String PHOTO_TITLE = "New";
    public static final String PHOTO_DESCRIPTION = "Picture for colorization";

    private static CameraProvider instance;

    private ContentValues values;

    private CameraProvider() {
        values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, PHOTO_TITLE);
        values.put(MediaStore.Images.Media.DESCRIPTION, PHOTO_DESCRIPTION);
    }

    public static CameraProvider getInstance() {
        if (instance == null) instance = new CameraProvider();
        return instance;
    }

    public Pair<Intent, Uri> getCameraIntent(Context context) {
        Uri imageUri = context.getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        return new Pair<>(intent, imageUri);
    }
}
