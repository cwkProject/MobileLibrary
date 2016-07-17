package org.mobile.library.cache.util;
/**
 * Created by 超悟空 on 2015/11/25.
 */

import android.graphics.Bitmap;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.mobile.library.cache.util.convert.CacheConvert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 缓存组工具<br>
 * 缓存组拥有一个缓存组key，
 * 该key可以对应复数个缓存文件，
 * 增加的缓存会被直接保存到文件系统，
 * 而不会进入内存缓存，
 * 对于以组为单位的复数缓存文件且不关心具体缓存文件对应关系和回收情况时可以选择使用缓存组
 *
 * @author 超悟空
 * @version 1.0 2015/11/25
 * @since 1.0
 */
public class CacheGroup {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "CacheGroup.";

    /**
     * 本缓存组对应的缓存组key
     */
    private String key = null;

    /**
     * 本缓存组对应的父缓存工具
     */
    private CacheTool parent = null;

    /**
     * 本缓存组对应的缓存层级信息对象
     */
    private CacheLevel cacheLevel = null;

    /**
     * 超时时间
     */
    private long timeout = 0;

    /**
     * 构造函数
     *
     * @param key    本缓存组key
     * @param parent 父缓存工具引用
     */
    public CacheGroup(@NotNull String key, @NotNull CacheTool parent, @NotNull CacheLevel
            cacheLevel) {
        this.key = key;
        this.parent = parent;
        this.cacheLevel = cacheLevel;
        this.timeout = cacheLevel.getTimeOut();
    }

    /**
     * 设置该缓存工具默认缓存超时时间<br>
     * 设置后默认增加的缓存都被设置为该超时时间，
     * 超时单位毫秒，可使用{@link CacheManager#MAGNITUDE_MILLISECOND}，
     * {@link CacheManager#MAGNITUDE_SECOND}，
     * {@link CacheManager#MAGNITUDE_MINUTE}，
     * {@link CacheManager#MAGNITUDE_HOUR}，
     * {@link CacheManager#MAGNITUDE_DAY}，
     * {@link CacheManager#MAGNITUDE_WEEK}，
     * {@link CacheManager#MAGNITUDE_MONTH}做乘法计算
     * <example>
     * // 设置超时时间为3天
     * cacheGroup.setTimeOut(3*CacheManager.MAGNITUDE_DAY);
     * </example>
     *
     * @param timeout 超时时间，0表示无限制
     */
    public void setTimeout(long timeout) {
        Log.v(LOG_TAG + "setTimeOut", "timeout is " + timeout);
        this.timeout = timeout;
    }

    /**
     * 获取该缓存组的标签
     *
     * @return 带有父层级key的完整key
     */
    public String getKey() {
        return key;
    }

    /**
     * 获取本缓存组对应的父缓存工具
     *
     * @return 父缓存工具
     */
    public CacheTool getParent() {
        return parent;
    }

    /**
     * 增加一个图片缓存
     *
     * @param bitmap 图片对象
     */
    public void put(@NotNull Bitmap bitmap) {
        put(bitmap, CacheManager.getBitmapCacheConvert(), CacheManager.FILE_TYPE_IMAGE);
    }

    /**
     * 增加一个文本缓存
     *
     * @param text 文本对象
     */
    public void put(@NotNull String text) {
        put(text, CacheManager.getTextCacheConvert(), CacheManager.FILE_TYPE_TEXT);
    }

