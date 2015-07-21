package org.mobile.model.fragment;
/**
 * Created by 超悟空 on 2015/1/22.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import org.mobile.common.webview.MobileWebViewClient;
import org.mobile.common.webview.MobileWebViewFactory;
import org.mobile.model.operate.BackHandle;

/**
 * 内嵌网页的Fragment片段基类
 *
 * @author 超悟空
 * @version 1.0 2015/1/22
 * @since 1.0
 */
public abstract class BaseIncludeWebFragment extends Fragment implements BackHandle {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "BaseIncludeWebFragment.";

    /**
     * 当前片段显示的网页控件
     */
    protected WebView webView = null;

    /**
     * 标识本次网页加载是否为一次全新加载
     */
    private boolean reopenUrl = false;

    /**
     * 设置根布局的资源ID
     *
     * @return 返回ID
     */
    protected abstract int initRootViewLayoutID();

    /**
     * 设置WebView控件的资源ID
     *
     * @return 返回ID
     */
    protected abstract int initWebViewLayoutID();

    /**
     * 设置本片段使用的WebView控件引用对象，
     * 默认通过{@link #initWebViewLayoutID()}返回值创建WebView对象，
     * 如果需要自定义返回WebView控件引用，
     * 则需要重写本方法返回WebView控件对象(此时{@link #initWebViewLayoutID()}可返回任意值)
     *
     * @param rootView 根布局
     *
     * @return WebView引用
     */
    protected WebView onInitWebView(View rootView) {
        return (WebView) rootView.findViewById(initWebViewLayoutID());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 获取根布局
        View rootView = inflater.inflate(initRootViewLayoutID(), container, false);
        // 获取WebView控件
        webView = onInitWebView(rootView);

        // 初始化WebView控件
        initWebView();

        Log.i(LOG_TAG + "onCreateView", "onCustomRootView(View) is invoked");
        // 执行自定义布局
        onCustomRootView(rootView);

        Log.i(LOG_TAG + "onCreateView", "onCreateUrl() is invoked");
        // 初始化加载的网址
        if (onCreateUrl() != null) {
            Log.i(LOG_TAG + "onCreateView", "url is " + onCreateUrl());
            loadUrl(onCreateUrl());
        } else {
            Log.i(LOG_TAG + "onCreateView", "url is null");
        }
        return rootView;
    }

    /**
     * 初始化WebView控件
     */
    private void initWebView() {
        // 初始化WebView
        MobileWebViewFactory.assemblingWebView(this.getActivity(), webView);

        // 新建一个WebView控制器
        MobileWebViewClient webViewClient = new MobileWebViewClient();
        // 设置一个加载完成回调方法
        webViewClient.setLoadFinishedCallBack(new MobileWebViewClient.LoadFinishedCallBack() {
            @Override
            public void operate(WebView view, String url) {
                // 如果是一次全新加载，则清空返回栈
                if (reopenUrl) {
                    // 清空历史
                    webView.clearHistory();
                    // 重置参数为默认值
                    reopenUrl = false;
                }
            }
        });

        // 设置WebViewClient为新的控制器
        webView.setWebViewClient(webViewClient);
    }

    /**
     * 初始化时加载的网址
     *
     * @return url字符串
     */
    protected abstract String onCreateUrl();

    /**
     * 对要加载的布局进行自定义或执行某些操作，使用时需在子类中重写，子类中需先调用此父类方法，在onCreateView返回布局之前执行
     *
     * @param rootView 根布局
     */
    protected void onCustomRootView(View rootView) {
    }

    /**
     * 处理网页后退
     *
     * @return 返回true表示处理，返回false表示不处理
     */
    public boolean onGoBack() {
        Log.i(LOG_TAG + "onGoBack", "onGoBack() is invoked");
        // 处理网页后退
        if (webView.canGoBack()) {
            // 返回webView的上一页面
            webView.goBack();
            return true;
        }
        return false;
    }

    @Override
    public boolean onBackPressed() {
        Log.i(LOG_TAG + "onBackPressed", "onBackPressed() is invoked");
        return onGoBack();
    }

    /**
     * 用于加载网页
     *
     * @param url 要加载的网页地址
     */
    public void loadUrl(String url) {
        Log.i(LOG_TAG + "loadUrl", "loadUrl(String) is invoked");
        Log.i(LOG_TAG + "loadUrl", "url is " + url);
        webView.loadUrl(url);
    }

    /**
     * 清空网页加载历史，即清空后退栈
     */
    public void clearWebHistory() {
        Log.i(LOG_TAG + "clearWebHistory", "clearWebHistory() is invoked");
        webView.clearHistory();
    }

    /**
     * 清空后退栈并重新加载网页
     *
     * @param url 要加载的网页地址
     */
    public void reloadUrl(String url) {
        Log.i(LOG_TAG + "reloadUrl", "reloadUrl(String) is invoked");
        Log.i(LOG_TAG + "reloadUrl", "url is " + url);
        // 设为一次全新加载
        reopenUrl = true;
        loadUrl(url);
    }

    /**
     * 获取本片段使用的WebView对象
     *
     * @return 返回WebView对象
     */
    protected final WebView getWebView() {
        return webView;
    }
}
