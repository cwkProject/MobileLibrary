package org.mobile.library.model.database;
/**
 * Created by 超悟空 on 2015/9/21.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库操作基类
 *
 * @param <DataModel> 数据表对应的数据模型
 *
 * @author 超悟空
 * @version 1.1 2015/10/13
 * @since 1.0
 */
public abstract class BaseOperator<DataModel> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "BaseOperator.";

    /**
     * 写专用数据库工具
     */
    protected SQLiteOpenHelper writeSqLiteHelper = null;

    /**
     * 读专用数据库工具
     */
    protected SQLiteOpenHelper sqLiteHelper = null;

    /**
     * 数据库表名
     */
    private String tableName = null;

    /**
     * 构造函数
     *
     * @param context 上下文
     */
    public BaseOperator(Context context) {
        // 尝试读和写分别创建
        this.sqLiteHelper = onCreateReadDatabaseHelper(context);
        this.writeSqLiteHelper = onCreateWriteDatabaseHelper(context);

        if (this.sqLiteHelper == null && this.writeSqLiteHelper == null) {
            this.sqLiteHelper = this.writeSqLiteHelper = onCreateDatabaseHelper(context);
        } else {
            if (this.sqLiteHelper == null) {
                this.sqLiteHelper = onCreateDatabaseHelper(context);
            }

            if (this.writeSqLiteHelper == null) {
                this.writeSqLiteHelper = onCreateDatabaseHelper(context);
            }
        }

        tableName = onCreateTableName();
        // 创建数据库表
        onCreateTable(this.writeSqLiteHelper);
    }

    /**
     * 创建数据库工具
     *
     * @param context 上下文
     *
     * @return 已实现的数据库工具对象
     */
    protected abstract SQLiteOpenHelper onCreateDatabaseHelper(Context context);

    /**
     * 创建写入专用数据库工具<br>
     * 一般用于并发写入同步，
     * 多个数据库工具共享一个写入数据库连接
     *
     * @param context 上下文
     *
     * @return 数据库连接工具
     */
    protected SQLiteOpenHelper onCreateWriteDatabaseHelper(Context context) {
        return null;
    }

    /**
     * 创建读取专用数据库工具<br>
     * 一般用于并发读取，
     * 即每个数据库操作工具分配一个单独的读取数据库连接
     *
     * @param context 上下文
     *
     * @return 数据库连接工具
     */
    protected SQLiteOpenHelper onCreateReadDatabaseHelper(Context context) {
        return null;
    }

    /**
     * 创建数据库操作工具执行的表名
     *
     * @return 数据库表名
     */
    protected abstract String onCreateTableName();

    /**
     * 创建数据库表
     *
     * @param sqLiteHelper 数据库工具
     */
    protected void onCreateTable(SQLiteOpenHelper sqLiteHelper) {
    }

    /**
     * 填充一条记录
     *
     * @param data 一条记录
     *
     * @return 数据库键值对
     */
    protected abstract ContentValues onFillData(DataModel data);

    /**
     * 查询数据
     *
     * @param sql 完整的sql语句
     *
     * @return 装配好的一组结果
     */
    protected abstract List<DataModel> query(String sql);

    /**
     * 用于{@link #update(Object)}，{@link #delete(Object)}处理单一记录时的定位子句语句，
     * 不需要包含"where"，子句形如("column=value")
     *
     * @param data 要处理的记录
     *
     * @return 定位子句
     */
    protected abstract String onWhereSql(DataModel data);

    /**
     * 更新一条记录
     *
     * @param data 要更新的记录
     *
     * @return 受影响的行数
     */
    public final int update(DataModel data) {
        Log.i(LOG_TAG + "update", "update is invoked");

        if (data == null) {
            Log.i(LOG_TAG + "update", "data is null");
            return 0;
        }

        // 得到数据库写对象
        SQLiteDatabase dbWriter = writeSqLiteHelper.getWritableDatabase();

        // 数据库值对象
        ContentValues cv = onFillData(data);

        // where子句
        String whereSql = onWhereSql(data);

        Log.i(LOG_TAG + "update", "where sql is " + whereSql);
        // 执行更新
        int rowCount = dbWriter.update(tableName, cv, whereSql, null);

        Log.i(LOG_TAG + "update", "update row count is " + rowCount);

        close(writeSqLiteHelper);

        return rowCount;
    }

    /**
     * 删除一条记录
     *
     * @param data 要删除的记录
     *
     * @return 删除的记录数
     */
    public final int delete(DataModel data) {
        Log.i(LOG_TAG + "delete", "delete is invoked");

        if (data == null) {
            Log.i(LOG_TAG + "delete", "data is null");
            return 0;
        }

        // 得到数据库写对象
        SQLiteDatabase dbWriter = writeSqLiteHelper.getWritableDatabase();

        // where子句
        String whereSql = onWhereSql(data);

        Log.i(LOG_TAG + "delete", "where sql is " + whereSql);

        // 执行删除
        int rowCount = dbWriter.delete(tableName, whereSql, null);

        Log.i(LOG_TAG + "delete", "delete row count is " + rowCount);

        close(writeSqLiteHelper);

        return rowCount;
    }

    /**
     * 清空表
     *
     * @return 删除的行数
     */
    public final int clear() {
        Log.i(LOG_TAG + "clear", "clear is invoked");

        // 执行删除
        int rowCount = writeSqLiteHelper.getWritableDatabase().delete(tableName, null, null);

        close(writeSqLiteHelper);

        Log.i(LOG_TAG + "clear", "delete row count is " + rowCount);

        return rowCount;
    }

    /**
     * 查询表中全部记录
     *
     * @return 全部数据集
     */
    public final List<DataModel> queryAll() {
        Log.i(LOG_TAG + "queryAll", "query is invoked");

        final String sql = "select * from " + tableName;

        return query(sql);
    }

    /**
     * 按照固定查询规则查询表中部分符合条件的记录，
     * 需要子类重写实现
     *
     * @return 全部数据集
     */
    public List<DataModel> queryPart() {
        return null;
    }

    /**
     * 按照固定查询规则和给定的条件参数，
     * 查询表中部分符合条件的记录，
     * 需要子类重写实现
     *
     * @param parameters 条件参数集合
     *
     * @return 全部数据集
     */
    public List<DataModel> queryWithCondition(String... parameters) {
        return null;
    }

    /**
     * 插入一组数据
     *
     * @param dataList 要插入的数据集
     *
     * @return 成功插入的新行id
     */
    public final List<Long> insert(List<DataModel> dataList) {
        Log.i(LOG_TAG + "insert", "insert is invoked");

        if (dataList == null || dataList.size() == 0) {
            Log.d(LOG_TAG + "insert", "data list is null");
            return new ArrayList<>();
        }

        // 数据库写对象
        SQLiteDatabase dbWriter = writeSqLiteHelper.getWritableDatabase();
        List<Long> idList = new ArrayList<>();

        for (DataModel data : dataList) {
            long id = dbWriter.insert(tableName, null, onFillData(data));
            if (id > -1) {
                idList.add(id);
            }
        }

        close(writeSqLiteHelper);

        return idList;
    }

    /**
     * 插入一条数据
     *
     * @param data 要插入的数据对象
     *
     * @return 成功插入的新行id，插入失败返回-1
     */
    public final long insert(DataModel data) {
        Log.i(LOG_TAG + "insert", "insert is invoked");

        if (data == null) {
            Log.d(LOG_TAG + "insert", "data list is null");
            return -1;
        }

        // 数据库写对象
        SQLiteDatabase dbWriter = writeSqLiteHelper.getWritableDatabase();

        long id = dbWriter.insert(tableName, null, onFillData(data));

        close(writeSqLiteHelper);

        return id;
    }

    /**
     * 判断数据表是否为空
     *
     * @return 无记录返回true，有记录返回false
     */
    public synchronized final boolean isEmpty() {
        final String sql = "select count(*) from " + tableName;

        Cursor cursor = sqLiteHelper.getReadableDatabase().rawQuery(sql, null);

        if (cursor.moveToNext()) {
            if (cursor.getInt(0) > 0) {
                cursor.close();
                close();
                return false;
            }
        }

        cursor.close();
        close(sqLiteHelper);
        return true;
    }

    /**
     * 判断数据库表是否存在
     *
     * @return 存在返回true，不存在返回false
     */
    public synchronized final boolean isExist() {
        final String sql = String.format("select count(*) from sqlite_master where type='table' " +
                "and " + "name='%s'", tableName);
        Cursor cursor = sqLiteHelper.getReadableDatabase().rawQuery(sql, null);
        if (cursor.moveToNext()) {
            if (cursor.getInt(0) > 0) {
                cursor.close();
                close();
                return true;
            }
        }
        cursor.close();
        close(sqLiteHelper);
        return false;
    }

    /**
     * 关闭数据库
     *
     * @param sqLiteHelper 要关闭的数据库
     */
    public final void close(SQLiteOpenHelper sqLiteHelper) {
        sqLiteHelper.close();
    }

    /**
     * 关闭数据库
     */
    protected final void close() {
        writeSqLiteHelper.close();
        sqLiteHelper.close();
    }
}
