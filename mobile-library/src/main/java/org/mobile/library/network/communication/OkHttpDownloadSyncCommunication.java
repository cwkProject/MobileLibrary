package org.mobile.library.network.communication;
/**
 * Created by 超悟空 on 2015/11/30.
 */

import android.support.annotation.NonNull;
import android.util.Log;

import org.mobile.library.global.GlobalApplication;
import org.mobile.library.network.util.NetworkProgressListener;
import org.mobile.library.network.util.NetworkRefreshProgressHandler;
import org.mobile.library.network.util.NetworkTimeoutHandler;
import org.mobile.library.network.util.ProgressResponseBody;
import org.mobile.library.network.util.SyncCommunication;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 基于OkHttp实现的同步文件下载请求通讯组件类，
 * 默认文件下载类，不可扩展，
 * 使用get请求访问下载地址
 *
 * @author 超悟空
 * @version 2.0 2016/3/7
 * @since 1.0
 */
public class OkHttpDownloadSyncCommunication implements SyncCommunication<Map<String, String>,
        InputStream>, NetworkRefreshProgressHandler, NetworkTimeoutHandler {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "OkHttpDownloadSyncCommunication.";

    /**
     * 请求地址的完整路径
     */
    protected String url = null;

    /**
     * 请求超时时间
     */
    protected int timeout = -1;

    /**
     * 请求数据编码，默认使用UTF-8
     */
    protected String encoded = "UTF-8";

    /**
     * 要返回的响应体
     */
    private ResponseBody response = null;

    /**
     * 标识请求是否成功
     */
    private boolean success = false;

    /**
     * 上传进度监听器
     */
    private NetworkProgressListener progressListener = null;

    /**
     * 读取超时时间
     */
    protected int readTimeout = 0;

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
     * 设置请求地址
     *
     * @param url 完整地址
     */
    @Override
    public void setTaskName(String url) {
        this.url = url;
        Log.i(LOG_TAG + "setTaskName", "url is " + this.url);
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

    @Override
    public void setNetworkProgressListener(NetworkProgressListener networkProgressListener) {
        this.progressListener = networkProgressListener;
    }

    @Override
    public void Request(Map<String, String> sendData) {
        Log.i(LOG_TAG + "Request", "Request start");
        Log.i(LOG_TAG + "Request", "url is " + url);

        if (url == null || !url.trim().toLowerCase().startsWith("http://")) {
            // 地址不合法
            Log.d(LOG_TAG + "Request", "url is error");

            this.success = false;
            response = null;
            return;
        }

        // 拼接参数
        String params = onBuildParameter(sendData);

        // 最终请求地址
        String finalUrl = params.length() == 0 ? url : url + "?" + params;
        Log.i(LOG_TAG + "Request", "final url is " + finalUrl);

        // 得到okHttpClient对象
        OkHttpClient okHttpClient = GlobalApplication.getOkHttpClient();

        // 创建请求
        Request request = new Request.Builder().url(finalUrl).build();

        OkHttpClient.Builder builder = okHttpClient.newBuilder();

        // 判断是否需要设置超时时间
        if (timeout > -1) {
            builder.connectTimeout(timeout, TimeUnit.MILLISECONDS);
        }

        if (readTimeout > -1) {
            builder.readTimeout(readTimeout, TimeUnit.MILLISECONDS);
        }

        if (writeTimeout > -1) {
            builder.writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);
        }

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

        okHttpClient = builder.build();

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
            } else {
                Log.i(LOG_TAG + "Request", "request is failed");
                this.success = false;
                this.response = null;
            }

        } catch (IOException e) {
            Log.e(LOG_TAG + "Request", "IOException type is " + e.toString());
            Log.e(LOG_TAG + "Request", "IOException message is " + e.getMessage());

            this.success = false;
            response = null;
        }
    }

    /**
     * 拼接参数字符串
     *
     * @param sendData 请求参数对
     *
     * @return 拼接完成的字符串
     */
    @NonNull
    private String onBuildParameter(Map<String, String> sendData) {
        // 请求参数装配器参数
        StringBuilder params = new StringBuilder();

        try {

            // 遍历sendData集合并加入请求参数对象
            if (sendData != null && !sendData.isEmpty()) {
                Log.i(LOG_TAG + "onBuildParameter", "sendData count is " + sendData.size());

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
            Log.e(LOG_TAG + "onBuildParameter", "response error IOException class is " + e
                    .toString());

            Log.e(LOG_TAG + "onBuildParameter", "response error IOException message is " + e
                    .getMessage());
        }
        return params.toString();
    }

    @Override
    public boolean isSuccessful() {
        return success;
    }

    @Override
    public InputStream Response() {
        return response == null ? null : response.byteStream();
    }

    @Override
    public void close() {
        if (response == null) {
            return;
        }

        response.close();
    }

    @Override
    public void cancel() {
        if (call != null) {
            call.cancel();
        }
    }
}
