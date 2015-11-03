package org.mobile.library.model.data;
/**
 * Created by 超悟空 on 2015/7/1.
 */

import java.util.Map;

/**
 * 常用的默认数据模型接口
 *
 * @param <Response> 要解析的结果数据类型
 * @param <Request>  要序列化的目标类型
 *
 * @author 超悟空
 * @version 2.0 2015/11/3
 * @since 1.0
 */
public interface IDefaultDataModel<Response, Request extends Map<String, ?>> extends
        IDataModel<Response, Request> {

    /**
     * 判断本次服务请求是否成功
     *
     * @return true表示成功，false表示失败
     */
    boolean isSuccess();

    /**
     * 获取本次请求返回的结果消息
     *
     * @return 消息字符串
     */
    String getMessage();
}
