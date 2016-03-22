package org.mobile.library.global;
/**
 * Created by 超悟空 on 2015/6/11.
 */

import org.mobile.library.model.config.TemporaryConfigModel;

/**
 * 登录状态数据类
 *
 * @author 超悟空
 * @version 3.0 2016/3/19
 * @since 1.0
 */
public class LoginStatus extends TemporaryConfigModel {

    /**
     * 标记是否已登录
     */
    private boolean login = false;

    /**
     * 用户标识
     */
    private String userID = null;

    /**
     * 构造函数
     */
    public LoginStatus() {
        super();
    }

    @Override
    protected void onCreate() {
        // 初始化用户参数
        setLogin(false);
        setUserID(null);
    }

    /**
     * 判断是否登录
     *
     * @return 返回状态
     */
    public boolean isLogin() {
        return login;
    }

    /**
     * 设置登录状态
     *
     * @param flag 状态标识
     */
    public synchronized void setLogin(boolean flag) {
        this.login = flag;
    }

    /**
     * 获取用户标识
     *
     * @return 用户标识串
     */
    public String getUserID() {
        return userID;
    }

    /**
     * 设置用户标识
     *
     * @param userID 用户标识串
     */
    public synchronized void setUserID(String userID) {
        this.userID = userID;
    }
}
