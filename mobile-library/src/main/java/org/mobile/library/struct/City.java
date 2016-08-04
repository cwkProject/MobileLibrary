package org.mobile.library.struct;
/**
 * Created by 超悟空 on 2016/3/4.
 */

/**
 * 城市数据结构
 *
 * @author 超悟空
 * @version 1.0 2016/3/4
 * @since 1.0
 */
public class City {

    /**
     * 城市编号
     */
    private int code = 0;

    /**
     * 城市名称
     */
    private String name = null;

    /**
     * 城市所在省份编号
     */
    private int province_code = 0;

    /**
     * 获取城市编号
     *
     * @return 城市编号
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取城市名称
     *
     * @return 城市名
     */
    public String getName() {
        return name;
    }

    /**
     * 获取城市所在省份编号
     *
     * @return 省份编号
     */
    public int getProvince_code() {
        return province_code;
    }

    /**
     * 构造函数
     *
     * @param province_code 省份编号
     * @param code          城市编号
     * @param name          城市名称
     */
    public City(int province_code, int code, String name) {
        this.code = code;
        this.name = name;
        this.province_code = province_code;
    }
}
