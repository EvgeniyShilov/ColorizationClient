package by.evgeniyshilov.colorizationclient.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Pair;

public class CameraProvider {

    private static final String PHOTO_TITLE = "New";
    private static final String PHOTO_DESCRIPTION = "Picture for colorization";

    private static ContentValues values;

    private static ContentValues getContentValues() {
        if (values == null) {
            values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, PHOTO_TITLE);
            values.put(MediaStore.Images.Media.DESCRIPTION, PHOTO_DESCRIPTION);
        }
        return values;
    }

    public static Pair<Intent, Uri> getCameraIntent(Context context) {
        Uri imageUri = context.getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, getContentValues());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        return new Pair<>(intent, imageUri);
    }
}
