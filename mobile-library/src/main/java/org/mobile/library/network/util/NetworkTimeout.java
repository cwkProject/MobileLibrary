package org.mobile.library.network.util;
/**
 * Created by 超悟空 on 2016/3/7.
 */

import android.util.Log;

/**
 * 网络超时类
 *
 * @author 超悟空
 * @version 1.0 2016/3/7
 * @since 1.0
 */
public class NetworkTimeout {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "NetworkTimeout.";

    /**
     * 请求超时时间
     */
    private int connectTimeout = -1;

    /**
     * 读取超时时间
     */
    private int readTimeout = -1;

    /**
     * 写入超时时间
     */
    private int writeTimeout = -1;

    /**
     * 设置读取超时时间
     *
     * @param timeout 超时时间，单位毫秒
     */
    public void setReadTimeout(int timeout) {
        Log.v(LOG_TAG + "setReadTimeout", "timeout is " + timeout);
        this.readTimeout = timeout;
    }

    /**
     * 设置写入超时时间
     *
     * @param timeout 超时时间，单位毫秒
     */
    public void setWriteTimeout(int timeout) {
        Log.v(LOG_TAG + "setWriteTimeout", "timeout is " + timeout);
        this.writeTimeout = timeout;
    }

    /**
     * 设置连接超时时间
     *
     * @param timeout 超时时间，单位毫秒
     */
    public void setConnectTimeout(int timeout) {
        Log.v(LOG_TAG + "setConnectTimeout", "timeout is " + timeout);
        this.connectTimeout = timeout;
    }

    /**
     * 获取连接超时时间
     *
     * @return 超时时间，单位毫秒
     */
    public int getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * 获取读取超时时间
     *
     * @return 超时时间，单位毫秒
     */
    public int getReadTimeout() {
        return readTimeout;
    }

    /**
     * 获取写入超时时间
     *
     * @return 超时时间，单位毫秒
     */
    public int getWriteTimeout() {
        return writeTimeout;
    }
}
