package org.mobile.library.model.function;
/**
 * Created by 超悟空 on 2015/7/22.
 */

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.mobile.library.model.operate.DataChangeObserver;

/**
 * 单一数组数据源选择列表基类
 *
 * @author 超悟空
 * @version 1.0 2015/7/22
 * @since 1.0
 */
public abstract class BaseArraySelectList implements ISelectList {
    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "BaseArraySelectList.";

    /**
     * 选择列表
     */
    private ListView selectListView = null;

    /**
     * 上下文
     */
    private Context context = null;

    /**
     * 选择结果回调
     */
    private DataChangeObserver<String> dataChangeObserver = null;

    /**
     * 构造函数
     *
     * @param context        上下文
     * @param selectListView 选择列表
     */
    protected BaseArraySelectList(Context context, ListView selectListView) {
        this.selectListView = selectListView;
        this.context = context;
    }

    /**
     * 设置选择完成监听器
     *
     * @param dataChangeObserver 监听器实例
     */
    public void setSelectFinishedListener(DataChangeObserver<String> dataChangeObserver) {
        this.dataChangeObserver = dataChangeObserver;
    }

    /**
     * 配置选择列表并绑定选择事件
     */
    public void selectSetting() {
        Log.i(LOG_TAG + "selectSetting", "Select start");

        // 获取数据源适配器
        final ArrayAdapter<String> adapter = onCreateAdapter(context);
        // 重置列表适配器
        selectListView.setAdapter(adapter);

        // 重置选中事件
        selectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(LOG_TAG + "selectSetting", "select position is " + position);
                Log.i(LOG_TAG + "selectSetting", "select item is " + adapter.getItem(position));
                // 设置文本结果
                if (dataChangeObserver != null) {
                    dataChangeObserver.notifyDataChange(adapter.getItem(position));
                }
            }
        });
    }

    /**
     * 创建数据源适配器
     *
     * @param context 上下文
     *
     * @return 仅字符串数组的列表适配器
     */
    protected abstract ArrayAdapter<String> onCreateAdapter(Context context);
}
