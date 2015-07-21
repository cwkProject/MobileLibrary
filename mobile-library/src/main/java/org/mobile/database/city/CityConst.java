package org.mobile.database.city;
/**
 * Created by 超悟空 on 2015/7/16.
 */

/**
 * 城市选择数据库常量
 *
 * @author 超悟空
 * @version 1.0 2015/7/16
 * @since 1.0
 */
public interface CityConst {

    /**
     * ID列
     */
    String _ID = "_id";

    /**
     * 数据库名
     */
    String DB_NAME = "city.db";

    /**
     * 数据库版本
     */
    int DB_VERSION = 1;

    /**
     * 编号列
     */
    String CODE = "code";

    /**
     * 名称列
     */
    String NAME = "name";

    /**
     * 省级表
     */
    String PROVINCE_TABLE = "province";

    /**
     * 市级表
     */
    String CITY_TABLE = "city";

    /**
     * 区级表
     */
    String DISTRICT_TABLE = "district";

    /**
     * 省编号列
     */
    String PROVINCE_CODE = "province_code";

    /**
     * 市编号列
     */
    String CITY_CODE = "city_code";
}
