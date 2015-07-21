package org.mobile.common.webview;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import org.mobile.common.dialog.NoneProgressDialog;
import org.mobile.model.dialog.IProgressDialog;

/**
 * 重写WebChromeClient类的某些功能
 *
 * @author 超悟空
 * @version 1.0 2015/1/7
 * @since 1.0
 */
public class MobileWebChromeClient extends WebChromeClient {

    // 存储上下文对象
    private Context context = null;

    // 进度条窗口
    private IProgressDialog myProgressDialog = null;

    /**
     * 构造函数
     *
     * @param context 上下文
     */
    public MobileWebChromeClient(Context context) {

        this.context = context;

        // 默认不加载进度条
        myProgressDialog = new NoneProgressDialog();
    }

    /**
     * 设置进度条工具，如果传入null则表示不加载进度条
     *
     * @param progressDialog 进度条工具对象
     */
    public void setProgressDialog(IProgressDialog progressDialog) {
        if (progressDialog == null) {
            // 空实现对象
            myProgressDialog = new NoneProgressDialog();
        } else {
            // 指定的进度条对象
            myProgressDialog = progressDialog;
        }
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {

        myProgressDialog.setProgress(newProgress);
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {

        // 新建一个弹出对话框
        Builder builder = new Builder(context);

        // 设置提示信息
        builder.setMessage(message);

        // 设置确认按钮
        builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 点击确定按钮之后，继续执行网页中的操作
                result.confirm();
            }
        });

        // 不可关闭
        builder.setCancelable(false);

        // 创建并显示
        builder.create();
        builder.show();
        return true;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {

        // 新建一个弹出对话框
        Builder builder = new Builder(context);

        // 设置提示信息
        builder.setMessage(message);

        // 设置确认按钮
        builder.setPositiveButton(android.R.string.ok, new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 点击确定按钮之后，继续执行网页中的操作
                result.confirm();
            }
        });

        // 设置取消按钮
        builder.setNegativeButton(android.R.string.cancel, new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 继续执行取消
                result.cancel();
            }
        });

        // 不可关闭
        builder.setCancelable(false);

        // 创建并显示
        builder.create();
        builder.show();
        return true;
    }
}
