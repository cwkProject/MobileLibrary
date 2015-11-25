package org.mobile.library;
/**
 * Created by 超悟空 on 2015/11/25.
 */

import org.junit.Before;
import org.junit.Test;
import org.mobile.library.cache.util.CacheManager;
import org.mobile.library.cache.util.CacheTool;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * 测试缓存控制器
 *
 * @author 超悟空
 * @version 1.0 2015/11/25
 * @since 1.0
 */
public class TestCacheControl {

    @Before
    public void setUp() throws Exception {
        CacheManager.autoClear();
    }

    /**
     * 测试文本类型缓存
     * @throws Exception
     */
    @Test
    public void textCache() throws Exception {

        // 缓存工具
        CacheTool cacheTool=CacheManager.getCacheTool("TextTest");

        // 设置文件超时
        cacheTool.setTimeout(10000);

        // 添加一个文本
        String text="但是你别忘记了，我们的目的是在UI层进行回调，而OkHttp的所有请求都不在UI层。于是我们还要实现我们写的接口，进行UI操作的回调。由于涉及到消息机制，我们对之前的两个接口回调传的参数进行封装，封装为一个实体类便于传递。";

        cacheTool.put("test",text);

        assertTrue(cacheTool.getForFile("test").exists());

        assertEquals(text, cacheTool.getForText("test"));

    }
}
