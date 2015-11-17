package org.mobile.library.cache.util;
/**
 * Created by 超悟空 on 2015/11/14.
 */

/**
 * 缓存数据管理工具<br>
 * 管理缓存使用的硬缓存集合，软缓存集合，文件缓存工具，缓存索引数据库工具
 *
 * @author 超悟空
 * @version 1.0 2015/11/14
 * @since 1.0
 */
public class CacheManager {

    /**
     * 文本类型
     */
    public static final int FILE_TYPE_TEXT = 1;

    /**
     * 图片类型
     */
    public static final int FILE_TYPE_IMAGE = 2;

    /**
     * 文件类型
     */
    public static final int FILE_TYPE_FILE = 3;

    /**
     * 自定义缓存类型
     */
    public static final int FILE_TYPE_CACHE_OBJECT = 4;

    /**
     * 倍数毫秒
     */
    public static final long MAGNITUDE_MILLISECOND = 1;

    /**
     * 倍数秒
     */
    public static final long MAGNITUDE_SECOND = 1000 * MAGNITUDE_MILLISECOND;

    /**
     * 倍数分钟
     */
    public static final long MAGNITUDE_MINUTE = 60 * MAGNITUDE_SECOND;

    /**
     * 倍数小时
     */
    public static final long MAGNITUDE_HOUR = 60 * MAGNITUDE_MINUTE;

    /**
     * 倍数天
     */
    public static final long MAGNITUDE_DAY = 24 * MAGNITUDE_HOUR;

    /**
     * 倍数周
     */
    public static final long MAGNITUDE_WEEK = 7 * MAGNITUDE_DAY;

    /**
     * 倍数月(30天)
     */
    public static final long MAGNITUDE_MONTH = 30 * MAGNITUDE_DAY;
}
