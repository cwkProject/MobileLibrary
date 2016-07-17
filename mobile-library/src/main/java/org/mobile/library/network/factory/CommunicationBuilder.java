package org.mobile.library.network.factory;
/**
 * Created by 超悟空 on 2015/4/23.
 */

import org.mobile.library.network.communication.Communication;
import org.mobile.library.network.communication.OkHttpDownloadCommunication;
import org.mobile.library.network.communication.OkHttpGetCommunication;
import org.mobile.library.network.communication.OkHttpPostCommunication;
import org.mobile.library.network.communication.OkHttpUploadCommunication;
import org.mobile.library.network.util.NetworkProgressListener;
import org.mobile.library.network.util.NetworkRefreshProgressHandler;
import org.mobile.library.network.util.NetworkTimeout;


/**
 * 通讯对象构造器
 *
 * @author 超悟空
 * @version 2.0 2015/11/2
 * @since 1.0
 */
public class CommunicationBuilder {

    /**
     * 网络工具请求类型
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

    /**
     * 请求编码
     */
    private String encoded = null;

    /**
     * 请求地址
     */
    private String url = null;

    /**
     * 新建网络工具构造器，默认为GET请求工具
     */
    public CommunicationBuilder() {
    }

    /**
     * 新建网络工具构造器
     *
     * @param networkType 网络请求类型
     */
    public CommunicationBuilder(NetworkType networkType) {
        this.networkType = networkType;
    }

    /**
     * 设置进度监听器，仅在上传和下载中有效
     *
     * @param progressListener 监听器对象
     *
     * @return 构造器
     */
    public CommunicationBuilder networkRefreshProgressListener(NetworkProgressListener
                                                                       progressListener) {
        this.progressListener = progressListener;
        return this;
    }

    /**
     * 设置连接超时时间
     *
     * @param timeout 超时时间，单位毫秒
     *
     * @return 构造器
     */
    public CommunicationBuilder connectTimeout(int timeout) {
        this.connectTimeout = timeout;
        return this;
    }

    /**
     * 设置读取超时时间
     *
     * @param timeout 超时时间，单位毫秒
     *
     * @return 构造器
     */
    public CommunicationBuilder readTimeout(int timeout) {
        this.readTimeout = timeout;
        return this;
    }

    /**
     * 设置写入超时时间
     *
     * @param timeout 超时时间，单位毫秒
     *
     * @return 构造器
     */
    public CommunicationBuilder writeTimeout(int timeout) {
        this.writeTimeout = timeout;
        return this;
    }

    /**
     * 设置请求编码，默认为utf-8
     *
     * @param encoded 编码
     *
     * @return 构造器
     */
    public CommunicationBuilder encoded(String encoded) {
        this.encoded = encoded;
        return this;
    }

    /**
     * 设置请求地址
     *
     * @param url 请求地址
     *
     * @return 构造器
     */
    public CommunicationBuilder url(String url) {
        this.url = url;
        return this;
    }

    /**
     * 构造网络请求工具
     *
     * @return OKHttp网络请求工具
     */
    public Communication build() {

        Communication communication;

        switch (networkType) {
            case GET:
                communication = new OkHttpGetCommunication();
                break;
            case POST:
                communication = new OkHttpPostCommunication();
                break;
            case UPLOAD:
                communication = new OkHttpUploadCommunication();
                break;
            case DOWNLOAD:
                communication = new OkHttpDownloadCommunication();
                break;
            default:
                throw new IllegalArgumentException("error networkType");
        }

        if (connectTimeout + readTimeout + writeTimeout > -3) {
            // 需要设置时间

            NetworkTimeout networkTimeout = new NetworkTimeout();

            networkTimeout.setConnectTimeout(connectTimeout);
            networkTimeout.setReadTimeout(readTimeout);
            networkTimeout.setWriteTimeout(writeTimeout);

            communication.setNetworkTimeout(networkTimeout);
        }

        if (progressListener != null && communication instanceof NetworkRefreshProgressHandler) {
            // 需要设置监听器

            NetworkRefreshProgressHandler refreshProgressHandler =
                    (NetworkRefreshProgressHandler) communication;

            refreshProgressHandler.setNetworkProgressListener(progressListener);
        }

        communication.setEncoded(encoded);
        communication.setTaskName(url);

        return communication;
    }
}
