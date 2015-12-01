package org.mobile.library;
/**
 * Created by 超悟空 on 2015/11/25.
 */

import org.junit.Before;
import org.junit.Test;
import org.mobile.library.cache.util.CacheManager;
import org.mobile.library.cache.util.CacheTool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * 测试文本缓存控制器
 *
 * @author 超悟空
 * @version 1.0 2015/11/25
 * @since 1.0
 */
public class TestTextCacheControl {

    /**
     * 添加一个文本
     */
    private String TEXT = "但是你别忘记了，我们的目的是在UI层进行回调，而OkHttp的所有请求都不在UI层。于是我们还要实现我们写的接口，进行UI" +
            "操作的回调。由于涉及到消息机制，我们对之前的两个接口回调传的参数进行封装，封装为一个实体类便于传递。";

    /**
     * 线程池线程数
     */
    private int POOL_COUNT = Runtime.getRuntime().availableProcessors() * 2 + 2;

    /**
     * 线程池
     */
    private ExecutorService taskExecutor = Executors.newFixedThreadPool(POOL_COUNT);

    /**
     * 缓存工具key
     */
    private static String LEVEL_KEY = "TextTest";

    @Before
    public void setUp() throws Exception {

        CacheManager.autoClear();
        // 缓存工具
        CacheTool cacheTool = CacheManager.getCacheTool(LEVEL_KEY);

        // 设置文件超时
        cacheTool.setTimeout(10000);
    }

    /**
     * 通常文本类型缓存测试
     *
     * @throws Exception
     */
    @Test
    public void textNormalCache() throws Exception {

        // 缓存工具
        CacheTool cacheTool = CacheManager.getCacheTool(LEVEL_KEY);

        CacheTool cacheTool2 = cacheTool.getChildCacheTool(LEVEL_KEY + "2");

        String key = "test";

        cacheTool.put(key, TEXT);

        cacheTool2.put(key, TEXT);

        assertTrue(cacheTool.getForFile(key).exists());

        assertEquals(TEXT, cacheTool.getForText(key));

        assertTrue(cacheTool2.getForFile(key).exists());

        assertEquals(TEXT, cacheTool2.getForText(key));
    }

    /**
     * 文本类型内存缓存测试
     *
     * @throws Exception
     */
    @Test
    public void textMemoryCache() throws Exception {

        // 缓存工具
        CacheTool cacheTool = CacheManager.getCacheTool(LEVEL_KEY);

        String key = "testMemory";

        cacheTool.put(key, TEXT, CacheManager.ONLY_MEMORY_CACHE);

        assertNull(cacheTool.getForFile(key));

        assertEquals(TEXT, cacheTool.getForText(key));

    }

    /**
     * 文本类型文件缓存测试
     *
     * @throws Exception
     */
    @Test
    public void textFileCache() throws Exception {

        // 缓存工具
        CacheTool cacheTool = CacheManager.getCacheTool(LEVEL_KEY);

        String key = "testFile";

        cacheTool.put(key, TEXT, CacheManager.ONLY_FILE_CACHE);

        assertTrue(cacheTool.getForFile(key).exists());

        assertEquals(TEXT, cacheTool.getForText(key));

    }

    /**
     * 文本类型文件数组缓存测试
     *
     * @throws Exception
     */
    @Test
    public void textListCache() throws Exception {

        // 缓存工具
        CacheTool cacheTool = CacheManager.getCacheTool(LEVEL_KEY);

        cacheTool.put("test1", TEXT);
        cacheTool.put("test2", TEXT);
        cacheTool.put("test3", TEXT);
        cacheTool.put("test4", TEXT);
        cacheTool.put("test5", TEXT);
        cacheTool.put("test6", TEXT);

        assertTrue(cacheTool.getForTexts().length >= 5);
    }

    /**
     * 文本类型移除缓存测试
     *
     * @throws Exception
     */
    @Test
    public void textRemoveCache() throws Exception {

        // 缓存工具
        CacheTool cacheTool = CacheManager.getCacheTool(LEVEL_KEY);

        String key = "testRemove";

        // 内存和文件
        cacheTool.put(key, TEXT);

        assertTrue(cacheTool.getForFile(key).exists());

        assertEquals(TEXT, cacheTool.getForText(key));

        cacheTool.remove(key);

        assertNull(cacheTool.getForFile(key));

        assertNull(cacheTool.getForText(key));

        // 仅文件
        cacheTool.put(key, TEXT, CacheManager.ONLY_FILE_CACHE);

        assertTrue(cacheTool.getForFile(key).exists());

        assertEquals(TEXT, cacheTool.getForText(key));

        cacheTool.remove(key);

        assertNull(cacheTool.getForFile(key));

        assertNull(cacheTool.getForText(key));

        // 仅内存
        cacheTool.put(key, TEXT, CacheManager.ONLY_MEMORY_CACHE);

        assertNull(cacheTool.getForFile(key));

        assertEquals(TEXT, cacheTool.getForText(key));

        cacheTool.remove(key);

        assertNull(cacheTool.getForFile(key));

        assertNull(cacheTool.getForText(key));
    }

    /**
     * 文本类型多线程缓存测试
     *
     * @throws Exception
     */
    @Test
    public void textMultithreadingCache() throws Exception {

        final String key = "test";

        // 多线程执行一百次
        for (int i = 0; i < 100; i++) {

            final int finalI = i;
            taskExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    CacheManager.getCacheTool(LEVEL_KEY).put(key + finalI, TEXT + finalI,
                            CacheManager.ONLY_FILE_CACHE);

                    assertTrue(CacheManager.getCacheTool(LEVEL_KEY).getForFile(key + finalI)
                            .exists());

                    assertEquals(TEXT + finalI, CacheManager.getCacheTool(LEVEL_KEY).getForText
                            (key + finalI));
                }
            });
        }

        taskExecutor.shutdown();

        while (true) {
            if (taskExecutor.isTerminated()) {
                assertTrue(CacheManager.getCacheTool(LEVEL_KEY).getForTexts().length >= 100);
                break;
            }
            Thread.sleep(1000);
        }
    }

}
