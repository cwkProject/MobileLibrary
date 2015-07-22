package org.mobile.library.database.city;
/**
 * Created by 超悟空 on 2015/7/17.
 */

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 城市选择数据库操作类
 *
 * @author 超悟空
 * @version 1.0 2015/7/17
 * @since 1.0
 */
public class CityOperator {
    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "CityOperator.";

    /**
     * 城市数据库工具
     */
    private CitySQLiteHelper citySQLiteHelper = null;

    /**
     * 构造函数
     *
     * @param context 上下文
     */
    public CityOperator(Context context) {
        this.citySQLiteHelper = new CitySQLiteHelper(context);
    }

    /**
     * 获取省份列表
     *
     * @return 省份列表键值对集合，
     * {@link CityConst#NAME},省份名称，
     * {@link CityConst#CODE},省份代码
     */
    public List<Map<String, Object>> getProvinceList() {
        Log.i(LOG_TAG + "getProvinceList", "getProvinceList() is invoked");

        // 查询语句
        String sql = String.format("SELECT %s, %s FROM %s ORDER BY %s", CityConst.NAME, CityConst
                .CODE, CityConst.PROVINCE_TABLE, CityConst._ID);
        Log.i(LOG_TAG + "getProvinceList", "SQL is " + sql);

        return queryForMap(sql);
    }

    /**
     * 获取指定省份的城市列表
     *
     * @param provinceCode 指定的省份代码
     *
     * @return 城市列表键值对集合，
     * {@link CityConst#NAME},城市名称，
     * {@link CityConst#CODE},城市代码
     */
    public List<Map<String, Object>> getCityList(int provinceCode) {
        Log.i(LOG_TAG + "getCityList", "getCityList(int) is invoked");

        // 查询语句
        String sql = String.format("SELECT %s, %s FROM %s where %s=%d ORDER BY %s", CityConst
                .NAME, CityConst.CODE, CityConst.CITY_TABLE, CityConst.PROVINCE_CODE,
                provinceCode, CityConst._ID);
        Log.i(LOG_TAG + "getCityList", "SQL is " + sql);

        return queryForMap(sql);
    }

    /**
     * 获取指定城市的区列表
     *
     * @param cityCode 指定的城市代码
     *
     * @return 区列表键值对集合，
     * {@link CityConst#NAME},区名称，
     * {@link CityConst#CODE},区代码
     */
    public List<Map<String, Object>> getDistrictList(int cityCode) {
        Log.i(LOG_TAG + "getDistrictList", "getDistrictList(int) is invoked");

        // 查询语句
        String sql = String.format("SELECT %s, %s FROM %s where %s=%d ORDER BY %s", CityConst
                .NAME, CityConst.CODE, CityConst.DISTRICT_TABLE, CityConst.CITY_CODE, cityCode,
                CityConst._ID);
        Log.i(LOG_TAG + "getDistrictList", "SQL is " + sql);

        return queryForMap(sql);
    }

    /**
     * 查询并填充结果集合
     *
     * @param sql 查询语句
     *
     * @return 结果集合
     */
    private List<Map<String, Object>> queryForMap(String sql) {

        // 查询数据
        Cursor cursor = citySQLiteHelper.getReadableDatabase().rawQuery(sql, null);

        // 列索引
        int nameIndex = cursor.getColumnIndex(CityConst.NAME);
        int codeIndex = cursor.getColumnIndex(CityConst.CODE);

        // 数据填充
        List<Map<String, Object>> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            // 一条记录
            Map<String, Object> map = new HashMap<>();

            map.put(CityConst.NAME, cursor.getString(nameIndex));
            map.put(CityConst.CODE, cursor.getInt(codeIndex));

            list.add(map);
        }

        // 关闭数据库
        cursor.close();
        citySQLiteHelper.close();

        return list;
    }
}
