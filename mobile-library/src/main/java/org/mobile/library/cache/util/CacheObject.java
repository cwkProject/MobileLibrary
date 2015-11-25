package org.mobile.library.cache.util;
/**
 * Created by 超悟空 on 2015/11/23.
 */

/**
 * 临时缓存对象
 *
 * @author 超悟空
 * @version 1.0 2015/11/23
 * @since 1.0
 */
public class CacheObject<CacheType> {

    /**
     * 对象保存的实际缓存对象
     */
    private CacheType cache = null;

    /**
     * 该对象占用的内存空间
     */
    private int size = 0;

    /**
     * 构造函数
     *
     * @param cache 实际保存的缓存对象
     * @param size  该缓存占用的内存空间
     */
    public CacheObject(CacheType cache, int size) {
        this.cache = cache;
        this.size = size;
    }

    /**
     * 获取实际缓存对象
     *
     * @return 缓存对象
     */
    public CacheType getCache() {
        return cache;
    }

    /**
     * 获取缓存占用的内存空间
     *
     * @return 缓存大小
     */
    public int getSize() {
        return size;
    }
}
