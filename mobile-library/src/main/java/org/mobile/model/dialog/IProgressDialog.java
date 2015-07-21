package org.mobile.model.dialog;
/**
 * Created by 超悟空 on 2015/1/22.
 */

/**
 * 进度条设置接口
 *
 * @param <SelfType> 实现类的类型，用于参数设置方法返回自身
 *
 * @author 超悟空
 * @version 1.0 2015/1/22
 * @since 1.0
 */
public interface IProgressDialog<SelfType> {

    /**
     * 设置进度，进度大于等于100时会自动关闭窗口
     *
     * @param progress 当前进度，最大值100
     */
    public void setProgress(int progress);

    /**
     * 设置超时时间，用于在超时情况下关闭进度条，默认不指定超时时间
     *
     * @param timeout 指定的超时时间，单位毫秒，0为不指定超时时间
     *
     * @return 设置参数后的当前对象
     */
    public SelfType setTimeout(int timeout);
}
