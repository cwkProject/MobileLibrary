package org.mobile.library.model.work;
/**
 * Created by 超悟空 on 2015/7/2.
 */

import android.util.Log;

import org.mobile.library.model.data.IDefaultDataModel;
import org.mobile.library.network.communication.ICommunication;
import org.mobile.library.network.factory.CommunicationFactory;
import org.mobile.library.network.factory.NetworkType;

/**
 * 默认实现结构的任务模型基类，
 * 内部使用{@link IDefaultDataModel}作为默认的数据模型类
 *
 * @param <Parameters>    功能所需参数类型
 * @param <Result>        结果数据类型
 * @param <DataModelType> 任务请求使用的数据模型类型
 *
 * @author 超悟空
 * @version 1.0 2015/7/2
 * @since 1.0
 */
public abstract class DefaultWorkModel<Parameters, Result, DataModelType extends
        IDefaultDataModel> extends WorkModel<Parameters, Result> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "DefaultWorkModel.";

    @Override
    protected final boolean onDoWork(Parameters... parameters) {
        if (!onCheckParameters(parameters)) {
            // 数据异常
            Log.d(LOG_TAG + "onDoWork", "parameters is error");
            // 执行异常回调
            onParameterError(parameters);
            return false;
        }

        DataModelType data = onCreateDataModel(parameters);

        // 新建带有服务器公钥的通讯工具
        ICommunication communication = CommunicationFactory.Create(onNetworkType());

        // 设置调用的方法名
        communication.setTaskName(onTaskUri());
        Log.i(LOG_TAG + "onDoWork", "task request url is " + onTaskUri());

        // 发送请求
        //noinspection unchecked
        communication.Request(data.serialization());

        // 解析数据
        if (data.parse(communication.Response())) {
            // 解析成功
            Log.i(LOG_TAG + "onDoWork", "result parse success");
            Log.i(LOG_TAG + "onDoWork", "onParseSuccess(IDefaultDataModel) is invoked");
            // 解析成功回调
            onParseSuccess(data);

            if (data.isSuccess()) {
                // 设置请求成功后返回的数据
                setResult(onRequestSuccessSetResult(data));
                return true;
            } else {
                // 设置请求失败后返回的数据
                setResult(onRequestFailedSetResult(data));
                return false;
            }
        } else {
            // 解析失败
            Log.i(LOG_TAG + "onDoWork", "result parse failed");
            Log.i(LOG_TAG + "onDoWork", "onParseFailed(IDefaultDataModel) is invoked");
            // 解析失败回调
            onParseFailed(data);
            // 设置解析失败返回的数据
            setResult(onParseFailedSetResult(data));
            return false;
        }
    }

    /**
     * 参数合法性检测，
     * 用于检测传入参数是否合法，
     * 需要子类重写检测规则，
     *
     * @param parameters 传入参数
     *
     * @return 检测结果，合法返回true，非法返回false
     */
    protected abstract boolean onCheckParameters(Parameters... parameters);

    /**
     * 参数检测不合法时执行的错误回调
     *
     * @param parameters 传入参数
     */
    protected void onParameterError(Parameters... parameters) {

    }

    /**
     * 设置任务请求地址
     *
     * @return 地址字符串
     */
    protected abstract String onTaskUri();

    /**
     * 设置网络请求类型，
     * 用于{@link CommunicationFactory#Create(NetworkType)}生产网络请求实例，
     * 默认为{@link NetworkType#HTTP_GET}
     *
     * @return 网络请求类型枚举
     */
    protected NetworkType onNetworkType() {
        return NetworkType.HTTP_GET;
    }

    /**
     * 服务返回数据解析成功后调用，
     * 即在{@link IDefaultDataModel#parse(Object)}返回true时调用
     *
     * @param data 解析后的数据模型对象
     */
    protected void onParseSuccess(DataModelType data) {
    }

    /**
     * 服务返回数据解析失败后调用
     * 即在{@link IDefaultDataModel#parse(Object)}返回false时调用
     *
     * @param data 数据模型对象
     */
    protected void onParseFailed(DataModelType data) {
    }

    /**
     * 服务返回数据解析成功后，
     * 并且服务执行为成功即{@link IDefaultDataModel#isSuccess()}返回true时，
     * 设置任务返回数据，
     * 即设置{@link #setResult(Object)}的参数。
     * 该方法在{@link #onParseSuccess(IDefaultDataModel)}之后调用
     *
     * @param data 解析后的数据模型对象
     *
     * @return 任务返回数据
     */
    protected abstract Result onRequestSuccessSetResult(DataModelType data);

    /**
     * 服务返回数据解析成功后，
     * 但是服务执行为失败即{@link IDefaultDataModel#isSuccess()}返回false时，
     * 设置任务返回数据，
     * 即设置{@link #setResult(Object)}的参数。
     * 该方法在{@link #onParseSuccess(IDefaultDataModel)}之后调用
     *
     * @param data 解析后的数据模型对象
     *
     * @return 任务返回数据
     */
    protected abstract Result onRequestFailedSetResult(DataModelType data);

    /**
     * 服务返回数据解析失败后，
     * 即在{@link IDefaultDataModel#parse(Object)}返回false时，
     * 设置任务返回数据，
     * 即设置{@link #setResult(Object)}的参数。
     * 该方法在{@link #onParseFailed(IDefaultDataModel)}之后调用
     *
     * @param data 数据模型对象
     *
     * @return 任务返回数据
     */
    protected Result onParseFailedSetResult(DataModelType data) {
        return null;
    }

    /**
     * 创建数据模型对象并填充参数
     *
     * @param parameters 传入参数
     *
     * @return 参数设置完毕后的数据模型对象
     */
    protected abstract DataModelType onCreateDataModel(Parameters... parameters);
}
