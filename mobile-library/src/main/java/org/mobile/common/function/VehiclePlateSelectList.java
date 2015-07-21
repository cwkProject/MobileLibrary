package org.mobile.common.function;
/**
 * Created by 超悟空 on 2015/7/18.
 */

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.mobile.adapter.SelectListAdapterFactory;
import org.mobile.model.operate.DataChangeObserver;

/**
 * 车牌选择抽屉列表
 *
 * @author 超悟空
 * @version 1.0 2015/7/18
 * @since 1.0
 */
public class VehiclePlateSelectList {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "VehiclePlateSelectList.";

    /**
     * 抽屉布局中的选择列表
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
    public VehiclePlateSelectList(Context context, ListView selectListView) {
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
        final ListAdapter adapter = SelectListAdapterFactory.CreateAdapter(context,
                SelectListAdapterFactory.DataType.VEHICLE_PLATE_NUMBER);
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
                    dataChangeObserver.notifyDataChange((String) adapter.getItem(position));
                }
            }
        });

    }
}
