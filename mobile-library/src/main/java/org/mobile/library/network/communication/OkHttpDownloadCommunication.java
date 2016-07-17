package org.mobile.library.network.communication;
/**
 * Created by 超悟空 on 2015/5/11.
 */

import android.util.Log;

import org.mobile.library.network.util.NetworkCallback;
import org.mobile.library.network.util.NetworkProgressListener;
import org.mobile.library.network.util.NetworkRefreshProgressHandler;
import org.mobile.library.network.util.ProgressResponseBody;
import org.mobile.library.network.util.RequestBodyBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 基于OkHttp实现的文件下载请求通讯组件类，
 * 默认文件下载类，不可扩展，
 * 使用get请求访问下载地址
 *
 * @author 超悟空
 * @version 2.0 2016/3/7
 * @since 1.0
 */
public class OkHttpDownloadCommunication extends Communication<Map<String, String>,
        InputStream> implements NetworkRefreshProgressHandler {
    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "OkHttpDownloadCommunication.";

    /**
     * 下载进度监听器
     */
    private NetworkProgressListener progressListener = null;

    @Override
    public void setNetworkProgressListener(NetworkProgressListener networkProgressListener) {
        this.progressListener = networkProgressListener;
    }

    @Override
    protected OkHttpClient.Builder onRebuildClient(OkHttpClient okHttpClient) {
        OkHttpClient.Builder builder = okHttpClient.newBuilder();

        if (progressListener != null) {
            // 增加拦截器监听下载进度
            builder.networkInterceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder().body(new ProgressResponseBody
                            (originalResponse.body(), progressListener)).build();
                }
            });
        }

        return builder;
    }

    @Override
    protected Request onCreateRequest(Map<String, String> sendData) {
        // 拼接参数
        String params;
        if (encoded != null) {
            params = RequestBodyBuilder.onBuildParameter(sendData, encoded);
        } else {
            params = RequestBodyBuilder.onBuildParameter(sendData);
        }

        // 最终请求地址
        String finalUrl = params.length() == 0 ? url : url + "?" + params;
        Log.i(LOG_TAG + "Request", "final url is " + finalUrl);

        return new Request.Builder().url(finalUrl).build();
    }

    @Override
    protected void onAsyncSuccess(ResponseBody body, NetworkCallback<InputStream> callback)
            throws IOException {
        callback.onFinish(true, body.byteStream());
    }

    @Override
    public InputStream Response() {
        return response == null ? null : response.byteStream();
    }
}
