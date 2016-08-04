package org.mobile.library.model.operate;
/**
 * Created by 超悟空 on 2015/3/4.
 */

/**
 * 带有传入数据的数据改变监听器，
 * 用于在数据改变时，
 * 或需要特定数据的某些事件被触发时，
 * 接收数据改变通知并执行特定回调任务的接口
 *
 * @param <DataType> 要传递的数据类型
 *
 * @author 超悟空
 * @version 1.0 2015/3/4
 * @since 1.0
 */
public interface DataChangeListener<DataType> {

    /**
     * 数据改变
     *
     * @param data 改变后的数据
     */
    void onDataChange(DataType data);
}
