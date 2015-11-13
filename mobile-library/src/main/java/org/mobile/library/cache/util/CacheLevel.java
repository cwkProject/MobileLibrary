package org.mobile.library.cache.util;
/**
 * Created by 超悟空 on 2015/11/12.
 */

/**
 * 持久化缓存数据层级对象，
 * 仅作为应用程序，缓存层级索引数据库，文件系统连接的缓存信息桥梁
 *
 * @author 超悟空
 * @version 1.0 2015/11/12
 * @since 1.0
 */
public class CacheLevel {

    /**
     * 缓存层级key索引
     */
    private String key = null;

    /**
     * 真实路径名
     */
    private String realPath = null;

    /**
     * 层级最大容量
     */
    private long maxCapacity = 0;

    /**
     * 子文件超时时间，默认0无限制
     */
    private long timeOut = 0;

    /**
     * 备注
     */
    private String remark = null;

    /**
     * 获取缓存层级键名
     *
     * @return 键名字符串
     */
    public String getKey() {
        return key;
    }

    /**
     * 获取缓存层级真实路径
     *
     * @return 路径字符串
     */
    public String getRealPath() {
        return realPath;
    }

    /**
     * 获取缓存层级最大容量
     *
     * @return 最大容量值
     */
    public long getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * 设置缓存层级最大容量
     *
     * @param maxCapacity 最大容量值
     */
    public void setMaxCapacity(long maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    /**
     * 获取子缓存默认超时时间，毫秒级
     *
     * @return 超时时间
     */
    public long getTimeOut() {
        return timeOut;
    }

    /**
     * 设置缓存超时时间，毫秒级<br>
     * 可以用预定义倍数计算，
     * {@link CacheInfo#MAGNITUDE_MILLISECOND}，
     * {@link CacheInfo#MAGNITUDE_SECOND}，
     * {@link CacheInfo#MAGNITUDE_MINUTE}，
     * {@link CacheInfo#MAGNITUDE_HOUR}，
     * {@link CacheInfo#MAGNITUDE_DAY}，
     * {@link CacheInfo#MAGNITUDE_WEEK}，
     * {@link CacheInfo#MAGNITUDE_MOUNTH}
     * <example>
     * // 设置超时时间为3天
     * CacheLevel cacheLevel=new CacheLevel("example", UUID);
     * cacheLevel.setTimeOut(3 * CacheInfo.MAGNITUDE_DAY);
     * Log.i("example.setTimeOut","time is "+cacheLevel.getTimeOut());
     * //*******
     * // 输出信息为： time is 259200000
     * <example/>
     *
     * @param timeOut 超时时间
     */
    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
    }

    /**
     * 获取缓存层级备注信息
     *
     * @return 备注信息
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置缓存层级备注信息
     *
     * @param remark 备注信息
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 构造函数
     *
     * @param key      缓存层级键名
     * @param realPath 缓存层级真实路径
     */
    public CacheLevel(String key, String realPath) {
        this.key = key;
        this.realPath = realPath;
    }
}
