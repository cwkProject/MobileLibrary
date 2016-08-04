package org.mobile.library.cache.util;
/**
 * Created by 超悟空 on 2015/11/14.
 */

import android.support.annotation.Nullable;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.mobile.library.cache.util.convert.BitmapCacheConvert;
import org.mobile.library.cache.util.convert.InputStreamCacheConvert;
import org.mobile.library.cache.util.convert.TextCacheConvert;
import org.mobile.library.global.Global;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
     * 日志标签前缀
     */
    private static final String LOG_TAG = "CacheManager.";

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
     * 同时在内存和文件进行缓存
     */
    public static final int MEMORY_WITH_FILE = 0;

    /**
     * 仅文件缓存
     */
    public static final int ONLY_FILE_CACHE = 1;

    /**
     * 仅内存缓存
     */
    public static final int ONLY_MEMORY_CACHE = 2;

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

    /**
     * 内存缓存
     */
    private static MemoryCache memoryCache = null;

    /**
     * 文件操作工具
     */
    private static CacheFileUtil cacheFileUtil = new CacheFileUtil(Global.getContext());

    /**
     * 存放缓存工具的集合
     */
    private static Map<String, CacheTool> cacheToolMap = new ConcurrentHashMap<>();

    /**
     * 图片对象转换器
     */
    private static BitmapCacheConvert bitmapCacheConvert = new BitmapCacheConvert();

    /**
     * 文本对象转换器
     */
    private static TextCacheConvert textCacheConvert = new TextCacheConvert();

    /**
     * 输入流对象转换器
     */
    private static InputStreamCacheConvert inputStreamCacheConvert = new InputStreamCacheConvert();

    /**
     * 根目录key
     */
    public static final String ROOT = "root";

    /**
     * 获取根缓存工具，缓存文件会被直接存放到程序包缓存目录中
     *
     * @return 缓存工具
     */
    public static CacheTool getCacheTool() {
        return getCacheTool(ROOT);
    }

    /**
     * 获取一个缓存工具，key指定缓存分级标签，没有会被新建，在文件系统中为一个目录
     *
     * @param key 层级标签
     *
     * @return 缓存工具
     */
    public static CacheTool getCacheTool(@NotNull String key) {
        return getCacheTool(key, null);
    }

    /**
     * 获取一个缓存工具，key指定缓存分级标签，没有会被新建，在文件系统中为一个目录
     *
     * @param key       层级标签
     * @param cacheTool 父缓存工具引用
     *
     * @return 缓存工具
     */
    public static CacheTool getCacheTool(@NotNull String key, @Nullable CacheTool cacheTool) {

        if (!cacheToolMap.containsKey(key)) {
            Log.i(LOG_TAG + "getCacheTool", "new CacheTool " + key);
            cacheToolMap.put(key, new CacheTool(key, cacheTool));
        } else {
            Log.v(LOG_TAG + "getCacheTool", "hit CacheTool " + key);
        }

        return cacheToolMap.get(key);
    }

    /**
     * 获取内存硬缓存空间
     *
     * @return 默认10M的硬缓存
     */
    public synchronized static MemoryCache getMemoryCache() {
        if (memoryCache == null) {
            memoryCache = new MemoryCache();
        }

        return memoryCache;
    }

    /**
     * 设置内存硬缓存空间最大容量
     *
     * @param size 新的最大容量
     */
    public synchronized static void setMemoryCacheSize(int size) {
        if (memoryCache == null) {
            memoryCache = new MemoryCache();
        }

        memoryCache.setMaxSize(size);
    }

    /**
     * 获取文件操作工具
     *
     * @return 文件操作工具
     */
    public static CacheFileUtil getCacheFileUtil() {
        return cacheFileUtil;
    }

    /**
     * 获取文本缓存对象转换器
     *
     * @return 文本缓存转换器
     */
    public static TextCacheConvert getTextCacheConvert() {
        return textCacheConvert;
    }

    /**
     * 获取图片缓存对象转换器
     *
     * @return 图片缓存转换器
     */
    public static BitmapCacheConvert getBitmapCacheConvert() {
        return bitmapCacheConvert;
    }

    /**
     * 获取输入流缓存对象转换器
     *
     * @return 输入流缓存转换器
     */
    public static InputStreamCacheConvert getInputStreamCacheConvert() {
        return inputStreamCacheConvert;
    }

    /**
     * 自动清理超时和超容量文件
     */
    public static void autoClear() {
        cacheFileUtil.autoClear();
    }

    /**
     * 清除全部缓存
     */
    public static void clear() {
        cacheFileUtil.clear();
    }
}
