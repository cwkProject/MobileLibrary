package org.mobile.model.operate;
/**
 * Created by 悟空 on 2015/3/2.
 */

/**
 * RecyclerView的Item的点击事件
 *
 * @param <DataSourceType> 绑定的数据源类型
 * @param <ViewHolderType> 绑定的自定义布局管理器类型
 *
 * @author 超悟空
 * @version 1.0 2015/3/2
 * @since 1.0
 */
public interface OnItemClickListenerForRecyclerViewItem<DataSourceType, ViewHolderType> {
    /**
     * 点击事件响应方法
     *
     * @param dataSource 当前适配器绑定的数据集
     * @param holder     点击位置的布局管理工具
     */
    public void onClick(DataSourceType dataSource, ViewHolderType holder);
}
