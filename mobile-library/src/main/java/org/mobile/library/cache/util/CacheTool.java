package org.mobile.library.cache.util;
/**
 * Created by 超悟空 on 2015/11/23.
 */

import android.graphics.Bitmap;
import android.util.Log;

import org.mobile.library.cache.util.convert.CacheConvert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 缓存操作工具
 *
 * @author 超悟空
 * @version 1.0 2015/11/23
 * @since 1.0
 */
public class CacheTool {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "CacheTool.";

    /**
     * 本缓存工具对应的缓存层级key
     */
    private String levelKey = null;

    /**
     * 本缓存工具对应的父缓存工具
     */
    private CacheTool parent = null;

    /**
     * 本缓存工具对应的缓存层级信息对象
     */
    private CacheLevel cacheLevel = null;

    /**
     * 构造函数
     *
     * @param key    本缓存工具key
     * @param parent 父缓存工具引用
     */
    public CacheTool(String key, CacheTool parent) {
        Log.i(LOG_TAG + "CacheTool", "key is " + key);
        Log.i(LOG_TAG + "CacheTool", "parent is " + (parent == null ? null : parent.getKey()));
        this.levelKey = key;
        this.parent = parent;
        init();
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
     * cacheLevel.setTimeOut(3*CacheManager.MAGNITUDE_DAY);
     * </example>
     *
     * @param timeout 超时时间，0表示无限制
     */
    public void setTimeout(long timeout) {
        Log.i(LOG_TAG + "setTimeOut", "timeout is " + timeout);
        cacheLevel.setTimeOut(timeout);

        // 更新缓存层级信息
        CacheManager.getCacheFileUtil().updateCacheLevel(cacheLevel);
    }

    /**
     * 设置缓存工具备注
     *
     * @param remark 备注信息
     */
    public void setRemark(String remark) {
        Log.i(LOG_TAG + "setRemark", "remark is " + remark);
        cacheLevel.setRemark(remark);

        // 更新缓存层级信息
        CacheManager.getCacheFileUtil().updateCacheLevel(cacheLevel);
    }

    /**
     * 获取缓存工具备注信息
     *
     * @return 备注信息
     */
    public String getRemark() {
        return cacheLevel.getRemark();
    }

    /**
     * 缓存工具初始化
     */
    private void init() {
        Log.i(LOG_TAG + "init", "init CacheTool");
        cacheLevel = CacheManager.getCacheFileUtil().getCacheLevel(levelKey, parent == null ?
                null : parent.getKey());
    }

    /**
     * 获取该缓存工具的标签
     *
     * @return 带有父层级key的完整key
     */
    public String getKey() {
        return levelKey;
    }

    /**
     * 获取本缓存工具对应的父缓存工具
     *
     * @return 父缓存工具
     */
    public CacheTool getParent() {
        return parent;
    }

    /**
     * 创建或获取一个子缓存工具
     *
     * @param key 子缓存key
     *
     * @return 子缓存工具
     */
    public CacheTool getChildCacheTool(String key) {
        Log.i(LOG_TAG + "getChildCacheTool", "create child CacheTool " + key);
        return CacheManager.getCacheTool(key, this);
    }

    /**
     * 获取一个缓存组
     *
     * @param key 缓存组key
     *
     * @return 缓存组对象
     */
    public CacheGroup getCacheGroup(String key) {
        return new CacheGroup(key, this);
    }

    /**
     * 增加一个图片缓存
     *
     * @param key    缓存标签
     * @param bitmap 图片对象
     */
    public void put(String key, Bitmap bitmap) {
        put(key, bitmap, cacheLevel.getTimeOut());
    }

    /**
     * 增加一个图片缓存
     *
     * @param key     缓存标签
     * @param bitmap  图片对象
     * @param timeout 超时时间，覆盖工具默认设定，0表示无限制
     */
    public void put(String key, Bitmap bitmap, long timeout) {
        put(key, bitmap, CacheManager.MEMORY_WITH_FILE, timeout);
    }

    /**
     * 增加一个图片缓存
     *
     * @param key       缓存标签
     * @param bitmap    图片对象
     * @param cacheMode 缓存模式<br>
     *                  默认为{@link CacheManager#MEMORY_WITH_FILE}，
     *                  可选有{@link CacheManager#ONLY_MEMORY_CACHE}，
     *                  {@link CacheManager#ONLY_FILE_CACHE}
     */
    public void put(String key, Bitmap bitmap, int cacheMode) {
        put(key, bitmap, cacheMode, cacheLevel.getTimeOut());
    }

    /**
     * 增加一个图片缓存
     *
     * @param key       缓存标签
     * @param bitmap    图片对象
     * @param cacheMode 缓存模式<br>
     *                  默认为{@link CacheManager#MEMORY_WITH_FILE}，
     *                  可选有{@link CacheManager#ONLY_MEMORY_CACHE}，
     *                  {@link CacheManager#ONLY_FILE_CACHE}
     * @param timeout   超时时间，覆盖工具默认设定，0表示无限制
     */
    public void put(String key, Bitmap bitmap, int cacheMode, long timeout) {
        put(key, bitmap, CacheManager.getBitmapCacheConvert(), cacheMode, timeout, CacheManager
                .FILE_TYPE_IMAGE);
    }

    /**
     * 增加一个文本缓存
     *
     * @param key  缓存标签
     * @param text 文本对象
     */
    public void put(String key, String text) {
        put(key, text, cacheLevel.getTimeOut());
    }

    /**
     * 增加一个文本缓存
     *
     * @param key       缓存标签
     * @param text      缓存标签
     * @param cacheMode 缓存模式<br>
     *                  默认为{@link CacheManager#MEMORY_WITH_FILE}，
     *                  可选有{@link CacheManager#ONLY_MEMORY_CACHE}，
     *                  {@link CacheManager#ONLY_FILE_CACHE}
     */
    public void put(String key, String text, int cacheMode) {
        put(key, text, cacheMode, cacheLevel.getTimeOut());
    }

    /**
     * 增加一个文本缓存
     *
     * @param key     缓存标签
     * @param text    文本对象
     * @param timeout 超时时间，覆盖工具默认设定，0表示无限制
     */
    public void put(String key, String text, long timeout) {
        put(key, text, CacheManager.MEMORY_WITH_FILE, timeout);
    }

    /**
     * 增加一个文本缓存
     *
     * @param key       缓存标签
     * @param text      缓存标签
     * @param cacheMode 缓存模式<br>
     *                  默认为{@link CacheManager#MEMORY_WITH_FILE}，
     *                  可选有{@link CacheManager#ONLY_MEMORY_CACHE}，
     *                  {@link CacheManager#ONLY_FILE_CACHE}
     * @param timeout   超时时间，覆盖工具默认设定，0表示无限制
     */
    public void put(String key, String text, int cacheMode, long timeout) {
        put(key, text, CacheManager.getTextCacheConvert(), cacheMode, timeout, CacheManager
                .FILE_TYPE_TEXT);
    }

    /**
     * 增加一个纯文件缓存
     *
     * @param key  缓存标签
     * @param file 文件缓存
     */
    public void put(String key, File file) {
        put(key, file, cacheLevel.getTimeOut());
    }

    /**
     * 增加一个纯文件缓存
     *
     * @param key     缓存标签
     * @param file    文件缓存
     * @param timeout 超时时间，覆盖工具默认设定，0表示无限制
     */
    public void put(String key, File file, long timeout) {
        try {
            put(key, new FileInputStream(file), timeout);
        } catch (FileNotFoundException e) {
            Log.d(LOG_TAG + "put", "FileNotFoundException is " + e.getMessage());
        }
    }

    /**
     * 增加一个纯文件缓存
     *
     * @param key         缓存标签
     * @param inputStream 输入流
     */
    public void put(String key, InputStream inputStream) {
        put(key, inputStream, cacheLevel.getTimeOut());
    }

    /**
     * 增加一个纯文件缓存
     *
     * @param key         缓存标签
     * @param inputStream 输入流
     * @param timeout     超时时间，覆盖工具默认设定，0表示无限制
     */
    public void put(String key, InputStream inputStream, long timeout) {
        put(key, inputStream, CacheManager.getInputStreamCacheConvert(), CacheManager
                .ONLY_FILE_CACHE, timeout, CacheManager.FILE_TYPE_FILE);
    }

    /**
     * 增加一个自定义缓存
     *
     * @param key          缓存标签
     * @param cacheObject  缓存对象
     * @param cacheConvert 缓存转换器
     */
    public <T> void put(String key, T cacheObject, CacheConvert<T> cacheConvert) {
        put(key, cacheObject, cacheConvert, cacheLevel.getTimeOut());
    }

    /**
     * 增加一个自定义缓存
     *
     * @param key          缓存标签
     * @param cacheObject  缓存对象
     * @param cacheConvert 缓存转换器
     * @param timeout      超时时间，覆盖工具默认设定，0表示无限制
     */
    public <T> void put(String key, T cacheObject, CacheConvert<T> cacheConvert, long timeout) {
        put(key, cacheObject, cacheConvert, CacheManager.MEMORY_WITH_FILE, timeout);
    }

    /**
     * 增加一个自定义缓存
     *
     * @param key          缓存标签
     * @param cacheObject  缓存对象
     * @param cacheConvert 缓存转换器
     * @param cacheMode    缓存模式<br>
     *                     默认为{@link CacheManager#MEMORY_WITH_FILE}，
     *                     可选有{@link CacheManager#ONLY_MEMORY_CACHE}，
     *                     {@link CacheManager#ONLY_FILE_CACHE}
     */
    public <T> void put(String key, T cacheObject, CacheConvert<T> cacheConvert, int cacheMode) {
        put(key, cacheObject, cacheConvert, cacheMode, cacheLevel.getTimeOut());
    }

    /**
     * 增加一个自定义缓存
     *
     * @param key          缓存标签
     * @param cacheObject  缓存对象
     * @param cacheConvert 缓存转换器
     * @param cacheMode    缓存模式<br>
     *                     默认为{@link CacheManager#MEMORY_WITH_FILE}，
     *                     可选有{@link CacheManager#ONLY_MEMORY_CACHE}，
     *                     {@link CacheManager#ONLY_FILE_CACHE}
     * @param timeout      超时时间，覆盖工具默认设定，0表示无限制
     */
    public <T> void put(String key, T cacheObject, CacheConvert<T> cacheConvert, int cacheMode,
                        long timeout) {
        put(key, cacheObject, cacheConvert, cacheMode, timeout, CacheManager
                .FILE_TYPE_CACHE_OBJECT);
    }

    /**
     * 增加一个缓存
     *
     * @param key          缓存标签
     * @param cacheObject  缓存对象
     * @param cacheConvert 缓存转换器
     * @param cacheMode    缓存模式<br>
     *                     默认为{@link CacheManager#MEMORY_WITH_FILE}，
     *                     可选有{@link CacheManager#ONLY_MEMORY_CACHE}，
     *                     {@link CacheManager#ONLY_FILE_CACHE}
     * @param timeout      超时时间，覆盖工具默认设定，0表示无限制
     * @param type         缓存类型
     */
    private <T> void put(String key, T cacheObject, CacheConvert<T> cacheConvert, int cacheMode,
                         long timeout, int type) {

        switch (cacheMode) {
            case CacheManager.MEMORY_WITH_FILE:
                // 加入内存缓存
                CacheManager.getMemoryCache().put(levelKey + "/" + key, cacheConvert
                        .toCacheObject(cacheObject));
            case CacheManager.ONLY_FILE_CACHE:
                // 写入文件缓存
                cacheConvert.saveFile(CacheManager.getCacheFileUtil().put(levelKey, key, timeout,
                        type), cacheObject);
                break;
            case CacheManager.ONLY_MEMORY_CACHE:
                // 加入内存缓存
                CacheManager.getMemoryCache().put(levelKey + "/" + key, cacheConvert
                        .toCacheObject(cacheObject));
                break;
        }
    }

    /**
     * 提取一个图片缓存
     *
     * @param key 缓存key
     *
     * @return 图片对象
     */
    public Bitmap getForBitmap(String key) {

        // 尝试从内存缓存获取
        CacheObject cacheObject = CacheManager.getMemoryCache().get(levelKey + "/" + key);

        Bitmap bitmap = CacheManager.getBitmapCacheConvert().toCache(cacheObject);

        if (bitmap == null) {
            // 尝试从文件读取
            bitmap = CacheManager.getBitmapCacheConvert().toCache(CacheManager.getCacheFileUtil()
                    .getPath(key, levelKey));
        }

        return bitmap;
    }

    /**
     * 提取一个文本缓存
     *
     * @param key 缓存key
     *
     * @return 文本对象
     */
    public String getForText(String key) {
        // 尝试从内存缓存获取
        CacheObject cacheObject = CacheManager.getMemoryCache().get(levelKey + "/" + key);

        String text = CacheManager.getTextCacheConvert().toCache(cacheObject);

        if (text == null) {
            // 尝试从文件读取
            text = CacheManager.getTextCacheConvert().toCache(CacheManager.getCacheFileUtil()
                    .getPath(key, levelKey));
        }

        return text;
    }

    /**
     * 提取一个缓存的文件对象
     *
     * @param key 缓存key
     *
     * @return 文件对象
     */
    public File getForFile(String key) {

        return new File(CacheManager.getCacheFileUtil().getPath(key, levelKey));
    }

    /**
     * 提取一个缓存文件输入流
     *
     * @param key 缓存key
     *
     * @return 输入流
     */
    public InputStream getInputStream(String key) {
        return CacheManager.getInputStreamCacheConvert().toCache(CacheManager.getCacheFileUtil()
                .getPath(key, levelKey));
    }

    /**
     * 提取一个自定义缓存
     *
     * @param key          缓存key
     * @param cacheConvert 缓存转换器
     *
     * @return 缓存对象
     */
    public <T> T getForCacheConvert(String key, CacheConvert<T> cacheConvert) {

        // 尝试从内存缓存获取
        CacheObject cacheObject = CacheManager.getMemoryCache().get(levelKey + "/" + key);

        T cache = cacheConvert.toCache(cacheObject);

        if (cache == null) {
            // 尝试从文件读取
            cache = cacheConvert.toCache(CacheManager.getCacheFileUtil().getPath(key, levelKey));
        }

        return cache;
    }

    /**
     * 获取该工具下的全部图片数组
     *
     * @return 图片数组
     */
    public Bitmap[] getForBitmaps() {

        String[] paths = CacheManager.getCacheFileUtil().getPath(levelKey, CacheManager
                .FILE_TYPE_IMAGE);

        List<Bitmap> list = new ArrayList<>();

        for (String path : paths) {
            Bitmap bitmap = CacheManager.getBitmapCacheConvert().toCache(path);
            if (bitmap != null) {
                // 跳过空项
                list.add(bitmap);
            }
        }

        return list.toArray(new Bitmap[list.size()]);
    }

    /**
     * 获取该工具下的全部文本数组
     *
     * @return 文本数组
     */
    public String[] getForTexts() {
        String[] paths = CacheManager.getCacheFileUtil().getPath(levelKey, CacheManager
                .FILE_TYPE_TEXT);

        List<String> list = new ArrayList<>();

        for (String path : paths) {
            String text = CacheManager.getTextCacheConvert().toCache(path);
            if (text != null) {
                // 跳过空项
                list.add(text);
            }
        }

        return list.toArray(new String[list.size()]);
    }

    /**
     * 获取该工具下的全部文件数组
     *
     * @return 文件数组
     */
    public File[] getForFiles() {
        String[] paths = CacheManager.getCacheFileUtil().getPath(levelKey, CacheManager
                .FILE_TYPE_FILE);

        List<File> list = new ArrayList<>();

        for (String path : paths) {
            File file = new File(path);

            if (file.exists()) {
                // 跳过空项
                list.add(file);
            }
        }

        return list.toArray(new File[list.size()]);
    }

    /**
     * 移除一个缓存文件
     *
     * @param key 缓存key
     */
    public void remove(String key) {
        // 尝试移除内存缓存
        CacheManager.getMemoryCache().remove(levelKey + "/" + key);

        // 尝试删除文件缓存
        CacheManager.getCacheFileUtil().delete(key, levelKey);
    }
}
