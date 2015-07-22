package org.mobile.library.parser;
/**
 * Created by 超悟空 on 2015/1/28.
 */

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * http请求结果的解析工具，由HttpEntity解析为单一字符串
 *
 * @author 超悟空
 * @version 1.0 2015/1/28
 * @since 1.0
 */
@Deprecated
public class HttpResponseHttpEntityToStringParser implements IResponseDataParser<String, Object> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "EntityToString.";

    /**
     * 结果数据编码，默认使用UTF-8
     */
    private String encoded = HTTP.UTF_8;

    /**
     * 设置编码格式
     *
     * @param encoded 编码字符串，默认为UTF-8
     */
    public void setEncoded(String encoded) {
        this.encoded = encoded;
    }

    @Override
    public String DataParser(Object data) {

        if (data == null || !(data instanceof HttpEntity)) {
            // 不是HttpEntity类型
            Log.d(LOG_TAG + "Parser", "data isn't HttpEntity");
            return null;
        }
        try {
            // 转为字符串
            Log.i(LOG_TAG + "Parser", "encoded is " + encoded);
            return EntityUtils.toString((HttpEntity) data, encoded);
        } catch (IOException e) {
            Log.e(LOG_TAG + "Parser", "IOException is " + e.getMessage());
            return null;
        } catch (ParseException e) {
            Log.e(LOG_TAG + "Parser", "ParseException is " + e.getMessage());
            return null;
        }
    }
}
