package org.mobile.library.util;
/**
 * Created by 超悟空 on 2016/3/19.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;

import org.mobile.library.global.ApplicationStaticValue;
import org.mobile.library.global.GlobalApplication;

import java.util.UUID;

/**
 * 设备ID工具
 *
 * @author 超悟空
 * @version 1.0 2016/3/19
 * @since 1.0
 */
public class DeviceID {

    /**
     * 设备ID键名
     */
    private static final String DEVICE_ID = "device_id";

    /**
     * 设备id
     */
    private static String deviceId = null;

    /**
     * 获取设备唯一标识
     *
     * @return 设备唯一标识
     */
    public static synchronized String getDeviceId() {

        if (deviceId != null) {
            return deviceId;
        }

        // 先从配置文件读取
        readConfig();

        if (deviceId != null) {
            writeConfig();
            return deviceId;
        }

        // 再通过IMEI生成
        fromIMEI();

        if (deviceId != null) {
            writeConfig();
            return deviceId;
        }

        // 再通过uuid生成
        fromUUID();
        writeConfig();

        return deviceId;
    }

    /**
     * 从配置文件读取
     */
    private static void readConfig() {
        SharedPreferences sharedPreferences = GlobalApplication.getGlobal().getSharedPreferences
                (ApplicationStaticValue.AppConfig.APPLICATION_CONFIG_FILE_NAME, Context
                        .MODE_PRIVATE);

        deviceId = sharedPreferences.getString(DEVICE_ID, null);
    }

    /**
     * 通过IMEI生成
     */
    private static void fromIMEI() {
        TelephonyManager tm = (TelephonyManager) GlobalApplication.getGlobal().getSystemService
                (Context.TELEPHONY_SERVICE);
        deviceId = tm.getDeviceId();
    }

    /**
     * 通过uuid生成
     */
    public static void fromUUID() {
        deviceId = UUID.randomUUID().toString();
    }

    /**
     * 写入配置文件
     */
    private static void writeConfig() {
        SharedPreferences.Editor editor = GlobalApplication.getGlobal().getSharedPreferences
                (ApplicationStaticValue.AppConfig.APPLICATION_CONFIG_FILE_NAME, Context
                        .MODE_PRIVATE).edit();
        editor.putString(DEVICE_ID, deviceId);
        editor.apply();
    }
}
