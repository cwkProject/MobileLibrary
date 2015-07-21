package org.mobile.model.config;
/**
 * Created by 超悟空 on 2015/1/27.
 */

import android.util.Log;

/**
 * 运行时系统参数配置抽象模型
 *
 * @author 超悟空
 * @version 1.0 2015/1/27
 * @since 1.0
 */
public abstract class TemporaryConfigModel {
    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "TemporaryConfigModel.";

    /**
     * 构造函数
     */
    protected TemporaryConfigModel() {
        onCreate();
    }

    /**
     * 初始化数据，
     * 在创建时执行一次，
     * 在{@link #Reset()}被调用
     */
    protected abstract void onCreate();

    /**
     * 刷新数据,
     * 在{@link #Refresh()}被调用
     */
    protected void onRefresh() {
    }

    /**
     * 刷新数据
     */
    public final void Refresh() {
        Log.i(LOG_TAG + "Refresh", "Refresh() is invoked");
        onRefresh();
    }

    /**
     * 重置数据
     */
    public final void Reset() {
        Log.i(LOG_TAG + "Reset", "Reset() is invoked");
        onCreate();
    }
}
