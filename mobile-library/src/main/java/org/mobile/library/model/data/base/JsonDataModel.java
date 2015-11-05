package org.mobile.library.model.data.base;
/**
 * Created by 超悟空 on 2015/7/2.
 */

import org.json.JSONObject;

/**
 * 解析响应结果为Json字符串的数据模型基类<br>
 * 请求数据为纯文本内容
 *
 * @author 超悟空
 * @version 2.0 2015/11/3
 * @since 1.0
 */
public abstract class JsonDataModel extends StandardDataModel<JSONObject, String, String> {

    @Override
    protected final boolean onCheckResponse(String response) {
        return response != null;
    }

    @Override
    protected final JSONObject onCreateHandle(String response) throws Exception {
        return new JSONObject(response);
    }
}
