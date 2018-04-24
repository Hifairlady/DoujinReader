package com.edgar.doujinreader;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;

public class ImageDecoder {

    private static final String DEBUG_MSG = "================";

    @Nullable
    public static Bitmap decodeFromFile(Context context, File f, int width, int height) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
            options.inSampleSize = caculateInSampleSize(options, width, height);
            options.inPreferredConfig = Bitmap.Config.ARGB_4444;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError oome) {
            oome.printStackTrace();
        }
        Bitmap errorImage = decodeFromResources(context.getResources(), R.drawable.default_cover);
        return errorImage;
    }

    @Nullable
    public static Bitmap decodeFromResources(Resources resources, int resId) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            Bitmap bitmap = BitmapFactory.decodeResource(resources, resId, options);
            options.inSampleSize = caculateInSampleSize(options, 150, 150);
            options.inPreferredConfig = Bitmap.Config.ARGB_4444;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeResource(resources, resId, options);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError oome) {
            oome.printStackTrace();
        }
        return null;
    }

    private static int caculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;
        if (width > reqWidth || height > reqHeight) {
            int widthRadio = Math.round(width * 1.0f / reqWidth);
            int heightRadio = Math.round(height * 1.0f / reqHeight);
            inSampleSize = Math.max(widthRadio, heightRadio);
        }
        return inSampleSize;
    }

    //ascending
    public static void sortFilesByName(File[] files) {
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && o2.isFile())
                    return -1;
                if (o1.isFile() && o2.isDirectory())
                    return 1;

                if (o1.getName().length() < o2.getName().length()) {
                    return -1;
                }

                if (o1.getName().length() > o2.getName().length()) {
                    return 1;
                }

                return o1.getName().compareTo(o2.getName());
            }
        });
    }

    //ascending
    public static void sortStringsByName(String[] strs) {
        Arrays.sort(strs, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {

                if (o1.length() < o2.length()) {
                    return -1;
                }

                if (o1.length() > o2.length()) {
                    return 1;
                }

                return o1.compareTo(o2);
            }
        });
    }

    public static boolean checkFomat(String filename) {

        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg") ||
                filename.endsWith(".png") || filename.endsWith(".tiff") ||
                filename.endsWith(".JPG") || filename.endsWith(".JPEG") ||
                filename.endsWith(".PNG") || filename.endsWith(".TIFF") ||
                filename.endsWith(".BMP") || filename.endsWith(".bmp")) {
            return true;
        }

        return false;
    }
}
