package org.mobile.library.network.util;
/**
 * Created by 超悟空 on 2015/10/29.
 */

/**
 * 网络请求执行进度监听器
 *
 * @author 超悟空
 * @version 1.0 2015/10/29
 * @since 1.0
 */
public interface NetworkProgressListener {

    /**
     * 刷新进度
     *
     * @param current 当前进度
     * @param total   数据总长度
     * @param done    执行状态，true表示已完成
     */
    void onRefreshProgress(long current, long total, boolean done);
}
