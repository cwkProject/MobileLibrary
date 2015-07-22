package org.mobile.library.util;
/**
 * Created by 超悟空 on 2015/4/23.
 */

/**
 * 存放类库使用的全局静态常量
 *
 * @author 超悟空
 * @version 1.0 2015/4/23
 * @since 1.0
 */
public interface StaticValueUtil {

    /**
     * 设备类型
     */
    String DEVICE_TYPE = "Android";

    /**
     * 应用程序默认使用的配置文件名
     */
    String APPLICATION_CONFIG_FILE_NAME = "app_system_config";

    /**
     * 应用升级下载文件的ID在配置文件中保存的标签
     */
    String UPDATE_APP_FILE_ID_TAG = "update_app_file_id_tag";

    /**
     * 应用登录默认的get服务请求地址
     */
    String LOGIN_URL = "http://218.92.115.55/MobilePlatform/Login.aspx";

    /**
     * 应用升级默认的get服务请求地址
     */
    String UPDATE_REQUEST_URL = "http://218.92.115.55/mobileplatform/Update.aspx";
}
