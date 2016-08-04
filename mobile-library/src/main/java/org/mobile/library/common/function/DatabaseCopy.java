package org.mobile.library.common.function;
/**
 * Created by 超悟空 on 2016/7/23.
 */

import android.content.Context;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 用于数据库复制，从assets中解压到项目目录
 *
 * @author 超悟空
 * @version 1.0 2016/7/23
 * @since 1.0
 */
public class DatabaseCopy {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "DatabaseCopy.";

    /**
     * 首次运行复制数据库
     */
    public static void copy(Context context, String databaseName) {
        Log.v(LOG_TAG + "copy", "copy() is invoked");

        try {
            //欲导入的数据库
            Log.v(LOG_TAG + "copy", "copy database begin");
            Log.v(LOG_TAG + "copy", "create database path");

            InputStream in = context.getAssets().open(databaseName);
            Log.v(LOG_TAG + "copy", "InputStream open");
            FileOutputStream out = new FileOutputStream(context.getDatabasePath(databaseName)
                    .getAbsoluteFile());
            Log.v(LOG_TAG + "copy", "FileOutputStream open");
            byte[] buffer = new byte[1024];
            int count;
            while ((count = in.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }
            Log.v(LOG_TAG + "copy", "copy database end");
            out.flush();
            out.close();
            in.close();
            Log.v(LOG_TAG + "copy", "stream close");
        } catch (IOException e) {
            Log.e(LOG_TAG + "copy", "IOException error", e);
        }
    }
}
