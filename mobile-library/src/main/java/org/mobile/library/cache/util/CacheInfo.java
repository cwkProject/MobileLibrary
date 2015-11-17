package org.mobile.library.cache.util;
/**
 * Created by 超悟空 on 2015/11/10.
 */

/**
 * 持久化缓存数据信息对象，
 * 仅作为应用程序，缓存索引数据库，文件系统连接的缓存信息桥梁
 *
 * @author 超悟空
 * @version 1.0 2015/11/10
 * @since 1.0
 */
public class CacheInfo {

    /**
     * 缓存key索引
     */
    private String key = null;

    /**
     * rom真实文件名
     */
    private String realFileName = null;

    /**
     * 文件类型
     */
    private int type = 0;

    /**
     * 超时时间，默认0无限制
     */
    private long timeOut = 0;

    /**
     * 标识是否为组中的文件
     */
    private boolean groupTag = false;

    /**
     * 缓存层级key
     */
    private String levelKey = null;

    /**
     * 标识是否在外部存储
     */
    private boolean external = true;

    /**
     * 获取缓存键名
     *
     * @return 键名字符串
     */
    public String getKey() {
        return key;
    }

    /**
     * 获取真实文件名
     *
     * @return 文件名字符串
     */
    public String getRealFileName() {
        return realFileName;
    }

    /**
     * 获取文件类型<br>
     *
     * @return 文件类型枚举
     */
    public int getType() {
        return type;
    }

    /**
     * 设置文件类型<br>
     *
     * @param type 文件类型枚举
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * 获取缓存超时时间，毫秒级
     *
     * @return 超时时间
     */
    public long getTimeOut() {
        return timeOut;
    }

    /**
     * 设置缓存超时时间，毫秒级
     *
     * @param timeOut 超时时间
     */
    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
    }

    /**
     * 判断缓存是否属于缓存组
     *
     * @return true表示该缓存数据属于key指定的缓存组
     */
    public boolean isGroupTag() {
        return groupTag;
    }

    /**
     * 设置缓存是否属于缓存组
     *
     * @param groupTag 状态标识，true表示该缓存数据属于key指定的缓存组
     */
    public void setGroupTag(boolean groupTag) {
        this.groupTag = groupTag;
    }

    /**
     * 获取缓存层级key
     *
     * @return 缓存层级key
     */
    public String getLevelKey() {
        return levelKey;
    }

    /**
     * 判断该文件是否被保存于外部存储中
     *
     * @return true表示在外部存储
     */
    public boolean isExternal() {
        return external;
    }

    /**
     * 设置该文件是否在外部存储中
     *
     * @param external 状态标识
     */
    public void setExternal(boolean external) {
        this.external = external;
    }

    /**
     * 构造方法
     *
     * @param key          缓存key
     * @param realFileName 缓存rom真实文件名
     * @param levelKey     缓存层级key
     */
    public CacheInfo(String key, String realFileName, String levelKey) {
        this.key = key;
        this.realFileName = realFileName;
        this.levelKey = levelKey;
    }
}
