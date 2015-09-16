package org.mobile.library.network.communication;
/**
 * Created by 超悟空 on 2015/1/28.
 */

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.protocol.HTTP;

import java.util.Map;

/**
 * 基于HttpClient的请求通讯组件基类
 *
 * @author 超悟空
 * @version 1.0 2015/1/28
 * @since 1.0
 */
@Deprecated
public abstract class BaseHttpClientCommunication implements ICommunication<Map<String, String>, HttpEntity> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "BaseHttpClientCommunication.";

    /**
     * 请求地址根路径，必须包含"http://"
     */
    protected String urlRoot = null;

    /**
     * 请求地址的完整路径
     */
    protected String url = null;

    /**
     * 请求数据编码，默认使用UTF-8
     */
    protected String encoded = HTTP.UTF_8;

    /**
     * 请求结果数据
     */
    protected HttpEntity response = null;

    /**
     * 请求超时时间
     */
    protected int timeout = 0;

    /**
     * 读取超时时间
     */
    protected int readTimeout = 0;

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
     * 设置请求地址根路径
     *
     * @param urlRoot 必须包含"http://"，
     *                不必是完整的地址，
     *                具体请求的地址可以在{@link #setTaskName(String)}中传入相对路径
     */
    public void setUrlRoot(String urlRoot) {
        // 自动补充一个"/"
        this.urlRoot = urlRoot + "/";
        Log.i(LOG_TAG + "setUrlRoot", "final urlRoot is " + this.urlRoot);
    }

    /**
     * 获取当前请求地址根路径
     *
     * @return 网络地址
     */
    public String getUrlRoot() {
        return urlRoot;
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
     * @param uri 可以是完整地址或地址的相对路径，
     *            如果是相对地址，
     *            则必须设置过{@link #setUrlRoot(String)}
     */
    @Override
    public void setTaskName(String uri) {

        Log.d(LOG_TAG + "setTaskName", "uri is " + uri);

        if (uri == null || uri.trim().equals("")) {
            // 如果是空，则使用根路径
            this.url = this.urlRoot;
            Log.i(LOG_TAG + "setTaskName", "url is " + this.url);
            return;
        }

        if (uri.toLowerCase().startsWith("http://")) {
            // 如果是完整地址，则直接使用
            this.url = uri;
        } else {
            // 否则认为uri是相对路径，进行拼接
            this.url = this.urlRoot + uri;
        }

        Log.i(LOG_TAG + "setTaskName", "url is " + this.url);
    }

    @Override
    public abstract void Request(Map<String, String> sendData);

    @Override
    public HttpEntity Response() {
        return response;
    }

    @Override
    public void close() {

    }
}
