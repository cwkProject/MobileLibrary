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
 * 默认实现的网络任务模型基类<br>
 * 内部使用{@link IDefaultDataModel}作为默认的数据模型类，
 * 使用{@link ICommunication}作为网络请求工具
 *
 * @param <Parameters>    功能所需参数类型
 * @param <Result>        结果数据类型
 * @param <DataModelType> 任务请求使用的数据模型类型
 *
 * @author 超悟空
 * @version 2.0 2015/10/17
 * @since 1.0
 */
public abstract class DefaultWorkModel<Parameters, Result, DataModelType extends
        IDefaultDataModel> extends WorkModel<Parameters, Result> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "DefaultWorkModel.";

    /**
     * 参数
     */
    private Parameters[] mParameters = null;

    @Override
    protected final boolean onDoWork(Parameters... parameters) {
        // 保存参数对象
        mParameters = parameters;

        if (!onCheckParameters(parameters)) {
            // 数据异常
            Log.d(LOG_TAG + "onDoWork", "parameters is error");
            // 执行异常回调
            onParameterError(parameters);
            return false;
        }

        // 创建数据模型
        DataModelType data = onCreateDataModel(parameters);

        // 新建通讯工具
        ICommunication communication = onCreateCommunication();

        // 设置调用的方法名
        communication.setTaskName(onTaskUri());
        Log.i(LOG_TAG + "onDoWork", "task request url is " + onTaskUri());

        if (!isCancelled()) {
            // 发送请求
            //noinspection unchecked
            communication.Request(data.serialization());
        } else {
            return false;
        }

        if (!isCancelled()) {
            // 解析数据
            if (data.parse(communication.Response())) {
                // 解析成功
                Log.i(LOG_TAG + "onDoWork", "result parse success");
                // 关闭网络连接
                communication.close();

                Log.i(LOG_TAG + "onDoWork", "onParseSuccess(IDefaultDataModel) is invoked");
                // 解析成功回调
                onParseSuccess(data);
                // 设置结果消息
                setMessage(onParseSuccessSetMessage(data.isSuccess(), data));

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
                // 关闭网络连接
                communication.close();

                Log.i(LOG_TAG + "onDoWork", "onParseFailed(IDefaultDataModel) is invoked");
                // 解析失败回调
                onParseFailed(data);
                // 设置结果消息
                setMessage(onParseFailedSetMessage(data));
                // 设置解析失败返回的数据
                setResult(onParseFailedSetResult(data));
                return false;
            }
        } else {
            // 关闭网络连接
            communication.close();
            return false;
        }
    }

    /**
     * 参数合法性检测<br>
     * 用于检测传入参数是否合法，
     * 需要子类重写检测规则<br>
     * 检测成功后续任务才会被正常执行，
     * 如果检测失败则{@link #onParameterError(Object[])}会被调用
     *
     * @param parameters 任务传入参数
     *
     * @return 检测结果，合法返回true，非法返回false
     */
    protected abstract boolean onCheckParameters(Parameters... parameters);

    /**
     * 参数检测不合法时调用，
     * 即{@link #onCheckParameters(Object[])}返回false时被调用，
     * 且后续任务不再执行
     *
     * @param parameters 任务传入参数
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
     * 设置网络请求类型<br>
     * 用于{@link CommunicationFactory#Create(NetworkType)}生产网络请求实例，
     * 默认为{@link NetworkType#HTTP_GET}
     *
     * @return 网络请求类型枚举
     */
    protected NetworkType onNetworkType() {
        return NetworkType.HTTP_GET;
    }

    /**
     * 创建网络请求工具<br>
     * 用于发送网络请求，
     * 默认使用{@link CommunicationFactory}工具进行创建，
     * 使用{@link #onNetworkType()}返回的请求类型，
     * 如果需要使用自定义网络请求工具请重写此方法
     *
     * @return 网络请求工具实例
     */
    protected ICommunication onCreateCommunication() {
        return CommunicationFactory.Create(onNetworkType());
    }

    /**
     * 服务器响应数据解析成功后调用，
     * 即在{@link IDefaultDataModel#parse(Object)}返回true时调用
     *
     * @param data 解析后的数据模型对象
     */
    protected void onParseSuccess(DataModelType data) {
    }

    /**
     * 设置解析成功后返回的结果消息<br>
     * 在{@link #onParseSuccess(IDefaultDataModel)}之后被调用，
     * 默认使用{@link IDefaultDataModel#getMessage()}的返回值，
     * 如果需要自定义返回结果消息请重写此方法
     *
     * @param state 服务执行结果
     * @param data  解析后的数据模型对象
     *
     * @return 要返回的任务结果消息
     */
    protected String onParseSuccessSetMessage(boolean state, DataModelType data) {
        return data.getMessage();
    }

    /**
     * 服务器响应数据解析失败后调用，
     * 即在{@link IDefaultDataModel#parse(Object)}返回false时调用
     *
     * @param data 数据模型对象
     */
    protected void onParseFailed(DataModelType data) {
    }

    /**
     * 设置解析失败后返回的结果消息<br>
     * 在{@link #onParseFailed(IDefaultDataModel)}之后被调用，
     * 默认使用{@link IDefaultDataModel#getMessage()}的返回值，
     * 如果需要自定义返回结果消息请重写此方法
     *
     * @param data 数据模型对象
     *
     * @return 要返回的任务结果消息
     */
    protected String onParseFailedSetMessage(DataModelType data) {
        return data.getMessage();
    }

    /**
     * 设置服务请求成功时的返回数据<br>
     * 服务返回数据解析成功后，
     * 并且服务执行结果为成功即{@link IDefaultDataModel#isSuccess()}返回true时，
     * 设置任务的返回数据，
     * 即设置{@link #setResult(Object)}的参数。
     * 该方法在{@link #onParseSuccess(IDefaultDataModel)}之后被调用
     *
     * @param data 解析后的数据模型对象
     *
     * @return 任务返回数据
     */
    protected abstract Result onRequestSuccessSetResult(DataModelType data);

    /**
     * 设置服务请求失败时的返回数据<br>
     * 服务返回数据解析成功后，
     * 但是服务执行结果为失败即{@link IDefaultDataModel#isSuccess()}返回false时设置任务返回数据，
     * 即设置{@link #setResult(Object)}的参数。<br>
     * 该方法在{@link #onParseSuccess(IDefaultDataModel)}之后被调用，
     * 默认返回null引用，
     * 如果需要自定义结果数据请重写该方法
     *
     * @param data 解析后的数据模型对象
     *
     * @return 任务返回数据
     */
    protected Result onRequestFailedSetResult(DataModelType data) {
        return null;
    }

    /**
     * 设置服务请求解析失败时的返回数据<br>
     * 服务返回数据解析失败后，
     * 即在{@link IDefaultDataModel#parse(Object)}返回false时设置任务返回数据，
     * 即设置{@link #setResult(Object)}的参数。<br>
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

    /**
     * 获取当前任务传入的参数，
     * {@link #onStartWork()}执行之后可获取
     *
     * @return 参数集合
     */
    protected final Parameters[] getParameters() {
        return mParameters;
    }
}
