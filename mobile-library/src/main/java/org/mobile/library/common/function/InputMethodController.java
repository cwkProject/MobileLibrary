package org.mobile.library.common.function;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * 软键盘控制器
 *
 * @author 超悟空
 * @version 1.0 2015/7/8
 * @since 1.0
 */
public class InputMethodController {

    /**
     * 关闭软键盘
     *
     * @param activity 上下文
     */
    public static void CloseInputMethod(Activity activity) {
        //得到InputMethodManager的实例
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context
                .INPUT_METHOD_SERVICE);

        if (imm.isActive() && activity.getCurrentFocus() != null) {
            //如果开启
            //关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}