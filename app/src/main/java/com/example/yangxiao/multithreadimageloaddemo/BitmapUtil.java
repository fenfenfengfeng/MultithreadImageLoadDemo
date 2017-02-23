package com.example.yangxiao.multithreadimageloaddemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Author yangxiao on 2/20/2017.
 */

public class BitmapUtil {

    static void download(String url, ImageView iv) {
        Bitmap bitmap = ImageCache.instance().getBitmapFromCache(url);
        if (bitmap != null) {
            iv.setImageBitmap(bitmap);
        } else {
            if (cancelPotentialTask(url, iv)) {
                ImageDownloaderTask task = new ImageDownloaderTask(iv);
                ImageViewDrawable drawable = new ImageViewDrawable(task);
                iv.setImageDrawable(drawable);
                task.execute(url);
            }
        }
    }

    private static boolean cancelPotentialTask(String url, ImageView iv) {
        ImageDownloaderTask task = getTask(iv);
        if (task != null) {
            String imageUrl = task.url;
            if (imageUrl == null || !imageUrl.equalsIgnoreCase(url)) {
                task.cancel(true);
            } else {
                return false;
            }
        }
        return true;
    }

    public static ImageDownloaderTask getTask(ImageView iv) {
        if (iv != null) {
            Drawable dw = iv.getDrawable();
            if (dw instanceof ImageViewDrawable) {
                return ((ImageViewDrawable) dw).getTask();
            }
        }
        return null;
    }

    /**
     * download bitmap from url
     * @param url url
     * @return bitmap, null if download fail
     */
    static Bitmap downloadBitmap(String url) {
        InputStream stream = null;
        Bitmap bitmap = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            stream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(new FlushedInputStream(stream));
            if (stream != null)
                stream.close();
            connection.disconnect();
            ImageCache.instance().addBitmapToCache(url, bitmap);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) {
                    int b = read();
                    if (b < 0) {
                        break;  // we reached EOF
                    } else {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }
}
