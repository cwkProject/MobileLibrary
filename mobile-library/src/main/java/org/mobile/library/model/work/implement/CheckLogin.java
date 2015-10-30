package org.mobile.library.model.work.implement;
/**
 * Created by 超悟空 on 2015/6/11.
 */

import android.util.Log;

import org.mobile.library.R;
import org.mobile.library.model.data.implement.LoginData;
import org.mobile.library.model.work.DefaultWorkModel;
import org.mobile.library.util.BroadcastUtil;
import org.mobile.library.global.GlobalApplication;
import org.mobile.library.global.LoginStatus;

/**
 * 登录检查任务类
 *
 * @author 超悟空
 * @version 1.0 2015/6/11
 * @since 1.0
 */
public class CheckLogin extends DefaultWorkModel<String, String, LoginData> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "CheckLogin.";

    @Override
    protected boolean onCheckParameters(String... parameters) {
        return parameters != null && parameters.length >= 2 && parameters[0] != null &&
                parameters[1] != null;
    }

    @Override
    protected String onTaskUri() {
        return LoginStatus.getLoginStatus().getLoginUrl();
    }

    @Override
    protected void onParameterError(String... parameters) {
        Log.d(LOG_TAG + "onDoWork", "userName or password is null");
        setResult(GlobalApplication.getGlobal().getString(R.string.login_error_parameter));
        sendBroadcast();
    }

    @Override
    protected void onParseFailed(LoginData data) {
        sendBroadcast();
    }

    @Override
    protected String onParseFailedSetResult(LoginData data) {
        return GlobalApplication.getGlobal().getString(R.string.login_error_field_required);
    }

    @Override
    protected String onRequestSuccessSetResult(LoginData data) {
        // 登录相关临时参数
        LoginStatus config = LoginStatus.getLoginStatus();

        // 登录成功设置参数
        config.setLogin(true);
        config.setUserID(data.getUserID());
        config.setCodeCompany(data.getCompanyCode());
        config.setCodeDepartment(data.getDepartmentCode());
        config.setNickname(data.getNickname());

        sendBroadcast();
        return data.getMessage();
    }

    @Override
    protected String onRequestFailedSetResult(LoginData data) {
        sendBroadcast();
        return data.getMessage();
    }

    @Override
    protected LoginData onCreateDataModel(String... parameters) {
        // 新建登录数据对象
        LoginData data = new LoginData();
        data.setUserName(parameters[0]);
        data.setPassword(parameters[1]);

        if (parameters.length >= 3) {
            // 设置应用标识
            data.setAppName(parameters[2]);
        }

        if (parameters.length >= 4) {
            // 设置设备UUID
            data.setDeviceToken(parameters[3]);
        }

        if (parameters.length >= 5) {
            // 设置设备类型
            data.setDeviceType(parameters[4]);
        }

        return data;
    }

    /**
     * 发送广播
     */
    private void sendBroadcast() {
        BroadcastUtil.sendBroadcast(GlobalApplication.getGlobal(), BroadcastUtil.MEMORY_STATE_LOGIN);
    }
}
