package org.mobile.util;
/**
 * Created by 超悟空 on 2015/4/23.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * 运行时临时数据监听的广播工具，
 * 用于当某些特定数据变化时或某些特定事件被触发时发送通知
 *
 * @author 超悟空
 * @version 1.0 2015/4/23
 * @since 1.0
 */
public class BroadcastUtil {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "MemoryBroadcast.";

    /**
     * 应用版本状态
     */
    public static final String APPLICATION_VERSION_STATE = "org.mobile.util.ApplicationVersion.latestVersion";

    /**
     * 登录状态
     */
    public static final String MEMORY_STATE_LOGIN = "org.mobile.util.LOGIN";

    /**
     * 用户标识状态
     */
    public static final String MEMORY_STATE_USER_ID = "org.mobile.util.userID";

    /**
     * 发送广播
     *
     * @param context 上下文
     * @param action  动作字符串
     */
    public static void sendBroadcast(Context context, String action) {
        Log.i(LOG_TAG + "send", "send action is " + action);
        sendBroadcast(context, new Intent(action));
    }

    /**
     * 发送广播
     *
     * @param context 上下文
     * @param intent  包含一组动作字符串的意图
     */
    public static void sendBroadcast(Context context, Intent intent) {
        Log.i(LOG_TAG + "send", "send intent");
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(context);
        lbm.sendBroadcast(intent);
    }
}
