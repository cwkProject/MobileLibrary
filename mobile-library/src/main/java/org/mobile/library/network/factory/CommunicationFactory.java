package org.mobile.library.network.factory;
/**
 * Created by 超悟空 on 2015/4/23.
 */

import org.mobile.library.R;
import org.mobile.library.network.communication.HttpURLConnectionGetCommunication;
import org.mobile.library.network.communication.HttpURLConnectionPostCommunication;
import org.mobile.library.network.communication.HttpURLConnectionUploadCommunication;
import org.mobile.library.network.util.SyncCommunication;
import org.mobile.library.global.GlobalApplication;


/**
 * 通讯对象工厂
 *
 * @author 超悟空
 * @version 1.1 2015/10/13
 * @since 1.0
 */
public class CommunicationFactory {

    private static int POST_TIMEOUT = GlobalApplication.getGlobal().getResources().getInteger(R
            .integer.http_post_default_timeout);

    private static int GET_TIMEOUT = GlobalApplication.getGlobal().getResources().getInteger(R.integer
            .http_get_default_timeout);

    private static int POST_READ_TIMEOUT = GlobalApplication.getGlobal().getResources().getInteger(R
            .integer.http_post_default_read_timeout);

    private static int GET_READ_TIMEOUT = GlobalApplication.getGlobal().getResources().getInteger(R
            .integer.http_get_default_read_timeout);

    private static int UPLOAD_TIMEOUT = GlobalApplication.getGlobal().getResources().getInteger(R
            .integer.http_upload_default_timeout);

    /**
     * 创建通讯工具对象
     *
     * @param networkType 网络工具类型
     *
     * @return 初始化完成的通讯工具
     */
    public static SyncCommunication CreateSyncCommunication(NetworkType networkType) {

        switch (networkType) {
            case HTTP_GET:
                // HttpGet请求对象
                return initHttpURLConnectionGetCommunication();
            case HTTP_POST:
                // HttpPost请求对象
                return initHttpURLConnectionPostCommunication();
            case UPLOAD:
                // HttpUpload请求对象
                return initHttpURLConnectionUploadCommuniction();
            default:
                throw new UnsupportedOperationException("network protocol not implemented");
        }
    }

    /**
     * 初始化Http Post请求对象
     *
     * @return 初始化完成的Http Post对象
     */
    private static HttpURLConnectionPostCommunication initHttpURLConnectionPostCommunication() {
        // 新建HttpPost请求对象
        HttpURLConnectionPostCommunication httpURLConnectionPostCommunication = new
                HttpURLConnectionPostCommunication();

        // 设置超时时间
        httpURLConnectionPostCommunication.setTimeout(POST_TIMEOUT);
        httpURLConnectionPostCommunication.setReadTimeout(POST_READ_TIMEOUT);

        return httpURLConnectionPostCommunication;
    }

    /**
     * 初始化Http Get请求对象
     *
     * @return 初始化完成的Http Get对象
     */
    private static HttpURLConnectionGetCommunication initHttpURLConnectionGetCommunication() {
        // 新建HttpGet请求对象
        HttpURLConnectionGetCommunication httpURLConnectionGetCommunication = new
                HttpURLConnectionGetCommunication();

        // 设置超时时间
        httpURLConnectionGetCommunication.setTimeout(GET_TIMEOUT);
        httpURLConnectionGetCommunication.setReadTimeout(GET_READ_TIMEOUT);

        return httpURLConnectionGetCommunication;
    }

    /**
     * 初始化Http Upload请求对象
     *
     * @return 初始化完成的Http Upload对象
     */
    private static HttpURLConnectionUploadCommunication initHttpURLConnectionUploadCommuniction() {
        // 新建HttpUpload请求对象
        HttpURLConnectionUploadCommunication httpURLConnectionUploadCommunication = new
                HttpURLConnectionUploadCommunication();

        // 设置超时时间
        httpURLConnectionUploadCommunication.setTimeout(UPLOAD_TIMEOUT);

        return httpURLConnectionUploadCommunication;
    }
}
