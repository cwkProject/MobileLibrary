package org.mobile.model.work;
/**
 * Created by 超悟空 on 2015/1/7.
 */

/**
 * 用于在任务执行中实时更新进度的接口，
 * 只在{@link WorkModel#beginExecute(Object[])}中有效
 *
 * @author 超悟空
 * @version 1.0 2015/1/7
 * @since 1.0
 */
public interface ProgressUpdate {
    /**
     * 进度更新的回调方法
     *
     * @param values 当前进度值
     */
    public void onProgressUpdate(Integer... values);
}
