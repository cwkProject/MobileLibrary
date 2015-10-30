package org.mobile.library.global;
/**
 * Created by 超悟空 on 2015/6/11.
 */

import org.mobile.library.model.config.TemporaryConfigModel;

/**
 * 应用相关参数
 *
 * @author 超悟空
 * @version 2.0 2015/10/30
 * @since 1.0
 */
public class ApplicationAttribute extends TemporaryConfigModel {

    /**
     * 设备UUID
     */
    private String deviceToken = null;

    /**
     * 设备类型，默认{@link ApplicationStaticValue#DEVICE_TYPE}
     */
    private String deviceType = ApplicationStaticValue.DEVICE_TYPE;

    /**
     * 应用标识
     */
    private String appName = null;

    /**
     * 设置设备UUID
     *
     * @param deviceToken 唯一标识
     */
    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    /**
     * 设置设备类型
     *
     * @param deviceType 设备类型码，默认{@link ApplicationStaticValue#DEVICE_TYPE}
     */
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    /**
     * 设置应用标识
     *
     * @param appName 应用标识码
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * 获取设备UUID
     *
     * @return 唯一标识
     */
    public String getDeviceToken() {
        return deviceToken;
    }

    /**
     * 获取设备类型
     *
     * @return 设备类型码，默认{@link ApplicationStaticValue#DEVICE_TYPE}
     */
    public String getDeviceType() {
        return deviceType;
    }

    /**
     * 获取应用标识
     *
     * @return 应用标识码
     */
    public String getAppName() {
        return appName;
    }

    /**
     * 构造函数
     */
    public ApplicationAttribute() {
        super();
    }

    @Override
    protected void onCreate() {
        setDeviceType(ApplicationStaticValue.DEVICE_TYPE);
        setDeviceToken(null);
        setAppName(null);
    }
}
