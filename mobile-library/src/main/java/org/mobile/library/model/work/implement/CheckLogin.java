package org.mobile.library.model.work.implement;
/**
 * Created by 超悟空 on 2015/6/11.
 */

import android.util.Log;

import org.mobile.library.R;
import org.mobile.library.global.ApplicationStaticValue;
import org.mobile.library.global.GlobalApplication;
import org.mobile.library.model.data.implement.LoginData;
import org.mobile.library.model.work.DefaultWorkModel;
import org.mobile.library.network.factory.NetworkType;
import org.mobile.library.util.BroadcastUtil;

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
        return ApplicationStaticValue.Url.LOGIN_URL;
    }

    @Override
    protected void onParameterError(String... parameters) {
        Log.d(LOG_TAG + "onDoWork", "userName or password is null");
        setResult(GlobalApplication.getGlobal().getString(R.string.login_error_parameter));
    }

    @Override
    protected String onParseFailedSetMessage(LoginData data) {
        return GlobalApplication.getGlobal().getString(R.string.login_error_field_required);
    }

    @Override
    protected String onRequestSuccessSetResult(LoginData data) {
        return data.getUserID();
    }

    @Override
    protected LoginData onCreateDataModel(String... parameters) {
        // 新建登录数据对象
        LoginData data = new LoginData();
        data.setUserName(parameters[0]);
        data.setPassword(parameters[1]);

        return data;
    }

    @Override
    protected NetworkType onNetworkType() {
        return NetworkType.POST;
    }

    @Override
    protected void onFinish() {
        // 发送广播
        BroadcastUtil.sendBroadcast(GlobalApplication.getGlobal(), ApplicationStaticValue
                .BroadcastAction.LOGIN_STATE);
    }
}
