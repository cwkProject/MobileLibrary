package org.mobile.library.cache.util.convert;
/**
 * Created by 超悟空 on 2015/11/23.
 */

import org.mobile.library.cache.util.CacheObject;

import java.io.FileOutputStream;

/**
 * 缓存对象转换器，
 * 用于程序对象和缓存文件之间的转换
 *
 * @author 超悟空
 * @version 1.0 2015/11/23
 * @since 1.0
 */
public interface CacheConvert<CacheType> {

    /**
     * 将实际缓存对象转为缓存工具使用的临时缓存对象
     *
     * @param cache 实际缓存对象
     *
     * @return 临时缓存对象
     */
    CacheObject<CacheType> toCacheObject(CacheType cache);

    /**
     * 将临时缓存对象转换为实际缓存对象
     *
     * @param cacheObject 临时缓存对象
     *
     * @return 实际缓存对象
     */
    CacheType toCache(CacheObject cacheObject);

    /**
     * 根据缓存文件实际路径转换为实际缓存对象
     *
     * @param path 缓存路径
     *
     * @return 实际缓存对象，文件不存在或失败返回null
     */
    CacheType toCache(String path);

    /**
     * 将缓存写入到文件
     *
     * @param outputStream 缓存文件输出流
     * @param cache        缓存对象
     */
    void saveFile(FileOutputStream outputStream, CacheType cache);
}
