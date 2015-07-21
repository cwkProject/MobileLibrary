package org.mobile.model.operate;
/**
 * Created by 超悟空 on 2015/4/1.
 */

/**
 * RecyclerView的Item的长按事件
 *
 * @param <DataSourceType> 绑定的数据源类型
 * @param <ViewHolderType> 绑定的自定义布局管理器类型
 *
 * @author 超悟空
 * @version 1.0 2015/4/1
 * @since 1.0
 */
public interface OnItemLongClickListenerForRecyclerViewItem<DataSourceType, ViewHolderType> {
    /**
     * 长按事件响应方法
     *
     * @param dataSource 当前适配器绑定的数据集
     * @param holder     点击位置的布局管理工具
     *
     * @return true表示事件被处理
     */
    public boolean onLongClick(DataSourceType dataSource, ViewHolderType holder);
}
