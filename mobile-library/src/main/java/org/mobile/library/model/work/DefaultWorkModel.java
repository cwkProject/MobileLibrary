package org.mobile.library.model.work;
/**
 * Created by 超悟空 on 2015/7/2.
 */

import android.util.Log;

import org.mobile.library.global.GlobalApplication;
import org.mobile.library.model.data.IDefaultDataModel;
import org.mobile.library.model.operate.AsyncExecute;
import org.mobile.library.model.operate.Cancelable;
import org.mobile.library.model.operate.SyncExecute;
import org.mobile.library.network.factory.CommunicationFactory;
import org.mobile.library.network.factory.NetworkType;
import org.mobile.library.network.util.AsyncCommunication;
import org.mobile.library.network.util.NetworkCallback;
import org.mobile.library.network.util.NetworkProgressListener;
import org.mobile.library.network.util.NetworkRefreshProgressHandler;
import org.mobile.library.network.util.SyncCommunication;

/**
 * 默认实现的网络任务模型基类<br>
 * 内部使用{@link IDefaultDataModel}作为默认的数据模型类，
 * 使用{@link SyncCommunication}作为同步网络请求工具，
 * 使用{@link AsyncCommunication}作为异步网络请求工具
 *
 * @param <Parameters>    功能所需参数类型
 * @param <Result>        结果数据类型
 * @param <DataModelType> 任务请求使用的数据模型类型
 *
 * @author 超悟空
 * @version 3.0 2015/11/2
 * @since 1.0
 */
