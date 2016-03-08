package org.mobile.library.model.function;
/**
 * Created by 超悟空 on 2015/7/22.
 */

/**
 * 选择列表接口
 *
 * @param <Source> 供选择的数据列表类型
 * @param <Result> 选择完成返回的结果
 *
 * @author 超悟空
 * @version 2.0 2016/3/4
 * @since 1.0
 */
public interface ISelectList<Source, Result> {

    /**
     * 选择结束接口
     *
     * @param <Source> 供选择的数据列表类型
     * @param <Result> 选择完成返回的结果
     */
    interface OnSelectedListener<Source, Result> {

        /**
         * 完成选择
         *
         * @param result 选择结果
         */
        void onFinish(Result result);

        /**
         * 取消选择
         *
         * @param source 选择列表控件
         */
        void onCancel(Source source);
    }

    /**
     * 设置选择完成的监听器
     *
     * @param onSelectedListener 选择完成监听器实例
     */
    void setOnSelectedListener(OnSelectedListener<Source, Result> onSelectedListener);

    /**
     * 加载选择列表
     *
     * @return 选择列表控件
     */
    Source loadSelect();
}
