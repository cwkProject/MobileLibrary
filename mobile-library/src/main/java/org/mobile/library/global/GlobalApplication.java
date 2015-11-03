package org.mobile.library.global;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.squareup.okhttp.OkHttpClient;

import org.mobile.library.R;

import java.util.concurrent.TimeUnit;

/**
 * 全局Application，用于在任意位置使用应用程序资源
 *
 * @author 超悟空
 * @version 2.0 2015/10/30
 * @since 1.0
 */
public class GlobalApplication extends Application {

    /**
     * 自身静态全局实例
     */
    private static GlobalApplication globalApplication = null;

    /**
     * 全局网络请求工具
     */
    private OkHttpClient okHttpClient = null;

    /**
     * 全局持久化配置对象
     */
    private ApplicationConfig applicationConfig = null;

    /**
     * 应用相关参数
     */
    private ApplicationAttribute applicationAttribute = null;

    /**
     * 保存当前应用版本信息的数据
     */
    private ApplicationVersion applicationVersion = null;

    /**
     * 登录状态数据
     */
    private LoginStatus loginStatus = null;

    /**
     * 全局UI线程Handler
     */
    private Handler handler = null;

    /**
     * 获取全局GlobalApplication对象
     *
     * @return 返回GlobalApplication对象
     */
    public static GlobalApplication getGlobal() {
        return globalApplication;
    }

    /**
     * 获取全局网络连接对象
     *
     * @return 带默认设置的OkHttpClient对象
     */
    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    /**
     * 获取全局持久化配置对象
     *
     * @return 全局持久化配置对象
     */
    public ApplicationConfig getApplicationConfig() {
        return applicationConfig;
    }

    /**
     * 获取全局应用相关参数
     *
     * @return 返回应用相关参数对象
     */
    public ApplicationAttribute getApplicationAttribute() {
        return applicationAttribute;
    }

    /**
     * 获取全局应用版本信息参数
     *
     * @return 返回应用版本信息对象
     */
    public ApplicationVersion getApplicationVersion() {
        return applicationVersion;
    }

    /**
     * 获取登录状态数据
     *
     * @return 登录状态数据数据对象
     */
    public LoginStatus getLoginStatus() {
        return loginStatus;
    }

    /**
     * 获取全局UI线程Handler
     *
     * @return UI线程Handler
     */
    public Handler getUiHandler() {
        return handler;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        globalApplication = this;
        applicationConfig = new ApplicationConfig(this);
        applicationAttribute = new ApplicationAttribute();
        applicationVersion = new ApplicationVersion();
        loginStatus = new LoginStatus();
        handler = new Handler(Looper.getMainLooper());
        okHttpClient = new OkHttpClient();
        // 设置默认连接超时时间
        okHttpClient.setConnectTimeout(getResources().getInteger(R.integer
                .http_default_connect_timeout), TimeUnit.MILLISECONDS);

        // 设置默认读取超时时间
        okHttpClient.setReadTimeout(getResources().getInteger(R.integer
                .http_default_read_timeout), TimeUnit.MILLISECONDS);
    }
}
