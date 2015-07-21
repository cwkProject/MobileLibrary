package org.mobile.common.function;
/**
 * Created by 超悟空 on 2015/6/15.
 */

import android.content.Context;
import android.util.Log;

import org.mobile.model.work.implement.CheckLogin;
import org.mobile.util.ApplicationAttribute;
import org.mobile.util.BroadcastUtil;
import org.mobile.util.ConfigUtil;

/**
 * 自动登录功能
 *
 * @author 超悟空
 * @version 1.0 2015/6/15
 * @since 1.0
 */
public class AutoLogin {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "AutoLogin.";

    /**
     * 检测自动登录，
     * 如果应用保存了自动登录状态则会执行自动登录，
     * 如果未保存则不会执行，
     * 执行结束后会发送广播{@link BroadcastUtil#MEMORY_STATE_LOGIN},
     * 如果需要配置登录参数则提前在{@link ApplicationAttribute}中设置
     *
     * @param appCode 应用代号
     */
    public static void checkAutoLogin(Context context, String appCode) {
        Log.i(LOG_TAG + "autoLogin", "autoLogin() is invoked");

        // 判断是否自动登录
        if (ConfigUtil.getInstance().isLoginAuto()) {
            Log.i(LOG_TAG + "autoLogin", "auto login");

            // 进行登录验证
            CheckLogin login = new CheckLogin();
            // 执行登录任务
            Log.i(LOG_TAG + "autoLogin", "auto login begin");

            login.beginExecute(ConfigUtil.getInstance().getUserName(), ConfigUtil.getInstance().getPassword(), appCode, ApplicationAttribute.getApplicationAttribute().getDeviceToken(), ApplicationAttribute.getApplicationAttribute().getDeviceType());
        } else {
            Log.i(LOG_TAG + "autoLogin", "no auto login");
            // 发送登录状态改变广播，标识一个加载动作结束
            BroadcastUtil.sendBroadcast(context, BroadcastUtil.MEMORY_STATE_LOGIN);
        }
    }
}
