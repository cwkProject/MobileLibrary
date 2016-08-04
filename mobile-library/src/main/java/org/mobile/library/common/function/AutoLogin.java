package org.mobile.library.common.function;
/**
 * Created by 超悟空 on 2015/6/15.
 */

import android.content.Context;
import android.util.Log;

import org.mobile.library.global.ApplicationAttribute;
import org.mobile.library.global.ApplicationStaticValue;
import org.mobile.library.global.Global;
import org.mobile.library.global.LoginStatus;
import org.mobile.library.model.work.WorkBack;
import org.mobile.library.model.work.implement.CheckLogin;
import org.mobile.library.util.BroadcastUtil;

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
     * 执行结束后会发送广播{@link org.mobile.library.global.ApplicationStaticValue
     * .BroadcastAction#LOGIN_STATE},
     * 如果需要配置登录参数则提前在{@link ApplicationAttribute}中设置
     *
     * @param context 上下文
     */
    public static void checkAutoLogin(Context context) {
        Log.v(LOG_TAG + "checkAutoLogin", "checkAutoLogin is invoked");

        // 判断是否有用户名密码
        if (Global.getApplicationConfig().getUserName() != null && Global
                .getApplicationConfig().getPassword() != null) {
            Log.i(LOG_TAG + "checkAutoLogin", "auto login");

            // 进行登录验证
            CheckLogin login = new CheckLogin();

            login.setWorkEndListener(new WorkBack<String>() {
                @Override
                public void doEndWork(boolean state, String data) {
                    LoginStatus loginStatus = Global.getLoginStatus();

                    loginStatus.setLogin(state);
                    loginStatus.setUserID(data);
                }
            }, false);

            // 执行登录任务
            Log.v(LOG_TAG + "checkAutoLogin", "auto login begin");
            login.beginExecute(Global.getApplicationConfig().getUserName(),
                    Global.getApplicationConfig().getPassword());
        } else {
            Log.i(LOG_TAG + "checkAutoLogin", "no auto login");
            // 发送登录状态改变广播，标识一个加载动作结束
            BroadcastUtil.sendBroadcast(context, ApplicationStaticValue.BroadcastAction
                    .LOGIN_STATE);
        }
    }
}
