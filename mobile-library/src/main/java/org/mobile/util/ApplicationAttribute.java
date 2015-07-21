package org.mobile.util;
/**
 * Created by 超悟空 on 2015/6/11.
 */

import org.mobile.model.config.TemporaryConfigModel;

/**
 * 应用相关参数
 *
 * @author 超悟空
 * @version 1.0 2015/6/11
 * @since 1.0
 */
public class ApplicationAttribute extends TemporaryConfigModel {
    /**
     * 自身的静态对象
     */
    private static ApplicationAttribute applicationAttribute = new ApplicationAttribute();

    /**
     * 设备UUID
     */
    private String deviceToken = null;

    /**
     * 设备类型，默认{@link org.mobile.util.StaticValueUtil#DEVICE_TYPE}
     */
    private String deviceType = StaticValueUtil.DEVICE_TYPE;

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
     * @param deviceType 设备类型码，默认{@link org.mobile.util.StaticValueUtil#DEVICE_TYPE}
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
     * @return 设备类型码，默认{@link org.mobile.util.StaticValueUtil#DEVICE_TYPE}
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
     * 私有构造函数
     */
    private ApplicationAttribute() {
        super();
    }

    @Override
    protected void onCreate() {
        setDeviceType(StaticValueUtil.DEVICE_TYPE);
        setDeviceToken(null);
        setAppName(null);
    }

    /**
     * 获取全局临时数据对象
     *
     * @return 数据对象
     */
    public static ApplicationAttribute getApplicationAttribute() {
        return applicationAttribute;
    }
}
