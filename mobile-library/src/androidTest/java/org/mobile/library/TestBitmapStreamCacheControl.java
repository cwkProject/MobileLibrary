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
import java.io.InputStream;
import java.io.OutputStream;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * 测试图片流缓存
 *
 * @author 超悟空
 * @version 1.0 2015/11/27
 * @since 1.0
 */
public class TestBitmapStreamCacheControl {

    /**
     * 图片路径1.88MB
     */
    private static final File imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera/IMG_20151127_094916.jpg");

    /**
     * 缓存工具key
     */
    private static String LEVEL_KEY = "BitmapTest";

    @Before
    public void setUp() throws Exception {

        CacheManager.autoClear();
        // 缓存工具
        CacheTool cacheTool = CacheManager.getCacheTool(LEVEL_KEY);

        // 设置文件超时
        cacheTool.setTimeout(100000);
    }

//    /**
//     * 批量测试
//     * @throws Exception
//     */
//    @Test
//    public void batch() throws Exception {
//
//        String key = "test";
//
//        for (int i=0;i<30;i++){
//            CacheManager.getCacheTool(LEVEL_KEY).put(key + i, BitmapFactory
//                    .decodeFile(imageFile.getPath()));
//        }
//
//        for (int i=0;i<30;i++){
//            assertTrue(CacheManager.getCacheTool(LEVEL_KEY).getForBitmap(key+i) instanceof Bitmap);
//        }
//    }

    /**
     * 主动写入流测试
     * @throws Exception
     */
    @Test
    public void writeStream() throws Exception {

        String key = "test";

        OutputStream outputStream= CacheManager.getCacheTool(LEVEL_KEY).putAndBack(key);

        BitmapFactory.decodeFile(imageFile.getPath()).compress(Bitmap.CompressFormat.JPEG, 30,
                outputStream);

        InputStream inputStream=CacheManager.getCacheTool(LEVEL_KEY).getInputStream(key);

        Log.i("TestBitmapStreamCacheControl.writeStream","inputStream size is "+inputStream
                .available());

        assertTrue(CacheManager.getCacheTool(LEVEL_KEY).getForBitmap(key) instanceof Bitmap);

        assertNotNull(BitmapFactory.decodeStream(inputStream));

    }
}
