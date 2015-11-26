package org.mobile.library.cache.util.convert;
/**
 * Created by 超悟空 on 2015/11/25.
 */

import android.util.Log;

import org.mobile.library.cache.util.CacheObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 输入流类型缓存转换器
 *
 * @author 超悟空
 * @version 1.0 2015/11/25
 * @since 1.0
 */
public class InputStreamCacheConvert implements CacheConvert<InputStream> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "TextCacheConvert.";

    @Override
    public CacheObject<InputStream> toCacheObject(InputStream cache) {
        throw new UnsupportedOperationException("File not take up memory");

    }

    @Override
    public InputStream toCache(CacheObject cacheObject) {
        throw new UnsupportedOperationException("File not take up memory");
    }

    @Override
    public InputStream toCache(String path) {

        if (path == null) {
            Log.d(LOG_TAG + "toCache", "path is null");
            return null;
        }

        try {
            return new FileInputStream(path);
        } catch (FileNotFoundException e) {
            Log.d(LOG_TAG + "saveFile", "FileNotFoundException is " + e.getMessage());
            return null;
        }
    }

    @Override
    public void saveFile(FileOutputStream outputStream, InputStream cache) {
        try {

            byte[] buffer = new byte[400000];
            int count = 0;
            while ((count = cache.read(buffer)) > 0) {
                outputStream.write(buffer, 0, count);
            }

            cache.close();

            outputStream.close();
        } catch (IOException e) {
            Log.d(LOG_TAG + "saveFile", "IOException is " + e.getMessage());
        }
    }
}
