package org.mobile.common.function;
/**
 * Created by 超悟空 on 2015/4/23.
 */

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import org.mobile.common.dialog.VersionUpdateDialog;
import org.mobile.library.R;
import org.mobile.model.work.WorkBack;
import org.mobile.model.work.implement.CheckVersion;
import org.mobile.util.ApplicationVersion;
import org.mobile.util.StaticValueUtil;

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
     * 设备类型
     */
    private String deviceType = null;

    /**
     * 应用代码
     */
    private String applicationCode = null;

    /**
     * 版本状态请求地址
     */
    private String updateRequestUrl = null;

    /**
     * 构造函数
     *
     * @param context         上下文
     * @param applicationCode 当前应用代码
     */
    public CheckUpdate(Context context, String applicationCode) {
        this(context, applicationCode, StaticValueUtil.UPDATE_REQUEST_URL);
    }

    /**
     * 构造函数
     *
     * @param context          上下文
     * @param applicationCode  当前应用代码
     * @param updateRequestUrl 版本状态请求地址
     */
    public CheckUpdate(Context context, String applicationCode, String updateRequestUrl) {
        this(context, applicationCode, updateRequestUrl, StaticValueUtil.DEVICE_TYPE);
    }

    /**
     * 构造函数
     *
     * @param context          上下文
     * @param applicationCode  当前应用代码
     * @param updateRequestUrl 版本状态请求地址
     * @param deviceType       设备类型码
     */
    public CheckUpdate(Context context, String applicationCode, String updateRequestUrl, String deviceType) {
        this.context = context;
        this.applicationCode = applicationCode;
        this.updateRequestUrl = updateRequestUrl;
        this.deviceType = deviceType;
    }

    /**
     * 设置当前设备类型
     *
     * @param deviceType 类型字符串，默认使用{@link org.mobile.util.StaticValueUtil#DEVICE_TYPE}
     */
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    /**
     * 设置应用代码，用于升级请求识别具体应用程序
     *
     * @param applicationCode 代码字符串
     */
    public void setApplicationCode(String applicationCode) {
        this.applicationCode = applicationCode;
    }

    /**
     * 获取当前使用的设备类型
     *
     * @return 设备类型码
     */
    public String getDeviceType() {
        return deviceType;
    }

    /**
     * 获取当前应用代码
     *
     * @return 应用代码串
     */
    public String getApplicationCode() {
        return applicationCode;
    }

    /**
     * 获取当前使用的版本状态请求地址
     *
     * @return 完整的地址串
     */
    public String getUpdateRequestUrl() {
        return updateRequestUrl;
    }

    /**
     * 设置版本状态请求地址
     *
     * @param updateRequestUrl 完整的地址串，默认使用{@link org.mobile.util.StaticValueUtil#UPDATE_REQUEST_URL}
     */
    public void setUpdateRequestUrl(String updateRequestUrl) {
        this.updateRequestUrl = updateRequestUrl;
    }

    /**
     * 执行检查更新，并且显示旋转进度和结果提示
     */
    public void checkWithSpinner() {
        Log.i(LOG_TAG + "Spinner", "check update is invoked");

        // 新建检查更新任务
        CheckVersion checkVersion = new CheckVersion();

        // 新建旋转进度
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // 设置提醒
        progressDialog.setMessage(context.getString(R.string.update_now_version_loading));
        progressDialog.setCancelable(true);

        // 设置任务结束回调
        checkVersion.setWorkBackListener(new WorkBack<String>() {
            @Override
            public void doEndWork(boolean state, String data) {

                // 关闭进度条
                progressDialog.cancel();

                if (state) {
                    // 执行成功

                    if (data == null) {
                        // 当前为最新版本
                        showLatest();
                    } else {
                        // 当前不是最新版本
                        showUpdate();
                    }
                } else {
                    // 执行失败
                    showError();
                }
            }
        });

        // 显示旋转进度
        progressDialog.show();

        // 执行任务
        checkVersion.beginExecute(deviceType, applicationCode, updateRequestUrl);
    }

    /**
     * 静默执行检查更新，结束后无提示
     */
    public void checkNoPrompt() {
        Log.i(LOG_TAG + "NoPrompt", "check update is invoked");

        // 新建检查更新任务
        CheckVersion checkVersion = new CheckVersion();

        // 执行任务
        checkVersion.beginExecute(deviceType, applicationCode, updateRequestUrl);
    }

    /**
     * 静默执行检查更新，存在新版本时提示更新
     */
    public void checkInBackground() {
        Log.i(LOG_TAG + "Background", "check update is invoked");

        // 新建检查更新任务
        CheckVersion checkVersion = new CheckVersion();

        // 设置任务结束回调
        checkVersion.setWorkBackListener(new WorkBack<String>() {
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
        checkVersion.beginExecute(deviceType, applicationCode, updateRequestUrl);
    }

    /**
     * 显示失败提示
     */
    private void showError() {
        // 提示框
        Dialog dialog = new Dialog(context);

        // 设置标题
        dialog.setTitle(R.string.update_now_version_error);

        // 显示提示框
        dialog.setCancelable(true);
        dialog.show();
    }

    /**
     * 当前不是最新版本的操作
     */
    private void showUpdate() {
        VersionUpdateDialog.showUpdate(context, ApplicationVersion.getVersionManager().getLatestVersionName(), ApplicationVersion.getVersionManager().getLatestVersionUrl());
    }

    /**
     * 当前为最新版本的操作
     */
    private void showLatest() {
        VersionUpdateDialog.showLatest(context);
    }
}
