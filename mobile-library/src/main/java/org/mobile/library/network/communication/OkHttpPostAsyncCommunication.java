package org.mobile.library.network.communication;
/**
 * Created by 超悟空 on 2015/11/2.
 */

import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.mobile.library.global.GlobalApplication;
import org.mobile.library.network.util.AsyncCommunication;
import org.mobile.library.network.util.NetworkCallback;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 基于OkHttp实现的异步Post请求通讯组件类，
 * 默认表单提交类，不可扩展，
 * 数据提交使用application/x-www-form-urlencoded表单，
 * 默认UTF-8字符编码提交，不支持其他编码
 *
 * @author 超悟空
 * @version 1.0 2015/11/2
 * @since 1.0
 */
public class OkHttpPostAsyncCommunication implements AsyncCommunication<Map<String, String>,
        String> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "OkHttpPostAsyncCommunication.";

    /**
     * 当前网络请求标签
     */
    private String tag = UUID.randomUUID().toString();

    /**
     * 请求地址的完整路径
     */
    protected String url = null;

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
        RequestBody body = onBuildForm(sendData);

        // 得到okHttpClient对象
        OkHttpClient okHttpClient = GlobalApplication.getGlobal().getOkHttpClient();

        // 创建请求
        Request request = new Request.Builder().tag(tag).url(url).post(body).build();

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
                Log.e(LOG_TAG + "Request", "onFailure IOException type is " + e.toString());
                Log.e(LOG_TAG + "Request", "onFailure IOException message is " + e.getMessage());

                if (callback != null) {
                    callback.onFinish(false, null);
                }
            }

            @Override
            public void onResponse(Response response) throws IOException {
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

    /**
     * 创建提交表单
     *
     * @param sendData 要发送的参数对
     *
     * @return 装配好的表单
     */
    private RequestBody onBuildForm(Map<String, String> sendData) {
        FormEncodingBuilder builder = new FormEncodingBuilder();

        // 遍历sendData集合并加入请求参数对象
        if (sendData != null && !sendData.isEmpty()) {
            Log.i(LOG_TAG + "onBuildForm", "sendData count is " + sendData.size());

            // 遍历并追加参数
            for (Map.Entry<String, String> dataEntry : sendData.entrySet()) {
                Log.i(LOG_TAG + "onBuildForm", "pair is " + dataEntry.getKey() + "=" + dataEntry
                        .getValue());
                // 加入表单
                builder.add(dataEntry.getKey(), dataEntry.getValue() == null ? "" : dataEntry
                        .getValue());
            }
        }

        return builder.build();
    }

    @Override
    public void cancel() {
        GlobalApplication.getGlobal().getOkHttpClient().cancel(tag);
    }
}
