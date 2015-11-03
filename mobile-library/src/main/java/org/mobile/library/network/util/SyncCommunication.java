package org.mobile.library.network.util;

import org.mobile.library.model.operate.Cancelable;

/**
 * 与服务器通讯同步模式执行接口
 *
 * @param <RequestType>  请求数据类型
 * @param <ResponseType> 接收数据类型
 *
 * @author 超悟空
 * @version 2.0 2015/10/30
 * @since 1.0
 */
public interface SyncCommunication<RequestType, ResponseType> extends Cancelable {

    /**
     * 设置请求的任务名
     *
     * @param uri 任务名字符串，如url，方法名等
     */
    void setTaskName(String uri);

    /**
     * 向服发送请求
     *
     * @param sendData 向服务器发送的数据
     */
    void Request(RequestType sendData);

    /**
     * 判断请求是否成功
     *
     * @return true表示请求成功
     */
    boolean isSuccessful();

    /**
     * 接收服务器响应的数据
     *
     * @return 服务器返回的数据
     */
    ResponseType Response();

    /**
     * 关闭网络连接
     */
    void close();
}
