package org.mobile.library.network.factory;
/**
 * Created by 超悟空 on 2015/4/23.
 */

import org.mobile.library.R;
import org.mobile.library.network.communication.HttpClientGetCommunication;
import org.mobile.library.network.communication.HttpURLConnectionGetCommunication;
import org.mobile.library.network.communication.HttpURLConnectionPostCommunication;
import org.mobile.library.network.communication.ICommunication;
import org.mobile.library.util.ContextUtil;


/**
 * 通讯对象工厂
 *
 * @author 超悟空
 * @version 1.0 2015/4/23
 * @since 1.0
 */
public class CommunicationFactory {

    /**
     * HttpGet请求对象
     */
    private static HttpClientGetCommunication httpClientGetCommunication = null;

    /**
     * Http Get请求对象
     */
    private static HttpURLConnectionGetCommunication httpURLConnectionGetCommunication = null;

    /**
     * Http Post请求对象
     */
    private static HttpURLConnectionPostCommunication httpURLConnectionPostCommunication = null;

    /**
     * 创建通讯工具对象
     *
     * @param networkType 网络工具类型
     *
     * @return 初始化完成的通讯工具
     */
    public static ICommunication Create(NetworkType networkType) {

        switch (networkType) {
            case HTTP_CONNECTION_GET:
                // HttpGet请求对象
                if (httpURLConnectionGetCommunication == null) {
                    httpURLConnectionGetCommunication = initHttpURLConnectionGetCommunication();
                }
                return httpURLConnectionGetCommunication;
            case HTTP_CONNECTION_POST:
                // HttpPost请求对象
                if (httpURLConnectionPostCommunication == null) {
                    httpURLConnectionPostCommunication = initHttpURLConnectionPostCommunication();
                }
                return httpURLConnectionPostCommunication;
            case HTTP_GET:
                // HttpGet请求对象
                if (httpClientGetCommunication == null) {
                    httpClientGetCommunication = initHttpClientGetCommunication();
                }
                return httpClientGetCommunication;
            default:
                throw new UnsupportedOperationException("network protocol not implemented");
        }
    }

    /**
     * 初始化HttpGet请求对象
     *
     * @return 初始化完成的HttpGet对象
     */
    private static HttpClientGetCommunication initHttpClientGetCommunication() {

        // 新建HttpGet请求对象
        HttpClientGetCommunication httpClient = new HttpClientGetCommunication();

        // 设置超时时间
        httpClient.setTimeout(ContextUtil.getContext().getResources().getInteger(R.integer
                .http_get_timeout));

        return httpClient;
    }

    /**
     * 初始化Http Post请求对象
     *
     * @return 初始化完成的Http Post对象
     */
    private static HttpURLConnectionPostCommunication initHttpURLConnectionPostCommunication() {
        // 新建HttpPost请求对象
        HttpURLConnectionPostCommunication httpURLConnectionPostCommunication = new
                HttpURLConnectionPostCommunication();

        // 设置超时时间
        httpURLConnectionPostCommunication.setTimeout(ContextUtil.getContext().getResources()
                .getInteger(R.integer.http_post_default_timeout));
        httpURLConnectionPostCommunication.setReadTimeout(ContextUtil.getContext().getResources()
                .getInteger(R.integer.http_post_default_read_timeout));

        return httpURLConnectionPostCommunication;
    }

    /**
     * 初始化Http Get请求对象
     *
     * @return 初始化完成的Http Get对象
     */
    private static HttpURLConnectionGetCommunication initHttpURLConnectionGetCommunication() {
        // 新建HttpGet请求对象
        HttpURLConnectionGetCommunication httpURLConnectionGetCommunication = new
                HttpURLConnectionGetCommunication();

        // 设置超时时间
        httpURLConnectionGetCommunication.setTimeout(ContextUtil.getContext().getResources()
                .getInteger(R.integer.http_get_default_timeout));
        httpURLConnectionGetCommunication.setReadTimeout(ContextUtil.getContext().getResources()
                .getInteger(R.integer.http_get_default_read_timeout));

        return httpURLConnectionGetCommunication;
    }
}
