package org.mobile.library.model.work;
/**
 * Created by 超悟空 on 2015/10/29.
 */

import android.util.Log;

/**
 * 任务流程的基本模型
 *
 * @param <Parameters> 任务所需参数类型
 * @param <Result>     结果数据类型
 *
 * @author 超悟空
 * @version 1.0 2015/10/29
 * @since 1.0
 */
public abstract class WorkProcessModel<Parameters, Result> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "WorkProcessModel.";

    /**
     * 结果数据对象
     */
    private Result result = null;

    /**
     * 执行结果信息
     */
    private String message = null;

    /**
     * 任务启动前置方法<br>
     * 在{@link #onDoWork(Object[])}之前被调用，
     * 运行于当前线程
     */
    protected void onStartWork() {
    }

    /**
     * 任务逻辑核心方法<br>
     * 任务主要逻辑应该在该方法中被实现，
     * 并且在该方法中通过调用{@link #setResult(Object)}设置任务结果回传数据，
     * 调用{@link #setMessage(String)}设置任务结果回传信息，
     * 并且方法返回任务执行结果。
     *
     * @param parameters 任务传入参数
     *
     * @return 执行结果
     */
    @SuppressWarnings("unchecked")
    protected abstract boolean onDoWork(Parameters... parameters);

    /**
     * 获取任务执行结果数据<br>
     * 该结果应该在{@link #onDoWork(Object[])}中调用{@link #setResult(Object)}进行赋值，
     * 否则你将会获取null引用
     *
     * @return 结果数据
     */
    public final Result getResult() {
        return result;
    }

    /**
     * 设置任务结果数据<br>
     * 应该在{@link #onDoWork(Object[])}中设置结果
     *
     * @param result 结果数据对象
     */
    protected final void setResult(Result result) {
        Log.i(LOG_TAG + "setResult", "result is " + result);
        this.result = result;
    }

    /**
     * 获取任务结果消息<br>
     * 该消息应该在{@link #onDoWork(Object[])}中调用{@link #setMessage(String)}进行赋值，
     * 否则你将会获取null引用
     *
     * @return 结果消息
     */
    public final String getMessage() {
        return message;
    }

    /**
     * 设置任务结果消息<br>
     * 应该在{@link #onDoWork(Object[])}中设置消息
     *
     * @param message 结果消息
     */
    protected final void setMessage(String message) {
        Log.i(LOG_TAG + "setMessage", "message is " + message);
        this.message = message;
    }

    /**
     * 任务完成后置方法<br>
     * 在{@link #onDoWork(Object[])}之后被调用
     *
     * @param state   任务执行结果
     * @param message 结果消息
     * @param result  结果数据对象
     */
    protected void onStopWork(boolean state, String message, Result result) {
    }
}
