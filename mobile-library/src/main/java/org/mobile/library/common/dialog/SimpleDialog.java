package org.mobile.library.common.dialog;
/**
 * Created by 超悟空 on 2015/4/18.
 */


import android.content.Context;
import android.support.v7.app.AlertDialog;


/**
 * 显示简单消息提示窗
 *
 * @author 超悟空
 * @version 1.0 2015/4/18
 * @since 1.0
 */
public class SimpleDialog {

    /**
     * 显示提示窗
     *
     * @param context 上下文
     * @param message 显示消息
     */
    public static void showDialog(Context context, String message) {
        // 提示框
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        // 设置标题
        dialog.setMessage(message);

        // 显示提示框
        dialog.setCancelable(true);
        dialog.show();
    }

    /**
     * 显示提示窗
     *
     * @param context   上下文
     * @param messageId 显示消息的id
     */
    public static void showDialog(Context context, int messageId) {
        // 提示框
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        // 设置标题
        dialog.setMessage(messageId);

        // 显示提示框
        dialog.setCancelable(true);
        dialog.show();
    }
}
