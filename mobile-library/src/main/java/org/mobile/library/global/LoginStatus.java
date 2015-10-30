package org.mobile.library.global;
/**
 * Created by 超悟空 on 2015/6/11.
 */

import org.mobile.library.model.config.TemporaryConfigModel;

/**
 * 登录状态数据类
 *
 * @author 超悟空
 * @version 2.0 2015/10/30
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
     * 部门标识
     */
    private String codeDepartment = null;

    /**
     * 公司标识
     */
    private String codeCompany = null;

    /**
     * 登录成功后返回的用户名称
     */
    private String nickname = null;

    /**
     * 登录的get请求地址，默认使用{@link ApplicationStaticValue#LOGIN_URL}
     */
    private String loginUrl = ApplicationStaticValue.LOGIN_URL;

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
        setCodeDepartment(null);
        setCodeCompany(null);
        setNickname(null);
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

    /**
     * 获取部门标识
     *
     * @return 部门标识串
     */
    public String getCodeDepartment() {
        return codeDepartment;
    }

    /**
     * 设置部门标识
     *
     * @param codeDepartment 部门标识串
     */
    public void setCodeDepartment(String codeDepartment) {
        this.codeDepartment = codeDepartment;
    }

    /**
     * 获取公司标识
     *
     * @return 公司标识串
     */
    public String getCodeCompany() {
        return codeCompany;
    }

    /**
     * 设置公司标识
     *
     * @param codeCompany 公司标识串
     */
    public void setCodeCompany(String codeCompany) {
        this.codeCompany = codeCompany;
    }

    /**
     * 获取用户昵称或姓名
     *
     * @return 名称字符串
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 设置用户昵称或姓名
     *
     * @param nickname 名称字符串
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 获取登录请求的地址
     *
     * @return get请求地址，默认使用{@link ApplicationStaticValue#LOGIN_URL}
     */
    public String getLoginUrl() {
        return loginUrl;
    }

    /**
     * 设置登录请求地址
     *
     * @param loginUrl get请求地址串
     */
    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }
}
