package org.mobile.library.model.config;
/**
 * Created by 超悟空 on 2015/1/7.
 */

import android.content.Context;
import android.util.Log;

import org.mobile.library.util.PreferencesUtil;

/**
 * 需持久化系统参数配置抽象模型
 *
 * @author 超悟空
 * @version 1.0 2015/1/7
 * @since 1.0
 */
public abstract class PersistenceConfigModel {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "PersistenceConfigModel.";

    /**
     * 持久化对象
     */
    private PreferencesUtil preferencesUtil = null;

    /**
     * 传入上下文的构造函数，以"config"作为配置文件名
     *
     * @param context 上下文对象
     */
    public PersistenceConfigModel(Context context) {
        // 默认使用"config"作为配置文件名
        this.preferencesUtil = new PreferencesUtil(context, "config");
        Log.i(LOG_TAG + "PersistenceConfigModel", "config name is config");
    }

    /**
     * 传入上下文和配置文件名的构造函数
     *
     * @param context  上下文对象
     * @param fileName 配置文件名
     */
    public PersistenceConfigModel(Context context, String fileName) {
        // 新建持久化对象
        this.preferencesUtil = new PreferencesUtil(context, fileName);
        Log.i(LOG_TAG + "PersistenceConfigModel", "config name is " + fileName);
    }

    /**
     * 保存设置
     */
    public void Save() {
        Log.i(LOG_TAG + "Save", "Save() is invoked");
        this.preferencesUtil.Save(this);
    }

    /**
     * 刷新配置参数，从配置文件中重新读取参数
     */
    public void Refresh() {
        Log.i(LOG_TAG + "Refresh", "Refresh() is invoked");
        this.preferencesUtil.Read(this);
    }

    /**
     * 清空配置文件，重置当前参数
     */
    public void Clear() {
        Log.i(LOG_TAG + "Clear", "Clear() is invoked");
        this.preferencesUtil.Clear(this);
        onDefault();
    }

    /**
     * 设置参数默认值，
     * 用于在{@link #Clear()}时调用以清空全局变量
     */
    protected void onDefault() {
    }

    /**
     * 获取当前对象的持久化工具对象
     *
     * @return 持久化对象
     */
    protected PreferencesUtil getPreferencesUtil() {
        return preferencesUtil;
    }
}
