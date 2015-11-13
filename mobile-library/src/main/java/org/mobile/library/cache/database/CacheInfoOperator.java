package org.mobile.library.cache.database;
/**
 * Created by 超悟空 on 2015/11/12.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import org.mobile.library.cache.util.CacheInfo;
import org.mobile.library.model.database.BaseOperator;

import java.util.ArrayList;
import java.util.List;

/**
 * 缓存文件信息索引数据库操作工具
 *
 * @author 超悟空
 * @version 1.0 2015/11/12
 * @since 1.0
 */
public class CacheInfoOperator extends BaseOperator<CacheInfo> {

    /**
     * 构造函数
     *
     * @param context 上下文
     */
    public CacheInfoOperator(Context context) {
        super(context);
    }

    @Override
    protected SQLiteOpenHelper onCreateDatabaseHelper(Context context) {
        return new CacheSQLiteOpenHelper(context);
    }

    @Override
    protected String onCreateTableName() {
        return CacheDatabaseConst.CACHE_INFO.TABLE_NAME;
    }

    @Override
    protected ContentValues onFillData(CacheInfo data) {
        // 新建一个数据库键值对
        ContentValues values = new ContentValues();

        // 填充数据
        values.put(CacheDatabaseConst.CACHE_INFO.KEY, data.getKey());
        values.put(CacheDatabaseConst.CACHE_INFO.REAL_FILE_NAME, data.getRealFileName());
        values.put(CacheDatabaseConst.CACHE_INFO.OLD_FILE_NAME, data.getOldFileName());
        values.put(CacheDatabaseConst.CACHE_INFO.FILE_TYPE, data.getType());
        values.put(CacheDatabaseConst.CACHE_INFO.TIMEOUT, data.getTimeOut());
        values.put(CacheDatabaseConst.CACHE_INFO.GROUP, data.isGroupTag() ? 1 : 0);
        values.put(CacheDatabaseConst.CACHE_INFO.LEVEL_KEY, data.getLevelKey());

        return values;
    }

    @Override
    protected List<CacheInfo> query(String sql) {
        // 查询数据
        Cursor cursor = sqLiteHelper.getReadableDatabase().rawQuery(sql, null);

        // 列索引
        int key = cursor.getColumnIndex(CacheDatabaseConst.CACHE_INFO.KEY);
        int realFileName = cursor.getColumnIndex(CacheDatabaseConst.CACHE_INFO.REAL_FILE_NAME);
        int oldFileName = cursor.getColumnIndex(CacheDatabaseConst.CACHE_INFO.OLD_FILE_NAME);
        int fileType = cursor.getColumnIndex(CacheDatabaseConst.CACHE_INFO.FILE_TYPE);
        int timeout = cursor.getColumnIndex(CacheDatabaseConst.CACHE_INFO.TIMEOUT);
        int group = cursor.getColumnIndex(CacheDatabaseConst.CACHE_INFO.GROUP);
        int levelKey = cursor.getColumnIndex(CacheDatabaseConst.CACHE_INFO.LEVEL_KEY);

        // 数据填充
        List<CacheInfo> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            // 一条记录
            CacheInfo data = new CacheInfo(cursor.getString(key), cursor.getString(realFileName),
                    cursor.getString(levelKey));

            data.setOldFileName(cursor.getString(oldFileName));
            data.setTimeOut(cursor.getLong(timeout));
            data.setType(cursor.getInt(fileType));
            data.setGroupTag(cursor.getInt(group) == 1);

            list.add(data);
        }

        // 关闭数据库
        cursor.close();
        close();

        return list;
    }

    @Override
    public List<CacheInfo> queryWithCondition(String... parameters) {




        return super.queryWithCondition(parameters);
    }

    @Override
    protected String onWhereSql(CacheInfo data) {
        return String.format("%s='%s'", CacheDatabaseConst.CACHE_INFO.KEY, data.getKey());
    }
}
