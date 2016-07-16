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
     * @param readTimeout 超时时间，单位毫秒
     */
    public void setReadTimeout(int readTimeout) {
        Log.i(LOG_TAG + "setReadTimeout", "readTimeout is " + readTimeout);
        this.readTimeout = readTimeout;
    }

    /**
     * 设置写入超时时间
     *
     * @param writeTimeout 超时时间，单位毫秒
     */
    public void setWriteTimeout(int writeTimeout) {
        Log.i(LOG_TAG + "setWriteTimeout", "writeTimeout is " + writeTimeout);
        this.writeTimeout = writeTimeout;
    }

    /**
     * 设置连接超时时间
     *
     * @param connectTimeout 超时时间，单位毫秒
     */
    public void setConnectTimeout(int connectTimeout) {
        Log.i(LOG_TAG + "setTimeout", "connectTimeout is " + connectTimeout);
        this.connectTimeout = connectTimeout;
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
