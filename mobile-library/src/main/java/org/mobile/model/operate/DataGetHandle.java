package org.mobile.model.operate;
/**
 * Created by 超悟空 on 2015/3/5.
 */

/**
 * 用于向数据提供对象索要数据的接口
 *
 * @param <DataType> 需要的数据类型
 *
 * @author 超悟空
 * @version 1.0 2015/3/5
 * @since 1.0
 */
public interface DataGetHandle<DataType> {

    /**
     * 取得数据
     *
     * @return 数据提供者整理的数据
     */
    public DataType getData();
}
