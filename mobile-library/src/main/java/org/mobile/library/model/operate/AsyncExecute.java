package org.mobile.library.model.operate;
/**
 * Created by 超悟空 on 2015/10/29.
 */

/**
 * 任务异步执行接口
 *
 * @param <Parameters> 任务传入参数类型
 *
 * @author 超悟空
 * @version 1.0 2015/10/29
 * @since 1.0
 */
public interface AsyncExecute<Parameters> {

    /**
     * 执行任务操作，运行于新线程
     *
     * @param parameters 任务所需参数
     */
    void beginExecute(Parameters... parameters);
}
