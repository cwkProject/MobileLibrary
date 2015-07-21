package org.mobile.model.data.implement;
/**
 * Created by 超悟空 on 2015/4/23.
 */

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.mobile.model.data.IDataModel;
import org.mobile.parser.HttpResponseHttpEntityToStringParser;
import org.mobile.util.ContextUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于检查更新的数据类
 *
 * @author 超悟空
 * @version 1.0 2015/4/23
 * @since 1.0
 */
public class UpdateData implements IDataModel<Object, Map<String, String>> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "UpdateData.";

    /**
     * 标识是否为最新版本
     */
    private boolean latest = true;

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
     * 判断当前是否最新版
     *
     * @return true为最新版
     */
    public boolean isLatest() {
        return latest;
    }

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
    public Map<String, String> serialization() {
        Log.i(LOG_TAG + "serialization", "serialization start");

        // 序列化后的参数集
        Map<String, String> dataMap = new HashMap<>();

        // 加入设备类型
        dataMap.put("DeviceType", deviceType);
        Log.i(LOG_TAG + "serialization", "deviceType is " + deviceType);

        try {
            // 包信息
            PackageInfo info = ContextUtil.getContext().getPackageManager().getPackageInfo(ContextUtil.getContext().getPackageName(), 0);
            versionName = info.versionName;
            versionBuild = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(LOG_TAG + "serialization", "PackageManager.NameNotFoundException is " + e.getMessage());
        }

        // 加入当前版本号
        dataMap.put("Build", String.valueOf(versionBuild));
        Log.i(LOG_TAG + "serialization", "version code is " + versionBuild);
        Log.i(LOG_TAG + "serialization", "version name is " + versionName);

        dataMap.put("AppName", appCode);
        Log.i(LOG_TAG + "serialization", "application code is " + appCode);

        return dataMap;
    }

    @Override
    public boolean parse(Object data) {
        Log.i(LOG_TAG + "parse", "parse start");

        if (data == null) {
            // 通信异常
            Log.d(LOG_TAG + "parse", "data is null");
            return false;
        }

        // 新建解析器
        HttpResponseHttpEntityToStringParser parser = new HttpResponseHttpEntityToStringParser();

        // 获取结果字符串
        String resultString = parser.DataParser(data);
        Log.i(LOG_TAG + "parse", "result string is " + resultString);

        try {
            // 将结果转换为JSON对象
            JSONObject jsonObject = new JSONObject(resultString);

            String resultState = jsonObject.getString("Update");

            if (resultState != null && "yes".equals(resultState.trim().toLowerCase())) {
                // 需要更新
                // 标记为不是最新版
                latest = false;
                // 获取最新版本名
                versionName = jsonObject.getString("Version");
                // 获取最新版本地址
                url = jsonObject.getString("Url");
            }

            return true;
        } catch (JSONException e) {
            Log.e(LOG_TAG + "parse", "JSONException is " + e.getMessage());
            return false;
        } finally {
            Log.i(LOG_TAG + "parse", "version state is " + latest);
            Log.i(LOG_TAG + "serialization", "version name is " + versionName);
            Log.i(LOG_TAG + "parse", "latest url is " + url);
        }
    }
}
