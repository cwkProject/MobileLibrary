package org.mobile.library.global;
/**
 * Created by 超悟空 on 2015/4/23.
 */

/**
 * 存放类库使用的全局静态常量
 *
 * @author 超悟空
 * @version 2.0 2016/3/19
 * @since 1.0
 */
public interface ApplicationStaticValue {

    /**
     * 应用程序配置
     */
    interface AppConfig {
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
    }

    /**
     * 网络请求地址
     */
    interface Url {
        /**
         * 应用登录默认的服务请求地址
         */
        String LOGIN_URL = "http://218.92.115.55/M_Platform/Entrance/Login.aspx";

        /**
         * 应用注册默认的服务请求地址
         */
        String REGISTER_URL = "http://218.92.115.55/M_Platform/Entrance/Register.aspx";

        /**
         * 应用升级默认的get服务请求地址
         */
        String UPDATE_REQUEST_URL = "http://218.92.115.55/mobileplatform/Update.aspx";

        /**
         * 发送手机验证码
         */
        String SEND_MOBILE_VERIFICATION_CODE_URL = "http://218.92.115" + "" +
                ".55/M_Platform/Entrance/GetMobileAuthCode.aspx";

        /**
         * 验证手机
         */
        String VERIFY_MOBILE_URL = "http://218.92.115.55/M_Platform/Entrance/VerifyAuthCode" +
                ".aspx";
    }

    /**
     * 广播动作
     */
    interface BroadcastAction {
        /**
         * 应用版本状态
         */
        String APPLICATION_VERSION_STATE = "org.mobile.library:app_version";

        /**
         * 登录状态
         */
        String LOGIN_STATE = "org.mobile.library:login";

        /**
         * 用户信息状态
         */
        String USER_INFO_STATE = "org.mobile.library:user_info";
    }
}
