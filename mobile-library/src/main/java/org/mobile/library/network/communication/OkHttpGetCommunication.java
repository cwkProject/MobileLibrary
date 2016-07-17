package org.mobile.library.network.communication;
/**
 * Created by 超悟空 on 2015/5/11.
 */

import android.util.Log;

import org.mobile.library.network.util.NetworkCallback;
import org.mobile.library.network.util.RequestBodyBuilder;

import java.io.IOException;
import java.util.Map;

import okhttp3.Request;
import okhttp3.ResponseBody;

/**
 * 基于OkHttp实现的Get请求通讯组件类
 *
 * @author 超悟空
 * @version 2.0 2016/3/7
 * @since 1.0
 */
public class OkHttpGetCommunication extends Communication<Map<String, String>, String> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "OkHttpGetCommunication.";

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
        Log.v(LOG_TAG + "Request", "final url is " + finalUrl);

        return new Request.Builder().url(finalUrl).build();
    }

    @Override
    protected void onAsyncSuccess(ResponseBody body, NetworkCallback<String> callback) throws
            IOException {
        String responseString = body.string();
        Log.v(LOG_TAG + "Request", "response is " + responseString);
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
}
