package org.mobile.library;
/**
 * Created by 超悟空 on 2016/1/6.
 */

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.Weighers;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

/**
 * 测试ConcurrentLinkedHashMap特性
 *
 * @author 超悟空
 * @version 1.0 2016/1/6
 * @since 1.0
 */
public class TestConcurrentLinkedHashMap {

    /**
     * 添加一个文本
     */
    private String TEXT = "但是你别忘记了，我们的目的是在UI层进行回调，而OkHttp的所有请求都不在UI层。于是我们还要实现我们写的接口，进行UI" +
            "操作的回调。由于涉及到消息机制，我们对之前的两个接口回调传的参数进行封装，封装为一个实体类便于传递。";

    @Test
    public void remove() throws Exception {
        Map<String, String> map = new ConcurrentLinkedHashMap.Builder<String, String>()
                .maximumWeightedCapacity(100).weigher(Weighers.singleton()).build();

        String key = "test";

        map.put(key, TEXT);

        assertEquals(TEXT, map.get(key));

        map.remove(key);

        assertFalse(map.containsKey(key));
        assertNull(map.get(key));
    }
}
