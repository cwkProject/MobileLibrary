package org.mobile.library.network.communication;
/**
 * Created by 超悟空 on 2015/10/30.
 */

import android.util.Log;

import org.mobile.library.global.GlobalApplication;
import org.mobile.library.network.util.AsyncCommunication;
import org.mobile.library.network.util.NetworkCallback;
import org.mobile.library.network.util.NetworkTimeoutHandler;
import org.mobile.library.network.util.RequestBodyBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 基于OkHttp实现的异步Get请求通讯组件类
 *
 * @author 超悟空
 * @version 2.0 2016/3/7
 * @since 1.0
 */
public class OkHttpGetAsyncCommunication implements AsyncCommunication<Map<String, String>,
        String>, NetworkTimeoutHandler {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "OkHttpGetAsyncCommunication.";

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
     * 写入超时时间
     */
    protected int writeTimeout = -1;

    /**
     * 一个请求对象
     */
    private Call call = null;

    @Override
    public void setReadTimeout(int readTimeout) {
        Log.i(LOG_TAG + "setReadTimeout", "readTimeout is " + readTimeout);
        this.readTimeout = readTimeout;
    }

    @Override
    public void setWriteTimeout(int writeTimeout) {
        Log.i(LOG_TAG + "setWriteTimeout", "writeTimeout is " + writeTimeout);
        this.writeTimeout = writeTimeout;
    }

    @Override
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

        // 拼接参数
        String params = RequestBodyBuilder.onBuildParameter(sendData, encoded);

        // 最终请求地址
        String finalUrl = params.length() == 0 ? url : url + "?" + params;
        Log.i(LOG_TAG + "Request", "final url is " + finalUrl);

        // 得到okHttpClient对象
        OkHttpClient okHttpClient = GlobalApplication.getOkHttpClient();

        // 创建请求
        Request request = new Request.Builder().url(finalUrl).build();

        // 判断是否需要克隆
        if (timeout + readTimeout + writeTimeout > -3) {
            OkHttpClient.Builder builder = okHttpClient.newBuilder();

            if (timeout > -1) {
                builder.connectTimeout(timeout, TimeUnit.MILLISECONDS);
            }

            if (readTimeout > -1) {
                builder.readTimeout(readTimeout, TimeUnit.MILLISECONDS);
            }

            if (writeTimeout > -1) {
                builder.writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);
            }

            okHttpClient = builder.build();
        }

        // 发送请求
        call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(LOG_TAG + "Request", "onFailure IOException type is " + e.toString());
                Log.e(LOG_TAG + "Request", "onFailure IOException message is " + e.getMessage());

                if (callback != null) {
                    callback.onFinish(false, null);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(LOG_TAG + "Request", "onResponse response code is " + response.code());
                Log.i(LOG_TAG + "Request", "onResponse response message is " + response.message());
                if (callback != null) {

                    if (response.isSuccessful()) {
                        Log.i(LOG_TAG + "Request", "request is success");
                        String responseString = response.body().string();
                        Log.i(LOG_TAG + "Request", "response is " + responseString);
                        callback.onFinish(true, responseString);
                        // 关闭流
                        response.body().close();
                    } else {
                        Log.i(LOG_TAG + "Request", "request is failed");
                        callback.onFinish(false, null);
                    }
                }
            }
        });

    }

    @Override
    public void cancel() {
        if (call != null) {
            call.cancel();
        }
    }
}
