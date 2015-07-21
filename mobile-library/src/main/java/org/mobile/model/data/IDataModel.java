package org.mobile.model.data;
/**
 * Created by 超悟空 on 2015/1/6.
 */

/**
 * 基础数据模型接口，用于存放功能组件的各种数据，提供必要的数据处理功能
 *
 * @param <Response> 要解析的结果数据类型
 * @param <Request>  要序列化的目标类型
 *
 * @author 超悟空
 * @version 1.0 2015/1/6
 * @since 1.0
 */
public interface IDataModel<Response, Request> {
    /**
     * 序列化要提交的数据,用于与服务器交互
     *
     * @return 返回Map集合
     */
    Request serialization();

    /**
     * 解析传回的数据
     *
     * @param response 要解析的数据
     */
    boolean parse(Response response);

}
