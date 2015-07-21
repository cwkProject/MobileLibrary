package org.mobile.model.work;
/**
 * Created by 超悟空 on 2015/1/7.
 */

/**
 * 功能处理结束后的回调接口
 *
 * @param <Result> 功能返回的结果数据类型
 *
 * @author 超悟空
 * @version 1.0 2015/1/7
 * @since 1.0
 */
public interface WorkBack<Result> {

    /**
     * 回调方法，在功能函数执行后被调用
     *
     * @param state 任务执行结果
     * @param data  结果数据
     */
    public void doEndWork(boolean state, Result data);
}
