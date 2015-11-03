package org.mobile.library.model.work;
/**
 * Created by 超悟空 on 2015/1/7.
 */

import android.os.AsyncTask;
import android.util.Log;

import org.mobile.library.model.operate.AsyncExecute;
import org.mobile.library.model.operate.Cancelable;
import org.mobile.library.model.operate.ProgressUpdate;
import org.mobile.library.model.operate.SyncExecute;

/**
 * 基础任务处理模型，
 * 用于执行任务逻辑，
 * 同时实现同步与异步流程，
 * 异步流程基于{@link AsyncTask}实现
 *
 * @param <Parameters> 任务所需参数类型
 * @param <Result>     结果数据类型
 *
 * @author 超悟空
 * @version 2.1 2015/10/29
 * @since 1.0
 */
public abstract class WorkModel<Parameters, Result> extends WorkProcessModel<Parameters, Result>
        implements SyncExecute<Parameters>, AsyncExecute<Parameters>, Cancelable {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "WorkModel.";

    /**
     * 任务完成回调接口
     */
    private IWorkEndListener<Result> workEndListener = null;

    /**
     * 任务取消回调接口
     */
    private IWorkEndListener<Result> workCancelledListener = null;

    /**
     * 任务的进度更新回调接口，默认不做任何事
     */
    private ProgressUpdate progressUpdateListener = new ProgressUpdate() {
        @Override
        public void onProgressUpdate(Integer... values) {
            // 一个空实现
        }
    };

    /**
     * 异步任务对象
     */
    private WorkModelAsyncTask work = null;

    /**
     * 同步执行时的任务取消状态标签
     */
    private volatile boolean cancelMark = false;

    /**
     * 执行任务操作，
     * 运行于当前线程
     *
     * @param parameters 任务所需参数
     *
     * @return 执行结果
     */
    @SafeVarargs
    @Override
    public final boolean execute(Parameters... parameters) {
        Log.i(LOG_TAG + "execute", "execute start");
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

        if (cancelMark) {
            // 任务被取消
            Log.i(LOG_TAG + "execute", "work is cancelled");
            Log.i(LOG_TAG + "execute", "onCancelWork(boolean , String , Object) is invoked");
            onCancelWork(state, getMessage(), getResult());

            if (workCancelledListener != null) {
                Log.i(LOG_TAG + "execute", "workCancelledListener.doEndWork(boolean , String , "
                        + "Object) is invoked");
                this.workCancelledListener.doEndWork(state, getMessage(), getResult());
            }
        } else {
            Log.i(LOG_TAG + "execute", "onStopWork(boolean , String , Object) is invoked");
            // 执行后继任务
            onStopWork(state, getMessage(), getResult());

            // 如果设置了回调接口则执行回调方法
            if (this.workEndListener != null) {
                Log.i(LOG_TAG + "execute", "workEndListener.doEndWork(boolean , String , Object) " +
                        "is " + "invoked");

                this.workEndListener.doEndWork(state, getMessage(), getResult());
            }

            Log.i(LOG_TAG + "execute", "execute end");
        }
        return state;
    }

    /**
     * 执行任务操作的异步方法，
     * 基于异步任务实现
     *
     * @param parameters 任务所需参数
     */
    @SafeVarargs
    @Override
    public final void beginExecute(Parameters... parameters) {
        Log.i(LOG_TAG + "beginExecute", "beginExecute start");
        // 新建异步任务
        work = new WorkModelAsyncTask();
        // 执行任务
        work.execute(parameters);
    }

    /**
     * 异步任务实现类
     */
    private class WorkModelAsyncTask extends AsyncTask<Parameters, Integer, Result> {

        // 用于保存执行结果
        boolean state = false;

        @SafeVarargs
        @Override
        protected final Result doInBackground(Parameters... params) {
            Log.i(LOG_TAG + "WorkModelAsyncTask.doInBackground", "doInBackground start");
            Log.i(LOG_TAG + "WorkModelAsyncTask.doInBackground", "onDoWork(Object[]) is invoked");
            // 执行核心任务
            state = onDoWork(params);

            Log.i(LOG_TAG + "WorkModelAsyncTask.doInBackground", "doInBackground end");
            return getResult();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            // 任务被取消
            Log.i(LOG_TAG + "WorkModelAsyncTask.onCancelled", "work is cancelled");
            Log.i(LOG_TAG + "WorkModelAsyncTask.onCancelled", "onCancelWork(boolean , String , "
                    + "Object) is invoked");
            onCancelWork(state, getMessage(), getResult());

            if (workCancelledListener != null) {
                Log.i(LOG_TAG + "WorkModelAsyncTask.onCancelled", "workCancelledListener" +
                        ".doEndWork(boolean , String , " + "Object) is invoked");
                workCancelledListener.doEndWork(state, getMessage(), getResult());
            }
        }

        @Override
        protected void onPreExecute() {
            Log.i(LOG_TAG + "WorkModelAsyncTask.onPreExecute", "onStartWork() is invoked");
            // 执行前导任务
            onStartWork();
        }

        @Override
        protected void onPostExecute(Result result) {
            Log.i(LOG_TAG + "WorkModelAsyncTask.onPostExecute", "onStopWork(boolean , String , " +
                    "Object) is " + "invoked");
            // 执行后继任务
            onStopWork(state, getMessage(), result);

            // 如果设置了回调接口则执行回调方法
            if (workEndListener != null) {
                Log.i(LOG_TAG + "WorkModelAsyncTask.onPostExecute", "workEndListener.doEndWork" +
                        "(boolean , String , Object) is invoked");
                workEndListener.doEndWork(state, getMessage(), getResult());
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // 如果设置了更新接口则会执行有效操作
            progressUpdateListener.onProgressUpdate(values);
        }
    }

    /**
     * 判断任务是否被取消<br>
     * 当{@link #onDoWork(Object[])}执行时，
     * 外部线程调用{@link #cancel()}方法后，
     * 执行该方法将会返回true表示任务已经被外部程序取消，
     * 该方法应该在{@link #onDoWork(Object[])}中定时检测以便在任务被取消时及时退出任务
     *
     * @return true表示已被取消
     */
    public final boolean isCancelled() {
        return work != null ? work.isCancelled() : cancelMark;
    }

    /**
     * 取消正在执行的任务<br>
     * 该方法被调用后{@link #isCancelled()}方法将会返回true，
     * 且在{@link #onDoWork(Object[])}执行结束后回调{@link #onCancelWork(boolean , String , Object)
     * }方法和{@link #setWorkCancelledListener(IWorkEndListener)}中定义的任务取消回调接口
     */
    @Override
    public final void cancel() {
        Log.i(LOG_TAG + "cancel", "cancel() is invoked");
        if (work != null) {
            Log.i(LOG_TAG + "cancel", "starting from beginExecute work is cancelled");
            work.cancel(true);
        } else {
            Log.i(LOG_TAG + "cancel", "starting from execute work is cancelled");
            cancelMark = true;
        }
    }

    /**
     * 任务启动前置方法<br>
     * 在{@link #onDoWork(Object[])}之前被调用，
     * 运行于当前线程
     */
    @Override
    protected void onStartWork() {
    }

    /**
     * 任务逻辑核心方法<br>
     * 任务主要逻辑应该在该方法中被实现，
     * 并且在该方法中通过调用{@link #setResult(Object)}设置任务结果回传数据，
     * 调用{@link #setMessage(String)}设置任务结果回传信息，
     * 并且方法返回任务执行结果。<br>
     * 如果是通过{@link #beginExecute(Object[])}激活的任务，
     * 则此方法将在独立线程中执行，
     * 如果是通过{@link #execute(Object[])}激活的任务，
     * 则此方法将在当前线程中执行。
     *
     * @param parameters 任务传入参数
     *
     * @return 执行结果
     */
    @SuppressWarnings("unchecked")
    @Override
    protected abstract boolean onDoWork(Parameters... parameters);

    /**
     * 任务完成后置方法<br>
     * 在{@link #onDoWork(Object[])}之后被调用，
     * 运行于当前线程，
     * 本方法默认会被{@link #onStopWork(boolean , String , Object)}调用，
     * 如果你重写该方法则不应该再重写{@link #onStopWork(boolean , String , Object)}，
     * 否则可能导致本方法不能被执行
     *
     * @param state  任务执行结果
     * @param result 结果数据对象
     */
    protected void onStopWork(boolean state, Result result) {
    }

    /**
     * 任务完成后置方法<br>
     * 在{@link #onDoWork(Object[])}之后被调用，
     * 运行于当前线程，
     * 本方法会调用{@link #onStopWork(boolean , Object)}，
     * 如果你重写该方法则不必重写{@link #onStopWork(boolean , Object)}，
     * 且不必在本方法中调用super.onStopWork(boolean , Object)
     *
     * @param state   任务执行结果
     * @param message 结果消息
     * @param result  结果数据对象
     */
    @Override
    protected void onStopWork(boolean state, String message, Result result) {
        Log.i(LOG_TAG + "onStopWork", "onStopWork(boolean , Object) is invoked");
        onStopWork(state, result);
    }

    /**
     * 任务被取消时调用<br>
     * 在{@link #onDoWork(Object[])}之后被调用，
     * 如果在{@link #onDoWork(Object[])}中任务被取消则该方法会被调用，
     * 且后续不再执行{@link #onStopWork(boolean , String , Object)}和
     * {@link #setWorkEndListener(IWorkEndListener)}中定义的任务完成回调接口，
     * 但会执行{@link #setWorkCancelledListener(IWorkEndListener)}中定义的任务取消回调接口。<br>
     * 本方法默认会被{@link #onCancelWork(boolean , String , Object)}调用，
     * 如果你重写该方法则不应该再重写{@link #onCancelWork(boolean , String , Object)}，
     * 否则可能导致本方法不能被执行
     *
     * @param state  任务执行结果
     * @param result 任务结果数据
     */
    protected void onCancelWork(boolean state, Result result) {
    }

    /**
     * 任务被取消时调用<br>
     * 在{@link #onDoWork(Object[])}之后被调用，
     * 如果在{@link #onDoWork(Object[])}中任务被取消则该方法会被调用，
     * 且后续不再执行{@link #onStopWork(boolean , String , Object)}和
     * {@link #setWorkEndListener(IWorkEndListener)}中定义的任务完成回调接口，
     * 但会执行{@link #setWorkCancelledListener(IWorkEndListener)}中定义的任务取消回调接口。<br>
     * 本方法会调用{@link #onCancelWork(boolean , Object)}，
     * 如果你重写该方法则不必重写{@link #onCancelWork(boolean , Object)}，
     * 且不必在本方法中调用super.onCancelWork(boolean , Object)，
     *
     * @param state   任务执行结果
     * @param message 结果消息
     * @param result  任务结果数据
     */
    protected void onCancelWork(boolean state, String message, Result result) {
        Log.i(LOG_TAG + "onCancelWork", "onCancelWork(boolean , Object) is invoked");
        onCancelWork(state, result);
    }

    /**
     * 设置任务被取消时的回调接口<br>
     * 在任务执行被取消后调用，
     * 运行于当前线程
     *
     * @param workCancelledListener 监听器对象
     */
    public final void setWorkCancelledListener(IWorkEndListener<Result> workCancelledListener) {
        this.workCancelledListener = workCancelledListener;
    }

    /**
     * 设置任务完成时的回调接口<br>
     * 在任务执行完成后被回调，
     * 运行于当前线程
     *
     * @param workEndListener 监听器对象
     */
    public final void setWorkEndListener(IWorkEndListener<Result> workEndListener) {
        this.workEndListener = workEndListener;
    }

    /**
     * 设置任务的进度更新回调接口<br>
     * 当使用{@link #beginExecute(Object[])}方法启动任务时该接口有效，
     * 在异步任务中实时更新任务执行进度，
     * 运行于当前线程
     *
     * @param progressUpdateListener 监听器对象
     */
    public final void setProgressUpdateListener(ProgressUpdate progressUpdateListener) {
        this.progressUpdateListener = progressUpdateListener;
    }
}
