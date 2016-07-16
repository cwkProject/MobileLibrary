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
     * 设置网络请求超时配置
     *
     * @param networkTimeout 超时配置
     */
    void setNetworkTimeout(NetworkTimeout networkTimeout);
}
