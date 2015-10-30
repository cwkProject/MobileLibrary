package org.mobile.library.network.communication;
/**
 * Created by 超悟空 on 2015/9/15.
 */

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 基于HttpURLConnection的Post请求通讯组件
 *
 * @author 超悟空
 * @version 1.0 2015/9/15
 * @since 1.0
 */
public class HttpURLConnectionPostCommunication extends BaseHttpURLConnectionCommunication {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "HttpURLConnectionPostCommunication.";

    @Override
    protected HttpURLConnection onCreateHttpURLConnection(String parameter) throws IOException {
        Log.i(LOG_TAG + "onCreateHttpURLConnection", "create httpURLConnection post");

        // 新建URL对象
        URL urlObject = new URL(url);

        // 创建的连接对象
        HttpURLConnection httpURLConnection = (HttpURLConnection) urlObject.openConnection();


        httpURLConnection.setRequestMethod("POST");
        // 不使用缓存
        httpURLConnection.setUseCaches(false);
        // 设置可写请求参数
        httpURLConnection.setDoOutput(true);

        return httpURLConnection;
    }

    @Override
    protected void onWriteOutputStream(HttpURLConnection httpURLConnection, String parameter)
            throws IOException {
        if (parameter != null && parameter.length() > 0) {
            // 写入请求参数
            OutputStream out = httpURLConnection.getOutputStream();
            out.write(parameter.getBytes());
            out.flush();
            out.close();
        }
    }
}
