package org.mobile.library.network.communication;
/**
 * Created by 超悟空 on 2016/3/7.
 */

import android.util.Log;

import org.mobile.library.network.util.AsyncCommunication;
import org.mobile.library.network.util.NetworkCallback;
import org.mobile.library.network.util.NetworkClient;
import org.mobile.library.network.util.NetworkTimeout;
import org.mobile.library.network.util.NetworkTimeoutHandler;
import org.mobile.library.network.util.SyncCommunication;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 基于OkHttp的网络请求基类
 *
 * @author 超悟空
 * @version 1.0 2016/3/7
 * @since 1.0
 */
public abstract class OKHttpCommunication<RequestType, ResponseType> implements
        AsyncCommunication<RequestType, ResponseType>, SyncCommunication<RequestType,
        ResponseType>, NetworkTimeoutHandler {
    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "OKHttpCommunication.";

    /**
     * 超时时间配置
     */
    protected NetworkTimeout networkTimeout = null;

    /**
     * 请求地址的完整路径
     */
    protected String url = null;

    /**
     * 请求数据编码，默认使用UTF-8
     */
    protected String encoded = null;

    /**
     * 一个请求对象
     */
    protected Call call = null;

    /**
     * 要返回的响应体
     */
    protected ResponseBody response = null;

    /**
     * 标识请求是否成功
     */
    protected boolean success = false;

    @Override
    public void setNetworkTimeout(NetworkTimeout networkTimeout) {
        this.networkTimeout = networkTimeout;
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
    public void Request(RequestType sendData) {
        Log.i(LOG_TAG + "Request", "Request start");
        Log.i(LOG_TAG + "Request", "url is " + url);

        if (url == null || (!url.trim().toLowerCase().startsWith("http://") && !url.trim()
                .toLowerCase().startsWith("https://"))) {
            // 地址不合法
            Log.d(LOG_TAG + "Request", "url is error");

            this.success = false;
            response = null;
            return;
        }

        // 进阶配置请求工具
        OkHttpClient okHttpClient = onConfigOkHttpClient();

        // 创建请求
        Request request = onCreateRequest(sendData);

        try {
            // 发起同步请求
            call = okHttpClient.newCall(request);
            Response response = call.execute();

            Log.i(LOG_TAG + "Request", "response code is " + response.code());
            Log.i(LOG_TAG + "Request", "response message is " + response.message());

            if (response.isSuccessful()) {
                Log.i(LOG_TAG + "Request", "request is success");
                this.success = true;
                this.response = response.body();
                Log.i(LOG_TAG + "Request", "response is " + this.response);
            } else {
                Log.i(LOG_TAG + "Request", "request is failed");
                this.success = false;
                this.response = null;
            }

        } catch (IOException e) {
            Log.e(LOG_TAG + "Request", "call error", e);

            this.success = false;
            response = null;
        }
    }

    /**
     * 进阶配置请求工具
     *
     * @return 请求工具
     */
    private OkHttpClient onConfigOkHttpClient() {
        // 得到okHttpClient对象
        OkHttpClient okHttpClient = NetworkClient.getOkHttpClient();

        OkHttpClient.Builder builder = onRebuildClient(okHttpClient);

        // 判断是否需要克隆
        if (networkTimeout != null) {

            if (builder == null) {
                builder = okHttpClient.newBuilder();
            }

            if (networkTimeout.getConnectTimeout() > -1) {
                builder.connectTimeout(networkTimeout.getConnectTimeout(), TimeUnit.MILLISECONDS);
            }

            if (networkTimeout.getReadTimeout() > -1) {
                builder.readTimeout(networkTimeout.getReadTimeout(), TimeUnit.MILLISECONDS);
            }

            if (networkTimeout.getWriteTimeout() > -1) {
                builder.writeTimeout(networkTimeout.getWriteTimeout(), TimeUnit.MILLISECONDS);
            }
        }

        // 尝试构建新配置的请求工具
        if (builder != null) {
            okHttpClient = builder.build();
        }
        return okHttpClient;
    }

    /**
     * 重新配置一个请求工具
     *
     * @param okHttpClient 原请求工具
     *
     * @return 新构建器，由父类完成构建
     */
    protected OkHttpClient.Builder onRebuildClient(OkHttpClient okHttpClient) {
        return null;
    }

    /**
     * 创建请求内容
     *
     * @param sendData 要发送的数据
     *
     * @return 请求对象
     */
    protected abstract Request onCreateRequest(RequestType sendData);

    @Override
    public boolean isSuccessful() {
        return success;
    }

    @Override
    public void close() {
        if (response == null) {
            return;
        }

        response.close();
    }

    @Override
    public void Request(RequestType sendData, final NetworkCallback<ResponseType> callback) {
        Log.i(LOG_TAG + "Request", "Request start");
        Log.i(LOG_TAG + "Request", "url is " + url);

        if (url == null || (!url.trim().toLowerCase().startsWith("http://") && !url.trim()
                .toLowerCase().startsWith("https://"))) {
            // 地址不合法
            Log.d(LOG_TAG + "Request", "url is error");

            if (callback != null) {
                callback.onFinish(false, null);
            }

            return;
        }

        // 进阶配置请求工具
        OkHttpClient okHttpClient = onConfigOkHttpClient();

        // 创建请求
        Request request = onCreateRequest(sendData);

        // 发送异步请求
        call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(LOG_TAG + "Request", "call error", e);

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

                        // 处理结果
                        onAsyncSuccess(response.body(), callback);

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
     * 异步请求成功后的执行，用于结果处理
     *
     * @param body     响应体
     * @param callback 回调
     */
    protected abstract void onAsyncSuccess(ResponseBody body, NetworkCallback<ResponseType>
            callback) throws IOException;

    @Override
    public void cancel() {
        if (call != null) {
            call.cancel();
        }
    }
}
