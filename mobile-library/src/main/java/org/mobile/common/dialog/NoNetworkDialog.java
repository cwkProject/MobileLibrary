package org.mobile.common.dialog;
/**
 * Created by 超悟空 on 2015/4/18.
 */

import android.app.Dialog;
import android.content.Context;

import org.mobile.library.R;

/**
 * 弹出无网络的提示窗类
 *
 * @author 超悟空
 * @version 1.0 2015/4/18
 * @since 1.0
 */
public class NoNetworkDialog {

    /**
     * 显示无网络提示
     *
     * @param context 上下文
     */
    public static void showNoNetworkDialog(Context context) {
        // 提示框
        Dialog dialog = new Dialog(context);

        // 设置标题
        dialog.setTitle(R.string.no_network);

        // 显示提示框
        dialog.setCancelable(true);
        dialog.show();
    }
}
