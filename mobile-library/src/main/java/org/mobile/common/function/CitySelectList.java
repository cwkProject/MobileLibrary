package org.mobile.common.function;
/**
 * Created by 超悟空 on 2015/7/18.
 */

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.mobile.adapter.SelectListAdapterFactory;
import org.mobile.database.city.CityConst;
import org.mobile.library.R;

import java.util.Map;

/**
 * 城市选择列表
 *
 * @author 超悟空
 * @version 1.0 2015/7/18
 * @since 1.0
 */
public class CitySelectList {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "CitySelectDrawer.";

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
         * 列表头布局
         */
        public View headView = null;

        /**
         * 列表头文本控件
         */
        public TextView headTextView = null;

        /**
         * 列表尾布局
         */
        public View footView = null;

        /**
         * 列表尾文本控件
         */
        public TextView footTextView = null;

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
     * 城市选择取消回调
     *
     * @author 超悟空
     * @version 1.0 2015/7/17
     * @since 1.0
     */
    public interface CitySelectCanceledListener {
        /**
         * 城市选择取消
         */
        void onCitySelectCancel();
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
     * 城市选择取消回调
     */
    private CitySelectCanceledListener citySelectCanceledListener = null;

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
     * 设置城市选择取消监听器
     *
     * @param citySelectCanceledListener 监听器实例
     */
    public void setCitySelectCanceledListener(CitySelectCanceledListener
                                                      citySelectCanceledListener) {
        this.citySelectCanceledListener = citySelectCanceledListener;
    }

    /**
     * 初始化
     */
    private void init() {
        // 初始化控件集
        initViewHolder();
        // 初始化输入框选择响应
        initSelectEdit();
    }

    /**
     * 初始化控件集引用
     */
    private void initViewHolder() {

        // 列表头
        // 创建头布局
        viewHolder.headView = LayoutInflater.from(context).inflate(R.layout.only_text_list_item,
                null, false);
        // 列表头文本框
        viewHolder.headTextView = (TextView) viewHolder.headView.findViewById(R.id
                .only_text_list_item_textView);
        // 列表尾
        viewHolder.footView = LayoutInflater.from(context).inflate(R.layout.only_text_list_item,
                null, false);
        // 列表尾文本框
        viewHolder.footTextView = (TextView) viewHolder.footView.findViewById(R.id
                .only_text_list_item_textView);
    }

    /**
     * 初始化输入框选择响应
     */
    private void initSelectEdit() {

        // 初始化列表头点击事件
        viewHolder.headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_TAG + "initSelectEdit", "list head is clicked");
                // 选择结束
                selectFinish();
            }
        });

        // 添加头布局
        viewHolder.selectListView.addHeaderView(viewHolder.headView);

        // 初始化列表尾文本
        viewHolder.footTextView.setText(R.string.back);

        // 初始化列表尾点击事件
        viewHolder.footView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_TAG + "initSelectEdit", "list foot is clicked");
                // 回到上级列表

                if (viewHolder.province == null) {
                    // 当前为省份列表，取消选择
                    selectCancel();
                }

                if (viewHolder.city == null) {
                    // 当前为城市列表，回到省份列表
                    selectProvince();
                } else {
                    // 当前为区列表，回到城市列表
                    selectCity();
                }
            }
        });

        // 添加尾布局
        viewHolder.selectListView.addFooterView(viewHolder.footView);
    }

    /**
     * 配置选择列表并绑定选择事件
     */
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

        // 设置头文本内容
        viewHolder.headTextView.setText(R.string.entire_country);

        // 获取数据源适配器
        final SimpleAdapter adapter = SelectListAdapterFactory.CreateSimpleAdapter(context,
                SelectListAdapterFactory.DataType.PROVINCE, 0);
        Log.i(LOG_TAG + "selectProvince", "adapter reference is " + adapter.hashCode());
        // 重置列表适配器
        viewHolder.selectListView.setAdapter(adapter);

        // 重置选中事件
        viewHolder.selectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 设置文本结果
                viewHolder.province = (Map<String, Object>) adapter.getItem(position - 1);
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

        // 设置头文本内容
        viewHolder.headTextView.setText(name);

        // 获取数据源适配器
        final SimpleAdapter adapter = SelectListAdapterFactory.CreateSimpleAdapter(context,
                SelectListAdapterFactory.DataType.CITY, code);
        Log.i(LOG_TAG + "selectCity", "adapter reference is " + adapter.hashCode());
        // 重置列表适配器
        viewHolder.selectListView.setAdapter(adapter);

        // 重置选中事件
        viewHolder.selectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 设置文本结果
                viewHolder.city = (Map<String, Object>) adapter.getItem(position - 1);
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

        // 设置头文本内容
        viewHolder.headTextView.setText(name);

        // 获取数据源适配器
        final SimpleAdapter adapter = SelectListAdapterFactory.CreateSimpleAdapter(context,
                SelectListAdapterFactory.DataType.DISTRICT, code);
        Log.i(LOG_TAG + "selectDistrict", "adapter reference is " + adapter.hashCode());
        // 重置列表适配器
        viewHolder.selectListView.setAdapter(adapter);

        // 重置选中事件
        viewHolder.selectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 设置文本结果
                viewHolder.district = (Map<String, Object>) adapter.getItem(position - 1);
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

    /**
     * 选择取消
     */
    private void selectCancel() {
        if (citySelectCanceledListener != null) {
            citySelectCanceledListener.onCitySelectCancel();
        }
    }
}
