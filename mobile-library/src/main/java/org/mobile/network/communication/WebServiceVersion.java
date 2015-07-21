package org.mobile.network.communication;
/**
 * Created by 超悟空 on 2015/1/6.
 */

import org.ksoap2.SoapEnvelope;

/**
 * WebService服务器版本号枚举
 *
 * @author 超悟空
 * @version 1.0 2015/1/6
 * @since 1.0
 */
public enum WebServiceVersion {
    /**
     * 版本10
     */
    VERSION10(SoapEnvelope.VER10),
    /**
     * 版本11
     */
    VERSION11(SoapEnvelope.VER11),
    /**
     * 版本12
     */
    VERSION12(SoapEnvelope.VER12);

    /**
     * 实际版本号
     */
    private int versionCode;

    /**
     * 构造方法
     *
     * @param value 版本号
     */
    private WebServiceVersion(int value) {
        this.versionCode = value;
    }

    /**
     * 获取版本号代码
     *
     * @return 实际版本号
     */
    public int getValue() {
        return versionCode;
    }
}
