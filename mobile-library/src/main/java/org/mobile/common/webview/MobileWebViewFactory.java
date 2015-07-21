package org.mobile.common.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * 初始化配置WebView的属性
 *
 * @author 超悟空
 * @version 1.0 2015/1/7
 * @since 1.0
 */
public class MobileWebViewFactory {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "MobileWebViewFactory.";

    /**
     * 初始化WebView的设置
     *
     * @param context 上下文
     * @param webView 要设置的WebView对象
     */
    @SuppressLint("SetJavaScriptEnabled")
    public static void assemblingWebView(final Context context, WebView webView) {

        if (webView == null) {
            Log.d(LOG_TAG + "assemblingWebView", "webView is null");
            return;
        }

        WebSettings webSettings = webView.getSettings();

        // 打开JS功能
        webSettings.setJavaScriptEnabled(true);

        // 关闭缓存
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        // 设置滚动条样式
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        // 宽度自适应
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);

        // 设置浏览器属性
        MobileWebViewClient mobileWebViewClient = new MobileWebViewClient();
        webView.setWebViewClient(mobileWebViewClient);

        MobileWebChromeClient mobileWebChromeClient = new MobileWebChromeClient(context);
        webView.setWebChromeClient(mobileWebChromeClient);

    }
}
