package org.mobile.library.cache.database;
/**
 * Created by 超悟空 on 2015/11/10.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 缓存文件信息索引数据库工具
 *
 * @author 超悟空
 * @version 1.0 2015/11/10
 * @since 1.0
 */
public class CacheSQLiteOpenHelper extends SQLiteOpenHelper {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "CacheSQLiteOpenHelper.";

    /**
     * 构造函数
     *
     * @param context 上下文
     */
    public CacheSQLiteOpenHelper(Context context) {
        super(context, CacheDatabaseConst.DB_NAME, null, CacheDatabaseConst.DB_VERSION);
        Log.i(LOG_TAG + "CacheSQLiteOpenHelper", "CacheSQLiteOpenHelper is instantiated");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(LOG_TAG + "onCreate", "onCreate is invoked");

        db.execSQL(CacheDatabaseConst.CACHE_LEVEL.CREATE_TABLE);
        db.execSQL(CacheDatabaseConst.CACHE_INFO.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
