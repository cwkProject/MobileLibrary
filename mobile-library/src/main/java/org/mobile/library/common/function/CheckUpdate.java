package org.mobile.library.common.function;
/**
 * Created by 超悟空 on 2015/4/23.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import org.mobile.library.R;
import org.mobile.library.common.dialog.VersionUpdateDialog;
import org.mobile.library.global.GlobalApplication;
import org.mobile.library.model.work.WorkBack;
import org.mobile.library.model.work.implement.CheckVersion;
import org.mobile.library.global.ApplicationStaticValue;

/**
 * 检查更新功能类
 *
 * @author 超悟空
 * @version 1.0 2015/4/23
 * @since 1.0
 */
public class CheckUpdate {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "CheckUpdate.";

    /**
     * 上下文
     */
    private Context context = null;

    /**
     * 构造函数
     *
     * @param context 上下文
     */
    public CheckUpdate(Context context) {
        this.context = context;
    }

    /**
     * 执行检查更新，并且显示旋转进度和结果提示
     */
    public void checkWithSpinner() {
        Log.v(LOG_TAG + "Spinner", "check update is invoked");

        // 新建检查更新任务
        CheckVersion checkVersion = new CheckVersion();

        // 新建旋转进度
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // 设置提醒
        progressDialog.setMessage(context.getString(R.string.update_now_version_loading));
        progressDialog.setCancelable(true);

        // 设置任务结束回调
        checkVersion.setWorkEndListener(new WorkBack<String>() {
            @Override
            public void doEndWork(boolean state, String data) {

                // 关闭进度条
                progressDialog.cancel();

                if (state) {
                    // 执行成功，当前不是最新版本
                    showUpdate();
                } else {
                    // 当前为最新版本，或执行失败
                    showLatest();
                }
            }
        });

        // 显示旋转进度
        progressDialog.show();

        // 执行任务
        checkVersion.beginExecute(ApplicationStaticValue.AppConfig.DEVICE_TYPE, GlobalApplication
                .getApplicationAttribute().getAppCode(), ApplicationStaticValue.Url
                .UPDATE_REQUEST_URL);
    }

    /**
     * 静默执行检查更新，结束后无提示
     */
    public void checkNoPrompt() {
        Log.v(LOG_TAG + "NoPrompt", "check update is invoked");

        // 新建检查更新任务
        CheckVersion checkVersion = new CheckVersion();

        // 执行任务
        checkVersion.beginExecute(ApplicationStaticValue.AppConfig.DEVICE_TYPE, GlobalApplication
                .getApplicationAttribute().getAppCode(), ApplicationStaticValue.Url
                .UPDATE_REQUEST_URL);
    }

    /**
     * 静默执行检查更新，存在新版本时提示更新
     */
    public void checkInBackground() {
        Log.v(LOG_TAG + "Background", "check update is invoked");

        // 新建检查更新任务
        CheckVersion checkVersion = new CheckVersion();

        // 设置任务结束回调
        checkVersion.setWorkEndListener(new WorkBack<String>() {
            @Override
            public void doEndWork(boolean state, String data) {

                if (state) {
                    // 执行成功
                    if (data != null) {
                        // 当前不是最新版本
                        showUpdate();
                    }
                }
            }
        });

        // 执行任务
        checkVersion.beginExecute(ApplicationStaticValue.AppConfig.DEVICE_TYPE, GlobalApplication
                .getApplicationAttribute().getAppCode(), ApplicationStaticValue.Url
                .UPDATE_REQUEST_URL);
    }

    /**
     * 当前不是最新版本的操作
     */
    private void showUpdate() {
        VersionUpdateDialog.showUpdate(context);
    }

    /**
     * 当前为最新版本的操作
     */
    private void showLatest() {
        VersionUpdateDialog.showLatest(context);
    }
}
