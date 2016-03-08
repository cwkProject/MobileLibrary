package org.mobile.library.network.util;
/**
 * Created by 超悟空 on 2015/11/30.
 */

/**
 * 可设置网络请求执行超时时间的工具接口
 *
 * @author 超悟空
 * @version 2.0 2016/3/7
 * @since 1.0
 */
public interface NetworkTimeoutHandler {

    /**
     * 设置读取超时时间
     *
     * @param readTimeout 超时时间，单位毫秒
     */
    void setReadTimeout(int readTimeout);

    /**
     * 设置写入超时时间
     *
     * @param readTimeout 超时时间，单位毫秒
     */
    void setWriteTimeout(int readTimeout);

    /**
     * 设置超时时间
     *
     * @param timeout 超时时间，单位毫秒
     */
    void setTimeout(int timeout);
}
