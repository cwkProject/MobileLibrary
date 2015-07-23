package org.mobile.library.common.function;
/**
 * Created by 超悟空 on 2015/7/18.
 */

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.mobile.library.R;
import org.mobile.library.database.city.CityConst;
import org.mobile.library.database.city.CityOperator;
import org.mobile.library.model.function.ISelectList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 城市选择列表
 *
 * @author 超悟空
 * @version 1.0 2015/7/18
 * @since 1.0
 */
public class CitySelectList implements ISelectList {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "CitySelectDrawer.";

    /**
     * 表示选中第一条，及父级选项的代码
     */
    private static final int FIRST_SELECT = 0;

    /**
     * 表示选中返回的代码
     */
    private static final int BACK_SELECT = -1;

    /**
     * 子控件集合
     *
     * @author 超悟空
     * @version 1.0 2015/7/17
     * @since 1.0
     */
    private class ViewHolder {

        /**
         * 选择列表
         */
        public ListView selectListView = null;

        /**
         * 已选择的省
         */
        public Map<String, Object> province = null;

        /**
         * 已选择的市
         */
        public Map<String, Object> city = null;

        /**
         * 已选择的区
         */
        public Map<String, Object> district = null;

        /**
         * 省份数据适配器
         */
        private SimpleAdapter provinceAdapter = null;

        /**
         * 城市数据库工具
         */
        private CityOperator cityOperator = null;
    }

    /**
     * 城市选择完成回调
     *
     * @author 超悟空
     * @version 1.0 2015/7/17
     * @since 1.0
     */
    public interface CitySelectFinishedListener {
        /**
         * 城市选择完成
         *
         * @param province 省
         * @param city     市
         * @param district 区
         */
        void onCitySelectFinish(String province, @Nullable String city, @Nullable String district);
    }

    /**
     * 上下文
     */
    private Context context = null;

    /**
     * 子控件集合对象
     */
    private ViewHolder viewHolder = new ViewHolder();

    /**
     * 城市选择完成回调
     */
    private CitySelectFinishedListener citySelectFinishedListener = null;

    /**
     * 构造函数
     *
     * @param context        上下文
     * @param selectListView 选择列表
     */
    public CitySelectList(Context context, ListView selectListView) {
        this.context = context;
        // 选择列表
        viewHolder.selectListView = selectListView;

        // 初始化
        init();
    }

    /**
     * 设置城市选择完成监听器
     *
     * @param citySelectFinishedListener 监听器实例
     */
    public void setCitySelectFinishedListener(CitySelectFinishedListener
                                                      citySelectFinishedListener) {
        this.citySelectFinishedListener = citySelectFinishedListener;
    }

    /**
     * 初始化
     */
    private void init() {
        // 初始化控件集
        initViewHolder();
    }

    /**
     * 初始化控件集引用
     */
    private void initViewHolder() {

        // 数据库工具
        viewHolder.cityOperator = new CityOperator(context);

        // 省份数据集
        List<Map<String, Object>> provinceList = viewHolder.cityOperator.getProvinceList();
        // 加入头数据
        Map<String, Object> map = new HashMap<>();

        map.put(CityConst.NAME, context.getString(R.string.entire_country));
        map.put(CityConst.CODE, FIRST_SELECT);
        provinceList.add(0, map);

        // 省份数据适配器
        viewHolder.provinceAdapter = new SimpleAdapter(context, provinceList, R.layout
                .only_text_list_item, new String[]{CityConst.NAME}, new int[]{R.id.only_text_list_item_textView});
    }

    /**
     * 配置选择列表并绑定选择事件
     */
    @Override
    public void selectSetting() {
        Log.i(LOG_TAG + "selectSetting", "Select start");

        // 选择省设置
        selectProvince();
    }

    /**
     * 选择省
     */
    private void selectProvince() {
        Log.i(LOG_TAG + "selectProvince", "select province is invoked");
        // 初始化参数
        viewHolder.province = null;
        viewHolder.city = null;
        viewHolder.district = null;

        // 重置列表适配器
        viewHolder.selectListView.setAdapter(viewHolder.provinceAdapter);

        // 重置选中事件
        viewHolder.selectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    Log.i(LOG_TAG + "selectProvince", "list head is clicked");
                    // 选择结束
                    selectFinish();
                }

                // 设置文本结果
                viewHolder.province = (Map<String, Object>) viewHolder.provinceAdapter.getItem
                        (position);
                Log.i(LOG_TAG + "selectProvince", "select position is " + position);
                Log.i(LOG_TAG + "selectProvince", "select province reference is " + viewHolder
                        .province.hashCode());

