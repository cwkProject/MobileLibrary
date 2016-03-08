package org.mobile.library.model.data.base;
/**
 * Created by 超悟空 on 2015/12/28.
 */

import org.json.JSONObject;

/**
 * 根据现有网络协议简化的数据模型
 *
 * @author 超悟空
 * @version 1.0 2016/3/8
 * @since 1.0
 */
public abstract class SimpleJsonDataModel extends JsonDataModel {
    @Override
    protected boolean onRequestResult(JSONObject handleResult) throws Exception {
        // 得到执行结果
        String resultState = handleResult.getString("IsSuccess");

        return resultState != null && "yes".equals(resultState.trim().toLowerCase());
    }

    @Override
    protected String onRequestMessage(boolean result, JSONObject handleResult) throws Exception {
        return handleResult.getString("Message");
    }

    @Override
    protected void onRequestSuccess(JSONObject handleResult) throws Exception {
        if (!handleResult.isNull("Data")) {
            onExtractData(handleResult);
        }
    }

    /**
     * 当请求成功且返回结果中存在"Data"标签的数据时被调用，
     * 即"Data"不为null时此方法用于提取装配结果数据
     *
     * @param jsonData 响应的完整数据结果(包含"Data")
     *
     * @throws Exception 处理过程抛出的异常
     */
    protected abstract void onExtractData(JSONObject jsonData) throws Exception;
}
