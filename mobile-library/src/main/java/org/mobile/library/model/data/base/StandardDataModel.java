package org.mobile.library.model.data.base;
/**
 * Created by 超悟空 on 2015/11/4.
 */

import android.util.Log;

import org.mobile.library.global.GlobalApplication;
import org.mobile.library.model.data.IDefaultDataModel;
import org.mobile.library.network.util.RequestSign;

import java.util.HashMap;
import java.util.Map;

/**
 * 规范的数据模型结构
 *
 * @param <Handle>   二次处理的结果数据类型
 * @param <Response> 要解析的结果数据类型
 * @param <Value>    要序列化的Map值类型
 *
 * @author 超悟空
 * @version 2.0 2016/3/19
 * @since 1.0
 */
public abstract class StandardDataModel<Handle, Response, Value> implements
        IDefaultDataModel<Response, Map<String, Value>> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "StandardDataModel.";

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
    public final boolean isSuccess() {
        return success;
    }

    @Override
    public final String getMessage() {
        return message;
    }

    /**
     * 设置服务请求结果消息
     *
     * @param message 消息字符串
     */
    protected final void setMessage(String message) {
        this.message = message;
    }

    @Override
    public final Map<String, Value> serialization() {
        Log.i(LOG_TAG + "serialization", "serialization start");
        // 序列化后的参数集
        Map<String, Value> dataMap = new HashMap<>();
        Log.i(LOG_TAG + "serialization", "onFillRequestParameters(Map<String, String>) is invoked");
        // 调用填充方法
        onFillRequestParameters(dataMap);
        Log.i(LOG_TAG + "serialization", "serialization end");

        if (onIsRequestSign()) {
            // 对参数进行签名
            Log.i(LOG_TAG + "serialization", "parameters sign");
            onRequestParametersSign(dataMap);
        }

        return dataMap;
    }

    /**
     * 填充服务请求所需的参数，
     * 即设置{@link #serialization()}返回值
     *
     * @param dataMap 参数数据集<参数名,参数值>
     */
    protected abstract void onFillRequestParameters(Map<String, Value> dataMap);

    @Override
    public final boolean parse(Response response) {
        Log.i(LOG_TAG + "parse", "parse start");
        Log.i(LOG_TAG + "parse", "result is " + response);
        if (!onCheckResponse(response)) {
            // 通信异常
            Log.d(LOG_TAG + "parse", "response is error");
            return false;
        }

        try {
            // 将结果转换为Handle对象
            Handle handle = onCreateHandle(response);

            Log.i(LOG_TAG + "parse", "onRequestResult(Object) is invoked");
            // 提取服务执行结果
            this.success = onRequestResult(handle);
            Log.i(LOG_TAG + "parse", "request result is " + this.success);
            Log.i(LOG_TAG + "parse", "onRequestResult(Object) is end");

            Log.i(LOG_TAG + "parse", "onRequestMessage(boolean) is invoked");
            // 提取服务返回的消息
            this.message = onRequestMessage(this.success, handle);
            Log.i(LOG_TAG + "parse", "request message is " + this.message);
            Log.i(LOG_TAG + "parse", "onRequestMessage(boolean) is end");

            if (this.success) {
                // 服务请求成功回调
                Log.i(LOG_TAG + "parse", "onRequestSuccess(Object) is invoked");
                onRequestSuccess(handle);
                Log.i(LOG_TAG + "parse", "onRequestSuccess(Object) is end");
            } else {
                // 服务请求失败回调
                Log.i(LOG_TAG + "parse", "onRequestFailed(Object) is invoked");
                onRequestFailed(handle);
                Log.i(LOG_TAG + "parse", "onRequestFailed(Object) is end");
            }

            return true;
        } catch (Exception e) {
            Log.e(LOG_TAG + "parse", "exception type is " + e);
            Log.e(LOG_TAG + "parse", "exception message is " + e.getMessage());
            return false;
        } finally {
            Log.i(LOG_TAG + "parse", "parse end");
        }
    }

    /**
     * 检测响应结果是否符合预期
     *
     * @param response 响应数据
     *
     * @return 检测结果
     */
    protected abstract boolean onCheckResponse(Response response);

    /**
     * 对响应结果数据进行二次处理
     *
     * @param response 响应结果数据
     *
     * @return 处理后的可操作对象
     *
     * @throws Exception 处理过程中可能出现的异常
     */
    protected abstract Handle onCreateHandle(Response response) throws Exception;

    /**
     * 提取服务执行结果
     *
     * @param handleResult 二次处理结果集
     *
     * @return 服务请求结果，true表示请求成功，false表示请求失败
     *
     * @throws Exception 处理过程中可能出现的异常
     */
    protected abstract boolean onRequestResult(Handle handleResult) throws Exception;

    /**
     * 提取服务返回的结果消息<br>
     * 在{@link #onRequestResult(Object)}之后被调用
     *
     * @param result       服务请求执行结果，
     *                     即{@link #onRequestResult(Object)}返回值
     * @param handleResult 二次处理结果集
     *
     * @return 消息字符串
     *
     * @throws Exception 处理过程中可能出现的异常
     */
    protected abstract String onRequestMessage(boolean result, Handle handleResult) throws
            Exception;

    /**
     * 提取服务反馈的结果数据<br>
     * 在服务请求成功后调用，
     * 即{@link #onRequestResult(Object)}返回值为true时，
     * 在{@link #onRequestMessage(boolean , Object)}之后被调用，
     *
     * @param handleResult 二次处理结果集
     *
     * @throws Exception 处理过程中可能出现的异常
     */
    protected abstract void onRequestSuccess(Handle handleResult) throws Exception;

    /**
     * 提取服务反馈的结果数据<br>
     * 在服务请求失败后调用，
     * 即{@link #onRequestResult(Object)}返回值为false时，
     * 在{@link #onRequestMessage(boolean , Object)}之后被调用，
     *
     * @param handleResult 二次处理结果集
     *
     * @throws Exception 处理过程中可能出现的异常
     */
    protected void onRequestFailed(Handle handleResult) throws Exception {

    }

    /**
     * 表示是否对请求参数进行校验签名
     *
     * @return true表示进行签名，默认为true
     */
    protected boolean onIsRequestSign() {
        return true;
    }

    /**
     * 对参数进行签名，
     * 需要在应用启动时对环境变量赋值，
     * {@link org.mobile.library.global.ApplicationAttribute#setAppCode(String)}，
     * {@link org.mobile.library.global.ApplicationAttribute#setAppToken(String)}
     *
     * @param dataMap 要发送的数据
     */
    protected void onRequestParametersSign(Map<String, Value> dataMap) {
        if (GlobalApplication.getApplicationAttribute().getAppCode() != null && GlobalApplication
                .getApplicationAttribute().getAppToken() != null) {
            //noinspection unchecked
            RequestSign.sign((Map<String, Object>) dataMap);
        }
    }
}
