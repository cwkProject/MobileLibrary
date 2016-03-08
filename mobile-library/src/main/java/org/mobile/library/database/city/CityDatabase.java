package org.mobile.library.database.city;
/**
 * Created by 超悟空 on 2015/7/18.
 */

import android.content.Context;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 城市数据库
 *
 * @author 超悟空
 * @version 1.0 2015/7/18
 * @since 1.0
 */
public class CityDatabase {
    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "CityDatabase.";

    /**
     * 首次运行复制数据库
     */
    public static void copyDatabase(Context context) {
        Log.i(LOG_TAG + "copyDatabase", "copyDatabase() is invoked");

        CityOperator cityOperator = new CityOperator(context);

        if (!cityOperator.isExist()) {
            // 数据库不存在
            Log.i(LOG_TAG + "copyDatabase", "no database");
            // 复制数据库

            try {
                //欲导入的数据库
                Log.i(LOG_TAG + "copyDatabase", "copy database begin");
                Log.i(LOG_TAG + "copyDatabase", "create database path");

                InputStream in = context.getAssets().open(CityConst.DB_NAME);
                Log.i(LOG_TAG + "copyDatabase", "InputStream open");
                FileOutputStream out = new FileOutputStream(context.getDatabasePath(CityConst
                        .DB_NAME).getAbsoluteFile());
                Log.i(LOG_TAG + "copyDatabase", "FileOutputStream open");
                byte[] buffer = new byte[1024];
                int count;
                while ((count = in.read(buffer)) > 0) {
                    out.write(buffer, 0, count);
                }
                Log.i(LOG_TAG + "copyDatabase", "copy database end");
                out.flush();
                out.close();
                in.close();
                Log.i(LOG_TAG + "copyDatabase", "stream close");
            } catch (IOException e) {
                Log.e(LOG_TAG + "copyDatabase", "IOException is " + e.getMessage());
            }
        }
    }
}
