package org.mobile.library;
/**
 * Created by 超悟空 on 2015/11/30.
 */

import android.os.Environment;
import android.util.Log;

import org.junit.Test;
import org.mobile.library.network.communication.Communication;
import org.mobile.library.network.factory.CommunicationBuilder;
import org.mobile.library.network.factory.NetworkType;
import org.mobile.library.network.util.NetworkCallback;
import org.mobile.library.network.util.NetworkProgressListener;
import org.mobile.library.network.util.NetworkRefreshProgressHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * 测试文件下载
 *
 * @author 超悟空
 * @version 1.0 2015/11/30
 * @since 1.0
 */
public class TestDownload {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "TestDownload.";

    /**
     * 一个安装包地址
     */
    private String url = "http://218.92.115.55/Mobile_App/hmw/Android/hmw.apk";

    /**
     * 存放文件路径
     */
    private File imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment
            .DIRECTORY_DOWNLOADS), "hmw.apk");

    /**
     * 异步下载
     *
     * @throws Exception
     */
    @Test
    public void async() throws Exception {

        // 通讯工具
        Communication communication = new CommunicationBuilder
                (NetworkType.DOWNLOAD).build();

        communication.setTaskName(url);

        // 任务开始时间
        final long startTime = System.currentTimeMillis();

        // 设置进度监听器
        NetworkRefreshProgressHandler refreshProgressHandler = (NetworkRefreshProgressHandler)
                communication;

        refreshProgressHandler.setNetworkProgressListener(new NetworkProgressListener() {
            @Override
            public void onRefreshProgress(long current, long total, boolean done) {
                Log.i(LOG_TAG + "async", "download " + current + "/" + total + "，  cast time " +
                        (System.currentTimeMillis() - startTime));
            }
        });

        final Integer LOCK = 1;

        communication.Request(null, new NetworkCallback() {
            @Override
            public void onFinish(boolean result, Object response) {
                assertTrue(result);
                assertNotNull(response);
                Log.i(LOG_TAG + "async", "download end cast time " + (System.currentTimeMillis()
                        - startTime));

                Log.i(LOG_TAG + "async", "write in file begin " + (System.currentTimeMillis() -
                        startTime));

                assertTrue(response instanceof InputStream);

                InputStream inputStream = (InputStream) response;

                try {

                    if (!imageFile.exists()) {
                        imageFile.createNewFile();
                    }

                    FileOutputStream outputStream = new FileOutputStream(imageFile);

                    byte[] buffer = new byte[100000];
                    int count = 0;

                    while ((count = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, count);
                        Log.i(LOG_TAG + "async", "write file " + count + " ， cast time " +
                                (System.currentTimeMillis() - startTime));
                    }

                    inputStream.close();

                    outputStream.close();

                    Log.i(LOG_TAG + "async", "write in file end " + (System.currentTimeMillis() -
                            startTime));

                } catch (IOException e) {
                    e.printStackTrace();
                }

                synchronized (LOCK) {
                    LOCK.notify();
                }
            }
        });

        synchronized (LOCK) {
            LOCK.wait();
        }
    }

    /**
     * 同步下载
     *
     * @throws Exception
     */
    @Test
    public void sync() throws Exception {

        // 通讯工具
        Communication communication = new CommunicationBuilder
                (NetworkType.DOWNLOAD).build();

        communication.setTaskName(url);

        // 任务开始时间
        final long startTime = System.currentTimeMillis();

        // 设置进度监听器
        NetworkRefreshProgressHandler refreshProgressHandler = (NetworkRefreshProgressHandler)
                communication;

        refreshProgressHandler.setNetworkProgressListener(new NetworkProgressListener() {
            @Override
            public void onRefreshProgress(long current, long total, boolean done) {
                Log.i(LOG_TAG + "sync", "download " + current + "/" + total + "，  cast time " +
                        (System.currentTimeMillis() - startTime));
            }
        });

        communication.Request(null);

        assertTrue(communication.isSuccessful());

        Log.i(LOG_TAG + "sync", "download end cast time " + (System.currentTimeMillis() -
                startTime));

        assertNotNull(communication.Response());

        assertTrue(communication.Response() instanceof InputStream);

        InputStream inputStream = (InputStream) communication.Response();

        try {

            if (!imageFile.exists()) {
                imageFile.createNewFile();
            }

            FileOutputStream outputStream = new FileOutputStream(imageFile);

            byte[] buffer = new byte[100000];
            int count = 0;
            Log.i(LOG_TAG + "sync", "write in file begin " + (System.currentTimeMillis() -
                    startTime));

            while ((count = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, count);
                Log.i(LOG_TAG + "sync", "write file " + count + " ， cast time " +
                        (System.currentTimeMillis() - startTime));
            }

            inputStream.close();

            outputStream.close();

            communication.close();

            Log.i(LOG_TAG + "sync", "write in file end " + (System.currentTimeMillis() -
                    startTime));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
