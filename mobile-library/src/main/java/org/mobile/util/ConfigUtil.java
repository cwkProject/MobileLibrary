package org.mobile.util;

/**
 * Created by 超悟空 on 2015/6/11.
 */

import org.mobile.model.config.PersistenceConfigModel;

/**
 * 持久化的全局配置对象
 *
 * @author 超悟空
 * @version 1.0 2015/6/11
 * @since 1.0
 */
public class ConfigUtil extends PersistenceConfigModel {

    /**
     * 全局配置对象
     */
    private static ConfigUtil ourInstance = new ConfigUtil();

    /**
     * 标记是否保存用户名密码
     */
    private boolean loginSave = false;

    /**
     * 标记是否自动登录
     */
    private boolean loginAuto = false;

    /**
     * 用户名
     */
    private String userName = null;

    /**
     * 密码
     */
    private String password = null;

    /**
     * 获取全局配置
     *
     * @return 全局配置对象
     */
    public static ConfigUtil getInstance() {
        return ourInstance;
    }

    /**
     * 构造函数
     */
    private ConfigUtil() {
        // 父类构造函数，传入全局内容提供者
        super(ContextUtil.getContext());
        // 加载数据
        Refresh();
    }

    @Override
    protected void onDefault() {
        super.onDefault();
        loginSave = false;
        loginAuto = false;
        userName = null;
        password = null;
    }

    /**
     * 判断是否保存密码
     *
     * @return 返回标记
     */
    public boolean isLoginSave() {
        return loginSave;
    }

    /**
     * 设置记住密码状态
     *
     * @param flag 标记
     */
    public void setLoginSave(boolean flag) {
        this.loginSave = flag;
    }

    /**
     * 判断是否自动登录
     *
     * @return 返回标记
     */
    public boolean isLoginAuto() {
        return loginAuto;
    }

    /**
     * 设置自动登录状态
     *
     * @param flag 标记
     */
    public void setLoginAuto(boolean flag) {
        this.loginAuto = flag;
    }

    /**
     * 得到用户名
     *
     * @return 用户名字符串
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 设置用户名
     *
     * @param userName 用户名字符串
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 得到密码
     *
     * @return 密码字符串
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码字符串
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
