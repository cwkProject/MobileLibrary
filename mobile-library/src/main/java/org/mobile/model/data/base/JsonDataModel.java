package org.mobile.model.data.base;
/**
 * Created by 超悟空 on 2015/7/2.
 */

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.mobile.model.data.IDefaultDataModel;
import org.mobile.parser.HttpResponseHttpEntityToStringParser;

import java.util.HashMap;
import java.util.Map;

/**
 * 解析Json字符串的数据模型基类
 *
 * @author 超悟空
 * @version 1.0 2015/7/2
 * @since 1.0
 */
public abstract class JsonDataModel implements IDefaultDataModel {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "JsonDataModel.";

    /**
     * 标识本次服务请求是否成功
     */
    private boolean success = false;

    /**
     * 本次服务请求返回的结果字符串
     */
    private String message = null;

    /**
     * 判断本次服务请求是否成功
     *
     * @return true表示成功，false表示失败
     */
    @Override
    public boolean isSuccess() {
        return success;
    }

    @Override
    public String getMessage() {
        return message;
    }

    /**
     * 设置服务请求结果消息
     *
     * @param message 消息字符串
     */
    protected void setMessage(String message) {
        this.message = message;
    }

    @Override
    public Map<String, String> serialization() {
        Log.i(LOG_TAG + "serialization", "serialization start");
        // 序列化后的参数集
        Map<String, String> dataMap = new HashMap<>();
        Log.i(LOG_TAG + "serialization", "onFillRequestParameters(Map<String, String>) is invoked");
        // 调用填充方法
        onFillRequestParameters(dataMap);
        Log.i(LOG_TAG + "serialization", "serialization end");
        return dataMap;
    }

    /**
     * 填充服务请求所需的参数，
     * 即设置{@link #serialization()}返回值
     *
     * @param dataMap 参数数据集<参数名,参数值>
     */
    protected abstract void onFillRequestParameters(Map<String, String> dataMap);

    @Override
    public boolean parse(Object data) {
        Log.i(LOG_TAG + "parse", "parse start");

        if (data == null) {
            // 通信异常
            Log.d(LOG_TAG + "parse", "data is null");
            return false;
        }

        // 新建解析器
        HttpResponseHttpEntityToStringParser parser = new HttpResponseHttpEntityToStringParser();

        // 获取结果字符串
        String resultString = parser.DataParser(data);
        Log.i(LOG_TAG + "parse", "result string is " + resultString);

        try {
            // 将结果转换为JSON对象
            JSONObject jsonObject = new JSONObject(resultString);

            Log.i(LOG_TAG + "parse", "onRequestResult(JSONObject) is invoked");
            // 提取服务执行结果
            this.success = onRequestResult(jsonObject);
            Log.i(LOG_TAG + "parse", "request result is " + this.success);
            Log.i(LOG_TAG + "parse", "onRequestResult(JSONObject) is end");

            Log.i(LOG_TAG + "parse", "onRequestMessage(boolean) is invoked");
            // 提取服务返回的消息
            this.message = onRequestMessage(this.success, jsonObject);
            Log.i(LOG_TAG + "parse", "request message is " + this.message);
            Log.i(LOG_TAG + "parse", "onRequestMessage(boolean) is end");

            if (this.success) {
                // 服务请求成功回调
                Log.i(LOG_TAG + "parse", "onRequestSuccess(JSONObject) is invoked");
                onRequestSuccess(jsonObject);
                Log.i(LOG_TAG + "parse", "onRequestSuccess(JSONObject) is end");
            } else {
                // 服务请求失败回调
                Log.i(LOG_TAG + "parse", "onRequestFailed(JSONObject) is invoked");
                onRequestFailed(jsonObject);
                Log.i(LOG_TAG + "parse", "onRequestFailed(JSONObject) is end");
            }

            return true;
        } catch (JSONException e) {
            Log.e(LOG_TAG + "parse", "JSONException is " + e.getMessage());
            return false;
        } finally {
            Log.i(LOG_TAG + "parse", "parse end");
        }
    }

    /**
     * 提取服务执行结果
     *
     * @param jsonResult Json结果集
     *
     * @return 服务请求结果，true表示请求成功，false表示请求失败
     *
     * @throws JSONException 解析过程中出现错误
     */
    protected abstract boolean onRequestResult(JSONObject jsonResult) throws JSONException;

    /**
     * 提取服务返回的结果消息，
     * 在{@link #onRequestResult(JSONObject)}之后被调用
     *
     * @param result     服务请求执行结果，
     *                   即{@link #onRequestResult(JSONObject)}返回值
     * @param jsonResult Json结果集
     *
     * @return 消息字符串
     *
     * @throws JSONException 解析过程中出现错误
     */
    protected abstract String onRequestMessage(boolean result, JSONObject jsonResult) throws JSONException;

    /**
     * 提取服务反馈的结果数据，
     * 在服务请求成功后调用，
     * 即{@link #onRequestResult(JSONObject)}返回值为true时，
     * 在{@link #onRequestMessage(boolean , JSONObject)}之后被调用，
     *
     * @param jsonResult Json结果集
     *
     * @throws JSONException 解析过程中出现错误
     */
    protected abstract void onRequestSuccess(JSONObject jsonResult) throws JSONException;

    /**
     * 提取服务反馈的结果数据，
     * 在服务请求失败后调用，
     * 即{@link #onRequestResult(JSONObject)}返回值为false时，
     * 在{@link #onRequestMessage(boolean , JSONObject)}之后被调用，
     *
     * @param jsonResult Json结果集
     *
     * @throws JSONException 解析过程中出现错误
     */
    protected void onRequestFailed(JSONObject jsonResult) throws JSONException {

    }
}
