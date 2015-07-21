package org.mobile.adapter;
/**
 * Created by 超悟空 on 2015/7/16.
 */

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import org.mobile.database.city.CityConst;
import org.mobile.database.city.CityOperator;
import org.mobile.library.R;

/**
 * 选择列表数据适配器工厂
 *
 * @author 超悟空
 * @version 1.0 2015/7/16
 * @since 1.0
 */
public class SelectListAdapterFactory {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "SelectListAdapterFactory.";

    /**
     * 车型数据适配器
     */
    private static ArrayAdapter<String> vehicleTypeAdapter = null;

    /**
     * 车长数据适配器
     */
    private static ArrayAdapter<String> vehicleLengthAdapter = null;

    /**
     * 车牌数据适配器
     */
    private static ArrayAdapter<String> vehiclePlateNumberAdapter = null;

    /**
     * 省份数据适配器
     */
    private static SimpleAdapter provinceAdapter = null;

    /**
     * 创建数据适配器
     *
     * @param context  上下文
     * @param dataType 选择列表数据类型
     *
     * @return 数据源适配器
     */
    public static ListAdapter CreateAdapter(Context context, DataType dataType) {

        switch (dataType) {
            case VEHICLE_TYPE:
                // 车型
                return getVehicleTypeAdapter(context);
            case VEHICLE_LENGTH:
                // 车长
                return getVehicleLengthAdapter(context);
            case VEHICLE_PLATE_NUMBER:
                // 车牌
                return getVehiclePlateNumberAdapter(context);
            default:
                return null;
        }
    }

    /**
     * 创建简单级联数据适配器
     *
     * @param context  上下文
     * @param dataType 选择列表数据类型
     * @param code     上级代码
     *
     * @return 数据源适配器
     */
    public static SimpleAdapter CreateSimpleAdapter(Context context, DataType dataType, int code) {

        switch (dataType) {
            case PROVINCE:
                // 车型
                return getProvinceAdapter(context);
            case CITY:
                // 车长
                return getCityAdapter(context, code);
            case DISTRICT:
                // 车牌
                return getDistrictAdapter(context, code);
            default:
                return null;
        }
    }

    /**
     * 生成车型数据适配器
     *
     * @param context 上下文
     *
     * @return 适配器对象
     */
    private static ArrayAdapter<String> getVehicleTypeAdapter(Context context) {
        if (vehicleTypeAdapter == null) {
            // 没有则创建

            String[] vehicleTypeList = context.getResources().getStringArray(R.array
                    .vehicle_type_select_list);
            vehicleTypeAdapter = new ArrayAdapter<>(context, R.layout.only_text_list_item,
                    vehicleTypeList);
        }

        return vehicleTypeAdapter;
    }

    /**
     * 生成车长数据适配器
     *
     * @param context 上下文
     *
     * @return 适配器对象
     */
    private static ArrayAdapter<String> getVehicleLengthAdapter(Context context) {
        if (vehicleLengthAdapter == null) {
            // 没有则创建

            String[] vehicleLengthList = context.getResources().getStringArray(R.array
                    .vehicle_length_select_list);
            vehicleLengthAdapter = new ArrayAdapter<>(context, R.layout.only_text_list_item,
                    vehicleLengthList);
        }

        return vehicleLengthAdapter;
    }

    /**
     * 生成车牌数据适配器
     *
     * @param context 上下文
     *
     * @return 适配器对象
     */
    private static ArrayAdapter<String> getVehiclePlateNumberAdapter(Context context) {
        if (vehiclePlateNumberAdapter == null) {
            // 没有则创建

            String[] vehicleLengthList = context.getResources().getStringArray(R.array
                    .vehicle_plate_select_list);
            vehiclePlateNumberAdapter = new ArrayAdapter<>(context, R.layout.only_text_list_item,
                    vehicleLengthList);
        }

        return vehiclePlateNumberAdapter;
    }

    /**
     * 生成省份数据适配器
     *
     * @param context 上下文
     *
     * @return 适配器对象
     */
    private static SimpleAdapter getProvinceAdapter(Context context) {
        if (provinceAdapter == null) {
            // 没有则创建

            provinceAdapter = new SimpleAdapter(context, new CityOperator(context)
                    .getProvinceList(), R.layout.only_text_list_item, new String[]{CityConst.NAME}, new int[]{R.id.only_text_list_item_textView});
        }

        return provinceAdapter;
    }

    /**
     * 生成城市数据适配器
     *
     * @param context      上下文
     * @param provinceCode 指定的省份代码
     *
     * @return 适配器对象
     */
    private static SimpleAdapter getCityAdapter(Context context, int provinceCode) {
        return new SimpleAdapter(context, new CityOperator(context).getCityList(provinceCode), R
                .layout.only_text_list_item, new String[]{CityConst.NAME}, new int[]{R.id.only_text_list_item_textView});
    }

    /**
     * 生成城市数据适配器
     *
     * @param context  上下文
     * @param cityCode 指定的城市代码
     *
     * @return 适配器对象
     */
    private static SimpleAdapter getDistrictAdapter(Context context, int cityCode) {
        return new SimpleAdapter(context, new CityOperator(context).getDistrictList(cityCode), R
                .layout.only_text_list_item, new String[]{CityConst.NAME}, new int[]{R.id.only_text_list_item_textView});
    }

    /**
     * 选择列表数据适配器工厂使用的枚举，
     * 用于指示生成何种选择列表数据源适配器
     *
     * @author 超悟空
     * @version 1.0 2015/7/16
     * @since 1.0
     */
    public enum DataType {
        /**
         * 车牌号
         */
        VEHICLE_PLATE_NUMBER,
        /**
         * 车型
         */
        VEHICLE_TYPE,
        /**
         * 车长
         */
        VEHICLE_LENGTH,

        /**
         * 省份
         */
        PROVINCE,

        /**
         * 城市
         */
        CITY,

        /**
         * 区
         */
        DISTRICT
    }
}
