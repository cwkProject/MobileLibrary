package org.mobile.network.factory;
/**
 * Created by 超悟空 on 2015/4/18.
 */

/**
 * 网络工具类型枚举
 *
 * @author 超悟空
 * @version 1.0 2015/4/18
 * @since 1.0
 */
public enum NetworkType {

    /**
     * http post类型的请求
     */
    HTTP_POST,
    /**
     * http get类型的请求，
     * 该枚举创建的连接器已被废弃，
     * 请使用{@link #HTTP_CONNECTION_GET}创建新的连接工具
     */
    @Deprecated HTTP_GET,

    /**
     * http get类型请求
     */
    HTTP_CONNECTION_GET,
    /**
     * 下载
     */
    DOWNLOAD,
    /**
     * 上传
     */
    UPDATE
}
