package com.example.yangxiao.multithreadimageloaddemo;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * @Author yangxiao on 2/20/2017.
 */

public class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap>{
    public String url;
    private WeakReference<ImageView> referenceImageView;

    public ImageDownloaderTask(ImageView imageView) {
        referenceImageView = new WeakReference<ImageView>(imageView);
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        url = strings[0];
        return BitmapUtil.downloadBitmap(url);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            return;
        }
        if (referenceImageView != null) {
            ImageView imageView = referenceImageView.get();
            ImageDownloaderTask task = BitmapUtil.getTask(imageView);
            if (imageView != null && task != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
