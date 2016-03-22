package org.mobile.library.network.communication;
/**
 * Created by 超悟空 on 2015/11/4.
 */

import android.util.Log;

import org.mobile.library.global.GlobalApplication;
import org.mobile.library.network.util.AsyncCommunication;
import org.mobile.library.network.util.NetworkCallback;
import org.mobile.library.network.util.NetworkProgressListener;
import org.mobile.library.network.util.NetworkRefreshProgressHandler;
import org.mobile.library.network.util.NetworkTimeoutHandler;
import org.mobile.library.network.util.ProgressRequestBody;
import org.mobile.library.network.util.RequestBodyBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 基于OkHttp实现的异步文件上传请求通讯组件类，
 * 默认文件上传类，不可扩展，
 * 数据上传使用multipart/form-data表单，
 * 默认UTF-8字符编码提交，不支持其他编码
 *
 * @author 超悟空
 * @version 2.0 2016/3/7
 * @since 1.0
 */
public class OkHttpUploadAsyncCommunication implements AsyncCommunication<Map<String, Object>,
        String>, NetworkRefreshProgressHandler, NetworkTimeoutHandler {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "OkHttpUploadAsyncCommunication.";

    /**
     * 请求地址的完整路径
     */
    protected String url = null;

    /**
     * 请求超时时间
     */
    protected int timeout = -1;

    /**
     * 上传进度监听器
     */
    private NetworkProgressListener progressListener = null;

    /**
     * 读取超时时间
     */
    protected int readTimeout = -1;

    /**
     * 写入超时时间
     */
    protected int writeTimeout = 0;

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
    public void Request(Map<String, Object> sendData, final NetworkCallback<String> callback) {
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
        RequestBody body = RequestBodyBuilder.onBuildUploadForm(sendData);

        // 考虑是否包装上传进度
        if (progressListener != null) {
            body = new ProgressRequestBody(body, progressListener);
        }

        // 得到okHttpClient对象
        OkHttpClient okHttpClient = GlobalApplication.getOkHttpClient();

        // 创建请求
        Request request = new Request.Builder().url(url).post(body).build();

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

    @Override
    public void setNetworkProgressListener(NetworkProgressListener networkProgressListener) {
        this.progressListener = networkProgressListener;
    }
}
