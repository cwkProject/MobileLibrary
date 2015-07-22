package org.mobile.library.common.dialog;
/**
 * Created by 超悟空 on 2015/4/18.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import org.mobile.library.R;
import org.mobile.library.util.StaticValueUtil;

/**
 * 弹出应用版本更新提示的功能类
 *
 * @author 超悟空
 * @version 1.0 2015/4/18
 * @since 1.0
 */
public class VersionUpdateDialog {

    /**
     * 当前不是最新版本的提示
     *
     * @param context     上下文
     * @param version     最新版本号
     * @param downloadUrl 新版本下载地址
     */
    public static void showUpdate(final Context context, String version, final String downloadUrl) {

        // 弹出确认对话框
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        // 设置标题
        dialog.setTitle(R.string.update_now_version_old);

        // 设置提示
        dialog.setMessage(context.getString(R.string.update_now_version_alert) + ":" + version);

        // 设置确认监听器
        dialog.setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 打开应用下载地址
               /* Uri uri = Uri.fromFile(new File(downloadUrl));
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                context.startActivity(intent);*/

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                            Uri uri = Uri.parse(downloadUrl);
                            DownloadManager.Request request = new DownloadManager.Request(uri);
                            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
                            request.setVisibleInDownloadsUi(false);
                            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                                // 优先使用SD卡
                                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1));
                            }
                            // 下载并保存目标文件id
                            long id = downloadManager.enqueue(request);
                            context.getSharedPreferences(StaticValueUtil.APPLICATION_CONFIG_FILE_NAME, Context.MODE_PRIVATE).edit().putLong(StaticValueUtil.UPDATE_APP_FILE_ID_TAG, id).commit();
                        } else {
                            Uri uri = Uri.parse(downloadUrl);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            context.startActivity(intent);
                        }
                    }
                });

                thread.start();

            }
        });

        // 设置取消监听器，无操作
        dialog.setNegativeButton(R.string.button_cancel, null);

        // 显示提示框
        dialog.show();
    }

    /**
     * 当前为最新版本的提示
     *
     * @param context 上下文
     */
    public static void showLatest(Context context) {

        // 提示框
        Dialog dialog = new Dialog(context);

        // 设置标题
        dialog.setTitle(R.string.update_now_version_latest);

        // 显示提示框
        dialog.setCancelable(true);
        dialog.show();
    }
}
