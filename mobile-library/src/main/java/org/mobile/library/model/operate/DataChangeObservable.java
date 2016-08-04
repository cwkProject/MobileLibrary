package org.mobile.library.model.operate;
/**
 * Created by 超悟空 on 2016/7/22.
 */

/**
 * 数据改变通知者
 *
 * @author 超悟空
 * @version 1.0 2015/3/4
 * @since 1.0
 */
public interface DataChangeObservable<DataType> {

    /**
     * 设置数据改变监听器
     *
     * @param listener 监听器对象
     */
    void setOnDataChangeListener(DataChangeListener<DataType> listener);
}
