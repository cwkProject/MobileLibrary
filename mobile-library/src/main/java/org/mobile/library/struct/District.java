package org.mobile.library.struct;
/**
 * Created by 超悟空 on 2016/3/4.
 */

/**
 * 区县数据结构
 *
 * @author 超悟空
 * @version 1.0 2016/3/4
 * @since 1.0
 */
public class District {

    /**
     * 区县编号
     */
    private int code = 0;

    /**
     * 区县名称
     */
    private String name = null;

    /**
     * 区县所在城市编号
     */
    private int city_code = 0;

    /**
     * 获取区县编号
     *
     * @return 编号
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取区县名称
     *
     * @return 区县名
     */
    public String getName() {
        return name;
    }

    /**
     * 获取区县所在城市编号
     *
     * @return 城市编号
     */
    public int getCity_code() {
        return city_code;
    }

    /**
     * 构造函数
     *
     * @param province_code 城市编号
     * @param code          区县编号
     * @param name          区县名称
     */
    public District(int province_code, int code, String name) {
        this.code = code;
        this.name = name;
        this.city_code = province_code;
    }
}
