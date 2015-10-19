package org.mobile.library.model.work;
/**
 * Created by 超悟空 on 2015/10/16.
 */

/**
 * 任务结束回调接口
 *
 * @author 超悟空
 * @version 1.0 2015/10/16
 * @since 1.0
 */
public interface IWorkEndListener<Result> {

    /**
     * 任务结束回调方法，
     * 在任务函数执行结束后被调用
     *
     * @param state   任务执行结果
     * @param message 结果消息
     * @param result  结果数据
     */
    void doEndWork(boolean state, String message, Result result);
}
