package org.mobile.library.cache.util.convert;
/**
 * Created by 超悟空 on 2015/11/25.
 */

import android.util.Log;

import org.mobile.library.cache.util.CacheObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;

/**
 * 文本类缓存转换器
 *
 * @author 超悟空
 * @version 1.0 2015/11/25
 * @since 1.0
 */
public class TextCacheConvert implements CacheConvert<String> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "TextCacheConvert.";

    @Override
    public CacheObject<String> toCacheObject(String cache) {
        return new CacheObject<>(cache, cache.length() * 2);
    }

    @Override
    public String toCache(CacheObject cacheObject) {
        if (cacheObject != null) {
            Object object = cacheObject.getCache();
            if (object instanceof String) {
                return (String) object;
            }
        }
        return null;
    }

    @Override
    public String toCache(String path) {

        if (path == null) {
            Log.d(LOG_TAG + "toCache", "path is null");
            return null;
        }

        Scanner scanner = null;

        try {
            scanner = new Scanner(new File(path));
            scanner.useDelimiter("");

            // 得到字符串
            StringBuilder builder = new StringBuilder();

            while (scanner.hasNext()) {
                builder.append(scanner.next());
            }

            return builder.toString();
        } catch (FileNotFoundException e) {
            Log.e(LOG_TAG + "toCache", "FileNotFoundException is " + e.getMessage());
            return null;
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    @Override
    public void saveFile(FileOutputStream outputStream, String cache) {

        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));

            writer.write(cache);

            writer.flush();

            writer.close();
        } catch (IOException e) {
            Log.e(LOG_TAG + "saveFile", "IOException is " + e.getMessage());
        }
    }
}
