package org.mobile.library.cache.util;
/**
 * Created by 超悟空 on 2015/11/16.
 */

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import org.mobile.library.cache.database.CacheInfoOperator;
import org.mobile.library.cache.database.CacheLevelOperator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 处理缓存文件的工具，用于保存读取缓存文件和数据库索引
 *
 * @author 超悟空
 * @version 1.0 2015/11/16
 * @since 1.0
 */
public class CacheFileUtil {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "CacheFileUtil.";

    /**
     * 上下文
     */
    private Context context = null;

    /**
     * 缓存层级数据库工具
     */
    private CacheLevelOperator cacheLevelOperator = null;

    /**
     * 缓存文件信息数据库工具
     */
    private CacheInfoOperator cacheInfoOperator = null;

    /**
     * 外部缓存根路径
     */
    private String externalCacheRootPath = null;

    /**
     * 内部缓存根路径
     */
    private String internalCacheRootPath = null;

    /**
     * 标识当前是否在使用外部存储
     */
    private boolean external = true;

    /**
     * 构造函数
     *
     * @param context 上下文
     */
    public CacheFileUtil(Context context) {
        this.context = context;

        this.cacheLevelOperator = new CacheLevelOperator(context);
        this.cacheLevelOperator = new CacheLevelOperator(context);

        // 获取缓存根目录
        File file = context.getExternalCacheDir();

        if (file != null) {
            externalCacheRootPath = file.getPath();
            external = true;
        }

        file = context.getCacheDir();
        internalCacheRootPath = file.getPath();
        if (externalCacheRootPath == null) {
            external = false;
        }
    }

    /**
     * 保存一个缓存文件信息到索引数据库
     *
     * @param levelKey 缓存层级key
     * @param cacheKey 缓存文件key
     * @param timeout  超时时间
     * @param type     缓存文件类型
     *
     * @return 返回文件输出流用于写缓存文件，如果文件系统存在异常则返回null
     */
    public FileOutputStream put(String levelKey, String cacheKey, long timeout, int type) {
        return put(levelKey, cacheKey, timeout, type, false);
    }

    /**
     * 保存一个缓存文件
     *
     * @param levelKey 缓存层级key
     * @param cacheKey 缓存文件key
     * @param timeout  超时时间
     * @param type     缓存文件类型
     * @param group    标识是否为组
     *
     * @return 返回文件输出流用于写缓存文件，如果文件系统存在异常则返回null
     */
    public FileOutputStream put(String levelKey, String cacheKey, long timeout, int type, boolean
            group) {

        Log.i(LOG_TAG + "put", "cache key is " + cacheKey);
        Log.i(LOG_TAG + "put", "level key is " + levelKey);
        Log.i(LOG_TAG + "put", "type is " + type);
        Log.i(LOG_TAG + "put", "group is " + group);
        Log.i(LOG_TAG + "put", "timeout is " + timeout);

        // 层级路径
        String levelPath = cacheLevelOperator.queryCacheLevel(levelKey).getRealPath();

        // 尝试获取存在的缓存信息
        CacheInfo cacheInfo = cacheInfoOperator.queryCacheInfo(cacheKey, levelKey);
        // 标识文件是否存在
        boolean exist = false;

        if (group || cacheInfo == null) {
            // 组缓存或不存在则新建
            cacheInfo = new CacheInfo(cacheKey, UUID.randomUUID().toString(), levelKey);
        } else {
            // 已存在，不需要新建
            Log.i(LOG_TAG + "put", "cache is exist");
            exist = true;
        }

        cacheInfo.setType(type);
        cacheInfo.setTimeOut(timeout);
        cacheInfo.setGroupTag(group);
        cacheInfo.setExternal(external);

        // 保存索引信息到数据库
        if (exist) {
            cacheInfoOperator.update(cacheInfo);
        } else {
            cacheInfoOperator.insert(cacheInfo);
        }

        // 保存文件
        File file = new File((external ? externalCacheRootPath : internalCacheRootPath) + "/" +
                levelPath, cacheInfo.getRealFileName());

        try {
            if (!file.exists()) {
                file.mkdir();
                file.createNewFile();
            }

            return new FileOutputStream(file);
        } catch (IOException e) {
            Log.e(LOG_TAG + "put", "IOException is " + e.getMessage());
            return null;
        }
    }

    /**
     * 获取缓存文件对象
     *
     * @param cacheKey 缓存key
     * @param levelKey 缓存层级key
     *
     * @return 文件对象，缓存不存在则返回null
     */
    public File getFile(String cacheKey, String levelKey) {
        Log.i(LOG_TAG + "getFile", "cache key is " + cacheKey);
        Log.i(LOG_TAG + "getFile", "level key is " + levelKey);

        // 文件路径
        String path = getPath(cacheKey, levelKey);

        if (path == null) {
            Log.i(LOG_TAG + "getFile", "path is null");
            return null;
        }

        File file = new File(path);

        return file.exists() ? file : null;
    }

