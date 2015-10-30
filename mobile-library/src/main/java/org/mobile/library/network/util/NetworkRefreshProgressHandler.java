package org.mobile.library.network.util;
/**
 * Created by 超悟空 on 2015/10/30.
 */

/**
 * 可设置网络请求执行进度监听器的工具接口
 *
 * @author 超悟空
 * @version 1.0 2015/10/30
 * @since 1.0
 */
public interface NetworkRefreshProgressHandler {

    /**
     * 设置网络请求进度监听器
     *
     * @param networkProgressListener 进度监听器实例
     */
    void setNetworkProgressListener(NetworkProgressListener networkProgressListener);
}
