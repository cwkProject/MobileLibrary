package org.mobile.library.cache.util.convert;
/**
 * Created by 超悟空 on 2015/11/24.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import org.mobile.library.cache.util.CacheObject;

import java.io.FileOutputStream;

/**
 * Bitmap类缓存转换器
 *
 * @author 超悟空
 * @version 1.0 2015/11/24
 * @since 1.0
 */
public class BitmapCacheConvert implements CacheConvert<Bitmap> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "BitmapCacheConvert.";

    private static BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

    static {
        bitmapOptions.inPurgeable = true;
    }

    @Override
    public CacheObject<Bitmap> toCacheObject(Bitmap cache) {
        if (cache != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
                return new CacheObject<>(cache, cache.getRowBytes() * cache.getHeight());
            } else {
                return new CacheObject<>(cache, cache.getByteCount());
            }
        }

        return null;
    }

    @Override
    public Bitmap toCache(CacheObject cacheObject) {

        if (cacheObject != null) {
            Object object = cacheObject.getCache();
            if (object instanceof Bitmap) {
                return (Bitmap) object;
            }
        }
        return null;
    }

    @Override
    public Bitmap toCache(String path) {
        if (path == null) {
            Log.d(LOG_TAG + "toCache", "path is null");
            return null;
        }

        return BitmapFactory.decodeFile(path, bitmapOptions);
    }

    @Override
    public void saveFile(FileOutputStream outputStream, Bitmap cache) {
        cache.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
    }
}
