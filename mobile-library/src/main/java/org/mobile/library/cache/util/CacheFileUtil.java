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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        // 保存文件
        File file;

        // 尝试清除内存目录中的文件
        if (exist && !cacheInfo.isExternal() && external) {
            file = new File(buildPath(levelPath, cacheInfo));
            file.delete();
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

        // 刷新文件
        file = new File(buildPath(levelPath, cacheInfo));

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
     * 获取一个缓存层级信息，若指定key不存在则会被新建
     *
     * @param key       层级key
     * @param parentKey 父级key
     *
     * @return 缓存层级信息对象
     */
    public CacheLevel getCacheLevel(String key, String parentKey) {
        Log.i(LOG_TAG + "getCacheLevel", "level key is " + key);
        Log.i(LOG_TAG + "getCacheLevel", "parent key is " + parentKey);

        String levelKey;

        if (parentKey == null || "root".equals(parentKey)) {
            levelKey = key;
        } else {
            levelKey = parentKey + "/" + key;
        }

        // 尝试查询
        CacheLevel cacheLevel = cacheLevelOperator.queryCacheLevel(levelKey);

        if (cacheLevel == null) {
            // 不存在则新建
            if (parentKey == null || "root".equals(parentKey)) {
                cacheLevel = new CacheLevel(levelKey, UUID.randomUUID().toString());
            } else {
                String levelPath = cacheLevelOperator.queryCacheLevel(parentKey).getRealPath();
                cacheLevel = new CacheLevel(levelKey, levelPath + "/" + UUID.randomUUID()
                        .toString());
            }

            // 保存到数据库
            cacheLevelOperator.insert(cacheLevel);
        }

        return cacheLevel;
    }

    /**
     * 更新一个缓存层级信息
     *
     * @param cacheLevel 缓存层级信息对象
     */
    public void updateCacheLevel(CacheLevel cacheLevel) {
        cacheLevelOperator.update(cacheLevel);
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
        Log.i(LOG_TAG + "delete", "cache key is " + cacheKey);
        Log.i(LOG_TAG + "delete", "level key is " + levelKey);

        // 先删除文件
        File file = getFile(cacheKey, levelKey);
        if (file != null) {
            deleteFile(file);
        }

        // 缓存组
        List<CacheInfo> cacheInfoList = cacheInfoOperator.queryCacheGroup(cacheKey, levelKey);

        // 文件路径
        String[] pathList = buildPathList(levelKey, cacheInfoList);

        // 获得文件数组
        File[] fileList = buildFileList(pathList);

        // 删除文件
        if (fileList != null) {
            for (File file1 : fileList) {
                deleteFile(file1);
            }
        }

        // 删除索引
        cacheInfoOperator.delete(cacheKey, levelKey);
    }

    /**
     * 清空缓存目录
     */
    public void clear() {
        Log.i(LOG_TAG + "delete", "clear all");
        deleteFile(new File(internalCacheRootPath));

        if (external) {
            deleteFile(new File(externalCacheRootPath));
        }

        cacheInfoOperator.clear();
    }

    /**
     * 清空缓存层级目录
     *
     * @param levelKey 缓存层级key
     */
    public void clear(String levelKey) {
        Log.i(LOG_TAG + "clear", "level key is " + levelKey);

        // 层级路径
        String levelPath = cacheLevelOperator.queryCacheLevel(levelKey).getRealPath();

        deleteFile(new File(internalCacheRootPath, levelPath));

        if (external) {
            deleteFile(new File(externalCacheRootPath, levelPath));
        }

        cacheInfoOperator.delete(levelKey);
    }

    /**
     * 自动清理超时缓存和超量缓存
     */
    public void autoClear() {
        Log.i(LOG_TAG + "clear", "auto clear");
        // 全部缓存文件
        List<CacheInfo> cacheInfoList = cacheInfoOperator.queryAll();

        // 排序并且删除超时文件，获得排序后的文件
        List<CacheFile> cacheFileList = sortAndDelete(cacheInfoList);

        // 删除超容量的文件
        deleteFile(cacheFileList);
    }

    /**
     * 当容量超过层级规定最大值时删除较旧的文件(当前仅实现根目录超载控制)
     *
     * @param cacheFileList 修改时间升序缓存文件
     */
    private void deleteFile(List<CacheFile> cacheFileList) {
        // 根层级
        CacheLevel cacheLevel = cacheLevelOperator.queryCacheLevel("root");

        // 最大容量
        long maxCapacity = cacheLevel.getMaxCapacity();

        // 当前容量
        long currentCapacity = 0;

        // 标记删除位
        int deleteIndex = -1;

        // 计算
        for (int i = cacheFileList.size() - 1; i >= 0; i--) {
            // 计算当前量
            currentCapacity += cacheFileList.get(i).getFile().length();

            if (currentCapacity > maxCapacity) {
                // 容量超出限制则停止计算
                deleteIndex = i;
                Log.i(LOG_TAG + "deleteFile", "limit exceeded, index is " + deleteIndex);
                break;
            }
        }

        if (deleteIndex > -1) {
            // 执行删除
            for (int i = 0; i <= deleteIndex; i++) {
                // 删除文件
                cacheFileList.get(i).getFile().delete();
                // 删除索引
                cacheInfoOperator.delete(cacheFileList.get(i).getCacheInfo());
            }
        }
    }

    /**
     * 按修改时间升序排序并且删除超时文件
     *
     * @param cacheInfoList 要处理的文件信息集合
     */
    private List<CacheFile> sortAndDelete(List<CacheInfo> cacheInfoList) {
        if (cacheInfoList == null) {
            Log.d(LOG_TAG + "sortAndDelete", "cache info list is null");
            return new ArrayList<>();
        }

        Log.i(LOG_TAG + "sortAndDelete", "cache info list count is " + cacheInfoList.size());

        // 将要返回的排序缓存文件集合
        List<CacheFile> cacheFileList = new ArrayList<>();

        // 缓存层级信息集合
        Map<String, CacheLevel> cacheLevelMap = new HashMap<>();

        // 缓存文件超时或已丢失的信息列表，将会被删除
        List<CacheInfo> deleteCacheInfoList = new ArrayList<>();

        for (CacheInfo cacheInfo : cacheInfoList) {

            // 当前缓存层级路径
            String levelPath = null;

            if (!cacheLevelMap.containsKey(cacheInfo.getLevelKey())) {

                // 找到缓存层级
                CacheLevel cacheLevel = cacheLevelOperator.queryCacheLevel(cacheInfo.getLevelKey());

                // 加入层级索引
                cacheLevelMap.put(cacheInfo.getLevelKey(), cacheLevel);

                // 得到路径
                levelPath = cacheLevel.getRealPath();
            } else {
                // 得到路径
                levelPath = cacheLevelMap.get(cacheInfo.getLevelKey()).getRealPath();
            }

            if (!external && cacheInfo.isExternal()) {
                // 当前连接不上存储卡
                continue;
            }

            // 缓存文件完整路径
            String path = buildPath(levelPath, cacheInfo);

            File file = new File(path);

            if (!file.exists()) {
                // 加入丢失列表
                deleteCacheInfoList.add(cacheInfo);
                continue;
            }

            if (System.currentTimeMillis() - file.lastModified() >= cacheInfo.getTimeOut()) {
                // 删除超时文件
                file.delete();
                deleteCacheInfoList.add(cacheInfo);
            }

            // 加入待排序列表
            cacheFileList.add(new CacheFile(cacheInfo, file));
        }

        Log.i(LOG_TAG + "sortAndDelete", "delete invalid list count is " + deleteCacheInfoList
                .size());
        // 删除失效索引
        for (CacheInfo cacheInfo : deleteCacheInfoList) {
            cacheInfoOperator.delete(cacheInfo);
        }

        // 按最后修改时间排序
        Collections.sort(cacheFileList, new Comparator<CacheFile>() {
            @Override
            public int compare(CacheFile lhs, CacheFile rhs) {
                long l = lhs.getFile().lastModified();
                long r = rhs.getFile().lastModified();

                if (l == r) {
                    return 0;
                }

                return l > r ? 1 : -1;
            }
        });

        Log.i(LOG_TAG + "sortAndDelete", "sort cache file list count is " + cacheFileList.size());
        return cacheFileList;
    }

    /**
     * 保存缓存文件对象和缓存信息对象
     */
    private class CacheFile {
        /**
         * 缓存信息对象
         */
        private CacheInfo cacheInfo = null;

        /**
         * 缓存对应的文件对象
         */
        private File file = null;

        /**
         * 构造函数
         *
         * @param cacheInfo 缓存信息
         * @param file      缓存文件
         */
        public CacheFile(CacheInfo cacheInfo, File file) {
            this.cacheInfo = cacheInfo;
            this.file = file;
        }

        /**
         * 获取缓存信息对象
         *
         * @return 缓存信息对象
         */
        public CacheInfo getCacheInfo() {
            return cacheInfo;
        }

        /**
         * 获取缓存对应的文件对象
         *
         * @return 文件对象
         */
        public File getFile() {
            return file;
        }
    }

    /**
     * 删除文件
     *
     * @param targetFile 指定文件或路径
     */
    private void deleteFile(File targetFile) {
        if (targetFile == null) {
            return;
        }

        if (targetFile.isDirectory()) {
            File[] files = targetFile.listFiles();
            for (File file : files) {
                deleteFile(file);
            }
        } else {
            targetFile.delete();
        }
    }
}
