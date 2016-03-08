package org.mobile.library.database.city;
/**
 * Created by 超悟空 on 2015/7/17.
 */

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.mobile.library.database.bean.City;
import org.mobile.library.database.bean.District;
import org.mobile.library.database.bean.Province;

import java.util.ArrayList;
import java.util.List;

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
        this.citySQLiteHelper = CitySQLiteHelper.getSqLiteHelper(context);
    }

    /**
     * 获取省份列表
     *
     * @return 省份列表集合
     */
    public List<Province> getProvinceList() {
        Log.i(LOG_TAG + "getProvinceList", "getProvinceList() is invoked");

        // 查询语句
        String sql = String.format("SELECT %s, %s FROM %s ORDER BY %s", CityConst.NAME, CityConst
                .CODE, CityConst.PROVINCE_TABLE, CityConst._ID);
        Log.i(LOG_TAG + "getProvinceList", "SQL is " + sql);

        // 查询数据
        Cursor cursor = citySQLiteHelper.getReadableDatabase().rawQuery(sql, null);

        // 列索引
        int nameIndex = cursor.getColumnIndex(CityConst.NAME);
        int codeIndex = cursor.getColumnIndex(CityConst.CODE);

        // 数据填充
        List<Province> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            // 一条记录
            Province province = new Province(cursor.getInt(codeIndex), cursor.getString(nameIndex));

            list.add(province);
        }

        // 关闭数据库
        cursor.close();
        citySQLiteHelper.close();

        return list;
    }

    /**
     * 获取指定省份的城市列表
     *
     * @param provinceCode 指定的省份代码
     *
     * @return 城市列表集合
     */
    public List<City> getCityList(int provinceCode) {
        Log.i(LOG_TAG + "getCityList", "getCityList(int) is invoked");

        // 查询语句
        String sql = String.format("SELECT %s, %s FROM %s where %s=%d ORDER BY %s", CityConst
                .NAME, CityConst.CODE, CityConst.CITY_TABLE, CityConst.PROVINCE_CODE,
                provinceCode, CityConst._ID);
        Log.i(LOG_TAG + "getCityList", "SQL is " + sql);

        // 查询数据
        Cursor cursor = citySQLiteHelper.getReadableDatabase().rawQuery(sql, null);

        // 列索引
        int nameIndex = cursor.getColumnIndex(CityConst.NAME);
        int codeIndex = cursor.getColumnIndex(CityConst.CODE);

        // 数据填充
        List<City> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            // 一条记录
            City city = new City(provinceCode, cursor.getInt(codeIndex), cursor.getString
                    (nameIndex));

            list.add(city);
        }

        // 关闭数据库
        cursor.close();
        citySQLiteHelper.close();

        return list;
    }

    /**
     * 获取指定城市的区列表
     *
     * @param cityCode 指定的城市代码
     *
     * @return 区列表集合
     */
    public List<District> getDistrictList(int cityCode) {
        Log.i(LOG_TAG + "getDistrictList", "getDistrictList(int) is invoked");

        // 查询语句
        String sql = String.format("SELECT %s, %s FROM %s where %s=%d ORDER BY %s", CityConst
                .NAME, CityConst.CODE, CityConst.DISTRICT_TABLE, CityConst.CITY_CODE, cityCode,
                CityConst._ID);
        Log.i(LOG_TAG + "getDistrictList", "SQL is " + sql);

        // 查询数据
        Cursor cursor = citySQLiteHelper.getReadableDatabase().rawQuery(sql, null);

        // 列索引
        int nameIndex = cursor.getColumnIndex(CityConst.NAME);
        int codeIndex = cursor.getColumnIndex(CityConst.CODE);

        // 数据填充
        List<District> list = new ArrayList<>();

        while (cursor.moveToNext()) {
            // 一条记录
            District district = new District(cityCode, cursor.getInt(codeIndex), cursor.getString
                    (nameIndex));

            list.add(district);
        }

        // 关闭数据库
        cursor.close();
        citySQLiteHelper.close();

        return list;
    }

    /**
     * 判断数据库表是否存在
     *
     * @return 存在返回true，不存在返回false
     */
    public synchronized final boolean isExist() {
        final String sql = String.format("select count(*) from sqlite_master where type='table' "
                + "and name in ('%s','%s','%s')", CityConst.PROVINCE_TABLE, CityConst.CITY_TABLE,
                CityConst.DISTRICT_TABLE);
        Cursor cursor = citySQLiteHelper.getReadableDatabase().rawQuery(sql, null);
        if (cursor.moveToNext()) {
            if (cursor.getInt(0) >= 3) {
                cursor.close();
                citySQLiteHelper.close();
                return true;
            }
        }
        cursor.close();
        citySQLiteHelper.close();
        return false;
    }
}
