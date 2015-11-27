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

        this.cacheLevelOperator = new CacheLevelOperator(context);
        this.cacheInfoOperator = new CacheInfoOperator(context);

        // 获取缓存根目录
        File file = context.getExternalCacheDir();

        if (file != null) {
            Log.i(LOG_TAG + "CacheFileUtil", "now external cache path");
            externalCacheRootPath = file.getPath();
            external = true;
        }

        file = context.getCacheDir();
        internalCacheRootPath = file.getPath();
        if (externalCacheRootPath == null) {
            Log.i(LOG_TAG + "CacheFileUtil", "now internal cache path");
            external = false;
        }
    }

    /**
     * 保存一个缓存文件信息到索引数据库
     *
     * @param cacheLevel 缓存层级对象
     * @param cacheKey   缓存文件key
     * @param timeout    超时时间
     * @param type       缓存文件类型
     *
     * @return 返回文件输出流用于写缓存文件，如果文件系统存在异常则返回null
     */
    public FileOutputStream put(CacheLevel cacheLevel, String cacheKey, long timeout, int type) {
        return put(cacheLevel, cacheKey, timeout, type, false);
    }

    /**
     * 保存一个缓存文件
     *
     * @param cacheLevel 缓存层级对象
     * @param cacheKey   缓存文件key
     * @param timeout    超时时间
     * @param type       缓存文件类型
     * @param group      标识是否为组
     *
     * @return 返回文件输出流用于写缓存文件，如果文件系统存在异常则返回null
     */
    public FileOutputStream put(CacheLevel cacheLevel, String cacheKey, long timeout, int type,
                                boolean group) {
        Log.i(LOG_TAG + "put", "cache key:" + cacheKey + " , level key:" + cacheLevel.getKey() +
                " , " +
                "timeout:" + timeout + " , type:" + type + " , group:" + group);

        // 层级路径
        String levelPath = cacheLevel.getRealPath();
        Log.i(LOG_TAG + "put", "level " + cacheLevel.getKey() + " path is " + levelPath);
        // 尝试获取存在的缓存信息
        CacheInfo cacheInfo = cacheInfoOperator.queryCacheInfo(cacheKey, cacheLevel.getKey());
        // 标识文件是否存在
        boolean exist = false;

        if (group || cacheInfo == null) {
            // 组缓存或不存在则新建
            Log.i(LOG_TAG + "put", cacheKey + " not exist");
            cacheInfo = new CacheInfo(cacheKey, UUID.randomUUID().toString(), cacheLevel.getKey());
        } else {
            // 已存在，不需要新建
            Log.i(LOG_TAG + "put", cacheKey + " exist");
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
     * @param key       完整层级key
     * @param parentKey 父级key
     *
     * @return 缓存层级信息对象
     */
    public CacheLevel getCacheLevel(String key, String parentKey) {
        Log.i(LOG_TAG + "getCacheLevel", "level key:" + key + " , parent key:" + parentKey);

        // 尝试查询
        CacheLevel cacheLevel = cacheLevelOperator.queryCacheLevel(key);

        if (cacheLevel == null) {
            // 不存在则新建
            Log.i(LOG_TAG + "getCacheLevel", "level " + key + " not exist");
            String path;
            if (parentKey == null || "root".equals(parentKey)) {
                path = UUID.randomUUID().toString();
            } else {
                path = cacheLevelOperator.queryCacheLevel(parentKey).getRealPath() + "/" + UUID
                        .randomUUID().toString();
            }
            Log.i(LOG_TAG + "getCacheLevel", "level " + key + " new path is " + path);
            cacheLevel = new CacheLevel(key, path);

            // 保存到数据库
            cacheLevelOperator.insert(cacheLevel);
        }

        // 尝试创建文件夹
        File file;
        if (external) {
            file = new File(externalCacheRootPath + "/" + cacheLevel.getRealPath());
        } else {
            file = new File(internalCacheRootPath + "/" + cacheLevel.getRealPath());
        }

        if (!file.exists() || !file.isDirectory()) {
            file.mkdirs();
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
     * 获取缓存文件路径
     *
     * @param cacheKey   缓存key
     * @param cacheLevel 缓存层级对象
     *
     * @return 文件路径，key未被添加过或该key是一个组则返回null，不保证指定路径一定有文件存在
     */
    public String getPath(String cacheKey, CacheLevel cacheLevel) {
        Log.i(LOG_TAG + "getPath", "cache key:" + cacheKey + " , level key:" + cacheLevel.getKey());

        CacheInfo cacheInfo = cacheInfoOperator.queryCacheInfo(cacheKey, cacheLevel.getKey());

        if (cacheInfo != null) {
            // 层级路径
            String levelPath = cacheLevel.getRealPath();

            return buildPath(levelPath, cacheInfo);
        } else {
            Log.i(LOG_TAG + "getPath", "no cache key:" + cacheKey + " in level key:" + cacheLevel
                    .getKey());
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

        // 完整路径
        String finalPath = (cacheInfo.isExternal() ? externalCacheRootPath :
                internalCacheRootPath) + "/" +
                levelPath + "/" + cacheInfo.getRealFileName();

        Log.i(LOG_TAG + "buildPath", "final cache path is " + finalPath);

        return finalPath;
    }

    /**
     * 获取指定类型的缓存文件路径
     *
     * @param cacheLevel 缓存层级对象
     * @param type       文件类型
     *
     * @return 路径数组
     */
    public String[] getPath(CacheLevel cacheLevel, int type) {
        Log.i(LOG_TAG + "getPath", "level key:" + cacheLevel.getKey() + " , type:" + type);

        List<CacheInfo> cacheInfoList = cacheInfoOperator.queryCacheInfo(cacheLevel.getKey(), type);

        return buildPathList(cacheLevel.getRealPath(), cacheInfoList);
    }

    /**
     * 获取指定层级的缓存文件路径
     *
     * @param cacheLevel 缓存层级对象
     *
     * @return 路径数组
     */
    public String[] getPath(CacheLevel cacheLevel) {
        Log.i(LOG_TAG + "getPath", "level key:" + cacheLevel.getKey());

        List<CacheInfo> cacheInfoList = cacheInfoOperator.queryCacheInfo(cacheLevel.getKey());

        return buildPathList(cacheLevel.getRealPath(), cacheInfoList);
    }

    /**
     * 获取指定类型的组缓存路径
     *
     * @param cacheKey   缓存组key
     * @param cacheLevel 缓存层级对象
     * @param type       缓存类型
     *
     * @return 路径数组
     */
    public String[] getGroupPath(String cacheKey, CacheLevel cacheLevel, int type) {
        Log.i(LOG_TAG + "getGroupPath", "level key:" + cacheLevel.getKey() + " , cache key:" +
                cacheKey +
                " , type:" + type);

        List<CacheInfo> cacheInfoList = cacheInfoOperator.queryCacheGroup(cacheKey, cacheLevel
                .getKey(), type);

        return buildPathList(cacheLevel.getRealPath(), cacheInfoList);
    }

    /**
     * 获取组全部缓存路径
     *
     * @param cacheKey   缓存组key
     * @param cacheLevel 缓存层级对象
     *
     * @return 路径数组
     */
    public String[] getGroupPath(String cacheKey, CacheLevel cacheLevel) {
        Log.i(LOG_TAG + "getGroupPath", "level key:" + cacheLevel.getKey() + " , cache key:" +
                cacheKey);

        List<CacheInfo> cacheInfoList = cacheInfoOperator.queryCacheGroup(cacheKey, cacheLevel
                .getKey());

        return buildPathList(cacheLevel.getRealPath(), cacheInfoList);
    }

    /**
     * 装配缓存真实路径数组
     *
     * @param levelPath     缓存层级路径
     * @param cacheInfoList 缓存信息列表
     *
     * @return 装配好的数组
     */
    @Nullable
    private String[] buildPathList(String levelPath, List<CacheInfo> cacheInfoList) {
        if (cacheInfoList.size() > 0) {
            Log.i(LOG_TAG + "buildPathList", "path list count is " + cacheInfoList.size());
            // 要返回的路径数组
            String[] pathList = new String[cacheInfoList.size()];

            for (int i = 0; i < cacheInfoList.size(); i++) {
                CacheInfo cacheInfo = cacheInfoList.get(i);

                pathList[i] = buildPath(levelPath, cacheInfo);
            }
            return pathList;
        } else {
            Log.i(LOG_TAG + "buildPathList", "path list count is 0");
            return new String[0];
        }
    }

    /**
     * 删除指定的缓存，如果该缓存key指向缓存组，则会删除全部组文件
     *
     * @param cacheKey   缓存key或缓存组key
     * @param cacheLevel 缓存层级对象
     */
    public void delete(String cacheKey, CacheLevel cacheLevel) {
        Log.i(LOG_TAG + "delete", "level key:" + cacheLevel.getKey() + ", cache key:" + cacheKey);

        // 先删除文件
        String cachePath = getPath(cacheKey, cacheLevel);
        if (cachePath != null) {
            File file = new File(cachePath);
            deleteFile(file);
        }

        // 缓存组
        List<CacheInfo> cacheInfoList = cacheInfoOperator.queryCacheGroup(cacheKey, cacheLevel
                .getKey());

        // 文件路径
        String[] pathList = buildPathList(cacheLevel.getRealPath(), cacheInfoList);

        // 删除文件
        if (pathList != null) {
            for (String path : pathList) {
                deleteFile(new File(path));
            }
        }

        // 删除索引
        cacheInfoOperator.delete(cacheKey, cacheLevel.getKey());
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
     * @param cacheLevel 缓存层级对象
     */
    public void clear(CacheLevel cacheLevel) {
        Log.i(LOG_TAG + "clear", "level key is " + cacheLevel.getKey());

        // 层级路径
        String levelPath = cacheLevel.getRealPath();

        deleteFile(new File(internalCacheRootPath, levelPath));

        if (external) {
            deleteFile(new File(externalCacheRootPath, levelPath));
        }

        cacheInfoOperator.delete(cacheLevel.getKey());
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
        deleteExceedCapacityFile(cacheFileList);
    }

    /**
     * 当容量超过层级规定最大值时删除较旧的文件(当前仅实现根目录超载控制)
     *
     * @param cacheFileList 修改时间升序缓存文件
     */
    private void deleteExceedCapacityFile(List<CacheFile> cacheFileList) {
        if (cacheFileList == null || cacheFileList.isEmpty()) {
            Log.d(LOG_TAG + "deleteExceedCapacityFile", "cache file list is null");
            return;
        }

        Log.i(LOG_TAG + "deleteExceedCapacityFile", "cache file list count is " + cacheFileList
                .size());

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
            try {
                currentCapacity += new FileInputStream(cacheFileList.get(i).getFile()).available();
            } catch (IOException e) {
                Log.i(LOG_TAG + "deleteExceedCapacityFile", "IOException is " + e.getMessage());
            }

            if (currentCapacity > maxCapacity) {
                // 容量超出限制则停止计算
                deleteIndex = i;
                Log.i(LOG_TAG + "deleteExceedCapacityFile", "limit exceeded, index is " +
                        deleteIndex);
                break;
            }
        }

        if (deleteIndex > -1) {
            // 执行删除
            for (int i = 0; i <= deleteIndex; i++) {
                // 删除文件
                CacheInfo cacheInfo = cacheFileList.get(i).getCacheInfo();
                File file = cacheFileList.get(i).getFile();

                Log.i(LOG_TAG + "deleteExceedCapacityFile", "exceed capacity cache key: " +
                        cacheInfo.getKey() + ", level: " + cacheInfo.getLevelKey() + " , file " +
                        "path:" + file.getAbsolutePath());

                file.delete();
                // 删除索引
                cacheInfoOperator.delete(cacheInfo);
            }
        }
    }

    /**
     * 按修改时间升序排序并且删除超时文件
     *
     * @param cacheInfoList 要处理的文件信息集合
     */
    private List<CacheFile> sortAndDelete(List<CacheInfo> cacheInfoList) {
        if (cacheInfoList == null || cacheInfoList.isEmpty()) {
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
            String levelPath;

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

            Log.i(LOG_TAG + "sortAndDelete", "level path is " + levelPath);

            if (!external && cacheInfo.isExternal()) {
                // 当前连接不上存储卡
                Log.i(LOG_TAG + "sortAndDelete", "now not SDCard");
                continue;
            }

            // 缓存文件完整路径
            String path = buildPath(levelPath, cacheInfo);

            File file = new File(path);

            if (!file.exists()) {
                // 加入丢失列表
                Log.i(LOG_TAG + "sortAndDelete", "miss cache key: " + cacheInfo.getKey() + ", " +
                        "level: " + cacheInfo.getLevelKey() + ", file path:" + file
                        .getAbsolutePath());
                deleteCacheInfoList.add(cacheInfo);
                continue;
            }

            if (cacheInfo.getTimeOut() > 0 && System.currentTimeMillis() - file.lastModified() >=
                    cacheInfo.getTimeOut()) {
                // 删除超时文件
                Log.i(LOG_TAG + "sortAndDelete", "timeout cache key: " + cacheInfo.getKey() +
                        ", level: " + cacheInfo.getLevelKey() + ", file path:" + file
                        .getAbsolutePath());
                file.delete();
                deleteCacheInfoList.add(cacheInfo);
                continue;
            }

            // 加入待排序列表
            Log.i(LOG_TAG + "sortAndDelete", "wait sort cache key: " + cacheInfo.getKey() +
                    ", level key: " + cacheInfo.getLevelKey() + ", file path:" + file
                    .getAbsolutePath());
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
            Log.d(LOG_TAG + "deleteFile", "file is null");
            return;
        }

        if (targetFile.isDirectory()) {
            Log.i(LOG_TAG + "deleteFile", "file is directory, path is" + targetFile.getName());
            File[] files = targetFile.listFiles();
            for (File file : files) {
                deleteFile(file);
            }
        } else {
            Log.i(LOG_TAG + "deleteFile", "file name is " + targetFile.getName());
            targetFile.delete();
        }
    }
}
