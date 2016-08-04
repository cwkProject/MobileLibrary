package org.mobile.library.global;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.mobile.library.BuildConfig;
import org.mobile.library.R;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 全局对象，用于在任意位置使用应用程序资源
 *
 * @author 超悟空
 * @version 3.0 2016/3/7
 * @since 1.0
 */
public class Global {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "Global.";

    /**
     * 自身静态全局实例
     */
    private static Global global = null;

    /**
     * 全局上下文实例
     */
    private Context context = null;

    /**
     * 全局网络请求工具
     */
    private OkHttpClient okHttpClient = null;

    /**
     * 全局持久化配置对象
     */
    private ApplicationConfig applicationConfig = null;

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
     * 获取全局Context对象
     *
     * @return 返回Context对象
     */
    public static Context getContext() {
        return global.context;
    }

    /**
     * 获取全局网络连接对象
     *
     * @return 带默认设置的OkHttpClient对象
     */
    public static OkHttpClient getOkHttpClient() {
        return global.okHttpClient;
    }

    /**
     * 获取全局持久化配置对象
     *
     * @return 全局持久化配置对象
     */
    public static ApplicationConfig getApplicationConfig() {
        return global.applicationConfig;
    }

    /**
     * 获取全局应用版本信息参数
     *
     * @return 返回应用版本信息对象
     */
    public static ApplicationVersion getApplicationVersion() {
        return global.applicationVersion;
    }

    /**
     * 获取登录状态数据
     *
     * @return 登录状态数据数据对象
     */
    public static LoginStatus getLoginStatus() {
        return global.loginStatus;
    }

    /**
     * 获取全局UI线程Handler
     *
     * @return UI线程Handler
     */
    public static Handler getUiHandler() {
        return global.handler;
    }

    /**
     * 初始化
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        global = new Global(context);
    }

    private Global(Context context) {
        this.context = context;
        handler = new Handler(Looper.getMainLooper());
        applicationConfig = new ApplicationConfig(context);
        applicationVersion = new ApplicationVersion();
        loginStatus = new LoginStatus();

        initOkHttpClient();
    }

    /**
     * 初始化网络工具
     */
    private void initOkHttpClient() {
        // 网络请求用户代理字符串
        final StringBuilder userAgentBuilder = new StringBuilder();

        userAgentBuilder.append(ApplicationStaticValue.AppConfig.DEVICE_TYPE);
        userAgentBuilder.append("/");
        // 制造商
        userAgentBuilder.append(Build.BRAND);
        userAgentBuilder.append("/");
        // 设备型号
        userAgentBuilder.append(Build.MODEL);
        userAgentBuilder.append("(");
        userAgentBuilder.append(Build.ID);
        userAgentBuilder.append(")");
        userAgentBuilder.append("/");

        // 系统版本
        userAgentBuilder.append(Build.VERSION.SDK_INT);
        userAgentBuilder.append("(");
        userAgentBuilder.append(Build.VERSION.RELEASE);
        userAgentBuilder.append(")");
        userAgentBuilder.append("/");

        // 框架版本
        userAgentBuilder.append(BuildConfig.VERSION_CODE);
        userAgentBuilder.append("(");
        userAgentBuilder.append(BuildConfig.VERSION_NAME);
        userAgentBuilder.append(")");
        userAgentBuilder.append("/");

        try {
            // 包信息
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName
                    (), 0);
            // 应用id
            userAgentBuilder.append(context.getPackageName());
            userAgentBuilder.append("/");

            // 应用版本
            userAgentBuilder.append(info.versionCode);
            userAgentBuilder.append("(");
            userAgentBuilder.append(info.versionName);
            userAgentBuilder.append(")");
            userAgentBuilder.append(".");

        } catch (PackageManager.NameNotFoundException e) {
            Log.e(LOG_TAG + "initOkHttpClient", "PackageManager error", e);
        }

        okHttpClient = new OkHttpClient.Builder()
                // 设置默认连接超时时间
                .connectTimeout(context.getResources().getInteger(R.integer
                        .http_default_connect_timeout), TimeUnit.MILLISECONDS)
                // 设置默认读取超时时间
                .readTimeout(context.getResources().getInteger(R.integer
                        .http_default_read_timeout), TimeUnit.MILLISECONDS)
                // 设置默认写入超时时间
                .writeTimeout(context.getResources().getInteger(R.integer
                        .http_default_write_timeout), TimeUnit.MILLISECONDS)
                // 设置用户代理信息拦截器
                .addNetworkInterceptor(new Interceptor() {

                    private static final String USER_AGENT_HEADER_NAME = "User-Agent";

                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        final Request originalRequest = chain.request();
                        final Request requestWithUserAgent = originalRequest.newBuilder()
                                .removeHeader(USER_AGENT_HEADER_NAME).addHeader
                                        (USER_AGENT_HEADER_NAME, userAgentBuilder.toString())
                                .build();
                        return chain.proceed(requestWithUserAgent);
                    }
                }).build();
    }
}
