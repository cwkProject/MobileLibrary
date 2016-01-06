package org.mobile.library.cache.util;
/**
 * Created by 超悟空 on 2015/11/23.
 */

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
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
    public CacheTool(@NotNull String key, @Nullable CacheTool parent) {
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
    public CacheTool getChildCacheTool(@NotNull String key) {
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
    public CacheGroup getCacheGroup(@NotNull String key) {
        return new CacheGroup(key, this, cacheLevel);
    }

    /**
     * 增加一个图片缓存
     *
     * @param key    缓存标签
     * @param bitmap 图片对象
     */
    public void put(@NotNull String key, @NotNull Bitmap bitmap) {
        put(key, bitmap, cacheLevel.getTimeOut());
    }

    /**
     * 增加一个图片缓存
     *
     * @param key     缓存标签
     * @param bitmap  图片对象
     * @param timeout 超时时间，覆盖工具默认设定，0表示无限制
     */
    public void put(@NotNull String key, @NotNull Bitmap bitmap, long timeout) {
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
    public void put(@NotNull String key, @NotNull Bitmap bitmap, int cacheMode) {
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
    public void put(@NotNull String key, @NotNull Bitmap bitmap, int cacheMode, long timeout) {
        put(key, bitmap, CacheManager.getBitmapCacheConvert(), cacheMode, timeout, CacheManager
                .FILE_TYPE_IMAGE);
    }

    /**
     * 增加一个文本缓存
     *
     * @param key  缓存标签
     * @param text 文本对象
     */
    public void put(@NotNull String key, @NotNull String text) {
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
    public void put(@NotNull String key, @NotNull String text, int cacheMode) {
        put(key, text, cacheMode, cacheLevel.getTimeOut());
    }

    /**
     * 增加一个文本缓存
     *
     * @param key     缓存标签
     * @param text    文本对象
     * @param timeout 超时时间，覆盖工具默认设定，0表示无限制
     */
    public void put(@NotNull String key, @NotNull String text, long timeout) {
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
    public void put(@NotNull String key, @NotNull String text, int cacheMode, long timeout) {
        put(key, text, CacheManager.getTextCacheConvert(), cacheMode, timeout, CacheManager
                .FILE_TYPE_TEXT);
    }

    /**
     * 增加一个纯文件缓存
     *
     * @param key  缓存标签
     * @param file 文件缓存
     */
    public void put(@NotNull String key, @NotNull File file) {
        put(key, file, cacheLevel.getTimeOut());
    }

    /**
     * 增加一个纯文件缓存
     *
     * @param key     缓存标签
     * @param file    文件缓存
     * @param timeout 超时时间，覆盖工具默认设定，0表示无限制
     */
    public void put(@NotNull String key, @NotNull File file, long timeout) {
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
    public void put(@NotNull String key, @NotNull InputStream inputStream) {
        put(key, inputStream, cacheLevel.getTimeOut());
    }

    /**
     * 增加一个纯文件缓存
     *
     * @param key         缓存标签
     * @param inputStream 输入流
     * @param timeout     超时时间，覆盖工具默认设定，0表示无限制
     */
    public void put(@NotNull String key, @NotNull InputStream inputStream, long timeout) {
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
    public <T> void put(@NotNull String key, @NotNull T cacheObject, CacheConvert<T> cacheConvert) {
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
    public <T> void put(@NotNull String key, @NotNull T cacheObject, CacheConvert<T>
            cacheConvert, long timeout) {
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
    public <T> void put(@NotNull String key, @NotNull T cacheObject, CacheConvert<T>
            cacheConvert, int cacheMode) {
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
    public <T> void put(@NotNull String key, @NotNull T cacheObject, @NotNull CacheConvert<T>
            cacheConvert, int cacheMode, long timeout) {
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
    private <T> void put(@NotNull String key, @NotNull T cacheObject, @NotNull CacheConvert<T>
            cacheConvert, int cacheMode, long timeout, int type) {
        Log.i(LOG_TAG + "put", "key:" + key + " , cacheMode:" + cacheMode + " , timeout:" +
                timeout + " , type:" + type);

        switch (cacheMode) {
            case CacheManager.MEMORY_WITH_FILE:
                Log.i(LOG_TAG + "put", key + " put memory cache");
                // 加入内存缓存
                CacheManager.getMemoryCache().put(levelKey + "/" + key, cacheConvert
                        .toCacheObject(cacheObject));
            case CacheManager.ONLY_FILE_CACHE:
                Log.i(LOG_TAG + "put", key + " put file cache");
                // 写入文件缓存
                cacheConvert.saveFile(CacheManager.getCacheFileUtil().putBackStream(cacheLevel,
                        key, timeout, type), cacheObject);
                break;
            case CacheManager.ONLY_MEMORY_CACHE:
                Log.i(LOG_TAG + "put", key + " only put memory cache");
                // 加入内存缓存
                CacheManager.getMemoryCache().put(levelKey + "/" + key, cacheConvert
                        .toCacheObject(cacheObject));
                break;
        }
    }

    /**
     * 手动写入缓存文件<br>
     * 直接保存缓存到文件系统，
     * 通过指定{@code key}来获取一个缓存控制系统中创建好的文件输出流，
     * 自行向输出流写入缓存文件并主动关闭输出流。<br>
     * 如果key已存在则输出流会覆盖原缓存文件。
     *
     * @param key 缓存标签
     *
     * @return 用于写入缓存的文件输出流
     */
    public FileOutputStream putBackStream(@NotNull String key) {
        return putBackStream(key, cacheLevel.getTimeOut());
    }

    /**
     * 手动写入缓存文件<br>
     * 直接保存缓存到文件系统，
     * 通过指定{@code key}来获取一个缓存控制系统中创建好的文件输出流，
     * 自行向输出流写入缓存文件并主动关闭输出流。<br>
     * 如果key已存在则输出流会覆盖原缓存文件。
     *
     * @param key     缓存标签
     * @param timeout 超时时间，覆盖工具默认设定，0表示无限制
     *
     * @return 用于写入缓存的文件输出流
     */
    public FileOutputStream putBackStream(@NotNull String key, long timeout) {
        Log.i(LOG_TAG + "putAndBack", "key:" + key + " timeout:" +
                timeout + " put file cache");
        // 写入文件缓存
        return CacheManager.getCacheFileUtil().putBackStream(cacheLevel, key, timeout,
                CacheManager.FILE_TYPE_FILE);
    }

    /**
     * 手动写入缓存文件<br>
     * 直接保存缓存到文件系统，
     * 通过指定{@code key}来获取一个缓存控制系统中创建好的文件对象，
     * 自行向输出流写入缓存文件并主动关闭输出流。<br>
     * 如果key已存在则输出流会覆盖原缓存文件。
     *
     * @param key 缓存标签
     *
     * @return 用于写入缓存的文件对象
     */
    public File putBackFile(@NotNull String key) {
        return putBackFile(key, cacheLevel.getTimeOut());
    }

    /**
     * 手动写入缓存文件<br>
     * 直接保存缓存到文件系统，
     * 通过指定{@code key}来获取一个缓存控制系统中创建好的文件对象，
     * 自行向输出流写入缓存文件并主动关闭输出流。<br>
     * 如果key已存在则输出流会覆盖原缓存文件。
     *
     * @param key     缓存标签
     * @param timeout 超时时间，覆盖工具默认设定，0表示无限制
     *
     * @return 用于写入缓存的文件对象
     */
    public File putBackFile(@NotNull String key, long timeout) {
        Log.i(LOG_TAG + "putAndBack", "key:" + key + " timeout:" +
                timeout + " put file cache");
        // 写入文件缓存
        return CacheManager.getCacheFileUtil().putBackFile(cacheLevel, key, timeout, CacheManager
                .FILE_TYPE_FILE);
    }

    /**
     * 手动写入缓存文件<br>
     * 直接保存缓存到文件系统，
     * 通过指定{@code key}来获取一个缓存控制系统中创建好的文件路径，
     * 自行向输出流写入缓存文件并主动关闭输出流。<br>
     * 如果key已存在则输出流会覆盖原缓存文件。
     *
     * @param key 缓存标签
     *
     * @return 用于写入缓存的文件路径
     */
    public String putBackPath(@NotNull String key) {
        return putBackPath(key, cacheLevel.getTimeOut());
    }

    /**
     * 手动写入缓存文件<br>
     * 直接保存缓存到文件系统，
     * 通过指定{@code key}来获取一个缓存控制系统中创建好的文件路径，
     * 自行向输出流写入缓存文件并主动关闭输出流。<br>
     * 如果key已存在则输出流会覆盖原缓存文件。
     *
     * @param key     缓存标签
     * @param timeout 超时时间，覆盖工具默认设定，0表示无限制
     *
     * @return 用于写入缓存的文件路径
     */
    public String putBackPath(@NotNull String key, long timeout) {
        Log.i(LOG_TAG + "putAndBack", "key:" + key + " timeout:" +
                timeout + " put file cache");
        // 写入文件缓存
        return CacheManager.getCacheFileUtil().putBackPath(cacheLevel, key, timeout, CacheManager
                .FILE_TYPE_FILE);
    }

    /**
     * 提取一个图片缓存
     *
     * @param key 缓存key
     *
     * @return 图片对象
     */
    public Bitmap getForBitmap(@NotNull String key) {
        Log.i(LOG_TAG + "getForBitmap", "key is " + key);
        // 尝试从内存缓存获取
        Log.i(LOG_TAG + "getForBitmap", "from memory read " + key);
        CacheObject cacheObject = CacheManager.getMemoryCache().get(levelKey + "/" + key);

        Bitmap bitmap = CacheManager.getBitmapCacheConvert().toCache(cacheObject);

        if (bitmap == null) {
            // 尝试从文件读取
            Log.i(LOG_TAG + "getForBitmap", "from file read " + key);
            bitmap = CacheManager.getBitmapCacheConvert().toCache(CacheManager.getCacheFileUtil()
                    .getPath(key, cacheLevel));

            if (bitmap != null) {
                // 成功读取文件
                // 加入内存缓存
                CacheManager.getMemoryCache().put(levelKey + "/" + key, CacheManager
                        .getBitmapCacheConvert().toCacheObject(bitmap));
            }
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
    public String getForText(@NotNull String key) {
        Log.i(LOG_TAG + "getForText", "key is " + key);
        // 尝试从内存缓存获取
        Log.i(LOG_TAG + "getForText", "from memory read " + key);
        CacheObject cacheObject = CacheManager.getMemoryCache().get(levelKey + "/" + key);

        String text = CacheManager.getTextCacheConvert().toCache(cacheObject);

        if (text == null) {
            // 尝试从文件读取
            Log.i(LOG_TAG + "getForText", "from file read " + key);
            text = CacheManager.getTextCacheConvert().toCache(CacheManager.getCacheFileUtil()
                    .getPath(key, cacheLevel));

            if (text != null) {
                // 成功读取文件
                // 加入内存缓存
                CacheManager.getMemoryCache().put(levelKey + "/" + key, CacheManager
                        .getTextCacheConvert().toCacheObject(text));
            }
        }

        return text;
    }

    /**
     * 提取一个缓存的文件对象
     *
     * @param key 缓存key
     *
     * @return 文件对象，如果缓存未保存到文件系统则返回null，如果文件已被清除则文件可能不存在
     */
    public File getForFile(@NotNull String key) {
        Log.i(LOG_TAG + "getForFile", "key is " + key);

        String path = CacheManager.getCacheFileUtil().getPath(key, cacheLevel);

        Log.i(LOG_TAG + "getForFile", "cache " + key + " path is " + path);

        return path == null ? null : new File(CacheManager.getCacheFileUtil().getPath(key,
                cacheLevel));
    }

    /**
     * 提取一个缓存文件输入流
     *
     * @param key 缓存key
     *
     * @return 输入流
     */
    public InputStream getInputStream(@NotNull String key) {
        Log.i(LOG_TAG + "getInputStream", "key is " + key);
        return CacheManager.getInputStreamCacheConvert().toCache(CacheManager.getCacheFileUtil()
                .getPath(key, cacheLevel));
    }

    /**
     * 提取一个自定义缓存
     *
     * @param key          缓存key
     * @param cacheConvert 缓存转换器
     *
     * @return 缓存对象
     */
    public <T> T getForCacheConvert(@NotNull String key, @NotNull CacheConvert<T> cacheConvert) {
        Log.i(LOG_TAG + "getForCacheConvert", "key is " + key);
        // 尝试从内存缓存获取
        Log.i(LOG_TAG + "getForCacheConvert", "from memory read " + key);
        CacheObject cacheObject = CacheManager.getMemoryCache().get(levelKey + "/" + key);

        T cache = cacheConvert.toCache(cacheObject);

        if (cache == null) {
            // 尝试从文件读取
            Log.i(LOG_TAG + "getForCacheConvert", "from file read " + key);
            cache = cacheConvert.toCache(CacheManager.getCacheFileUtil().getPath(key, cacheLevel));
        }

        return cache;
    }

    /**
     * 获取该工具下的全部图片数组
     *
     * @return 图片数组
     */
    public Bitmap[] getForBitmaps() {
        String[] paths = CacheManager.getCacheFileUtil().getPath(cacheLevel, CacheManager
                .FILE_TYPE_IMAGE);
        Log.i(LOG_TAG + "getForBitmaps", "get level " + levelKey + " all bitmap path count is " +
                paths.length);

        List<Bitmap> list = new ArrayList<>();

        for (String path : paths) {
            Bitmap bitmap = CacheManager.getBitmapCacheConvert().toCache(path);
            if (bitmap != null) {
                // 跳过空项
                list.add(bitmap);
            }
        }
        Log.i(LOG_TAG + "getForBitmaps", "get level " + levelKey + " all bitmap count is " + list
                .size());

        return list.toArray(new Bitmap[list.size()]);
    }

    /**
     * 获取该工具下的全部文本数组
     *
     * @return 文本数组
     */
    public String[] getForTexts() {
        String[] paths = CacheManager.getCacheFileUtil().getPath(cacheLevel, CacheManager
                .FILE_TYPE_TEXT);
        Log.i(LOG_TAG + "getForTexts", "get level " + levelKey + " all text path count is " +
                paths.length);
        List<String> list = new ArrayList<>();

        for (String path : paths) {
            String text = CacheManager.getTextCacheConvert().toCache(path);
            if (text != null) {
                // 跳过空项
                list.add(text);
            }
        }
        Log.i(LOG_TAG + "getForTexts", "get level " + levelKey + " all text count is " + list
                .size());

        return list.toArray(new String[list.size()]);
    }

    /**
     * 获取该工具下的全部文件数组
     *
     * @return 文件数组
     */
    public File[] getForFiles() {
        String[] paths = CacheManager.getCacheFileUtil().getPath(cacheLevel);
        Log.i(LOG_TAG + "getForFiles", "get level " + levelKey + " all file path count is " +
                paths.length);

        List<File> list = new ArrayList<>();

        for (String path : paths) {
            File file = new File(path);

            if (file.exists()) {
                // 跳过空项
                list.add(file);
            }
        }
        Log.i(LOG_TAG + "getForFiles", "get level " + levelKey + " all file count is " + list
                .size());

        return list.toArray(new File[list.size()]);
    }

    /**
     * 移除一个缓存文件或缓存组
     *
     * @param key 缓存key
     */
    public void remove(@NotNull String key) {
        Log.i(LOG_TAG + "remove", "remove cache " + key);

        // 尝试移除内存缓存
        Log.i(LOG_TAG + "remove", "from memory remove cache " + key);
        CacheManager.getMemoryCache().remove(levelKey + "/" + key);

        // 尝试删除文件缓存
        Log.i(LOG_TAG + "remove", "from file remove cache " + key);
        CacheManager.getCacheFileUtil().delete(key, cacheLevel);
    }

    /**
     * 清空本缓存工具下的缓存文件，包括子缓存工具下的文件
     */
    public void clear() {
        Log.i(LOG_TAG + "clear", "clear cache level " + levelKey);
        CacheManager.getCacheFileUtil().clear(cacheLevel);
    }
}
