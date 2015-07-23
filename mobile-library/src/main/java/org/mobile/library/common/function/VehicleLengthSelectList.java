package org.mobile.library.common.function;
/**
 * Created by 超悟空 on 2015/7/18.
 */

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.mobile.library.R;
import org.mobile.library.model.function.BaseArraySelectList;

/**
 * 车长选择抽屉列表
 *
 * @author 超悟空
 * @version 1.0 2015/7/18
 * @since 1.0
 */
public class VehicleLengthSelectList extends BaseArraySelectList {

    /**
     * 车长数据适配器
     */
    private ArrayAdapter<String> vehicleLengthAdapter = null;

    /**
     * 构造函数
     *
     * @param context        上下文
     * @param selectListView 选择列表
     */
    public VehicleLengthSelectList(Context context, ListView selectListView) {
        super(context, selectListView);
    }

    @Override
    protected ArrayAdapter<String> onCreateAdapter(Context context) {
        if (vehicleLengthAdapter == null) {
            // 没有则创建

            String[] vehicleLengthList = context.getResources().getStringArray(R.array
                    .vehicle_length_select_list);
            vehicleLengthAdapter = new ArrayAdapter<>(context, R.layout.only_text_list_item,
                    vehicleLengthList);
        }

        return vehicleLengthAdapter;
    }
}
