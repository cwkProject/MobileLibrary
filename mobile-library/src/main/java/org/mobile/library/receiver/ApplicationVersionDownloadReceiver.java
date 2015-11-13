package org.mobile.library.receiver;
/**
 * Created by 超悟空 on 2015/7/3.
 */

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import org.mobile.library.model.operate.EmptyParameterObserver;
import org.mobile.library.global.ApplicationStaticValue;

import java.io.File;

/**
 * 版本升级的下载文件接收者
 *
 * @author 超悟空
 * @version 1.0 2015/7/3
 * @since 1.0
 */
public class ApplicationVersionDownloadReceiver extends BroadcastReceiver {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "ApplicationVersionDownloadReceiver.";

    /**
     * 下载完成回调
     */
    private EmptyParameterObserver endObserver = null;

    /**
     * 设置下载完成监听
     *
     * @param endObserver 监听器
     */
    public void setEndObserver(EmptyParameterObserver endObserver) {
        this.endObserver = endObserver;
    }

    /**
     * 得到本接收者监听的动作集合
     *
     * @return 填充完毕的意图集合
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public final IntentFilter getRegisterIntentFilter() {
        // 新建动作集合
        IntentFilter filter = new IntentFilter();
        // 下载结果监听
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        return filter;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {

            // 下载完成
            Log.i(LOG_TAG + "onReceive", "ACTION_DOWNLOAD_COMPLETE");
            long nowId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            Log.i(LOG_TAG + "onReceive", "now download file id is " + nowId);
            long tagId = context.getSharedPreferences(ApplicationStaticValue
                    .APPLICATION_CONFIG_FILE_NAME, Context.MODE_PRIVATE).getLong
                    (ApplicationStaticValue.UPDATE_APP_FILE_ID_TAG, 0);
            Log.i(LOG_TAG + "onReceive", "target file id is " + tagId);

            if (nowId == tagId) {
                // 查询下载状态
                queryDownloadStatus(context, nowId);
            }
        }
    }

    /**
     * 查询目标下载文件状态
     *
     * @param context 上下文
     * @param id      下载的文件ID
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void queryDownloadStatus(Context context, long id) {
        Log.i(LOG_TAG + "queryDownloadStatus", "queryDownloadStatus(Context) is invoked");
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(id);
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context
                .DOWNLOAD_SERVICE);
        Cursor cursor = downloadManager.query(query);

        if (cursor != null && cursor.moveToFirst()) {
            Log.i(LOG_TAG + "down", "cursor row count is " + cursor.getCount());
            Log.i(LOG_TAG + "down", "cursor column count is " + cursor.getColumnCount());
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case DownloadManager.STATUS_PAUSED:
                    Log.i(LOG_TAG + "down", "STATUS_PAUSED");
                case DownloadManager.STATUS_PENDING:
                    Log.i(LOG_TAG + "down", "STATUS_PENDING");
                case DownloadManager.STATUS_RUNNING:
                    //正在下载，不做任何事情
                    Log.i(LOG_TAG + "down", "STATUS_RUNNING");
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    //完成
                    Log.i(LOG_TAG + "down", "STATUS_SUCCESSFUL");
                    String path = cursor.getString(cursor.getColumnIndex(DownloadManager
                            .COLUMN_LOCAL_URI));
                    doInstall(context, path);
                    break;
                case DownloadManager.STATUS_FAILED:
                    //清除已下载的内容，重新下载
                    Log.i(LOG_TAG + "down", "STATUS_FAILED");
                    context.getSharedPreferences(ApplicationStaticValue
                            .APPLICATION_CONFIG_FILE_NAME, Context.MODE_PRIVATE).edit().remove
                            (ApplicationStaticValue.UPDATE_APP_FILE_ID_TAG).commit();
                    break;
            }
            cursor.close();
        }
    }

    /**
     * 执行安装
     *
     * @param context 上下文
     * @param path    文件的本地路径
     */
    private void doInstall(Context context, String path) {
        Log.i(LOG_TAG + "doInstall", "path is " + path);
        Cursor cursor = context.getContentResolver().query(Uri.parse(path), null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            Log.i(LOG_TAG + "doInstall", "cursor row count is " + cursor.getCount());
            Log.i(LOG_TAG + "doInstall", "cursor column count is " + cursor.getColumnCount());

            doInstall(context, cursor.getString(cursor.getColumnIndex("_data")));
            cursor.close();
        } else {
            Uri uri = null;

            if (path != null && path.trim().length() != 0) {
                if (path.startsWith("file:///")) {
                    uri = Uri.parse(path);
                } else {
                    uri = Uri.fromFile(new File(path));
                }
            }

            if (uri != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                context.startActivity(intent);
            }

            if (endObserver != null) {
                endObserver.invoke();
            }
        }
    }
}

