package org.mobile.library;
/**
 * Created by 超悟空 on 2015/11/27.
 */

import org.junit.Test;
import org.mobile.library.cache.util.CacheManager;
import org.mobile.library.cache.util.CacheTool;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * 测试根缓存控制
 *
 * @author 超悟空
 * @version 1.0 2015/11/27
 * @since 1.0
 */
public class TestRootCacheControl {

    /**
     * 添加一个文本
     */
    private String TEXT = "但是你别忘记了，我们的目的是在UI层进行回调，而OkHttp的所有请求都不在UI层。于是我们还要实现我们写的接口，进行UI" +
            "操作的回调。由于涉及到消息机制，我们对之前的两个接口回调传的参数进行封装，封装为一个实体类便于传递。";

    @Test
    public void root() throws Exception {
        // 缓存工具
        CacheTool cacheTool = CacheManager.getCacheTool();

        String key = "test";

        assertTrue(cacheTool.getForFile(key).exists());

        assertEquals(TEXT, cacheTool.getForText(key));

        assertEquals(TEXT, cacheTool.getForText(key));
    }
}
