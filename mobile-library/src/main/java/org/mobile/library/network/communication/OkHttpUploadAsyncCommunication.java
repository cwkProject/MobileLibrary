package org.mobile.library.network.communication;
/**
 * Created by 超悟空 on 2015/11/4.
 */

import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.mobile.library.global.GlobalApplication;
import org.mobile.library.network.util.AsyncCommunication;
import org.mobile.library.network.util.NetworkCallback;
import org.mobile.library.network.util.NetworkProgressListener;
import org.mobile.library.network.util.NetworkRefreshProgressHandler;
import org.mobile.library.network.util.ProgressRequestBody;
import org.mobile.library.struct.FileInfo;
import org.mobile.library.util.MIMEUtil;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 基于OkHttp实现的异步文件上传请求通讯组件类，
 * 默认文件上传类，不可扩展，
 * 数据上传使用multipart/form-data表单，
 * 默认UTF-8字符编码提交，不支持其他编码
 *
 * @author 超悟空
 * @version 1.0 2015/11/4
 * @since 1.0
 */
public class OkHttpUploadAsyncCommunication implements AsyncCommunication<Map<String, Object>,
        String>, NetworkRefreshProgressHandler {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "OkHttpUploadAsyncCommunication.";

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
     * 上传进度监听器
     */
    private NetworkProgressListener progressListener = null;

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
        RequestBody body = onBuildForm(sendData);

        // 得到okHttpClient对象
        OkHttpClient okHttpClient = GlobalApplication.getGlobal().getOkHttpClient();

        // 创建请求
        Request request = new Request.Builder().tag(tag).url(url).post(body).build();

        // 判断是否需要克隆
        if (timeout > -1) {
            okHttpClient = okHttpClient.clone();
            okHttpClient.setConnectTimeout(timeout, TimeUnit.MILLISECONDS);
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
                Log.i(LOG_TAG + "Request", "onResponse response message is " + response.message());
                if (callback != null) {

                    if (response.isSuccessful()) {
                        Log.i(LOG_TAG + "Request", "request is success");
                        String responseString = response.body().string();
                        Log.i(LOG_TAG + "Request", "response is " + responseString);
                        callback.onFinish(true, responseString);
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
    private RequestBody onBuildForm(Map<String, Object> sendData) {
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);

        // 遍历sendData集合并加入请求参数对象
        if (sendData != null && !sendData.isEmpty()) {
            Log.i(LOG_TAG + "onBuildForm", "sendData count is " + sendData.size());
            // 遍历并追加参数
            for (Map.Entry<String, Object> dataEntry : sendData.entrySet()) {
                Log.i(LOG_TAG + "onBuildForm", "pair is " + dataEntry.getKey() + "=" + dataEntry
                        .getValue());

                if (dataEntry.getValue() instanceof FileInfo) {
                    // 参数是文件包装类型
                    FileInfo value = (FileInfo) dataEntry.getValue();
                    // 加入表单
                    builder.addFormDataPart(dataEntry.getKey(), value.getFileName(), RequestBody
                            .create(MediaType.parse(value.getMimeType()), value.getFile()));
                    continue;
                }

                if (dataEntry.getValue() instanceof File) {
                    // 参数是文件类型
                    File value = (File) dataEntry.getValue();
                    // 加入表单
                    builder.addFormDataPart(dataEntry.getKey(), value.getName(), RequestBody
                            .create(MediaType.parse(MIMEUtil.getMimeType(value)), value));
                    continue;
                }

                // 处理剩余情况，包括String，Integer，Boolean等类型
                String value = "";

                if (dataEntry.getValue() != null) {
                    // 参数不为空
                    value += dataEntry.getValue();
                }

                // 加入表单
                builder.addFormDataPart(dataEntry.getKey(), value);
            }
        }

        // 考虑是否包装上传进度
        if (progressListener != null) {
            return new ProgressRequestBody(builder.build(), progressListener);
        } else {
            return builder.build();
        }
    }

    @Override
    public void cancel() {
        GlobalApplication.getGlobal().getOkHttpClient().cancel(tag);
    }

    @Override
    public void setNetworkProgressListener(NetworkProgressListener networkProgressListener) {
        this.progressListener = networkProgressListener;
    }
}
