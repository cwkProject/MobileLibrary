package org.mobile.model.operate;
/**
 * Created by 超悟空 on 2015/1/26.
 */

/**
 * 用于设置和返回对象标题的接口
 *
 * @author 超悟空
 * @version 1.0 2015/1/26
 * @since 1.0
 */
public interface TitleHandle {

    /**
     * 获取对象当前标题
     *
     * @return 标题字符串
     */
    public abstract String getTitle();

    /**
     * 设置对象标题
     *
     * @param title 标题字符串
     */
    public abstract void setTitle(CharSequence title);
}
