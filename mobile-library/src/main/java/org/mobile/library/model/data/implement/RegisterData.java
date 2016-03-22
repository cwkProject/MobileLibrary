package org.mobile.library.model.data.implement;
/**
 * Created by 超悟空 on 2016/3/21.
 */

import org.json.JSONObject;
import org.mobile.library.model.data.base.SimpleJsonDataModel;

import java.util.Map;

/**
 * 用户注册数据模型
 *
 * @author 超悟空
 * @version 1.0 2016/3/21
 * @since 1.0
 */
public class RegisterData extends SimpleJsonDataModel {

    /**
     * 用户名
     */
    private String userName = null;

    /**
     * 密码1
     */
    private String password1 = null;

    /**
     * 密码2
     */
    private String password2 = null;

    /**
     * 手机号
     */
    private String mobile = null;

    /**
     * 用户ID
     */
    private String userId = null;

    /**
     * 设置用户名
     *
     * @param userName 用户名
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * 设置密码1
     *
     * @param password1 密码1
     */
    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    /**
     * 设置密码2
     *
     * @param password2 密码2
     */
    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    /**
     * 设置手机号
     *
     * @param mobile 手机号
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取用户id
     *
     * @return 用户id
     */
    public String getUserId() {
        return userId;
    }

    @Override
    protected void onExtractData(JSONObject jsonData) throws Exception {
        userId = jsonData.getString(DATA_TAG);
    }

    @Override
    protected void onFillRequestParameters(Map<String, String> dataMap) {
        dataMap.put("Logogram", userName);
        dataMap.put("Mobile", mobile);
        dataMap.put("Password1", password1);
        dataMap.put("Password2", password2);
    }
}
