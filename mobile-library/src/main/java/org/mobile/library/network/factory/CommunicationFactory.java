package org.mobile.library.network.factory;
/**
 * Created by 超悟空 on 2015/4/23.
 */

import org.mobile.library.network.communication.OkHttpGetAsyncCommunication;
import org.mobile.library.network.communication.OkHttpGetSyncCommunication;
import org.mobile.library.network.communication.OkHttpPostAsyncCommunication;
import org.mobile.library.network.communication.OkHttpPostSyncCommunication;
import org.mobile.library.network.communication.OkHttpUploadAsyncCommunication;
import org.mobile.library.network.communication.OkHttpUploadSyncCommunication;
import org.mobile.library.network.util.AsyncCommunication;
import org.mobile.library.network.util.SyncCommunication;


/**
 * 通讯对象工厂
 *
 * @author 超悟空
 * @version 2.0 2015/11/2
 * @since 1.0
 */
public class CommunicationFactory {

    /**
     * 创建同步通讯工具对象
     *
     * @param networkType 网络工具类型
     *
     * @return 初始化完成的通讯工具
     */
    public static SyncCommunication CreateSyncCommunication(NetworkType networkType) {

        switch (networkType) {
            case HTTP_GET:
                // HttpGet请求对象
                return new OkHttpGetSyncCommunication();
            case HTTP_POST:
                // HttpPost请求对象
                return new OkHttpPostSyncCommunication();
            case UPLOAD:
                // HttpUpload请求对象
                return new OkHttpUploadSyncCommunication();
            default:
                throw new UnsupportedOperationException("network protocol not implemented");
        }
    }

    /**
     * 创建异步通信工具
     *
     * @param networkType 网络工具类型
     *
     * @return 初始化完成的通讯工具
     */
    public static AsyncCommunication CreateAsyncCommunication(NetworkType networkType) {
        switch (networkType) {
            case HTTP_GET:
                // HttpGet请求对象
                return new OkHttpGetAsyncCommunication();
            case HTTP_POST:
                // HttpPost请求对象
                return new OkHttpPostAsyncCommunication();
            case UPLOAD:
                // HttpUpload请求对象
                return new OkHttpUploadAsyncCommunication();
            default:
                throw new UnsupportedOperationException("network protocol not implemented");
        }
    }
}