    /**
     * 增加一个纯文件缓存
     *
     * @param file 文件缓存
     */
    public void put(@NotNull File file) {
        try {
            put(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            Log.d(LOG_TAG + "put", "FileNotFoundException is " + e.getMessage());
        }
    }

    /**
     * 增加一个纯文件缓存
     *
     * @param inputStream 输入流
     */
    public void put(@NotNull InputStream inputStream) {
        put(inputStream, CacheManager.getInputStreamCacheConvert(), CacheManager.FILE_TYPE_FILE);
    }

    /**
     * 写入一个缓存文件，仅文写入文件系统
     *
     * @param cacheObject  缓存对象
     * @param cacheConvert 缓存转换器
     * @param type         缓存类型
     * @param <T>          实际缓存类型
     */
    private <T> void put(@NotNull T cacheObject, @NotNull CacheConvert<T> cacheConvert, int type) {
        // 写入文件缓存
        Log.v(LOG_TAG + "put", "group key:" + key + " , cacheMode:only file cache , timeout:" +
                timeout + " , type:" + type);
        cacheConvert.saveFile(CacheManager.getCacheFileUtil().putBackStream(cacheLevel, key,
                timeout, type, true), cacheObject);
    }

    /**
     * 手动写入缓存文件<br>
     * 直接保存缓存到文件系统，
     * 获取一个缓存控制系统中创建好的文件输出流，
     * 自行向输出流写入缓存文件并主动关闭输出流。<br>
     *
     * @return 用于写入缓存的文件输出流
     */
    public FileOutputStream putBackStream() {
        Log.v(LOG_TAG + "putAndBack", "group key:" + key + " timeout:" +
                timeout + " put file cache");
        // 写入文件缓存
        return CacheManager.getCacheFileUtil().putBackStream(cacheLevel, key, timeout,
                CacheManager.FILE_TYPE_FILE, true);
    }

    /**
     * 手动写入缓存文件<br>
     * 直接保存缓存到文件系统，
     * 获取一个缓存控制系统中创建好的文件路径，
     * 自行向输出流写入缓存文件并主动关闭输出流。<br>
     *
     * @return 用于写入缓存的文件路径
     */
    public String putBackPath() {
        Log.v(LOG_TAG + "putAndBack", "group key:" + key + " timeout:" +
                timeout + " put file cache");
        // 写入文件缓存
        return CacheManager.getCacheFileUtil().putBackPath(cacheLevel, key, timeout, CacheManager
                .FILE_TYPE_FILE, true);
    }

    /**
     * 手动写入缓存文件<br>
     * 直接保存缓存到文件系统，
     * 获取一个缓存控制系统中创建好的文件对象，
     * 自行向输出流写入缓存文件并主动关闭输出流。<br>
     *
     * @return 用于写入缓存的文件对象
     */
    public File putBackFile() {
        Log.v(LOG_TAG + "putAndBack", "group key:" + key + " timeout:" +
                timeout + " put file cache");
        // 写入文件缓存
        return CacheManager.getCacheFileUtil().putBackFile(cacheLevel, key, timeout, CacheManager
                .FILE_TYPE_FILE, true);
    }

    /**
     * 获取该缓存组的全部图片缓存
     *
     * @return 图片数组
     */
    public Bitmap[] getForBitmaps() {
        String[] paths = CacheManager.getCacheFileUtil().getGroupPath(key, cacheLevel,
                CacheManager.FILE_TYPE_IMAGE);
        Log.v(LOG_TAG + "getForBitmaps", "get group key " + key + " all bitmap path count is " +
                paths.length);
        List<Bitmap> list = new ArrayList<>();

        for (String path : paths) {
            Bitmap bitmap = CacheManager.getBitmapCacheConvert().toCache(path);
            if (bitmap != null) {
                // 跳过空项
                list.add(bitmap);
            }
        }

        Log.v(LOG_TAG + "getForFiles", "get group key " + key + " all bitmap count is " + list
                .size());

        return list.toArray(new Bitmap[list.size()]);
    }

    /**
     * 获取该缓存组的全部文本缓存
     *
     * @return 文本数组
     */
    public String[] getForTexts() {
        String[] paths = CacheManager.getCacheFileUtil().getGroupPath(key, cacheLevel,
                CacheManager.FILE_TYPE_TEXT);
        Log.v(LOG_TAG + "getForTexts", "get group key " + key + " all text path count is " +
                paths.length);
        List<String> list = new ArrayList<>();

        for (String path : paths) {
            String text = CacheManager.getTextCacheConvert().toCache(path);
            if (text != null) {
                // 跳过空项
                list.add(text);
            }
        }

        Log.v(LOG_TAG + "getForFiles", "get group key " + key + " all text count is " + list.size
                ());

        return list.toArray(new String[list.size()]);
    }

    /**
     * 获取该缓存组的全部文本缓存
     *
     * @return 文本数组
     */
    public File[] getForFiles() {
        String[] paths = CacheManager.getCacheFileUtil().getGroupPath(key, cacheLevel);
        Log.v(LOG_TAG + "getForFiles", "get group key " + key + " all file path count is " +
                paths.length);
        List<File> list = new ArrayList<>();

        for (String path : paths) {
            File file = new File(path);

            if (file.exists()) {
                // 跳过空项
                list.add(file);
            }
        }

        Log.v(LOG_TAG + "getForFiles", "get group key " + key + " all file count is " + list.size
                ());

        return list.toArray(new File[list.size()]);
    }

    /**
     * 清除该缓存组的全部数据
     */
    public void clear() {
        parent.remove(key);
    }
}
