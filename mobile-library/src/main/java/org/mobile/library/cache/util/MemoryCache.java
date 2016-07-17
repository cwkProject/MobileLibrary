package org.mobile.library.cache.util;
/**
 * Created by 超悟空 on 2015/11/25.
 */

import android.support.v4.util.LruCache;
import android.util.Log;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.Weighers;

import java.lang.ref.SoftReference;
import java.util.Map;

/**
 * 内存缓存，包括硬缓存和软引用
 *
 * @author 超悟空
 * @version 1.0 2015/11/25
 * @since 1.0
 */
@SuppressWarnings("SynchronizeOnNonFinalField")
public class MemoryCache {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "MemoryCache.";

    /**
     * 默认10M硬缓存空间
     */
    private static final int LRU_CACHE_SIZE = 50 * 1024 * 1024;

    /**
     * 默认48软缓存空间
     */
    private static final int SOFT_CACHE_SIZE = 48;

    /**
     * 硬缓存
     */
    private static LruCache<String, CacheObject> lruCache = null;

    /**
     * 软缓存
     */
    private static Map<String, SoftReference<CacheObject>> softCache = null;

    /**
     * 构造函数
     */
    public MemoryCache() {
        createMemoryCache();
    }

    /**
     * 设置最大缓存容量
     *
     * @param size 新容量
     */
    public void setMaxSize(int size) {
        createMemoryCache();
        lruCache.resize(size);
    }

    /**
     * 新建内存缓存
     */
    private synchronized void createMemoryCache() {
        if (softCache == null) {
            Log.v(LOG_TAG + "createMemoryCache", "create soft reference");
            softCache = new ConcurrentLinkedHashMap.Builder<String, SoftReference<CacheObject>>()
                    .maximumWeightedCapacity(SOFT_CACHE_SIZE).weigher(Weighers.singleton()).build();
        }

        if (lruCache == null) {
            Log.v(LOG_TAG + "createMemoryCache", "create hard cache");
            lruCache = new LruCache<String, CacheObject>(LRU_CACHE_SIZE) {
                @Override
                protected void entryRemoved(boolean evicted, String key, CacheObject oldValue,
                                            CacheObject newValue) {
                    if (evicted) {
                        Log.v(LOG_TAG + "entryRemoved", "hard cache is full , push to soft cache " +
                                "is " + key);
                        softCache.put(key, new SoftReference<>(oldValue));
                    }
                }

                @Override
                protected int sizeOf(String key, CacheObject value) {
                    int size = value.getSize();

                    Log.v(LOG_TAG + "sizeOf", "hard cache put " + key + " , size is " +
                            "is " + size);

                    return size;
                }
            };
        }
    }

    /**
     * 写入一个缓存
     *
     * @param key   缓存key
     * @param cache 缓存对象
     */
    public void put(String key, CacheObject cache) {
        Log.v(LOG_TAG + "put", "put cache is " + key);
        if (key != null && cache != null) {
            lruCache.put(key, cache);
        }
    }

    /**
     * 读取缓存对象
     *
     * @param key 缓存key
     *
     * @return 缓存对象
     */
    public CacheObject get(String key) {
        Log.v(LOG_TAG + "get", "get cache is " + key);
        if (key != null) {
            CacheObject cache = lruCache.get(key);
            if (cache != null) {
                Log.v(LOG_TAG + "get", "hit " + key + " in hard cache");
                return cache;
            }

            SoftReference<CacheObject> softReference = softCache.get(key);

            if (softReference != null) {
                cache = softReference.get();

                if (cache == null) {
                    // 已被回收
                    Log.v(LOG_TAG + "get", "soft reference cache " + key + " is recycled");
                    softCache.remove(key);
                } else {
                    Log.v(LOG_TAG + "get", "hit " + key + " in soft reference");
                    return cache;
                }
            } else {
                Log.v(LOG_TAG + "get", "memory cache not exist " + key);
            }
        }

        return null;
    }

    /**
     * 移除一个缓存
     *
     * @param key 缓存key
     */
    public void remove(String key) {
        Log.v(LOG_TAG + "remove", "remove cache is " + key);
        if (key != null) {
            Log.v(LOG_TAG + "remove", "remove " + key + " in hard cache");
            lruCache.remove(key);

            Log.v(LOG_TAG + "remove", "remove " + key + " in soft reference");
            softCache.remove(key);
        }
    }
}
