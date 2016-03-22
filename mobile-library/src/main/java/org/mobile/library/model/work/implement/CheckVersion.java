package org.mobile.library.model.work.implement;
/**
 * Created by 超悟空 on 2015/4/23.
 */

import org.mobile.library.global.ApplicationStaticValue;
import org.mobile.library.model.data.implement.UpdateData;
import org.mobile.library.model.work.DefaultWorkModel;
import org.mobile.library.global.ApplicationVersion;
import org.mobile.library.util.BroadcastUtil;
import org.mobile.library.global.GlobalApplication;


/**
 * 检查更新任务类
 *
 * @author 超悟空
 * @version 1.0 2015/4/23
 * @since 1.0
 */
public class CheckVersion extends DefaultWorkModel<String, String, UpdateData> {

    @Override
    protected boolean onCheckParameters(String... parameters) {
        return parameters != null && parameters.length >= 3 && parameters[0] != null &&
                parameters[1] != null && parameters[2] != null;
    }

    @Override
    protected String onTaskUri() {
        return getParameters()[2];
    }

    @Override
    protected void onParameterError(String... parameters) {
        sendBroadcast();
    }

    @Override
    protected void onParseSuccess(UpdateData data) {
        // 改变全局临时变量
        ApplicationVersion config = GlobalApplication.getApplicationVersion();
        config.setLatestVersion(!data.isSuccess());
        config.setLatestVersionName(data.getVersionName());
        config.setLatestVersionUrl(data.getUrl());

        sendBroadcast();
    }

    @Override
    protected void onParseFailed(UpdateData data) {
        sendBroadcast();
    }

    @Override
    protected String onRequestSuccessSetResult(UpdateData data) {
        // 不是最新版本，返回最新版本号
        return data.getVersionName();
    }

    @Override
    protected String onRequestFailedSetResult(UpdateData data) {
        // 当前为最新版本,不返回字符串
        return null;
    }

    @Override
    protected UpdateData onCreateDataModel(String... parameters) {
        // 新建更新数据对象
        UpdateData data = new UpdateData();
        // 设置参数
        data.setDeviceType(parameters[0]);
        data.setAppCode(parameters[1]);

        return data;
    }

    /**
     * 发送广播
     */
    private void sendBroadcast() {
        // 发送版本检查结果广播
        BroadcastUtil.sendBroadcast(GlobalApplication.getGlobal(), ApplicationStaticValue
                .BroadcastAction.APPLICATION_VERSION_STATE);
    }
}
