package org.mobile.library.network.communication;
/**
 * Created by 超悟空 on 2015/5/11.
 */

import android.util.Log;

import org.mobile.library.network.util.NetworkCallback;
import org.mobile.library.network.util.NetworkProgressListener;
import org.mobile.library.network.util.NetworkRefreshProgressHandler;
import org.mobile.library.network.util.ProgressRequestBody;
import org.mobile.library.network.util.RequestBodyBuilder;

import java.io.IOException;
import java.util.Map;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * 基于OkHttp实现的文件上传请求通讯组件类，
 * 默认文件上传类，不可扩展，
 * 媒体类型multipart/form-data，
 * 同时传输的文本默认UTF-8字符编码提交，不支持其他编码
 *
 * @author 超悟空
 * @version 1.0 2016/7/16
 * @since 1.0
 */
public class OkHttpUploadCommunication extends Communication<Map<String, Object>, String>
        implements NetworkRefreshProgressHandler {
    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "OkHttpUploadCommunication.";

    /**
     * 上传进度监听器
     */
    private NetworkProgressListener progressListener = null;

    @Override
    protected Request onCreateRequest(Map<String, Object> sendData) {
        // 拼接参数
        RequestBody body = RequestBodyBuilder.onBuildUploadForm(sendData);

        // 考虑是否包装上传进度
        if (progressListener != null) {
            body = new ProgressRequestBody(body, progressListener);
        }

        return new Request.Builder().url(url).post(body).build();
    }

    @Override
    protected void onAsyncSuccess(ResponseBody body, NetworkCallback<String> callback) throws
            IOException {
        String responseString = body.string();
        Log.i(LOG_TAG + "Request", "response is " + responseString);
        callback.onFinish(true, responseString);
    }

    @Override
    public String Response() {
        try {
            return response == null ? null : response.string();
        } catch (IOException e) {
            Log.e(LOG_TAG + "Response", "error", e);

            return null;
        }
    }

    @Override
    public void setNetworkProgressListener(NetworkProgressListener networkProgressListener) {
        this.progressListener = networkProgressListener;
    }
}
