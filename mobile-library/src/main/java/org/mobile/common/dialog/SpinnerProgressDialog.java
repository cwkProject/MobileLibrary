package org.mobile.common.dialog;
/**
 * Created by 超悟空 on 2015/1/22.
 */

import android.app.ProgressDialog;
import android.content.Context;

import org.mobile.library.R;
import org.mobile.model.dialog.IProgressDialog;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 旋转型进度条设置
 *
 * @author 超悟空
 * @version 1.0 2015/1/22
 * @since 1.0
 */
public class SpinnerProgressDialog implements IProgressDialog<SpinnerProgressDialog> {

    /**
     * 进度条窗口
     */
    private ProgressDialog progressDialog = null;

    /**
     * 存储上下文对象
     */
    private Context context = null;

    /**
     * 超时时间
     */
    private int timeout = 0;

    /**
     * 超时定时器
     */
    private Timer timeoutTimer = null;

    /**
     * 构造函数
     *
     * @param context 上下文
     */
    public SpinnerProgressDialog(Context context) {
        this.context = context;
    }

    @Override
    public void setProgress(int progress) {

        if (progressDialog == null) {
            // 没有就新建进度条
            progressDialog = new ProgressDialog(context);

            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            // 提示信息
            progressDialog.setMessage(context.getString(R.string.web_view_spinner_progress_dialog_message));
            progressDialog.setCancelable(false);
            progressDialog.show();
            // 启动超时定时器
            runTimeoutTimer();
        }

        if (progress >= 100) {
            // 关闭进度条
            progressDialog.cancel();
            progressDialog = null;

            if (timeoutTimer != null) {
                // 正常执行完毕关闭定时器
                timeoutTimer.cancel();
                timeoutTimer = null;
            }
        }
    }

    @Override
    public SpinnerProgressDialog setTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * 启动超时定时器
     */
    public void runTimeoutTimer() {
        if (timeout > 0) {
            // 有超时时间才执行
            timeoutTimer = new Timer();
            timeoutTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // 超时后关闭进度条
                    if (progressDialog != null) {
                        progressDialog.cancel();
                    }
                    // 重置为null
                    timeoutTimer = null;
                }
            }, timeout);
        }
    }
}
