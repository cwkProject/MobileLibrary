package org.mobile.library.database.city;
/**
 * Created by 超悟空 on 2015/7/16.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 城市选择数据库工具
 *
 * @author 超悟空
 * @version 1.0 2015/7/16
 * @since 1.0
 */
public class CitySQLiteHelper extends SQLiteOpenHelper {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "CitySQLiteHelper.";

    public CitySQLiteHelper(Context context) {
        super(context, CityConst.DB_NAME, null, CityConst.DB_VERSION);
        Log.i(LOG_TAG + "CitySQLiteHelper", "CitySQLiteHelper is invoked");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(LOG_TAG + "onCreate", "onCreate is invoked");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(LOG_TAG + "onUpgrade", "onUpgrade is invoked");
    }

    /**
     * 判断数据库是否为空
     *
     * @return true表示为空
     */
    public boolean isEmpty() {
        String sql = String.format("select count(*) from sqlite_master where type='table' and " +
                "name='%s'", CityConst.PROVINCE_TABLE);
        Cursor cursor = getReadableDatabase().rawQuery(sql, null);
        if (cursor.moveToNext()) {
            if (cursor.getInt(0) > 0) {
                return false;
            }
        }
        cursor.close();

        return true;
    }
}
