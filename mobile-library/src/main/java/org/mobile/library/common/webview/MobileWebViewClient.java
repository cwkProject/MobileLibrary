package org.mobile.library.common.webview;

import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.mobile.library.R;
import org.mobile.library.global.GlobalApplication;

/**
 * 重写WebViewClient类的某些功能
 *
 * @author 超悟空
 * @version 1.0 2015/1/7
 * @since 1.0
 */
public class MobileWebViewClient extends WebViewClient {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "MobileWebViewClient.";

    /**
     * 网页加载完成的回调接口
     */
    private LoadFinishedCallBack loadFinishedCallBack = null;

    /**
     * 标识是否保留WebView后退栈，默认保留
     */
    private boolean saveBackStack = true;

    /**
     * 是否保留后退栈
     *
     * @return true表示保留后退栈
     */
    public boolean isSaveBackStack() {
        return saveBackStack;
    }

    /**
     * 设置WebView是否保留后退栈，即是否只加载一个页面
     *
     * @param saveBackStack true为保留后退栈，false为不保留后退栈，默认为true
     */
    public void setSaveBackStack(boolean saveBackStack) {
        this.saveBackStack = saveBackStack;
    }

    /**
     * 设置页面加载完成后的回调接口
     *
     * @param loadFinishedCallBack 要设置的回调接口
     */
    public void setLoadFinishedCallBack(LoadFinishedCallBack loadFinishedCallBack) {
        this.loadFinishedCallBack = loadFinishedCallBack;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        Log.v(LOG_TAG + "shouldOverrideUrlLoading", "url is --> " + url);
        // 用自身打开超链接
        view.loadUrl(url);

        return true;
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

        //用javascript隐藏系统定义的404页面信息

        view.loadUrl("javascript:document.title=\"" + GlobalApplication.getGlobal().getString(
                R.string.web_view_load_error_title) + "\";document.body.innerHTML=\"" + GlobalApplication.getGlobal().getString(R.string.web_view_load_error_body) + "\"");
    }

    @Override
    public void onPageFinished(WebView view, String url) {

        // 检查是否保留后退栈
        if (!isSaveBackStack()) {
            view.clearHistory();
        }

        // 存在回调就执行
        if (this.loadFinishedCallBack != null) {
            Log.v(LOG_TAG + "onPageFinished", "call back invoke");
            this.loadFinishedCallBack.operate(view, url);
        }
        super.onPageFinished(view, url);
    }

    /**
     * 网页加载完成的回调接口
     *
     * @author 超悟空
     * @version 1.0
     */
    public interface LoadFinishedCallBack {

        /**
         * 网页加载完毕后执行的操作
         */
        public abstract void operate(WebView view, String url);
    }
}
