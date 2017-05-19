package by.evgeniyshilov.colorizationclient.utils;

import android.graphics.Bitmap;

public final class BitmapHolder {

    private static Bitmap bitmap;

    private BitmapHolder() {

    }

    public static void save(Bitmap bitmap) {
        BitmapHolder.bitmap = bitmap;
    }

    public static Bitmap get() {
        Bitmap bitmap = BitmapHolder.bitmap;
        BitmapHolder.bitmap = null;
        return bitmap;
    }
}
