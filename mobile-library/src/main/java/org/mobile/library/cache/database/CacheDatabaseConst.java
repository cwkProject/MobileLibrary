package org.mobile.library.cache.database;
/**
 * Created by 超悟空 on 2015/11/10.
 */

/**
 * 缓存文件信息索引数据库相关常量
 *
 * @author 超悟空
 * @version 1.0 2015/11/10
 * @since 1.0
 */
public interface CacheDatabaseConst {

    /**
     * ID列
     */
    String _ID = "_id";

    /**
     * 数据库名
     */
    String DB_NAME = "cache.db";

    /**
     * 数据库版本
     */
    int DB_VERSION = 1;

    /**
     * 缓存文件信息表
     */
    interface CACHE_INFO {

        /**
         * 表名
         */
        String TABLE_NAME = "tb_cache_info";

        /**
         * 缓存索引key字段名
         */
        String KEY = "key";

        /**
         * 真实缓存文件名字段名
         */
        String REAL_FILE_NAME = "real_file_name";

        /**
         * 缓存文件类型枚举字段名
         */
        String FILE_TYPE = "file_type";

        /**
         * 缓存文件是否为组的字段名
         */
        String GROUP = "cache_group";

        /**
         * 缓存文件超时时间字段名
         */
        String TIMEOUT = "timeout";

        /**
         * 缓存层级外键key字段名
         */
        String LEVEL_KEY = "level_key";

        /**
         * 标识缓存文件是否在外部存储中的字段名
         */
        String EXTERNAL = "external";

        /**
         * 建表语句
         */
        String CREATE_TABLE = String.format("CREATE TABLE IF NOT EXISTS %s ( %s INTEGER PRIMARY " +
                        "KEY, %s TEXT NOT NULL,  %s TEXT UNIQUE, %s INTEGER, %s INTEGER, " +
                        "%s INTEGER, %s TEXT NOT NULL, %s INTEGER, FOREIGN KEY(%s) REFERENCES %s" +
                        "(%s))", TABLE_NAME, _ID, KEY, REAL_FILE_NAME, FILE_TYPE, GROUP, TIMEOUT,
                LEVEL_KEY, EXTERNAL, LEVEL_KEY, CACHE_LEVEL.TABLE_NAME, CACHE_LEVEL.KEY);
    }

    /**
     * 缓存分级信息表
     */
    interface CACHE_LEVEL {

        /**
         * 表名
         */
        String TABLE_NAME = "tb_cache_level";

        /**
         * 缓存层路径key字段名
         */
        String KEY = "key";

        /**
         * 缓存层真实文件路径字段名
         */
        String REAL_PATH = "real_path";

        /**
         * 缓存层最大容量字段名
         */
        String MAX_CAPACITY = "max_capacity";

        /**
         * 子缓存文件超时时间字段名
         */
        String TIMEOUT = "timeout";

        /**
         * 备注字段名
         */
        String REMARK = "remark";

        /**
         * 建表语句
         */
        String CREATE_TABLE = String.format("CREATE TABLE IF NOT EXISTS %s ( %s INTEGER " +
                "PRIMARY KEY, %s TEXT UNIQUE,  %s TEXT UNIQUE, %s INTEGER, %s INTEGER, " +
                "%s TEXT)", TABLE_NAME, _ID, KEY, REAL_PATH, MAX_CAPACITY, TIMEOUT, REMARK);

        String INSERT_ROOT = String.format("insert into %s (%s,%s,%s,%s,%s) VALUES ('%s','%s',%s," +
                        "%s,'%s')" + "", TABLE_NAME, KEY, REAL_PATH, MAX_CAPACITY, TIMEOUT,
                REMARK, "root", "", 30 * 1024 * 1024, 0, "cache root path");
    }
}
