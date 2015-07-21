package org.mobile.network.communication;
/**
 * Created by 超悟空 on 2015/1/28.
 */

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import java.io.IOException;
import java.util.Map;

/**
 * 基于HttpClient的Get请求通讯组件
 *
 * @author 超悟空
 * @version 1.0 2015/1/28
 * @since 1.0
 */
@Deprecated
public class HttpClientGetCommunication extends BaseHttpClientCommunication {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "HttpClientGetCommunication.";

    /**
     * 设置请求地址
     *
     * @param uri 可以是完整地址或地址的相对路径，
     *            不需要加末尾的"?"和参数列表，
     *            参数可以在{@link #Request(java.util.Map)}中传入，
     *            如果是包含参数的完整地址或后缀，
     *            则在调用{@link #Request(java.util.Map)}时传入null或者空{@link java.util.Map}
     *            如果是相对地址，
     *            则必须设置过{@link #setUrlRoot(String)}
     */
    @Override
    public void setTaskName(String uri) {
        super.setTaskName(uri);
    }

    @Override
    public void Request(Map<String, String> sendData) {
        Log.i(LOG_TAG + "Request", "Request(Map<String, String>) start");
        Log.i(LOG_TAG + "Request", "url is " + url);

        if (url == null || !url.trim().startsWith("http://")) {
            // 地址不合法
            response = null;
            return;
        }

        // 最终的get请求地址装配器
        StringBuilder urlGet = new StringBuilder(url);

        // 遍历sendData集合并加入请求参数对象
        if (sendData != null && !sendData.isEmpty()) {
            Log.i(LOG_TAG + "Request", "sendData count is " + sendData.size());

            // 先加入一个"?"
            urlGet.append('?');
            // 遍历并追加参数
            for (Map.Entry<String, String> dataEntry : sendData.entrySet()) {
                urlGet.append(dataEntry.getKey());
                urlGet.append('=');
                urlGet.append(dataEntry.getValue());
                urlGet.append('&');
            }
            // 移除末尾的'&'
            urlGet.deleteCharAt(urlGet.length() - 1);
        }

        Log.i(LOG_TAG + "Request", "final url is " + urlGet);

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

        // 新建get请求
        HttpGet httpGet = new HttpGet(urlGet.toString());

        try {
            Log.i(LOG_TAG + "Request", "HttpResponse start");
            // 新建响应对象
            HttpResponse httpResponse = httpClient.execute(httpGet);

            // 取出数据
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                Log.i(LOG_TAG + "Request", "HttpResponse success");
                response = httpResponse.getEntity();
            } else {
                response = null;
                Log.d(LOG_TAG + "Request", "HttpResponse failed status code " + httpResponse.getStatusLine().getStatusCode());
            }
        } catch (IOException e) {
            response = null;
            Log.e(LOG_TAG + "Request", "IOException is " + e.getMessage());
        }
    }
}
