package org.mobile.library.network.util;
/**
 * Created by 超悟空 on 2015/10/30.
 */

import org.mobile.library.model.operate.Cancelable;

/**
 * 与服务器通讯异步模式执行接口
 *
 * @param <RequestType>  请求数据类型
 * @param <ResponseType> 接收数据类型
 *
 * @author 超悟空
 * @version 1.0 2015/10/30
 * @since 1.0
 */
public interface AsyncCommunication<RequestType, ResponseType> extends Cancelable {

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
     * @param callback 结果回调
     */
    void Request(RequestType sendData, NetworkCallback<ResponseType> callback);
}
