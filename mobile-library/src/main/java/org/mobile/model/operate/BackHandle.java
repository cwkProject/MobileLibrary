package org.mobile.model.operate;
/**
 * Created by 超悟空 on 2015/1/26.
 */

/**
 * 处理返回键的接口
 *
 * @author 超悟空
 * @version 1.0 2015/1/26
 * @since 1.0
 */
public interface BackHandle {
    /**
     * 点击返回键的操作，
     * 返回true表示处理，
     * 返回false表示不处理
     *
     * @return 处理标识
     */
    public abstract boolean onBackPressed();
}
