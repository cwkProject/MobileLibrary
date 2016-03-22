package org.mobile.library.global;
/**
 * Created by 超悟空 on 2015/6/11.
 */

import org.mobile.library.model.config.TemporaryConfigModel;

/**
 * 应用相关参数
 *
 * @author 超悟空
 * @version 3.0 2016/3/19
 * @since 1.0
 */
public class ApplicationAttribute extends TemporaryConfigModel {

    /**
     * 设备类型，默认{@link org.mobile.library.global.ApplicationStaticValue.AppConfig#DEVICE_TYPE}
     */
    private String deviceType = ApplicationStaticValue.AppConfig.DEVICE_TYPE;

    /**
     * 应用标识
     */
    private String appCode = null;

    /**
     * 应用令牌
     */
    private String appToken = null;

    /**
     * 标识是否在全局范围内对应用的网络请求进行签名
     */
    private boolean requestSign = false;

    /**
     * 设置设备类型
     *
     * @param deviceType 设备类型码，默认{@link org.mobile.library.global.ApplicationStaticValue
     * .AppConfig#DEVICE_TYPE}
     */
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    /**
     * 设置应用标识
     *
     * @param appCode 应用标识码
     */
    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    /**
     * 获取设备类型
     *
     * @return 设备类型码，默认{@link org.mobile.library.global.ApplicationStaticValue
     * .AppConfig#DEVICE_TYPE}
     */
    public String getDeviceType() {
        return deviceType;
    }

    /**
     * 获取应用标识
     *
     * @return 应用标识码
     */
    public String getAppCode() {
        return appCode;
    }

    /**
     * 获取应用令牌
     *
     * @return 应用令牌
     */
    public String getAppToken() {
        return appToken;
    }

    /**
     * 设置应用令牌
     *
     * @param appToken 应用令牌
     */
    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }

    /**
     * 是否对网络请求进行签名
     *
     * @return true表示需要签名
     */
    public boolean isRequestSign() {
        return requestSign;
    }

    /**
     * 设置应用全局范围内是否对网络请求进行签名
     *
     * @param requestSign true表示需要签名，默认为false
     */
    public void setRequestSign(boolean requestSign) {
        this.requestSign = requestSign;
    }

    /**
     * 构造函数
     */
    public ApplicationAttribute() {
        super();
    }

    @Override
    protected void onCreate() {
        setDeviceType(ApplicationStaticValue.AppConfig.DEVICE_TYPE);
        setAppCode(null);
        setAppToken(null);
    }
}