    /**
     * 获取缓存文件路径
     *
     * @param cacheKey 缓存key
     * @param levelKey 缓存层级key
     *
     * @return 文件路径，key未被添加过或该key是一个组则返回null，不保证指定路径一定有文件存在
     */
    public String getPath(String cacheKey, String levelKey) {
        Log.i(LOG_TAG + "getPath", "cache key is " + cacheKey);
        Log.i(LOG_TAG + "getPath", "level key is " + levelKey);

        CacheInfo cacheInfo = cacheInfoOperator.queryCacheInfo(cacheKey, levelKey);

        if (cacheInfo != null) {
            // 层级路径
            String levelPath = cacheLevelOperator.queryCacheLevel(levelKey).getRealPath();

            return buildPath(levelPath, cacheInfo);
        } else {
            return null;
        }
    }

    /**
     * 装配缓存完整路径
     *
     * @param levelPath 缓存层级路径
     * @param cacheInfo 缓存信息对象
     *
     * @return 完整路径
     */
    private String buildPath(String levelPath, CacheInfo cacheInfo) {
        return (cacheInfo.isExternal() ? externalCacheRootPath : internalCacheRootPath) + "/" +
                levelPath + "/" + cacheInfo.getRealFileName();
    }

    /**
     * 获取缓存文件输入流
     *
     * @param cacheKey 缓存key
     * @param levelKey 缓存层级key
     *
     * @return 文件输入流，缓存不存在则返回null
     */
    public FileInputStream getStream(String cacheKey, String levelKey) {
        Log.i(LOG_TAG + "getStream", "cache key is " + cacheKey);
        Log.i(LOG_TAG + "getStream", "level key is " + levelKey);

        // 文件路径
        String path = getPath(cacheKey, levelKey);

        if (path == null) {
            Log.i(LOG_TAG + "getStream", "path is null");
            return null;
        }

        try {
            return new FileInputStream(path);
        } catch (FileNotFoundException e) {
            Log.e(LOG_TAG + "getStream", "FileNotFoundException is " + e.getMessage());
            return null;
        }
    }

    /**
     * 获取指定类型的缓存文件路径
     *
     * @param levelKey 缓存层级key
     * @param type     文件类型
     *
     * @return 路径数组，异常或目录为空则返回null，不保证路径下一定有文件
     */
    public String[] getPath(String levelKey, int type) {
        Log.i(LOG_TAG + "getPath", "type is " + type);
        Log.i(LOG_TAG + "getPath", "level key is " + levelKey);

        List<CacheInfo> cacheInfoList = cacheInfoOperator.queryCacheInfo(levelKey, type);

        return buildPathList(levelKey, cacheInfoList);
    }

    /**
     * 获取指定类型的缓存文件对象
     *
     * @param levelKey 缓存层级key
     * @param type     文件类型
     *
     * @return 文件数组，异常或目录为空则返回null
     */
    public File[] getFile(String levelKey, int type) {
        Log.i(LOG_TAG + "getFile", "type is " + type);
        Log.i(LOG_TAG + "getFile", "level key is " + levelKey);

        // 文件路径
        String[] pathList = getPath(levelKey, type);

        return buildFileList(pathList);
    }

    /**
     * 装配文件数组
     *
     * @param pathList 文件路径数组
     *
     * @return 装配好的文件对象数组
     */
    @Nullable
    private File[] buildFileList(String[] pathList) {
        if (pathList == null) {
            Log.i(LOG_TAG + "buildFileList", "path list is null");
            return null;
        }

        // 文件数组
        List<File> fileList = new ArrayList<>();

        for (String path : pathList) {
            File file = new File(path);
            if (file.exists()) {
                fileList.add(file);
            }
        }

        if (fileList.size() == 0) {
            Log.i(LOG_TAG + "buildFileList", "fileList is null");
            return null;
        }

        return fileList.toArray(new File[fileList.size()]);
    }

    /**
     * 获取指定类型的缓存文件输入流
     *
     * @param levelKey 缓存层级key
     * @param type     文件类型
     *
     * @return 输入流数组，异常或目录为空则返回null
     */
    public FileInputStream[] getStream(String levelKey, int type) {
        Log.i(LOG_TAG + "getStream", "type is " + type);
        Log.i(LOG_TAG + "getStream", "level key is " + levelKey);

        // 文件路径
        String[] pathList = getPath(levelKey, type);

        return buildFileInputStreamList(pathList);
    }