public abstract class DefaultWorkModel<Parameters, Result, DataModelType extends
        IDefaultDataModel> extends WorkProcessModel<Parameters, Result> implements
        SyncExecute<Parameters>, AsyncExecute<Parameters>, Cancelable {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "DefaultWorkModel.";

    /**
     * 任务完成回调接口
     */
    private IWorkEndListener<Result> workEndListener = null;

    /**
     * 网络请求进度监听器，可用于上传和下载进度监听
     */
    private NetworkProgressListener networkProgressListener = null;

    /**
     * 同步网络请求工具
     */
    private SyncCommunication syncCommunication = null;

    /**
     * 异步网络请求工具
     */
    private AsyncCommunication asyncCommunication = null;

    /**
     * 指示是否将取消回调接口在UI线程执行，默认为发送到UI线程
     */
    private boolean isEndUiThread = true;

    /**
     * 指示是否将进度回调接口在UI线程执行，默认为发送到UI线程
     */
    private boolean isProgressUiThread = true;

    /**
     * 任务取消状态标签
     */
    private volatile boolean cancelMark = false;

    /**
     * 标识是否异步启动任务
     */
    private boolean isAsync = true;

    /**
     * 参数
     */
    private Parameters[] mParameters = null;

    @SafeVarargs
    @Override
    protected final boolean onDoWork(Parameters... parameters) {
        // 保存参数对象
        mParameters = parameters;

        // 校验参数
        if (!onCheckParameters(parameters)) {
            // 数据异常
            Log.d(LOG_TAG + "onDoWork", "parameters is error");
            // 执行异常回调
            onParameterError(parameters);
            return false;
        }

        // 创建数据模型
        final DataModelType data = onCreateDataModel(parameters);

        if (!cancelMark) {
            Log.i(LOG_TAG + "onDoWork", "task request url is " + onTaskUri());

            // 进入同步异步请求分支
            if (isAsync) {
                // 异步分支

                // 设置请求地址
                asyncCommunication.setTaskName(onTaskUri());
                // 发送请求
                //noinspection unchecked
                asyncCommunication.Request(data.serialization(), new NetworkCallback() {
                    @Override
                    public void onFinish(boolean result, Object response) {
                        if (!cancelMark) {
                            // 解析响应数据
                            boolean success = onParseResult(data, result, response);

                            Log.i(LOG_TAG + "onDoWork", "onStopWork(boolean , String , Object) is" +
                                    " " +
                                    "" + "invoked");
                            // 执行后继任务
                            onStopWork(success, getMessage(), getResult());
                        }
                    }
                });

                // 表示成功发送请求，任务被受理
                return true;
            } else {
                // 同步分支

                // 设置请求地址
                syncCommunication.setTaskName(onTaskUri());
                // 发送请求
                //noinspection unchecked
                syncCommunication.Request(data.serialization());

                // 解析响应数据
                boolean success = onParseResult(data, syncCommunication.isSuccessful(),
                        syncCommunication.Response());

                // 关闭网络
                syncCommunication.close();
                return success;
            }
        } else {
            return false;
        }
    }

    /**
     * 解析响应数据
     *
     * @param data     数据模型
     * @param result   请求结果
     * @param response 响应数据
     *
     * @return 任务执行结果，true表示成功
     */
    private boolean onParseResult(DataModelType data, boolean result, Object response) {
        Log.i(LOG_TAG + "onParseResult", "result parse start");
        // 解析数据
        //noinspection unchecked
        if (result && data.parse(response)) {
            // 解析成功
            Log.i(LOG_TAG + "onParseResult", "result parse success");
            Log.i(LOG_TAG + "onParseResult", "onParseSuccess(IDefaultDataModel) is invoked");
            // 解析成功回调
            onParseSuccess(data);
            // 设置结果消息
            setMessage(onParseSuccessSetMessage(data.isSuccess(), data));

            if (data.isSuccess()) {
                // 设置请求成功后返回的数据
                Log.i(LOG_TAG + "onParseResult", "work success");
                setResult(onRequestSuccessSetResult(data));
                return true;
            } else {
                // 设置请求失败后返回的数据
                Log.i(LOG_TAG + "onParseResult", "work failed");
                setResult(onRequestFailedSetResult(data));
                return false;
            }
        } else {
            // 解析失败
            Log.i(LOG_TAG + "onDoWork", "result parse failed");
            Log.i(LOG_TAG + "onDoWork", "onParseFailed(IDefaultDataModel) is invoked");
            // 解析失败回调
            onParseFailed(data);
            // 设置结果消息
            setMessage(onParseFailedSetMessage(data));
            // 设置解析失败返回的数据
            setResult(onParseFailedSetResult(data));
            return false;
        }
    }

    @SafeVarargs
    @Override
    public final void beginExecute(Parameters... parameters) {
        Log.i(LOG_TAG + "beginExecute", "beginExecute start");

        isAsync = true;

        if (!cancelMark) {
            Log.i(LOG_TAG + "beginExecute", "onStartWork() is invoked");
            // 执行前导任务
            onStartWork();
        }

        if (!cancelMark) {
            Log.i(LOG_TAG + "beginExecute", "onDoWork(Object[]) is invoked");
            // 执行核心任务
            if (!onDoWork(parameters)) {
                Log.i(LOG_TAG + "beginExecute", "onStopWork(boolean , String , Object) is invoked");
                // 任务启动出错
                // 执行后继任务
                onStopWork(false, getMessage(), getResult());
            }
        }
    }

    @SafeVarargs
    @Override
    public final boolean execute(Parameters... parameters) {
        Log.i(LOG_TAG + "execute", "execute start");

        isAsync = false;

        // 用于保存执行结果
        boolean state = false;

        if (!cancelMark) {
            Log.i(LOG_TAG + "execute", "onStartWork() is invoked");
            // 执行前导任务
            onStartWork();
        }

        if (!cancelMark) {
            Log.i(LOG_TAG + "execute", "onDoWork(Object[]) is invoked");
            // 执行核心任务
            state = onDoWork(parameters);
        }

        if (!cancelMark) {
            Log.i(LOG_TAG + "execute", "onStopWork(boolean , String , Object) is invoked");
            // 执行后继任务
            onStopWork(state, getMessage(), getResult());
        }

        return state;
    }

    @Override
    public final void cancel() {
        Log.i(LOG_TAG + "cancel", "work cancel");
        this.cancelMark = true;
        if (syncCommunication != null) {
            syncCommunication.cancel();
        }
        if (asyncCommunication != null) {
            asyncCommunication.cancel();
        }
    }

    @Override
    protected void onStartWork() {
        Log.i(LOG_TAG + "onStartWork", "work start");
        if (isAsync) {
            // 新建通讯工具
            asyncCommunication = onCreateAsyncCommunication();
            // 尝试绑定进度监听器
            if (asyncCommunication instanceof NetworkRefreshProgressHandler) {
                onBindProgressListener((NetworkRefreshProgressHandler) asyncCommunication);
            }
        } else {
            // 新建通讯工具
            syncCommunication = onCreateSyncCommunication();
            // 尝试绑定进度监听器
            if (syncCommunication instanceof NetworkRefreshProgressHandler) {
                onBindProgressListener((NetworkRefreshProgressHandler) syncCommunication);
            }
        }
    }

    /**
     * 绑定进度监听器
     *
     * @param progressHandler 可设置监听器的网络工具
     */
    private void onBindProgressListener(NetworkRefreshProgressHandler progressHandler) {
        if (networkProgressListener != null) {
            // 开始绑定
            Log.i(LOG_TAG + "onBindProgressListener", "set ProgressListener");

            if (isProgressUiThread) {
                // 发送到UI线程
                progressHandler.setNetworkProgressListener(new NetworkProgressListener() {
                    @Override
                    public void onRefreshProgress(final long current, final long total, final
                    boolean done) {
                        GlobalApplication.getGlobal().getUiHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                networkProgressListener.onRefreshProgress(current, total, done);
                            }
                        });
                    }
                });
            } else {
                // 在当前线程
                // 直接绑定
                progressHandler.setNetworkProgressListener(networkProgressListener);
            }
        }
    }

    @Override
    protected void onStopWork(final boolean state, final String message, final Result result) {
        Log.i(LOG_TAG + "onStopWork", "work stop");
        // 如果设置了回调接口则执行回调方法
        if (this.workEndListener != null) {
            Log.i(LOG_TAG + "onStopWork", "workEndListener.doEndWork(boolean , String , Object) " +
                    "is " + "invoked");
            if (isEndUiThread) {
                // 发送到UI线程
                GlobalApplication.getGlobal().getUiHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        workEndListener.doEndWork(state, message, result);
                    }
                });
            } else {
                // 发送到当前线程
                this.workEndListener.doEndWork(state, message, result);
            }
        }

        Log.i(LOG_TAG + "onStopWork", "work end");
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
    @SuppressWarnings("unchecked")
    protected abstract boolean onCheckParameters(Parameters... parameters);

    /**
     * 参数检测不合法时调用，
     * 即{@link #onCheckParameters(Object[])}返回false时被调用，
     * 且后续任务不再执行
     *
     * @param parameters 任务传入参数
     */
    @SuppressWarnings("unchecked")
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
     * 用于{@link CommunicationFactory#CreateSyncCommunication(NetworkType)}生产网络请求实例，
     * 默认为{@link NetworkType#HTTP_GET}
     *
     * @return 网络请求类型枚举
     */
    protected NetworkType onNetworkType() {
        return NetworkType.HTTP_GET;
    }

    /**
     * 创建同步网络请求工具<br>
     * 用于发送网络请求，
     * 默认使用{@link CommunicationFactory}工具进行创建，
     * 使用{@link #onNetworkType()}返回的请求类型，
     * 如果需要使用自定义网络请求工具请重写此方法
     *
     * @return 网络请求工具实例
     */
    protected SyncCommunication onCreateSyncCommunication() {
        return CommunicationFactory.CreateSyncCommunication(onNetworkType());
    }

    /**
     * 创建异步网络请求工具<br>
     * 用于发送网络请求，
     * 默认使用{@link CommunicationFactory}工具进行创建，
     * 使用{@link #onNetworkType()}返回的请求类型，
     * 如果需要使用自定义网络请求工具请重写此方法
     *
     * @return 网络请求工具实例
     */
    protected AsyncCommunication onCreateAsyncCommunication() {
        return CommunicationFactory.CreateAsyncCommunication(onNetworkType());
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
    @SuppressWarnings("unchecked")
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

    /**
     * 设置任务完成时的回调接口<br>
     * 在任务执行完成后被回调，
     * 运行于UI线程
     *
     * @param workEndListener 监听器对象
     */
    public final void setWorkEndListener(IWorkEndListener<Result> workEndListener) {
        setWorkEndListener(workEndListener, true);
    }

    /**
     * 设置任务的进度更新回调接口<br>
     * 在异步任务中实时更新任务执行进度，
     * 运行于UI线程
     *
     * @param networkProgressListener 监听器对象
     */
    public final void setProgressListener(NetworkProgressListener networkProgressListener) {
        setProgressListener(networkProgressListener, true);
    }

    /**
     * 设置任务完成时的回调接口<br>
     * 在任务执行完成后被回调，
     * 并设置是否在当前线程执行
     *
     * @param workEndListener 监听器对象
     * @param isUiThread      指示是否在UI线程回调，
     *                        true表示在UI线程回调，
     *                        false表示在当前线程回调，
     *                        默认为true
     */
    public final void setWorkEndListener(IWorkEndListener<Result> workEndListener, boolean
            isUiThread) {
        this.workEndListener = workEndListener;
        this.isEndUiThread = isUiThread;
    }

    /**
     * 设置任务的进度更新回调接口<br>
     * 在异步任务中实时更新任务执行进度，
     * 并设置是否在当前线程执行
     *
     * @param networkProgressListener 监听器对象
     * @param isUiThread              指示是否在UI线程回调，
     *                                true表示在UI线程回调，
     *                                false表示在当前线程回调，
     *                                默认为true
     */
    public void setProgressListener(NetworkProgressListener networkProgressListener, boolean
            isUiThread) {
        this.networkProgressListener = networkProgressListener;
        this.isProgressUiThread = isUiThread;
    }
}
