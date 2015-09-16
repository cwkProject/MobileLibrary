package org.mobile.library.network.communication;
/**
 * Created by 超悟空 on 2015/9/15.
 */

import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 基于HttpURLConnection的Get请求通讯组件
 *
 * @author 超悟空
 * @version 1.0 2015/9/15
 * @since 1.0
 */
public class HttpURLConnectionGetCommunication extends BaseHttpURLConnectionCommunication {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "HttpURLConnectionGetCommunication.";

    @Override
    protected HttpURLConnection onCreateHttpURLConnection(String param) throws IOException {
        Log.i(LOG_TAG + "onCreateHttpURLConnection", "create httpURLConnection get");

        // 完整的请求地址
        String fullUrl = url;

        if (param != null && param.length() > 0) {
            // 拼接参数
            fullUrl += "?" + param;
        }

        Log.i(LOG_TAG + "onCreateHttpURLConnection", "full url is " + fullUrl);

        // 新建URL对象
        URL urlObject = new URL(fullUrl);

        // 创建的连接对象
        HttpURLConnection httpURLConnection = (HttpURLConnection) urlObject.openConnection();

        httpURLConnection.setRequestMethod("GET");

        return httpURLConnection;
    }
}
