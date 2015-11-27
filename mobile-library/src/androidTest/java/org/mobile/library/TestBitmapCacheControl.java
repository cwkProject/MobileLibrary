package org.mobile.library;
/**
 * Created by 超悟空 on 2015/11/27.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.mobile.library.cache.util.CacheManager;
import org.mobile.library.cache.util.CacheTool;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * 测试图片缓存控制
 *
 * @author 超悟空
 * @version 1.0 2015/11/27
 * @since 1.0
 */
public class TestBitmapCacheControl {

    /**
     * 图片路径1.88MB
     */
    private static final File imageFile = new File(Environment.getExternalStoragePublicDirectory
            (Environment.DIRECTORY_DCIM), "Camera/IMG_20151127_094916.jpg");

    /**
     * 图片路径1.88MB
     */
    private static final File imageFile2 = new File(Environment.getExternalStoragePublicDirectory
            (Environment.DIRECTORY_DCIM), "Camera/IMG_20151127_094916.jpg");

    /**
     * 图片路径1.88MB
     */
    private static final File imageFile3 = new File(Environment.getExternalStoragePublicDirectory
            (Environment.DIRECTORY_DCIM), "Camera/IMG_20151127_094916.jpg");

    /**
     * 缓存工具key
     */
    private static String LEVEL_KEY = "BitmapTest";

    /**
     * 线程池线程数
     */
    private static final int POOL_COUNT = Runtime.getRuntime().availableProcessors() * 2 + 2;

    /**
     * 线程池
     */
    private static ExecutorService taskExecutor = Executors.newFixedThreadPool(POOL_COUNT);

    @Before
    public void setUp() throws Exception {

        CacheManager.autoClear();
        // 缓存工具
        CacheTool cacheTool = CacheManager.getCacheTool(LEVEL_KEY);

        // 设置文件超时
        cacheTool.setTimeout(100000);


    }

    /**
     * 常规测试
     *
     * @throws Exception
     */
    @Test
    public void normal() throws Exception {

        // 缓存工具
        CacheTool cacheTool = CacheManager.getCacheTool(LEVEL_KEY);

        CacheTool cacheTool2 = cacheTool.getChildCacheTool(LEVEL_KEY + "2");

        String key = "test";

        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath());

        Log.i("TestBitmapCacheControl.setUp", "bitmap ByteCount is " + bitmap.getByteCount());
        Log.i("TestBitmapCacheControl.setUp", "bitmap Width is " + bitmap.getWidth());
        Log.i("TestBitmapCacheControl.setUp", "bitmap Height is " + bitmap.getHeight());

        cacheTool.put(key, bitmap);

        cacheTool2.put(key, imageFile);

        assertTrue(cacheTool.getForFile(key).exists());

        assertEquals(bitmap, cacheTool.getForBitmap(key));

        assertTrue(cacheTool2.getForFile(key).exists());

        assertNotNull(cacheTool2.getForBitmap(key));

        assertTrue(cacheTool2.getForBitmap(key) instanceof Bitmap);
    }

    /**
     * 多线程缓存测试
     *
     * @throws Exception
     */
    @Test
    public void multithreading() throws Exception {

        final String key = "test";

        // 多线程执行66次
        int i = 0;

        while (i < 66) {

            final int finalI = i;
            taskExecutor.submit(new Runnable() {
                @Override
                public void run() {

                    synchronized (imageFile) {

                        CacheManager.getCacheTool(LEVEL_KEY).put(key + finalI, BitmapFactory
                                .decodeFile(imageFile.getPath()));

                    }
                }
            });

            taskExecutor.submit(new Runnable() {
                @Override
                public void run() {

                    synchronized (imageFile2) {

                        CacheManager.getCacheTool(LEVEL_KEY).put(key + (finalI + 1),
                                BitmapFactory.decodeFile(imageFile2.getPath()));

                    }
                }
            });

            taskExecutor.submit(new Runnable() {
                @Override
                public void run() {

                    synchronized (imageFile3) {

                        CacheManager.getCacheTool(LEVEL_KEY).put(key + (finalI + 2),
                                BitmapFactory.decodeFile(imageFile3.getPath()));

                    }
                }
            });

            i += 3;
        }

        taskExecutor.shutdown();

        while (true) {
            if (taskExecutor.isTerminated()) {
                assertTrue(CacheManager.getCacheTool(LEVEL_KEY).getForFiles().length >= 66);
                break;
            }
            Thread.sleep(1000);
        }
    }
}
