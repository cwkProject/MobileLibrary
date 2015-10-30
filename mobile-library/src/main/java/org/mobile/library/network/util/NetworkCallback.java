package org.mobile.library.network.util;
/**
 * Created by 超悟空 on 2015/10/30.
 */

/**
 * 网络请求结果回调接口
 *
 * @author 超悟空
 * @version 1.0 2015/10/30
 * @since 1.0
 */
public interface NetworkCallback<ResponseType> {

    /**
     * 网络请求结束回调
     *
     * @param result   请求结果
     * @param response 响应数据
     */
    void onFinish(boolean result, ResponseType response);
}
