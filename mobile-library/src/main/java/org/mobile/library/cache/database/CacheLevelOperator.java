package org.mobile.library.cache.database;
/**
 * Created by 超悟空 on 2015/11/12.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import org.mobile.library.cache.util.CacheLevel;
import org.mobile.library.model.database.BaseOperator;

import java.util.ArrayList;
import java.util.List;

/**
 * 缓存文件层级索引数据库操作工具
 *
 * @author 超悟空
 * @version 1.0 2015/11/12
 * @since 1.0
 */
public class CacheLevelOperator extends BaseOperator<CacheLevel> {

    /**
     * 构造函数
     *
     * @param context 上下文
     */
    public CacheLevelOperator(Context context) {
        super(context);
    }

    @Override
    protected SQLiteOpenHelper onCreateDatabaseHelper(Context context) {
        return new CacheSQLiteOpenHelper(context);
    }

    @Override
    protected String onCreateTableName() {
        return CacheDatabaseConst.CACHE_LEVEL.TABLE_NAME;
    }

    @Override
    protected ContentValues onFillData(CacheLevel data) {

        // 新建一个数据库键值对
        ContentValues values = new ContentValues();

        // 填充数据
        values.put(CacheDatabaseConst.CACHE_LEVEL.KEY, data.getKey());
        values.put(CacheDatabaseConst.CACHE_LEVEL.REAL_PATH, data.getRealPath());
        values.put(CacheDatabaseConst.CACHE_LEVEL.MAX_CAPACITY, data.getMaxCapacity());
        values.put(CacheDatabaseConst.CACHE_LEVEL.TIMEOUT, data.getTimeOut());
        values.put(CacheDatabaseConst.CACHE_LEVEL.REMARK, data.getRemark());

        return values;
    }

    @Override
    protected List<CacheLevel> query(String sql) {
        // 查询数据
        Cursor cursor = sqLiteHelper.getReadableDatabase().rawQuery(sql, null);

        // 列索引
        int key = cursor.getColumnIndex(CacheDatabaseConst.CACHE_LEVEL.KEY);
        int realPath = cursor.getColumnIndex(CacheDatabaseConst.CACHE_LEVEL.REAL_PATH);
        int maxCapacity = cursor.getColumnIndex(CacheDatabaseConst.CACHE_LEVEL.MAX_CAPACITY);
        int timeout = cursor.getColumnIndex(CacheDatabaseConst.CACHE_LEVEL.TIMEOUT);
        int remark = cursor.getColumnIndex(CacheDatabaseConst.CACHE_LEVEL.REMARK);

        // 数据填充
        List<CacheLevel> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            // 一条记录
            CacheLevel data = new CacheLevel(cursor.getString(key), cursor.getString(realPath));

            data.setMaxCapacity(cursor.getLong(maxCapacity));
            data.setTimeOut(cursor.getLong(timeout));
            data.setRemark(cursor.getString(remark));

            list.add(data);
        }

        // 关闭数据库
        cursor.close();
        close();

        return list;
    }

    @Override
    protected String onWhereSql(CacheLevel data) {
        return String.format("%s='%s'", CacheDatabaseConst.CACHE_LEVEL.KEY, data.getKey());
    }
}
