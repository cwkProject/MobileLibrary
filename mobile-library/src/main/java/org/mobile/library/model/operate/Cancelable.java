package org.mobile.library.model.operate;
/**
 * Created by 超悟空 on 2015/10/29.
 */

/**
 * 取消接口
 *
 * @author 超悟空
 * @version 1.0 2015/10/29
 * @since 1.0
 */
public interface Cancelable {

    /**
     * 取消正在执行的工作
     */
    void cancel();
}
