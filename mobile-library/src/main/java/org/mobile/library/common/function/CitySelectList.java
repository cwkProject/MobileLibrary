package org.mobile.library.common.function;
/**
 * Created by 超悟空 on 2016/3/7.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.mobile.library.R;
import org.mobile.library.database.bean.City;
import org.mobile.library.database.bean.District;
import org.mobile.library.database.bean.Province;
import org.mobile.library.database.city.CityOperator;
import org.mobile.library.model.function.ISelectList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 城市选择功能
 *
 * @author 超悟空
 * @version 1.0 2016/3/7
 * @since 1.0
 */
public class CitySelectList implements ISelectList<Fragment, String> {

    /**
     * 选择结束监听器
     */
    private OnSelectedListener<Fragment, String> onSelectedListener = null;

    /**
     * 城市数据库工具
     */
    private CityOperator operator = null;

    /**
     * 城市列表
     */
    private Map<Integer, List<City>> cityMap = new HashMap<>();

    /**
     * 省份列表
     */
    private List<Province> provinceList = new ArrayList<>();

    /**
     * 区县列表
     */
    private Map<Integer, List<District>> districtMap = new HashMap<>();

    /**
     * 选择工具依托的选择布局
     */
    private CitySelectFragment fragment = null;

    /**
     * 构造函数
     *
     * @param context 上下文
     */
    public CitySelectList(Context context) {
        operator = new CityOperator(context);
    }

    @Override
    public void setOnSelectedListener(OnSelectedListener<Fragment, String> onSelectedListener) {
        this.onSelectedListener = onSelectedListener;
    }

    @Override
    public Fragment loadSelect() {
        if (fragment == null) {
            fragment = CitySelectFragment.getInstance(this);
        }

        return fragment;
    }

    /**
     * 选择完成
     *
     * @param result 选取的结果
     */
    private void onFinish(String result) {
        if (onSelectedListener != null) {
            onSelectedListener.onFinish(result);
        }
    }

    /**
     * 城市选择列表布局
     */
    public static class CitySelectFragment extends Fragment {

        /**
         * 控件工具
         */
        private class ViewHolder {

            /**
             * 省数据适配器
             */
            public ArrayAdapter<String> provinceAdapter = null;

            /**
             * 城市数据适配器
             */
            public ArrayAdapter<String> cityAdapter = null;

            /**
             * 区县数据适配器
             */
            public ArrayAdapter<String> districtAdapter = null;

            /**
             * 省数据列表
             */
            public ListView provinceListView = null;

            /**
             * 城市数据列表
             */
            public ListView cityListView = null;

            /**
             * 区县数据列表
             */
            public ListView districtListView = null;

            /**
             * 当前状态已选择的省份
             */
            public Province province = null;

            /**
             * 当前状态已选择的城市
             */
            public City city = null;
        }

        /**
         * 控件工具
         */
        private ViewHolder viewHolder = new ViewHolder();

        /**
         * 城市选择功能实例
         */
        private CitySelectList citySelectList = null;

        /**
         * 获取一个布局实例
         *
         * @param citySelectList 城市选择功能实例
         *
         * @return Fragment实例
         */
        public static CitySelectFragment getInstance(CitySelectList citySelectList) {
            CitySelectFragment fragment = new CitySelectFragment();
            fragment.citySelectList = citySelectList;
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
                savedInstanceState) {
            // 根布局
            View rootView = inflater.inflate(R.layout.fragment_city_select, container, false);

            // 初始化布局
            onInitView(rootView);

            // 初始化事件绑定
            onInitEvent();

            // 初始化数据
            onInitData();

            return rootView;
        }

        /**
         * 初始化布局
         *
         * @param rootView 根布局
         */
        private void onInitView(View rootView) {
            viewHolder.provinceListView = (ListView) rootView.findViewById(R.id
                    .fragment_city_select_province_listView);
            viewHolder.cityListView = (ListView) rootView.findViewById(R.id
                    .fragment_city_select_city_listView);
            viewHolder.districtListView = (ListView) rootView.findViewById(R.id
                    .fragment_city_select_district_listView);

            viewHolder.provinceAdapter = new ArrayAdapter<>(getActivity(), android.R.layout
                    .simple_list_item_activated_1);

            viewHolder.cityAdapter = new ArrayAdapter<>(getActivity(), android.R.layout
                    .simple_list_item_activated_1);

            viewHolder.districtAdapter = new ArrayAdapter<>(getActivity(), android.R.layout
                    .simple_list_item_activated_1);

            viewHolder.provinceListView.setAdapter(viewHolder.provinceAdapter);

            viewHolder.cityListView.setAdapter(viewHolder.cityAdapter);

            viewHolder.districtListView.setAdapter(viewHolder.districtAdapter);
        }

