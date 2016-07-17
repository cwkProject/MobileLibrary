package org.mobile.library;
/**
 * Created by 超悟空 on 2015/10/28.
 */

import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;

import org.junit.Test;
import org.mobile.library.network.factory.CommunicationBuilder;
import org.mobile.library.network.factory.NetworkType;
import org.mobile.library.network.util.AsyncCommunication;
import org.mobile.library.network.util.NetworkCallback;
import org.mobile.library.network.util.NetworkProgressListener;
import org.mobile.library.network.util.NetworkRefreshProgressHandler;
import org.mobile.library.network.util.SyncCommunication;
import org.mobile.library.struct.FileInfo;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * 文件上传单元测试
 *
 * @author 超悟空
 * @version 1.0 2015/10/28
 * @since 1.0
 */
public class TestUpload {

    /**
     * 文件上传地址
     */
    private String URL = "http://218.92.115.55/M_Lhgl/Service/Handover/UploadFile.aspx";

    /**
     * 参数名
     */
    private String NAME = "CodeToken";

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "TestUpload.";

    @Test
    public void syncUpload() throws Exception {
        // 要上传的文件
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment
                .DIRECTORY_DOWNLOADS), "wallpaper.jpg");
        assertNotNull(file);
        Log.d(LOG_TAG + "syncUpload", "file is " + file);
        // 参数
        Map<String, Object> map = new HashMap<>();

        map.put(NAME, UUID.randomUUID().toString());

        // 加入文件
        map.put("File", new FileInfo(file, "wallpaper", MimeTypeMap.getSingleton()
                .getMimeTypeFromExtension("jpg")));

        SyncCommunication communication = new CommunicationBuilder(NetworkType.UPLOAD).url(URL)
                .build();

        final long startTime = System.currentTimeMillis();

        NetworkRefreshProgressHandler refreshProgressHandler = (NetworkRefreshProgressHandler)
                communication;

        refreshProgressHandler.setNetworkProgressListener(new NetworkProgressListener() {
            @Override
            public void onRefreshProgress(long current, long total, boolean done) {
                Log.i(LOG_TAG + "syncUpload", "upload " + current + "/" + total + "，  cast time " +
                        "" + (System.currentTimeMillis() - startTime));
            }
        });


        communication.Request(map);

        Log.i(LOG_TAG + "syncUpload", "upload end cast time " + (System.currentTimeMillis() -
                startTime));

        assertTrue(communication.isSuccessful());
        assertNotNull(communication.Response());
        communication.close();
    }

    @Test
    public void asyncUpload() throws Exception {
        // 要上传的文件
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment
                .DIRECTORY_DOWNLOADS), "wallpaper.jpg");
        assertNotNull(file);
        Log.d(LOG_TAG + "asyncUpload", "file is " + file);
        // 参数
        Map<String, Object> map = new HashMap<>();
        map.put(NAME, UUID.randomUUID().toString());

        // 加入文件
        map.put("File", new FileInfo(file, "wallpaper", MimeTypeMap.getSingleton()
                .getMimeTypeFromExtension("jpg")));

        AsyncCommunication communication = new CommunicationBuilder(NetworkType.UPLOAD).url(URL)
                .build();

        final long startTime = System.currentTimeMillis();

        NetworkRefreshProgressHandler refreshProgressHandler = (NetworkRefreshProgressHandler)
                communication;

        refreshProgressHandler.setNetworkProgressListener(new NetworkProgressListener() {
            @Override
            public void onRefreshProgress(long current, long total, boolean done) {
                Log.i(LOG_TAG + "asyncUpload", "upload " + current + "/" + total + "，  cast time " +
                        "" + (System.currentTimeMillis() - startTime));
            }
        });

        final Integer LOCK = 1;

        communication.Request(map, new NetworkCallback() {
            @Override
            public void onFinish(boolean result, Object response) {
                assertTrue(result);
                assertNotNull(response);
                Log.i(LOG_TAG + "asyncUpload", "upload end cast time " + (System
                        .currentTimeMillis() - startTime));

                synchronized (LOCK) {
                    LOCK.notify();
                }
            }
        });

        synchronized (LOCK) {
            LOCK.wait();
        }
    }

    @Test
    public void syncCancelUpload() throws Exception {
        // 要上传的文件
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment
                .DIRECTORY_DOWNLOADS), "wallpaper.jpg");
        assertNotNull(file);
        Log.d(LOG_TAG + "syncCancelUpload", "file is " + file);
        // 参数
        Map<String, Object> map = new HashMap<>();

        map.put(NAME, UUID.randomUUID().toString());

        // 加入文件
        map.put("File", new FileInfo(file, "wallpaper", MimeTypeMap.getSingleton()
                .getMimeTypeFromExtension("jpg")));

        final SyncCommunication communication = new CommunicationBuilder(NetworkType.UPLOAD).url
                (URL).build();

        final long startTime = System.currentTimeMillis();

        NetworkRefreshProgressHandler refreshProgressHandler = (NetworkRefreshProgressHandler)
                communication;

        refreshProgressHandler.setNetworkProgressListener(new NetworkProgressListener() {
            @Override
            public void onRefreshProgress(long current, long total, boolean done) {
                Log.i(LOG_TAG + "syncCancelUpload", "upload " + current + "/" + total + "，  cast " +
                        "time " +
                        "" + (System.currentTimeMillis() - startTime));
            }
        });

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                communication.cancel();
            }
        }, 30);

        communication.Request(map);

        Log.i(LOG_TAG + "syncCancelUpload", "upload end cast time " + (System.currentTimeMillis()
                - startTime));

        assertFalse(communication.isSuccessful());
        assertNull(communication.Response());

        communication.close();
    }

    @Test
    public void asyncCancelUpload() throws Exception {
        // 要上传的文件
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment
                .DIRECTORY_DOWNLOADS), "wallpaper.jpg");
        assertNotNull(file);
        Log.d(LOG_TAG + "asyncCancelUpload", "file is " + file);
        // 参数
        Map<String, Object> map = new HashMap<>();

        map.put(NAME, UUID.randomUUID().toString());

        // 加入文件
        map.put("File", new FileInfo(file, "wallpaper", MimeTypeMap.getSingleton()
                .getMimeTypeFromExtension("jpg")));

        final AsyncCommunication communication = new CommunicationBuilder(NetworkType.UPLOAD).url
                (URL).build();

        final long startTime = System.currentTimeMillis();

        NetworkRefreshProgressHandler refreshProgressHandler = (NetworkRefreshProgressHandler)
                communication;

        refreshProgressHandler.setNetworkProgressListener(new NetworkProgressListener() {
            @Override
            public void onRefreshProgress(long current, long total, boolean done) {
                Log.i(LOG_TAG + "asyncCancelUpload", "upload " + current + "/" + total + "，  cast" +
                        " time " +
                        "" + (System.currentTimeMillis() - startTime));
            }
        });

        final Integer LOCK = 1;

        communication.Request(map, new NetworkCallback() {
            @Override
            public void onFinish(boolean result, Object response) {
                assertFalse(result);
                assertNull(response);
                Log.i(LOG_TAG + "asyncCancelUpload", "upload end cast time " + (System
                        .currentTimeMillis() - startTime));

                synchronized (LOCK) {
                    LOCK.notifyAll();
                }
            }
        });

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                communication.cancel();
            }
        }, 30);

        synchronized (LOCK) {
            LOCK.wait();
        }
    }
}
