package org.mobile.model.work;
/**
 * Created by 超悟空 on 2015/1/7.
 */

import android.os.AsyncTask;
import android.util.Log;

/**
 * 基础功能处理模型，用于执行功能逻辑
 *
 * @param <Parameters> 功能所需参数类型
 * @param <Result>     结果数据类型
 *
 * @author 超悟空
 * @version 1.0 2015/1/7
 * @since 1.0
 */
public abstract class WorkModel<Parameters, Result> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "WorkModel.";

    /**
     * 结果数据对象
     */
    private Result result = null;

    /**
     * 功能回调接口
     */
    private WorkBack<Result> workBackListener = null;

    /**
     * 功能的进度更新回调接口，默认不做任何事
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
     * 执行功能操作，运行于UI线程
     *
     * @param parameters 功能所需参数
     *
     * @return 执行结果
     */
    @SafeVarargs
    public final boolean execute(Parameters... parameters) {
        Log.i(LOG_TAG + "execute", "execute start");
        // 用于保存执行结果
        boolean state = false;

        try {
            Log.i(LOG_TAG + "execute", "onStartWork() is invoked");
            // 执行前导任务
            onStartWork();

            Log.i(LOG_TAG + "execute", "onDoWork(Object[]) is invoked");
            // 执行核心任务
            state = onDoWork(parameters);
        } catch (Exception e) {
            state = false;
            Log.e(LOG_TAG + "execute", "exception is " + e.getMessage());
        } finally {
            Log.i(LOG_TAG + "execute", "onStopWork(boolean, Object) is invoked");
            // 执行后继任务
            onStopWork(state, getResult());
        }

        // 如果设置了回调接口则执行回调方法
        if (this.workBackListener != null) {
            Log.i(LOG_TAG + "execute", "workBackListener.doEndWork(boolean, Object) is invoked");
            this.workBackListener.doEndWork(state, getResult());
        }

        Log.i(LOG_TAG + "execute", "execute end");
        return state;
    }

    /**
     * 执行功能操作的异步方法，基于异步任务实现
     *
     * @param parameters 功能所需参数
     */
    @SafeVarargs
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
            Log.i(LOG_TAG + "WorkModelAsyncTask.onCancelled", "onCancelWork() is invoked");
            // 取消任务回调
            onCancelWork();
        }

        @Override
        protected void onPreExecute() {
            Log.i(LOG_TAG + "WorkModelAsyncTask.onPreExecute", "onStartWork() is invoked");
            // 执行前导任务
            onStartWork();
        }

        @Override
        protected void onPostExecute(Result result) {
            Log.i(LOG_TAG + "WorkModelAsyncTask.onPostExecute", "onStopWork(boolean,Object) is invoked");
            // 执行后继任务
            onStopWork(state, result);

            // 如果设置了回调接口则执行回调方法
            if (workBackListener != null) {
                Log.i(LOG_TAG + "WorkModelAsyncTask.onPostExecute", "workBackListener.doEndWork(boolean,Object) is invoked");
                workBackListener.doEndWork(state, getResult());
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // 如果设置了更新接口则会执行有效操作
            progressUpdateListener.onProgressUpdate(values);
        }
    }

    /**
     * 获取执行结果数据，运行于UI线程
     *
     * @return 结果数据
     */
    public final Result getResult() {
        Log.i(LOG_TAG + "getResult", "getResult() is invoked");
        return result;
    }

    /**
     * 设置结果数据
     *
     * @param result 结果数据对象
     */
    protected final void setResult(Result result) {
        Log.i(LOG_TAG + "setResult", "setResult(Object) is invoked");
        this.result = result;
    }

    /**
     * 判断任务是否被取消，
     * 只有异步调用有效，
     * 即通过{@link #beginExecute(Object[])}启动的任务
     *
     * @return true表示已被取消，
     * 通过{@link #execute(Object[])}启动的任务将永远返回false
     */
    public final boolean isCancelled() {
        return work != null && work.isCancelled();
    }

    /**
     * 当任务通过{@link #beginExecute(Object[])}启动时，
     * 调用此方法会停止任务
     */
    public final void cancel() {
        Log.i(LOG_TAG + "cancel", "cancel() is invoked");
        if (work != null && work.getStatus() == AsyncTask.Status.RUNNING) {
            work.cancel(true);
        }
    }

    /**
     * 在{@link #onDoWork(Object[])}之前被调用，运行于UI线程
     */
    protected void onStartWork() {
    }

    /**
     * 用于执行功能逻辑核心方法，
     * 如果通过{@link #beginExecute(Object[])}激活功能，
     * 此方法将在独立线程中执行，
     * 要返回的结果数据需在此方法中调用{@link #setResult(Object)}方法进行赋值，
     * 否则你将得到null引用结果
     *
     * @param parameters 功能传入参数
     *
     * @return 执行结果
     */
    @SuppressWarnings("unchecked")
    protected abstract boolean onDoWork(Parameters... parameters);

    /**
     * 在{@link #onDoWork(Object[])}之后被调用，运行于UI线程
     *
     * @param state  任务执行结果
     * @param result 结果数据对象
     */
    protected void onStopWork(boolean state, Result result) {
    }

    /**
     * 取消任务时回调
     */
    protected void onCancelWork() {

    }

    /**
     * 设置功能的回调接口，在功能执行结束后被回调，运行于UI线程
     *
     * @param workBackListener 监听器对象
     */
    public final void setWorkBackListener(WorkBack<Result> workBackListener) {
        this.workBackListener = workBackListener;
    }

    /**
     * 设置功能的进度更新回调接口，在异步任务中实时回调，运行于UI线程
     *
     * @param progressUpdateListener 监听器对象
     */
    public final void setProgressUpdateListener(ProgressUpdate progressUpdateListener) {
        this.progressUpdateListener = progressUpdateListener;
    }
}
