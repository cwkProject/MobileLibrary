package org.mobile.library.network.communication;
/**
 * Created by 超悟空 on 2015/10/30.
 */

import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.mobile.library.global.GlobalApplication;
import org.mobile.library.network.util.AsyncCommunication;
import org.mobile.library.network.util.NetworkCallback;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 基于OkHttp实现的异步请求通讯组件类
 *
 * @author 超悟空
 * @version 1.0 2015/10/30
 * @since 1.0
 */
public class OkHttpGetAsyncCommunication implements AsyncCommunication<Map<String, String>,
        String> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "OkHttpGetAsyncCommunication.";

    /**
     * 当前网络请求标签
     */
    private String tag = UUID.randomUUID().toString();

    /**
     * 请求地址的完整路径
     */
    protected String url = null;

    /**
     * 请求数据编码，默认使用UTF-8
     */
    protected String encoded = "UTF-8";

    /**
     * 请求超时时间
     */
    protected int timeout = -1;

    /**
     * 读取超时时间
     */
    protected int readTimeout = -1;

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
     * @param url 完整地址
     */
    @Override
    public void setTaskName(String url) {
        this.url = url;
        Log.i(LOG_TAG + "setTaskName", "url is " + this.url);
    }

    @Override
    public void Request(Map<String, String> sendData, final NetworkCallback<String> callback) {
        Log.i(LOG_TAG + "Request", "Request start");
        Log.i(LOG_TAG + "Request", "url is " + url);

        if (url == null || !url.trim().toLowerCase().startsWith("http://")) {
            // 地址不合法
            Log.d(LOG_TAG + "Request", "url is error");

            if (callback != null) {
                callback.onFinish(false, null);
            }

            return;
        }

        // 请求参数装配器参数
        StringBuilder params = new StringBuilder();

        try {

            // 遍历sendData集合并加入请求参数对象
            if (sendData != null && !sendData.isEmpty()) {
                Log.i(LOG_TAG + "Request", "sendData count is " + sendData.size());

                // 遍历并追加参数
                for (Map.Entry<String, String> dataEntry : sendData.entrySet()) {
                    params.append(dataEntry.getKey());
                    params.append('=');
                    if (dataEntry.getValue() != null && dataEntry.getValue().length() > 0) {
                        params.append(URLEncoder.encode(dataEntry.getValue(), encoded));
                    }
                    params.append('&');
                }
                // 移除末尾的'&'
                params.deleteCharAt(params.length() - 1);
            }
        } catch (UnsupportedEncodingException e) {
            Log.e(LOG_TAG + "Request", "response error IOException class is " + e.toString());
            Log.e(LOG_TAG + "Request", "response error IOException message is " + e.getMessage());
        }

        // 最终请求地址
        String finalUrl = url + "?" + params.toString();
        Log.i(LOG_TAG + "Request", "final url is " + finalUrl);

        // 得到okHttpClient对象
        OkHttpClient okHttpClient = GlobalApplication.getGlobal().getOkHttpClient();

        // 创建请求
        final Request request = new Request.Builder().tag(tag).url(finalUrl).build();

        // 判断是否需要克隆
        if (timeout + readTimeout > -2) {
            okHttpClient = okHttpClient.clone();

            if (timeout > -1) {
                okHttpClient.setConnectTimeout(timeout, TimeUnit.MILLISECONDS);
            }

            if (readTimeout > -1) {
                okHttpClient.setReadTimeout(readTimeout, TimeUnit.MILLISECONDS);
            }
        }

        // 发送请求
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e(LOG_TAG + "Request", "onFailure IOException is " + e.getMessage());

                if (callback != null) {
                    callback.onFinish(false, null);
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.e(LOG_TAG + "Request", "onResponse response message is " + response.message());
                if (callback != null) {

                    if (response.isSuccessful()) {
                        callback.onFinish(true, response.body().string());
                    } else {
                        callback.onFinish(false, null);
                    }
                }
            }
        });

    }

    @Override
    public void cancel() {

    }
}
