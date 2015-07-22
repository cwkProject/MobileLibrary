package org.mobile.library.model.operate;
/**
 * Created by 超悟空 on 2015/4/20.
 */

/**
 * 无参数的观察者，
 * 用于执行无传入参数的特定回调任务的接口
 *
 * @author 超悟空
 * @version 1.0 2015/4/20
 * @since 1.0
 */
public interface EmptyParameterObserver {

    /**
     * 执行回调操作
     */
    void invoke();
}
