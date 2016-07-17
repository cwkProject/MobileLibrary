package org.mobile.library.common.function;
/**
 * Created by 超悟空 on 2015/7/3.
 */

import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * 解析下载地址中的文件名
 *
 * @author 超悟空
 * @version 1.0 2015/7/3
 * @since 1.0
 */
public class ParserUrlFileName {


    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "ParserUrlFileName.";

    public static String getReallyFileName(String url) {
        String filename = null;
        URL myURL;
        HttpURLConnection conn = null;
        if (url == null || url.length() < 1) {
            return null;
        }

        try {
            myURL = new URL(url);
            conn = (HttpURLConnection) myURL.openConnection();
            conn.connect();
            conn.getResponseCode();
            // 获得真实Url
            URL absUrl = conn.getURL();
            Log.v(LOG_TAG + "getReallyFileName", "H3C x: " + absUrl);
            // 打印输出服务器Header信息
            Map<String, List<String>> map = conn.getHeaderFields();
            for (String str : map.keySet()) {
                if (str != null) {
                    Log.v(LOG_TAG + "getReallyFileName", "H3C " + str + " : " + map.get(str));
                }
            }

            // 通过Content-Disposition获取文件名，这点跟服务器有关，需要灵活变通
            filename = conn.getHeaderField("Content-Disposition");
            if (filename == null || filename.length() == 0) {
                filename = absUrl.getFile();
            }
        } catch (IOException e) {
            Log.e(LOG_TAG + "getReallyFileName", e.getMessage());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return filename;
    }
}
