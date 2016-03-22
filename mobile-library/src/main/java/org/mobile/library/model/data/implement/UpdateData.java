package org.mobile.library.model.data.implement;
/**
 * Created by 超悟空 on 2015/4/23.
 */

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.mobile.library.model.data.base.JsonDataModel;
import org.mobile.library.global.GlobalApplication;

import java.util.Map;

/**
 * 用于检查更新的数据类
 *
 * @author 超悟空
 * @version 1.0 2015/4/23
 * @since 1.0
 */
public class UpdateData extends JsonDataModel {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "UpdateData.";

    /**
     * 最新版本号名称
     */
    private String versionName = null;

    /**
     * 最新内部版本号
     */
    private int versionBuild = 0;

    /**
     * 设备类型
     */
    private String deviceType = null;

    /**
     * 应用代码
     */
    private String appCode = null;

    /**
     * 新版本地址
     */
    private String url = null;

    /**
     * 获取最新版本号
     *
     * @return 版本号字符串
     */
    public String getVersionName() {
        return versionName;
    }

    /**
     * 获取最新版本地址
     *
     * @return 下载地址
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置当前设备类型
     *
     * @param deviceType 类型字符串
     */
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    /**
     * 设置应用代码，用于升级请求识别具体应用程序
     *
     * @param appCode 代码字符串
     */
    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    @Override
    protected void onFillRequestParameters(Map<String, String> dataMap) {
        // 加入设备类型
        dataMap.put("DeviceType", deviceType);

        try {
            // 包信息
            PackageInfo info = GlobalApplication.getGlobal().getPackageManager().getPackageInfo
                    (GlobalApplication.getGlobal().getPackageName(), 0);
            versionName = info.versionName;
            versionBuild = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(LOG_TAG + "onFillRequestParameters", "PackageManager.NameNotFoundException is "
                    + e.getMessage());
        }

        // 加入当前版本号
        dataMap.put("Build", String.valueOf(versionBuild));
        dataMap.put("AppName", appCode);
    }

    @Override
    protected boolean onRequestResult(JSONObject jsonResult) throws JSONException {
        String resultState = jsonResult.getString("Update");

        return resultState != null && "yes".equals(resultState.trim().toLowerCase());
    }

    @Override
    protected String onRequestMessage(boolean result, JSONObject jsonResult) throws JSONException {
        return null;
    }

    @Override
    protected void onRequestSuccess(JSONObject jsonResult) throws JSONException {
        // 获取最新版本名
        versionName = jsonResult.getString("Version");
        // 获取最新版本地址
        url = jsonResult.getString("Url");

        Log.i(LOG_TAG + "serialization", "version name is " + versionName);
        Log.i(LOG_TAG + "parse", "latest url is " + url);
    }
}
