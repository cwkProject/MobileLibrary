package org.mobile.library.util;
/**
 * Created by 超悟空 on 2015/4/23.
 */

import org.mobile.library.model.config.TemporaryConfigModel;

/**
 * 保存当前应用版本信息的数据类
 *
 * @author 超悟空
 * @version 1.0 2015/4/23
 * @since 1.0
 */
public class ApplicationVersion extends TemporaryConfigModel {

    /**
     * 自身的静态对象
     */
    private static ApplicationVersion applicationVersion = new ApplicationVersion();

    /**
     * 标识当前是否为最新版本，默认为true
     */
    private boolean latestVersion = true;

    /**
     * 最新版本名
     */
    private String latestVersionName = null;

    /**
     * 最新版本地址
     */
    private String latestVersionUrl = null;

    /**
     * 最新版本文件下载ID
     */
    private long appFileId = 0;

    /**
     * 私有构造函数
     */
    private ApplicationVersion() {
        super();
    }

    /**
     * 获取全局临时数据对象
     *
     * @return 数据对象
     */
    public static ApplicationVersion getVersionManager() {

        return applicationVersion;
    }

    @Override
    protected void onCreate() {
        setLatestVersion(true);
        setLatestVersionName(null);
        setLatestVersionUrl(null);
    }

    /**
     * 判断当前是否为最新版本
     *
     * @return true为最新版，false表示存在新版本
     */
    public boolean isLatestVersion() {
        return latestVersion;
    }

    /**
     * 设置当前版本状态
     *
     * @param latestVersion 标记是否为最新版
     */
    public void setLatestVersion(boolean latestVersion) {
        this.latestVersion = latestVersion;
    }

    /**
     * 获取最新版本名
     *
     * @return 版本名字符串
     */
    public String getLatestVersionName() {
        return latestVersionName;
    }

    /**
     * 设置最新版本名
     *
     * @param latestVersionName 版本名字符串
     */
    public void setLatestVersionName(String latestVersionName) {
        this.latestVersionName = latestVersionName;
    }

    /**
     * 获取最新版本下载地址
     *
     * @return 地址字符串
     */
    public String getLatestVersionUrl() {
        return latestVersionUrl;
    }

    /**
     * 设置最新版本下载地址
     *
     * @param url 地址字符串
     */
    public void setLatestVersionUrl(String url) {
        this.latestVersionUrl = url;
    }

    /**
     * 获取文件下载ID
     *
     * @return ID值
     */
    public long getAppFileId() {
        return appFileId;
    }

    /**
     * 设置文件下载ID
     *
     * @param appFileId ID值
     */
    public void setAppFileId(long appFileId) {
        this.appFileId = appFileId;
    }
}
