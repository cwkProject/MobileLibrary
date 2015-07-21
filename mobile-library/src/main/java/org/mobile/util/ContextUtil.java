package org.mobile.util;

import android.app.Application;

/**
 * 全局Context，用于在任意位置使用应用程序资源
 *
 * @author 超悟空
 * @version 1.0 2015/1/7
 * @since 1.0
 */
public class ContextUtil extends Application {


    /**
     * 静态全局对象
     */
    private static ContextUtil context = null;

    /**
     * 获取全局Context对象
     *
     * @return 返回ContextUtil对象
     */
    public static ContextUtil getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}