    /**
     * 获取指定类型的组缓存路径
     *
     * @param cacheKey 缓存组key
     * @param levelKey 缓存层级key
     * @param type     缓存类型
     *
     * @return 路径数组，异常或目录为空则返回null，不保证路径下一定有文件
     */
    public String[] getGroupPath(String cacheKey, String levelKey, int type) {
        Log.i(LOG_TAG + "getGroupPath", "type is " + type);
        Log.i(LOG_TAG + "getGroupPath", "cache key is " + cacheKey);
        Log.i(LOG_TAG + "getGroupPath", "level key is " + levelKey);

        List<CacheInfo> cacheInfoList = cacheInfoOperator.queryCacheGroup(levelKey, levelKey, type);

        return buildPathList(levelKey, cacheInfoList);
    }

    /**
     * 装配缓存真实路径数组
     *
     * @param levelKey      缓存层级key
     * @param cacheInfoList 缓存信息列表
     *
     * @return 装配好的数组，如果文件不存在或则返回null
     */
    @Nullable
    private String[] buildPathList(String levelKey, List<CacheInfo> cacheInfoList) {
        if (cacheInfoList.size() > 0) {

            // 要返回的路径数组
            String[] pathList = new String[cacheInfoList.size()];
            // 层级路径
            String levelPath = cacheLevelOperator.queryCacheLevel(levelKey).getRealPath();

            for (int i = 0; i < cacheInfoList.size(); i++) {
                CacheInfo cacheInfo = cacheInfoList.get(i);

                pathList[i] = buildPath(levelPath, cacheInfo);
            }
            return pathList;
        } else {
            return null;
        }
    }

    /**
     * 获取指定类型的组缓存输入流
     *
     * @param cacheKey 缓存组key
     * @param levelKey 缓存层级key
     * @param type     缓存类型
     *
     * @return 输入流数组，异常或目录为空则返回null
     */
    public FileInputStream[] getGroupStream(String cacheKey, String levelKey, int type) {
        Log.i(LOG_TAG + "getGroupStream", "cache key is " + cacheKey);
        Log.i(LOG_TAG + "getGroupStream", "type is " + type);
        Log.i(LOG_TAG + "getGroupStream", "level key is " + levelKey);

        // 文件路径
        String[] pathList = getGroupPath(cacheKey, levelKey, type);

        return buildFileInputStreamList(pathList);
    }

    /**
     * 装配文件输入流数组
     *
     * @param pathList 文件路径数组
     *
     * @return 装配好的文件输入流数组
     */
    @Nullable
    private FileInputStream[] buildFileInputStreamList(String[] pathList) {
        if (pathList == null) {
            Log.i(LOG_TAG + "buildFileInputStreamList", "path list is null");
            return null;
        }

        // 输入流数组
        List<FileInputStream> fileInputStreamList = new ArrayList<>();

        for (String path : pathList) {
            try {
                FileInputStream fileInputStream = new FileInputStream(path);
                fileInputStreamList.add(fileInputStream);
            } catch (FileNotFoundException e) {
                Log.e(LOG_TAG + "buildFileInputStreamList", "FileNotFoundException is " + e
                        .getMessage());
            }
        }

        if (fileInputStreamList.size() == 0) {
            Log.i(LOG_TAG + "buildFileInputStreamList", "fileInputStreamList is null");
            return null;
        }

        return fileInputStreamList.toArray(new FileInputStream[fileInputStreamList.size()]);
    }

    /**
     * 获取指定类型的组缓存文件对象
     *
     * @param cacheKey 缓存组key
     * @param levelKey 缓存层级key
     * @param type     文件类型
     *
     * @return 文件数组，异常或目录为空则返回null
     */
    public File[] getGroupFile(String cacheKey, String levelKey, int type) {
        Log.i(LOG_TAG + "getGroupFile", "cache key is " + cacheKey);
        Log.i(LOG_TAG + "getGroupFile", "type is " + type);
        Log.i(LOG_TAG + "getGroupFile", "level key is " + levelKey);

        // 文件路径
        String[] pathList = getGroupPath(cacheKey, levelKey, type);

        return buildFileList(pathList);
    }

    /**
     * 删除指定的缓存，如果该缓存key指向缓存组，则会删除全部组文件
     *
     * @param cacheKey 缓存key或缓存组key
     * @param levelKey 缓存层级key
     */
    public void delete(String cacheKey, String levelKey) {
        // 先删除文件
        File file = getFile(cacheKey, levelKey);
        if (file != null) {
            file.delete();
        }


        // 删除索引
        cacheInfoOperator.delete(cacheKey, levelKey);
    }

    /**
     * 清空缓存目录
     */
    public void clear() {

    }

    /**
     * 清空缓存层级目录
     *
     * @param levelKey 缓存层级key
     */
    public void clear(String levelKey) {

    }
}