                // 选择市
                selectCity();
            }
        });
    }

    /**
     * 选择城市
     */
    private void selectCity() {
        Log.i(LOG_TAG + "selectCity", "select city is invoked");
        int code = (int) viewHolder.province.get(CityConst.CODE);
        String name = (String) viewHolder.province.get(CityConst.NAME);

        // 初始化参数
        viewHolder.city = null;
        viewHolder.district = null;

        // 省份数据集
        List<Map<String, Object>> cityList = viewHolder.cityOperator.getCityList(code);

        // 加入头数据
        Map<String, Object> map = new HashMap<>();

        map.put(CityConst.NAME, name);
        map.put(CityConst.CODE, FIRST_SELECT);
        cityList.add(0, map);

        // 加入尾数据
        map = new HashMap<>();

        map.put(CityConst.NAME, context.getString(R.string.back));
        map.put(CityConst.CODE, BACK_SELECT);
        cityList.add(map);

        // 获取数据源适配器
        final SimpleAdapter adapter = new SimpleAdapter(context, cityList, R.layout
                .only_text_list_item, new String[]{CityConst.NAME}, new int[]{R.id.only_text_list_item_textView});

        Log.i(LOG_TAG + "selectCity", "adapter reference is " + adapter.hashCode());

        // 重置列表适配器
        viewHolder.selectListView.setAdapter(adapter);

        // 重置选中事件
        viewHolder.selectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Log.i(LOG_TAG + "selectCity", "list head is clicked");
                    // 选择结束
                    selectFinish();
                    return;
                }

                if (position == parent.getCount() - 1) {
                    // 返回省份列表
                    selectProvince();
                    return;
                }

                // 设置文本结果
                viewHolder.city = (Map<String, Object>) adapter.getItem(position);
                Log.i(LOG_TAG + "selectCity", "select position is " + position);
                Log.i(LOG_TAG + "selectCity", "select city reference is " + viewHolder.city
                        .hashCode());
                // 选择区
                selectDistrict();
            }
        });
    }

    /**
     * 选择区
     */
    private void selectDistrict() {
        Log.i(LOG_TAG + "selectDistrict", "select district is invoked");
        int code = (int) viewHolder.city.get(CityConst.CODE);
        String name = (String) viewHolder.city.get(CityConst.NAME);

        // 初始化参数
        viewHolder.district = null;

        // 省份数据集
        List<Map<String, Object>> districtList = viewHolder.cityOperator.getDistrictList(code);

        // 加入头数据
        Map<String, Object> map = new HashMap<>();

        map.put(CityConst.NAME, name);
        map.put(CityConst.CODE, FIRST_SELECT);
        districtList.add(0, map);

        // 加入尾数据
        map = new HashMap<>();

        map.put(CityConst.NAME, context.getString(R.string.back));
        map.put(CityConst.CODE, BACK_SELECT);
        districtList.add(map);

        // 获取数据源适配器
        final SimpleAdapter adapter = new SimpleAdapter(context, districtList, R.layout
                .only_text_list_item, new String[]{CityConst.NAME}, new int[]{R.id.only_text_list_item_textView});

        Log.i(LOG_TAG + "selectCity", "adapter reference is " + adapter.hashCode());

        // 重置列表适配器
        viewHolder.selectListView.setAdapter(adapter);

        // 重置选中事件
        viewHolder.selectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    Log.i(LOG_TAG + "selectCity", "list head is clicked");
                    // 选择结束
                    selectFinish();
                    return;
                }

                if (position == parent.getCount() - 1) {
                    // 返回城市列表
                    selectCity();
                    return;
                }

                // 设置文本结果
                viewHolder.district = (Map<String, Object>) adapter.getItem(position);
                Log.i(LOG_TAG + "selectDistrict", "select position is " + position);
                Log.i(LOG_TAG + "selectDistrict", "select district reference is " + viewHolder
                        .district.hashCode());

                // 选择结束
                selectFinish();
            }
        });
    }

    /**
     * 选择完成
     */
    private void selectFinish() {

        if (citySelectFinishedListener != null) {
            citySelectFinishedListener.onCitySelectFinish(viewHolder.province != null ? (String)
                    viewHolder.province.get(CityConst.NAME) : context.getString(R.string
                    .entire_country), viewHolder.city != null ? (String) viewHolder.city.get
                    (CityConst.NAME) : null, viewHolder.district != null ? (String) viewHolder
                    .district.get(CityConst.NAME) : null);
        }
    }
}
