package org.mobile.library.database.bean;
/**
 * Created by 超悟空 on 2016/3/4.
 */

/**
 * 省份数据结构
 *
 * @author 超悟空
 * @version 1.0 2016/3/4
 * @since 1.0
 */
public class Province {

    /**
     * 省份编号
     */
    private int code = 0;

    /**
     * 省份名称
     */
    private String name = null;

    /**
     * 获取省份编号
     *
     * @return 省份编号
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取省份名称
     *
     * @return 省份名
     */
    public String getName() {
        return name;
    }

    /**
     * 构造函数
     *
     * @param code 省份编号
     * @param name 省份名称
     */
    public Province(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
