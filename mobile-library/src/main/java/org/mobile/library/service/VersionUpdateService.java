package org.mobile.library.service;
/**
 * Created by 超悟空 on 2015/10/27.
 */

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.mobile.library.global.ApplicationStaticValue;
import org.mobile.library.global.GlobalApplication;
import org.mobile.library.model.operate.EmptyParameterObserver;
import org.mobile.library.receiver.ApplicationVersionDownloadReceiver;

/**
 * 软件升级服务
 *
 * @author 超悟空
 * @version 1.0 2015/10/27
 * @since 1.0
 */
public class VersionUpdateService extends Service {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "VersionUpdateService.";

    /**
     * 本地广播管理器
     */
    private LocalBroadcastManager localBroadcastManager = null;

    /**
     * 版本升级的下载文件接收者
     */
    private ApplicationVersionDownloadReceiver receiver = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 注册广播接收者
        registerReceivers();
        // 启动下载
        onStartDownload();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 注销广播接收者
        unregisterReceivers();
    }

    /**
     * 启动下载
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void onStartDownload() {
        // 下载地址
        String downloadUrl = GlobalApplication.getGlobal().getApplicationVersion()
                .getLatestVersionUrl();

        if (downloadUrl == null) {
            Log.d(LOG_TAG + "onStartDownload", "downloadUrl is null");
            // 停止服务
            stopSelf();
            return;
        }

        // 下载管理器
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context
                .DOWNLOAD_SERVICE);
        // 包装
        Uri uri = Uri.parse(downloadUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager
                .Request.NETWORK_WIFI);
        request.setVisibleInDownloadsUi(false);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // 优先使用SD卡
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                    downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1));
        }
        // 下载并保存目标文件id
        long id = downloadManager.enqueue(request);
        getSharedPreferences(ApplicationStaticValue.APPLICATION_CONFIG_FILE_NAME, Context
                .MODE_PRIVATE).edit().putLong(ApplicationStaticValue.UPDATE_APP_FILE_ID_TAG, id)
                .commit();
    }

    /**
     * 注册广播接收者
     */
    private void registerReceivers() {
        Log.i(LOG_TAG + "registerReceivers", "registerReceivers() is invoked");
        // 新建本地广播管理器
        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        // 新建接收者
        receiver = new ApplicationVersionDownloadReceiver();

        // 设置完成回调
        receiver.setEndObserver(new EmptyParameterObserver() {
            @Override
            public void invoke() {
                // 停止服务
                stopSelf();
            }
        });

        // 注册
        localBroadcastManager.registerReceiver(receiver, receiver.getRegisterIntentFilter());
    }

    /**
     * 注销广播接收者
     */
    private void unregisterReceivers() {
        Log.i(LOG_TAG + "unregisterReceivers", "unregisterReceivers() is invoked");
        if (localBroadcastManager == null) {
            return;
        }

        if (receiver != null) {
            localBroadcastManager.unregisterReceiver(receiver);
        }
    }

}
