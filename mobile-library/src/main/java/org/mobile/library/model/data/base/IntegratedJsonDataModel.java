package org.mobile.library.model.data.base;
/**
 * Created by 超悟空 on 2016/7/23.
 */


import org.json.JSONObject;
import org.mobile.library.global.ApplicationAttribute;
import org.mobile.library.model.data.util.RequestSign;

import java.util.Map;

/**
 * 集成化Json数据模型基类<br>
 * 解析响应结果为Json字符串的数据模型基类<br>
 * 请求参数为纯文本内容
 *
 * @author 超悟空
 * @version 1.0 2016/7/23
 * @since 1.0
 */
public abstract class IntegratedJsonDataModel<Parameters, Result> extends
        IntegratedDataModel<Parameters, Result, JSONObject, String, String> {

    @Override
    protected boolean onCheckResponse(String response) {
        return response != null;
    }

    @Override
    protected final JSONObject onCreateHandle(String response) throws Exception {
        return new JSONObject(response);
    }

    /**
     * 对参数进行签名，
     * 需要在应用启动时对环境变量赋值，
     * {@link ApplicationAttribute#create()}}
     *
     * @param dataMap 要发送的数据
     */
    @Override
    protected void onRequestParametersSign(Map<String, String> dataMap) {
        RequestSign.signForText(dataMap);
    }
}
