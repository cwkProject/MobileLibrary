package org.mobile.library.model.work;
/**
 * Created by 超悟空 on 2015/1/7.
 */

/**
 * 功能处理结束后的回调接口，
 * 隐藏了结果消息回传参数
 *
 * @param <Result> 功能返回的结果数据类型
 *
 * @author 超悟空
 * @version 2.0 2015/10/16
 * @since 1.0
 */
public abstract class WorkBack<Result> implements IWorkEndListener<Result> {

    /**
     * 任务结束回调方法，
     * 在任务函数执行结束后被调用
     *
     * @param state  任务执行结果
     * @param result 结果数据
     */
    public abstract void doEndWork(boolean state, Result result);

    @Override
    public final void doEndWork(boolean state, String message, Result result) {
        doEndWork(state, result);
    }
}
