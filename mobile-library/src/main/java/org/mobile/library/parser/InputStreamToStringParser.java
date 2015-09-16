package org.mobile.library.parser;
/**
 * Created by 超悟空 on 2015/9/15.
 */

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * 将http请求响应的InputStream转换为字符串的解析器
 *
 * @author 超悟空
 * @version 1.0 2015/9/15
 * @since 1.0
 */
public class InputStreamToStringParser implements IResponseDataParser<String, InputStream> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "InputStreamToStringParser.";

    /**
     * 结果数据编码，默认使用UTF-8
     */
    private String encoded = "UTF-8";

    /**
     * 设置编码格式
     *
     * @param encoded 编码字符串，默认为UTF-8
     */
    public void setEncoded(String encoded) {
        this.encoded = encoded;
    }

    /**
     * 解析服务器响应的数据，解析完成后流会被关闭
     *
     * @param data 响应的输入流
     *
     * @return 解析后数据
     */
    @Override
    public String DataParser(InputStream data) {
        if (data == null) {
            // 数据为空
            Log.d(LOG_TAG + "DataParser", "data is null");
            return null;
        }

        Log.d(LOG_TAG + "DataParser", "data is " + data.toString());

        // 转为字符串
        Log.i(LOG_TAG + "DataParser", "encoded is " + encoded);

        // 数据解析器
        Scanner scanner = null;

        try {

            scanner = new Scanner(data, encoded);

            // 得到字符串
            String text = scanner.next();

            Log.i(LOG_TAG + "DataParser", "text is " + text);

            return text;
        } catch (Exception e) {
            Log.i(LOG_TAG + "DataParser", "Exception is " + e.getMessage());
            return null;
        } finally {
            if (scanner != null) {
                // 关闭解析器
                scanner.close();
            } else {
                try {
                    data.close();
                } catch (IOException e) {
                    Log.i(LOG_TAG + "DataParser", "IOException is " + e.getMessage());
                }
            }
        }

    }
}
