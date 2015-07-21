package org.mobile.model.work.implement;
/**
 * Created by 超悟空 on 2015/4/23.
 */

import android.util.Log;

import org.mobile.model.data.implement.UpdateData;
import org.mobile.model.work.WorkModel;
import org.mobile.network.communication.ICommunication;
import org.mobile.network.factory.CommunicationFactory;
import org.mobile.network.factory.NetworkType;
import org.mobile.util.ApplicationVersion;
import org.mobile.util.BroadcastUtil;
import org.mobile.util.ContextUtil;


/**
 * 检查更新任务类
 *
 * @author 超悟空
 * @version 1.0 2015/4/23
 * @since 1.0
 */
public class CheckVersion extends WorkModel<String, String> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "CheckVersion.";

    @Override
    protected boolean onDoWork(String... parameters) {

        if (parameters == null || parameters.length < 3 || parameters[0] == null || parameters[1] == null || parameters[2] == null) {
            // 数据异常
            Log.d(LOG_TAG + "onDoWork", "device type or application code or update request url is null");
            // 执行失败
            Log.i(LOG_TAG + "onDoWork", "check failed");
            // 发送版本检查结果广播
            BroadcastUtil.sendBroadcast(ContextUtil.getContext(), BroadcastUtil.APPLICATION_VERSION_STATE);
            return false;
        }

        // 新建http get请求的通讯工具
        ICommunication communication = CommunicationFactory.Create(NetworkType.HTTP_GET);

        // 新建更新数据对象
        UpdateData data = new UpdateData();
        // 设置参数
        data.setDeviceType(parameters[0]);
        data.setAppCode(parameters[1]);

        // 设置调用的方法名
        communication.setTaskName(parameters[2]);
        Log.i(LOG_TAG + "onDoWork", "update request url is " + parameters[2]);

        // 发送请求
        //noinspection unchecked
        communication.Request(data.serialization());

        // 解析数据
        if (data.parse(communication.Response())) {
            // 执行成功
            Log.i(LOG_TAG + "onDoWork", "check success");

            // 改变全局临时变量
            ApplicationVersion config = ApplicationVersion.getVersionManager();
            config.setLatestVersion(data.isLatest());
            config.setLatestVersionName(data.getVersionName());
            config.setLatestVersionUrl(data.getUrl());

            if (data.isLatest()) {
                // 当前为最新版本,不返回字符串
                setResult(null);
            } else {
                // 不是最新版本，返回最新版本号
                setResult(data.getVersionName());
            }
            // 发送版本检查结果广播
            BroadcastUtil.sendBroadcast(ContextUtil.getContext(), BroadcastUtil.APPLICATION_VERSION_STATE);
            return true;
        } else {
            // 执行失败
            Log.i(LOG_TAG + "onDoWork", "check failed");
            // 发送版本检查结果广播
            BroadcastUtil.sendBroadcast(ContextUtil.getContext(), BroadcastUtil.APPLICATION_VERSION_STATE);
            return false;
        }
    }
}
