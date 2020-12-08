package com.sjh.common_utils.common;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import java.io.ByteArrayOutputStream;

public class BitmapUtils {

    public static Bitmap viewConvertBitmap(View v) {
        int w = v.getWidth();
        int h = v.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);

        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */

//        v.layout(0, 0, w, h);
        v.draw(c);

        return bmp;
    }

    /**
     * 将Drawable对象转换成Bitmap对象
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            if (!(drawable instanceof ColorDrawable)) {
                Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                        Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Bitmap对象转换成Drawable对象
     * @param bitmap
     * @return
     */
    public static Drawable bitmapToDrawable(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            return new BitmapDrawable(Resources.getSystem(), bitmap);
        }

        return null;
    }

    /**
     * 缩放Bitmap
     * @param sourceBitmap
     * @param maxSize
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap sourceBitmap, int maxSize) {
        if (sourceBitmap != null && !sourceBitmap.isRecycled()) {
            int bitmapSize = sourceBitmap.getByteCount();
            if (bitmapSize > maxSize) {
                float scale = maxSize * 1.0f / bitmapSize;
                Matrix matrix = new Matrix();
                matrix.postScale(scale, scale);
                return Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight(), matrix, true);
            } else {
                return sourceBitmap;
            }
        }

        return null;
    }

    /**
     * 根据指定宽度和高度缩放Bitmap
     * @param sourceBitmap
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Bitmap zoomBitmap(Bitmap sourceBitmap, float newWidth, float newHeight) {
        if (sourceBitmap != null && !sourceBitmap.isRecycled()) {
            float width = sourceBitmap.getWidth();
            float height = sourceBitmap.getHeight();
            Matrix matrix = new Matrix();
            // 计算宽高缩放率
            float scaleWidth = newWidth / width;
            float scaleHeight = newHeight / height;
            // 对图片进行缩放
            matrix.postScale(scaleWidth, scaleHeight);
            return Bitmap.createBitmap(sourceBitmap, 0, 0, (int) width, (int) height, matrix, true);
        }

        return null;
    }

    /**
     * 根据指定宽度和高度缩放Drawable
     * @param drawable
     * @param newWidth
     * @param newHeight
     * @return
     */
    public static Drawable zoomDrawable(Drawable drawable, int newWidth, int newHeight) {
        Bitmap bitmap = drawableToBitmap(drawable);
        return bitmapToDrawable(zoomBitmap(bitmap, newWidth, newHeight));
    }

    /**
     * 获取Bitmap对象的字节流
     * @param bitmap
     * @param needRecycle
     * @return
     */
    public static byte[] bitmapToByteArray(final Bitmap bitmap, final boolean needRecycle) {
        byte[] result = null;
        if (bitmap != null && !bitmap.isRecycled()) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            if (needRecycle) {
                bitmap.recycle();
            }

            result = output.toByteArray();
            try {
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static Bitmap scale(final Bitmap src,
                               final int newWidth,
                               final int newHeight,
                               final boolean recycle) {
        if (isEmptyBitmap(src)) return null;
        Bitmap ret = Bitmap.createScaledBitmap(src, newWidth, newHeight, true);
        if (recycle && !src.isRecycled() && ret != src) src.recycle();
        return ret;
    }


    private static boolean isEmptyBitmap(final Bitmap src) {
        return src == null || src.getWidth() == 0 || src.getHeight() == 0;
    }

}
