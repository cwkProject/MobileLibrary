package org.mobile.library.network.factory;
/**
 * Created by 超悟空 on 2015/4/23.
 */

import org.mobile.library.network.util.NetworkProgressListener;


/**
 * 通讯对象工厂
 *
 * @author 超悟空
 * @version 2.0 2015/11/2
 * @since 1.0
 */
public class CommunicationFactory {



    public class Builder {

        /**
         * 网络工具类型
         */
        private NetworkType networkType = NetworkType.GET;

        /**
         * 进度监听器，仅上传和下载时有效
         */
        private NetworkProgressListener progressListener = null;

        /**
         * 请求超时时间
         */
        private int connectTimeout = -1;

        /**
         * 读取超时时间
         */
        private int readTimeout = -1;

        /**
         * 写入超时时间
         */
        private int writeTimeout = -1;


    }
}
