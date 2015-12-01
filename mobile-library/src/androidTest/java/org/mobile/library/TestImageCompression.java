package org.mobile.library;
/**
 * Created by 超悟空 on 2015/12/1.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import org.junit.Test;
import org.mobile.library.common.function.ImageCompression;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import static org.junit.Assert.assertTrue;

/**
 * 图片压缩工具测试
 *
 * @author 超悟空
 * @version 1.0 2015/12/1
 * @since 1.0
 */
public class TestImageCompression {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "TestImageCompression.";

    /**
     * 图片路径1.88MB
     */
    private File imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment
            .DIRECTORY_DCIM), "Camera/IMG_20151201_133943.jpg");

    /**
     * 保留高分辨率
     *
     * @throws Exception
     */
    @Test
    public void highResolution() throws Exception {

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(imageFile.getPath(), options);

        options.inSampleSize = ImageCompression.calculateLowSampleSize(options, 720, 1280);

        assertTrue(options.inSampleSize >= 1);

        options.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath(), options);

        // 新图片路径
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment
                .DIRECTORY_DOWNLOADS), "highResolution.jpg");

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));

        Log.i(LOG_TAG + "highResolution", "new width:" + bitmap.getWidth() + ", new height:" +
                bitmap.getHeight());
        Log.i(LOG_TAG + "highResolution", "bitmap ByteCount is " + bitmap.getByteCount());

        assertTrue(bitmap.getWidth() <= 1800 || bitmap.getHeight() <= 1400);
    }

    /**
     * 保留低分辨率
     *
     * @throws Exception
     */
    @Test
    public void lowResolution() throws Exception {

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(imageFile.getPath(), options);

        options.inSampleSize = ImageCompression.calculateHighSampleSize(options, 720, 1280);

        assertTrue(options.inSampleSize > 1);

        options.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath(), options);

        // 新图片路径
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment
                .DIRECTORY_DOWNLOADS), "lowResolution.jpg");

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));

        Log.i(LOG_TAG + "lowResolution", "new width:" + bitmap.getWidth() + ", new height:" +
                bitmap.getHeight());
        Log.i(LOG_TAG + "lowResolution", "bitmap ByteCount is " + bitmap.getByteCount());

        assertTrue(bitmap.getWidth() <= 1400 && bitmap.getHeight() <= 900);
    }

    /**
     * 以宽为基准分辨率
     *
     * @throws Exception
     */
    @Test
    public void widthResolution() throws Exception {

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(imageFile.getPath(), options);

        options.inSampleSize = ImageCompression.calculateWidthSampleSize(options, 720);

        assertTrue(options.inSampleSize > 1);

        options.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath(), options);

        // 新图片路径
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment
                .DIRECTORY_DOWNLOADS), "widthResolution.jpg");

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));

        Log.i(LOG_TAG + "widthResolution", "new width:" + bitmap.getWidth() + ", new height:" +
                bitmap.getHeight());
        Log.i(LOG_TAG + "widthResolution", "bitmap ByteCount is " + bitmap.getByteCount());

        assertTrue(bitmap.getWidth() <= 900);
    }

    /**
     * 以高为基准分辨率
     *
     * @throws Exception
     */
    @Test
    public void heightResolution() throws Exception {

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(imageFile.getPath(), options);

        options.inSampleSize = ImageCompression.calculateHeightSampleSize(options, 1280);

        assertTrue(options.inSampleSize > 1);

        options.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath(), options);

        // 新图片路径
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment
                .DIRECTORY_DOWNLOADS), "heightResolution.jpg");

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));

        Log.i(LOG_TAG + "heightResolution", "new width:" + bitmap.getWidth() + ", new height:" +
                bitmap.getHeight());
        Log.i(LOG_TAG + "heightResolution", "bitmap ByteCount is " + bitmap.getByteCount());

        assertTrue(bitmap.getHeight() <= 1400);
    }

    /**
     * 手动指定比率压缩
     *
     * @throws Exception
     */
    @Test
    public void scaleCompress() throws Exception {

        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath());
        Log.i(LOG_TAG + "scaleCompress", "old bitmap ByteCount is " + bitmap.getByteCount());

        // 新图片路径
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment
                .DIRECTORY_DOWNLOADS), "scaleCompress.jpg");

        // 压缩为30%
        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, new FileOutputStream(file));

        Bitmap newBitmap = BitmapFactory.decodeFile(file.getPath());
        Log.i(LOG_TAG + "scaleCompress", "new bitmap ByteCount is " + newBitmap.getByteCount());
    }

    /**
     * 期望大小压缩
     *
     * @throws Exception
     */
    @Test
    public void sizeCompress() throws Exception {

        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath());
        Log.i(LOG_TAG + "sizeCompress", "old bitmap ByteCount is " + bitmap.getByteCount());

        // 新图片路径
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment
                .DIRECTORY_DOWNLOADS), "sizeCompress.jpg");

        // 期望压缩到200K
        ByteArrayOutputStream outputStream = ImageCompression.compressImage(bitmap, 200);

        assertTrue(outputStream.size() <= 200 * 1024);

        FileOutputStream fileOutputStream = new FileOutputStream(file);

        outputStream.writeTo(new FileOutputStream(file));

        fileOutputStream.flush();
        fileOutputStream.close();
        outputStream.flush();
        outputStream.close();

        Bitmap newBitmap = BitmapFactory.decodeStream(new FileInputStream(file));
        Log.i(LOG_TAG + "sizeCompress", "new bitmap ByteCount is " + newBitmap.getByteCount());
    }

    /**
     * 尺寸和质量同时压缩
     *
     * @throws Exception
     */
    @Test
    public void allCompress() throws Exception {

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(imageFile.getPath(), options);

        options.inSampleSize = ImageCompression.calculateHighSampleSize(options, 720, 1280);

        assertTrue(options.inSampleSize >= 1);

        options.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath(), options);

        Log.i(LOG_TAG + "allCompress", "resolution compress bitmap ByteCount is " + bitmap
                .getByteCount());

        // 新图片路径
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment
                .DIRECTORY_DOWNLOADS), "allCompress.jpg");

        // 期望压缩到50K
        ByteArrayOutputStream outputStream = ImageCompression.compressImage(bitmap, 50);

        assertTrue(outputStream.size() <= 50 * 1024);

        FileOutputStream fileOutputStream = new FileOutputStream(file);

        outputStream.writeTo(new FileOutputStream(file));

        fileOutputStream.flush();
        fileOutputStream.close();
        outputStream.flush();
        outputStream.close();

        Bitmap newBitmap = BitmapFactory.decodeStream(new FileInputStream(file));
        Log.i(LOG_TAG + "allCompress", "final bitmap ByteCount is " + newBitmap.getByteCount());
    }
}
