package com.example.yangxiao.multithreadimageloaddemo;

import android.graphics.Bitmap;
import android.util.LruCache;

import java.util.HashMap;

/**
 * @Author yangxiao on 2/23/2017.
 */

public class ImageCache {
    private static ImageCache mCacheTool;
    private static int mCacheSize;
    private static LruCache<String, Bitmap> mLruCache;

    private ImageCache() {
        int maxMemory = (int) Runtime.getRuntime().maxMemory() / 1024;
        mCacheSize = maxMemory / 8;
        mLruCache = new LruCache<String, Bitmap>(mCacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
    }

    public static ImageCache instance() {
        if (mCacheTool == null) {
            synchronized (ImageCache.class) {
                if (mCacheTool == null) {
                    mCacheTool = new ImageCache();
                }
            }
        }
        return mCacheTool;
    }

    public void addBitmapToCache(String key, Bitmap bitmap) {
        if (getBitmapFromCache(key) == null) {
            mLruCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromCache(String key) {
        return mLruCache.get(key);
    }
}
