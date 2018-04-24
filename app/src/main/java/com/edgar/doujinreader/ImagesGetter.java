package com.edgar.doujinreader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import java.io.File;

public class ImagesGetter {

    private static final String DEBUG_MSG = "================";
    private LruCache<String, Bitmap> mMemoryCache;
    private Context context;

    public ImagesGetter(Context context) {
        this.context = context;
        int maxMemory = (int)(Runtime.getRuntime().totalMemory() / 1024);
        int cacheSize = maxMemory / 8;
        this.mMemoryCache = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
    }

    public void startSetImageTask(ImageView imageView, String filename) {
        new SetImageTask(imageView).execute(filename);
    }

    private class SetImageTask extends AsyncTask<String, Void, Integer> {
        private ImageView imageview;
        private Bitmap bitmap;

        public SetImageTask(ImageView imageview) {
            this.imageview = imageview;
        }

        @Override
        protected Integer doInBackground(String... params) {
            String filename = params[0];
            bitmap = mMemoryCache.get(filename);
            if (bitmap != null) {
                return 1;
            }
            bitmap = ImageDecoder.decodeFromFile(context, new File(filename), 1080, 1920);
            if (bitmap != null) {
                mMemoryCache.put(filename, bitmap);
            } else {
                return 0;
            }
            return 1;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == 1) {
                imageview.setImageBitmap(bitmap);
            }
            super.onPostExecute(result);
        }

    }
}
