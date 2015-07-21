package org.mobile.network.communication;

/**
 * 与服务器通讯接口
 *
 * @param <RequestType>  请求数据类型
 * @param <ResponseType> 接收数据类型
 *
 * @author 超悟空
 * @version 1.0 2015/1/6
 * @since 1.0
 */
public interface ICommunication<RequestType, ResponseType> {

    /**
     * 设置请求的任务名
     *
     * @param uri 任务名字符串，如url，方法名等
     */
    void setTaskName(String uri);

    /**
     * 向服发送请求
     *
     * @param sendData 向服务器发送请求时附加的数据
     */
    void Request(RequestType sendData);

    /**
     * 接收服务器响应的数据
     *
     * @return 服务器返回的数据
     */
    ResponseType Response();

    /**
     * 关闭对象
     */
    void Close();
}
