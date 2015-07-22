package org.mobile.library.network.communication;
/**
 * Created by 超悟空 on 2015/1/28.
 */

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 基于HttpClient的Post请求通讯组件
 *
 * @author 超悟空
 * @version 1.0 2015/1/28
 * @since 1.0
 */
@Deprecated
public class HttpClientPostCommunication extends BaseHttpClientCommunication {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "HttpClientPostCommunication.";

    @Override
    public void Request(Map<String, String> sendData) {
        Log.i(LOG_TAG + "Request", "Request(Map<String, String>) start");
        Log.i(LOG_TAG + "Request", "url is " + url);

        if (url == null || !url.trim().startsWith("http://")) {
            // 地址不合法
            response = null;
            return;
        }

        // 要提交的参数
        List<BasicNameValuePair> params = new ArrayList<>();

        // 遍历sendData集合并加入请求参数对象
        if (sendData != null) {
            Log.i(LOG_TAG + "Request", "sendData count is " + sendData.size());

            for (Map.Entry<String, String> dataEntry : sendData.entrySet()) {
                params.add(new BasicNameValuePair(dataEntry.getKey(), dataEntry.getValue()));
            }
        }

        // 新建http客户端
        DefaultHttpClient httpClient = new DefaultHttpClient();

        if (timeout > 0) {
            // 设置请求超时时间
            httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
        }

        if (readTimeout > 0) {
            // 设置读取超时时间
            httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, timeout);
        }

        // 新建post请求
        HttpPost httpPost = new HttpPost(url);

        try {
            // 给Post方式提供参数，参数最终由服务器接收
            httpPost.setEntity(new UrlEncodedFormEntity(params, encoded));
            Log.i(LOG_TAG + "Request", "HttpResponse start");
            // 新建响应对象
            HttpResponse httpResponse = httpClient.execute(httpPost);

            // 取出数据
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                Log.i(LOG_TAG + "Request", "HttpResponse success");
                response = httpResponse.getEntity();
            } else {
                Log.d(LOG_TAG + "Request", "HttpResponse failed status code " + httpResponse.getStatusLine().getStatusCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG + "Request", "IOException is " + e.getMessage());
        }
    }
}
