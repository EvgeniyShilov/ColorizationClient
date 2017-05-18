package by.evgeniyshilov.colorizationclient.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public final class ImageProcessor {

    private ImageProcessor() {

    }

    public static Bitmap getGrayScale(Bitmap original) {
        int width, height;
        height = original.getHeight();
        width = original.getWidth();
        Bitmap grayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(grayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(original, 0, 0, paint);
        return grayscale;
    }

    public static Bitmap resize(Bitmap original, float scale) {
        int width = original.getWidth();
        int height = original.getHeight();
        int newWidth = (int) (width * scale);
        int newHeight = (int) (height * scale);
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(original, 0, 0, width, height, matrix, false);
    }
}
