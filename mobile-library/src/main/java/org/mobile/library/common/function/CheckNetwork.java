package org.mobile.library.common.function;
/**
 * Created by 超悟空 on 2015/6/11.
 */

import android.content.Context;
import android.net.ConnectivityManager;

import org.mobile.library.global.Global;

/**
 * 检测网络连接状态
 *
 * @author 超悟空
 * @version 1.0 2015/6/11
 * @since 1.0
 */
public class CheckNetwork {

    /**
     * 对网络连接状态进行判断
     *
     * @return true可用；false不可用
     */
    public static boolean isOpenNetwork() {
        ConnectivityManager connManager = (ConnectivityManager) Global.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connManager.getActiveNetworkInfo() != null && connManager.getActiveNetworkInfo().isAvailable();
    }
}
