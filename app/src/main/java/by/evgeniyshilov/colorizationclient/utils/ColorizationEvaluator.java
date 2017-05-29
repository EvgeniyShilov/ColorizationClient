package by.evgeniyshilov.colorizationclient.utils;

import android.graphics.Bitmap;

public class ColorizationEvaluator {

    private static Bitmap original;
    private static Bitmap result;

    public static void setOriginal(Bitmap original) {
        ColorizationEvaluator.original = original;
    }

    public static void setResult(Bitmap result) {
        ColorizationEvaluator.result = result;
    }

    public static Float getEvaluation() {
        if (original == null || result == null
                || original.getHeight() != result.getHeight()
                || original.getWidth() != result.getWidth())
            return null;
        int height = original.getHeight();
        int width = original.getWidth();
        float evaluationSum = 0;
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                int originalPixel = original.getPixel(i, j);
                int resultPixel = result.getPixel(i, j);
                int originalR = getRChannel(originalPixel);
                int resultR = getRChannel(resultPixel);
                int originalG = getGChannel(originalPixel);
                int resultG = getGChannel(resultPixel);
                int originalB = getBChannel(originalPixel);
                int resultB = getBChannel(resultPixel);
                evaluationSum += getEvaluationForChannel(originalR, resultR);
                evaluationSum += getEvaluationForChannel(originalG, resultG);
                evaluationSum += getEvaluationForChannel(originalB, resultB);
            }
        return evaluationSum / (height * width * 3);
    }

    private static float getEvaluationForChannel(int original, int result) {
        float maxDelta = original > 127 ? original : 255 - original;
        float error = Math.abs(result - original);
        return (maxDelta - error) / maxDelta;
    }

    private static int getRChannel(int pixel) {
        return (pixel & 0x00FF0000) >> 16;
    }

    private static int getGChannel(int pixel) {
        return (pixel & 0x0000FF00) >> 8;
    }

    private static int getBChannel(int pixel) {
        return (pixel & 0x000000FF);
    }

    public static void clearBitmaps() {
        original = null;
        result = null;
    }
}
