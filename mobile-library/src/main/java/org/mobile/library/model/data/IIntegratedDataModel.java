package org.mobile.library.model.data;
/**
 * Created by 超悟空 on 2016/7/23.
 */

import java.util.Map;

/**
 * 进一步简化的数据模型接口，集成化数据模型
 *
 * @author 超悟空
 * @version 1.0 2016/7/23
 * @since 1.0
 */
public interface IIntegratedDataModel<Parameters, Result, Response, Request extends Map<String,
        ?>> extends IDefaultDataModel<Response, Request> {

    /**
     * 设置任务传入参数
     *
     * @param parameters 参数
     */
    @SuppressWarnings("unchecked")
    void setParameters(Parameters... parameters);

    /**
     * 获取处理完成的响应结果
     *
     * @return 响应数据
     */
    Result getResult();
}
