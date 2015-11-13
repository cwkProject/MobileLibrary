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
     * 文本类型
     */
    public static final int FILE_TYPE_TEXT = 1;

    /**
     * 图片类型
     */
    public static final int FILE_TYPE_IMAGE = 2;

    /**
     * 音频类型
     */
    public static final int FILE_TYPE_AUDIO = 3;

    /**
     * 视频类型
     */
    public static final int FILE_TYPE_VIDEO = 4;

    /**
     * 文件类型
     */
    public static final int FILE_TYPE_FILE = 5;

    /**
     * 自定义缓存类型
     */
    public static final int FILE_TYPE_CACHE_OBJECT = 6;

    /**
     * 倍数毫秒
     */
    public static final long MAGNITUDE_MILLISECOND = 1;

    /**
     * 倍数秒
     */
    public static final long MAGNITUDE_SECOND = 1000 * MAGNITUDE_MILLISECOND;

    /**
     * 倍数分钟
     */
    public static final long MAGNITUDE_MINUTE = 60 * MAGNITUDE_SECOND;

    /**
     * 倍数小时
     */
    public static final long MAGNITUDE_HOUR = 60 * MAGNITUDE_MINUTE;

    /**
     * 倍数天
     */
    public static final long MAGNITUDE_DAY = 24 * MAGNITUDE_HOUR;

    /**
     * 倍数周
     */
    public static final long MAGNITUDE_WEEK = 7 * MAGNITUDE_DAY;

    /**
     * 倍数月(30天)
     */
    public static final long MAGNITUDE_MOUNTH = 30 * MAGNITUDE_DAY;

    /**
     * 缓存key索引
     */
    private String key = null;

    /**
     * rom真实文件名
     */
    private String realFileName = null;

    /**
     * 原文件名
     */
    private String oldFileName = null;

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
     * 获取原文件名
     *
     * @return 文件名字符串
     */
    public String getOldFileName() {
        return oldFileName;
    }

    /**
     * 设置原文件名
     *
     * @param oldFileName 文件名字符串
     */
    public void setOldFileName(String oldFileName) {
        this.oldFileName = oldFileName;
    }

    /**
     * 获取文件类型<br>
     * 可选的值有{@link #FILE_TYPE_TEXT}，
     * {@link #FILE_TYPE_IMAGE}，
     * {@link #FILE_TYPE_AUDIO}，
     * {@link #FILE_TYPE_VIDEO}，
     * {@link #FILE_TYPE_FILE}，
     * {@link #FILE_TYPE_CACHE_OBJECT}
     *
     * @return 文件类型枚举
     */
    public int getType() {
        return type;
    }

    /**
     * 设置文件类型<br>
     * 可选的值有{@link #FILE_TYPE_TEXT}，
     * {@link #FILE_TYPE_IMAGE}，
     * {@link #FILE_TYPE_AUDIO}，
     * {@link #FILE_TYPE_VIDEO}，
     * {@link #FILE_TYPE_FILE}，
     * {@link #FILE_TYPE_CACHE_OBJECT}
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
     * 设置缓存超时时间，毫秒级<br>
     * 可以用预定义倍数计算，
     * {@link #MAGNITUDE_MILLISECOND}，
     * {@link #MAGNITUDE_SECOND}，
     * {@link #MAGNITUDE_MINUTE}，
     * {@link #MAGNITUDE_HOUR}，
     * {@link #MAGNITUDE_DAY}，
     * {@link #MAGNITUDE_WEEK}，
     * {@link #MAGNITUDE_MOUNTH}
     * <example>
     * // 设置超时时间为3天
     * CacheInfo cacheInfo=new CacheInfo("example", UUID,"root/web");
     * cacheInfo.setTimeOut(3 * CacheInfo.MAGNITUDE_DAY);
     * Log.i("example.setTimeOut","time is "+cacheInfo.getTimeOut());
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
