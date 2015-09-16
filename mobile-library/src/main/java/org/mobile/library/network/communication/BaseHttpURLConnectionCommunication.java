package org.mobile.library.network.communication;
/**
 * Created by 超悟空 on 2015/7/4.
 */

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 基于HttpURLConnection实现的请求通讯组件基类
 *
 * @author 超悟空
 * @version 1.0 2015/7/4
 * @since 1.0
 */
public abstract class BaseHttpURLConnectionCommunication implements ICommunication<Map<String,
        String>, InputStream> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "BaseHttpURLConnectionCommunication.";

    /**
     * 请求地址的完整路径
     */
    protected String url = null;

    /**
     * 请求数据编码，默认使用UTF-8
     */
    protected String encoded = "UTF-8";

    /**
     * 请求结果数据流
     */
    protected InputStream response = null;

    /**
     * 请求超时时间
     */
    protected int timeout = 0;

    /**
     * 读取超时时间
     */
    protected int readTimeout = 0;

    /**
     * 网络连接
     */
    private HttpURLConnection httpURLConnection = null;

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
     * 设置超时时间
     *
     * @param timeout 超时时间，单位毫秒
     */
    public void setTimeout(int timeout) {
        Log.i(LOG_TAG + "setTimeout", "timeout is " + timeout);
        this.timeout = timeout;
    }

    /**
     * 设置编码格式
     *
     * @param encoded 编码字符串，默认为UTF-8
     */
    public void setEncoded(String encoded) {
        Log.i(LOG_TAG + "setEncoded", "encoded is " + encoded);
        this.encoded = encoded;
    }

    /**
     * 设置请求地址
     *
     * @param uri 完整地址
     */
    @Override
    public void setTaskName(String uri) {
        this.url = uri;
        Log.i(LOG_TAG + "setTaskName", "url is " + this.url);
    }

    @Override
    public void Request(Map<String, String> sendData) {
        Log.i(LOG_TAG + "Request", "Request(Map<String, String>) start");
        Log.i(LOG_TAG + "Request", "url is " + url);

        if (url == null || !url.trim().startsWith("http://")) {
            // 地址不合法
            response = null;
            Log.d(LOG_TAG + "Request", "url is error");
            return;
        }

        try {

            // 请求参数装配器参数
            StringBuilder params = new StringBuilder();

            // 遍历sendData集合并加入请求参数对象
            if (sendData != null && !sendData.isEmpty()) {
                Log.i(LOG_TAG + "Request", "sendData count is " + sendData.size());

                // 遍历并追加参数
                for (Map.Entry<String, String> dataEntry : sendData.entrySet()) {
                    params.append(dataEntry.getKey());
                    params.append('=');
                    if (dataEntry.getValue() != null && dataEntry.getValue().length() > 0) {
                        params.append(URLEncoder.encode(dataEntry.getValue(), encoded));
                    } else {
                        params.append(dataEntry.getValue());
                    }
                    params.append('&');
                }
                // 移除末尾的'&'
                params.deleteCharAt(params.length() - 1);
            }

            Log.i(LOG_TAG + "Request", "params is " + params);

            httpURLConnection = onCreateHttpURLConnection(params.toString());

            // 设置超时时间
            httpURLConnection.setConnectTimeout(timeout);
            httpURLConnection.setReadTimeout(readTimeout);

            // 建立连接
            httpURLConnection.connect();

            // 得到响应结果
            response = httpURLConnection.getInputStream();

            // 判断请求是否正常
            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.d(LOG_TAG + "Request", "response failed code is " + httpURLConnection
                        .getResponseCode());
                response = null;
            } else {
                Log.i(LOG_TAG + "Request", "response success");
            }
        } catch (IOException e) {
            Log.e(LOG_TAG + "Request", "response error IOException is " + e.getMessage());
            response = null;
        }
    }

    /**
     * 创建{@link java.net.HttpURLConnection}连接对象，
     * 为{@link #Request(Map)}生成的连接请求对象
     *
     * @param param 请求参数
     *
     * @return 创建好的连接参数
     */
    protected abstract HttpURLConnection onCreateHttpURLConnection(String param) throws IOException;

    @Override
    public InputStream Response() {
        return response;
    }

    @Override
    public void close() {
        // 关闭连接
        if (httpURLConnection != null) {
            httpURLConnection.disconnect();
        }
    }
}
