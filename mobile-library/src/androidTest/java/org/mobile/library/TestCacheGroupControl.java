package org.mobile.library;
/**
 * Created by 超悟空 on 2015/11/27.
 */

import android.graphics.BitmapFactory;
import android.os.Environment;

import org.junit.Before;
import org.junit.Test;
import org.mobile.library.cache.util.CacheGroup;
import org.mobile.library.cache.util.CacheManager;
import org.mobile.library.cache.util.CacheTool;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.Assert.assertTrue;

/**
 * 缓存组测试
 *
 * @author 超悟空
 * @version 1.0 2015/11/27
 * @since 1.0
 */
public class TestCacheGroupControl {

    /**
     * 图片路径1.88MB
     */
    private File imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment
            .DIRECTORY_DCIM), "Camera/IMG_20151127_094916.jpg");

    /**
     * 添加一个文本
     */
    private String TEXT = "但是你别忘记了，我们的目的是在UI层进行回调，而OkHttp的所有请求都不在UI层。于是我们还要实现我们写的接口，进行UI" +
            "操作的回调。由于涉及到消息机制，我们对之前的两个接口回调传的参数进行封装，封装为一个实体类便于传递。";


    /**
     * 缓存工具key
     */
    private String LEVEL_KEY = "BitmapTest";

    @Before
    public void setUp() throws Exception {

        CacheManager.autoClear();
        // 缓存工具
        CacheTool cacheTool = CacheManager.getCacheTool(LEVEL_KEY);

        // 设置文件超时
        cacheTool.setTimeout(100000);
    }

    /**
     * 批量数据
     *
     * @throws Exception
     */
    @Test
    public void batch() throws Exception {

        CacheGroup cacheGroup = CacheManager.getCacheTool(LEVEL_KEY).getCacheGroup("testGroup");

        cacheGroup.put(imageFile);

        cacheGroup.put(BitmapFactory.decodeFile(imageFile.getPath()));

        cacheGroup.put(new FileInputStream(imageFile));

        for (int i = 0; i < 10; i++) {
            cacheGroup.put(TEXT);
        }

        assertTrue(CacheManager.getCacheTool(LEVEL_KEY).getCacheGroup("testGroup").getForFiles()
                .length >= 13);
    }
}