        /**
         * 初始化事件绑定
         */
        private void onInitEvent() {
            // 省份列表点击事件
            viewHolder.provinceListView.setOnItemClickListener(new AdapterView
                    .OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    viewHolder.province = null;
                    viewHolder.city = null;

                    if (position == 0) {
                        // 点击了全国标签
                        // 选择完成
                        citySelectList.onFinish((String) parent.getItemAtPosition(0));
                    } else {
                        viewHolder.province = citySelectList.provinceList.get(position - 1);
                        viewHolder.cityAdapter.clear();
                        viewHolder.districtAdapter.clear();

                        // 选中的省份编码
                        int code = viewHolder.province.getCode();

                        if (!citySelectList.cityMap.containsKey(code)) {
                            // 装载数据
                            citySelectList.cityMap.put(code, citySelectList.operator.getCityList
                                    (code));
                        }

                        // 加入头
                        viewHolder.cityAdapter.add(getString(R.string.any));

                        // 加入城市数据
                        for (City city : citySelectList.cityMap.get(code)) {
                            viewHolder.cityAdapter.add(city.getName());
                        }

                        viewHolder.cityAdapter.notifyDataSetChanged();
                        viewHolder.districtAdapter.notifyDataSetChanged();
                    }
                }
            });

            // 城市列表点击事件
            viewHolder.cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    viewHolder.city = null;

                    if (position == 0) {
                        // 点击了不限标签
                        // 选择完成
                        citySelectList.onFinish(viewHolder.province.getName());
                    } else {
                        viewHolder.city = citySelectList.cityMap.get(viewHolder.province.getCode
                                ()).get(position - 1);
                        viewHolder.districtAdapter.clear();

                        // 选中的城市编码
                        int code = viewHolder.city.getCode();

                        if (!citySelectList.districtMap.containsKey(code)) {
                            // 装载数据
                            citySelectList.districtMap.put(code, citySelectList.operator
                                    .getDistrictList(code));
                        }

                        // 加入头
                        viewHolder.districtAdapter.add(getString(R.string.any));

                        // 加入区县数据
                        for (District district : citySelectList.districtMap.get(code)) {
                            viewHolder.districtAdapter.add(district.getName());
                        }

                        viewHolder.districtAdapter.notifyDataSetChanged();
                    }
                }
            });

            // 区县列表点击事件
            viewHolder.districtListView.setOnItemClickListener(new AdapterView
                    .OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        // 点击了不限标签
                        // 选择完成
                        citySelectList.onFinish(viewHolder.province.getName() + "-" + viewHolder
                                .city.getName());
                    } else {
                        // 选择完成
                        citySelectList.onFinish(viewHolder.province.getName() + "-" + viewHolder
                                .city.getName() + "-" + parent.getItemAtPosition(position));
                    }
                }
            });
        }

        /**
         * 初始化数据
         */
        private void onInitData() {
            viewHolder.provinceAdapter.clear();
            viewHolder.cityAdapter.clear();
            viewHolder.districtAdapter.clear();

            if (citySelectList.provinceList.isEmpty()) {
                // 装载数据
                citySelectList.provinceList.addAll(citySelectList.operator.getProvinceList());
            }

            // 加入头
            viewHolder.provinceAdapter.add(getString(R.string.all_country));

            // 加入省份数据
            for (Province province : citySelectList.provinceList) {
                viewHolder.provinceAdapter.add(province.getName());
            }

            viewHolder.provinceAdapter.notifyDataSetChanged();
            viewHolder.cityAdapter.notifyDataSetChanged();
            viewHolder.districtAdapter.notifyDataSetChanged();
        }
    }
}
