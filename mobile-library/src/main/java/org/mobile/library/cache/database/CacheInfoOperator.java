package org.mobile.library.cache.database;
/**
 * Created by 超悟空 on 2015/11/12.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.mobile.library.cache.util.CacheInfo;
import org.mobile.library.model.database.BaseOperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 缓存文件信息索引数据库操作工具
 *
 * @author 超悟空
 * @version 1.0 2015/11/12
 * @since 1.0
 */
public class CacheInfoOperator extends BaseOperator<CacheInfo> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "CacheInfoOperator.";

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
        Log.i(LOG_TAG + "query", "query sql: " + sql);

        // 查询数据
        Cursor cursor = sqLiteHelper.getReadableDatabase().rawQuery(sql, null);

        Log.i(LOG_TAG + "query", "result cursor count is " + cursor.getCount());

        // 数据填充
        List<CacheInfo> list = new ArrayList<>();

        // 列索引
        int key = cursor.getColumnIndex(CacheDatabaseConst.CACHE_INFO.KEY);
        int realFileName = cursor.getColumnIndex(CacheDatabaseConst.CACHE_INFO.REAL_FILE_NAME);
        int oldFileName = cursor.getColumnIndex(CacheDatabaseConst.CACHE_INFO.OLD_FILE_NAME);
        int fileType = cursor.getColumnIndex(CacheDatabaseConst.CACHE_INFO.FILE_TYPE);
        int timeout = cursor.getColumnIndex(CacheDatabaseConst.CACHE_INFO.TIMEOUT);
        int group = cursor.getColumnIndex(CacheDatabaseConst.CACHE_INFO.GROUP);
        int levelKey = cursor.getColumnIndex(CacheDatabaseConst.CACHE_INFO.LEVEL_KEY);

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

    /**
     * 查询单个缓存信息，目标不能是缓存组
     *
     * @param key      缓存key
     * @param levelKey 缓存层级key
     *
     * @return 缓存信息对象，如果目标不存在或目标为缓存组则返回null
     */
    public CacheInfo queryCacheInfo(String key, String levelKey) {
        Log.i(LOG_TAG + "queryCacheInfo", "query key is " + key);
        Log.i(LOG_TAG + "queryCacheInfo", "query levelKey is " + levelKey);

        // 查询sql
        String sql = String.format("select * from %s where %s='%s' and %s='%s' and %s=0",
                CacheDatabaseConst.CACHE_INFO.TABLE_NAME, CacheDatabaseConst.CACHE_INFO.KEY, key,
                CacheDatabaseConst.CACHE_INFO.LEVEL_KEY, levelKey, CacheDatabaseConst.CACHE_INFO
                        .GROUP);
        // 查询结果
        List<CacheInfo> list = query(sql);

        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 查询指定缓存层级中的全部缓存信息，目标不包括缓存组
     *
     * @param levelKey 缓存层级key
     *
     * @return 缓存信息对象集合，如果层级中没有缓存或仅包含缓存组则返回空集合
     */
    public List<CacheInfo> queryCacheInfo(String levelKey) {
        Log.i(LOG_TAG + "queryCacheInfo", "query levelKey is " + levelKey);

        // 查询sql
        String sql = String.format("select * from %s where %s='%s' and %s=0", CacheDatabaseConst
                .CACHE_INFO.TABLE_NAME, CacheDatabaseConst.CACHE_INFO.LEVEL_KEY, levelKey,
                CacheDatabaseConst.CACHE_INFO.GROUP);

        return query(sql);
    }

    /**
     * 查询缓存组信息
     *
     * @param key      缓存key
     * @param levelKey 缓存层级key
     *
     * @return 缓存组信息列表，如果目标组不存在或目标不是缓存组则回返回空集合
     */
    public List<CacheInfo> queryCacheGroup(String key, String levelKey) {
        Log.i(LOG_TAG + "queryCacheInfo", "query key is " + key);
        Log.i(LOG_TAG + "queryCacheInfo", "query levelKey is " + levelKey);

        // 查询sql
        String sql = String.format("select * from %s where %s='%s' and %s='%s' and %s=1",
                CacheDatabaseConst.CACHE_INFO.TABLE_NAME, CacheDatabaseConst.CACHE_INFO.KEY, key,
                CacheDatabaseConst.CACHE_INFO.LEVEL_KEY, levelKey, CacheDatabaseConst.CACHE_INFO
                        .GROUP);

        return query(sql);
    }

    /**
     * 查询指定缓存层级中的全部缓存组信息
     *
     * @param levelKey 缓存层级key
     *
     * @return 缓存组键值对集合
     */
    public Map<String, List<CacheInfo>> queryCacheGroup(String levelKey) {
        Log.i(LOG_TAG + "queryCacheInfo", "query levelKey is " + levelKey);

        // 查询sql
        String sql = String.format("select * from %s where %s='%s' and %s=1", CacheDatabaseConst
                .CACHE_INFO.TABLE_NAME, CacheDatabaseConst.CACHE_INFO.LEVEL_KEY, levelKey,
                CacheDatabaseConst.CACHE_INFO.GROUP);

        // 获取查询结果
        List<CacheInfo> list = query(sql);

        // 新建结果集
        Map<String, List<CacheInfo>> map = new HashMap<>();

        // 便利分组数据
        for (CacheInfo cacheInfo : list) {
            // 提取key
            String key = cacheInfo.getKey();

            // 验证是否已有该key
            if (map.containsKey(key)) {
                // key已存在
                // 提取对应列表并追加记录
                map.get(key).add(cacheInfo);
            } else {
                // key不存在
                // 新建列表并追加记录
                List<CacheInfo> cacheInfoList = new ArrayList<>();
                cacheInfoList.add(cacheInfo);
                map.put(key, cacheInfoList);
            }
        }

        return map;
    }

    @Override
    protected String onWhereSql(CacheInfo data) {
        return String.format("%s='%s'", CacheDatabaseConst.CACHE_INFO.REAL_FILE_NAME, data
                .getRealFileName());
    }
}