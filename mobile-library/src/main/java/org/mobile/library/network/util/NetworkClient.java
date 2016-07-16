package org.mobile.library.network.util;
/**
 * Created by 超悟空 on 2015/5/11.
 */

import org.mobile.library.global.GlobalApplication;

import okhttp3.OkHttpClient;

/**
 * 全局网络访问工具
 *
 * @author 超悟空
 * @version 2.0 2016/3/7
 * @since 1.0
 */
public class NetworkClient {

    /**
     * 全局网络请求工具
     */
    private static OkHttpClient okHttpClient = null;

    /**
     * 获取网络工具
     *
     * @return 带默认设置的OkHttpClient对象
     */
    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = GlobalApplication.getOkHttpClient();
        }

        return okHttpClient;
    }

    /**
     * 设置网络工具
     *
     * @param okHttpClient 带默认设置的OkHttpClient对象
     */
    public static void setOkHttpClient(OkHttpClient okHttpClient) {
        NetworkClient.okHttpClient = okHttpClient;
    }
}
