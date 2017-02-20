package com.example.yangxiao.multithreadimageloaddemo;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.lang.ref.WeakReference;

/**
 * @Author yangxiao on 2/20/2017.
 */

public class ImageViewDrawable extends BitmapDrawable {
    private WeakReference<ImageDownloaderTask> mTask;
    public ImageViewDrawable(ImageDownloaderTask task) {
        super();
        mTask = new WeakReference<ImageDownloaderTask>(task);
    }

    public ImageDownloaderTask getTask() {
        if (mTask != null) {
            return mTask.get();
        } else {
            return null;
        }
    }


}
