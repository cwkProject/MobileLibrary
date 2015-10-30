package org.mobile.library.model.data.implement;
/**
 * Created by 超悟空 on 2015/6/11.
 */

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.mobile.library.model.data.base.JsonDataModel;
import org.mobile.library.global.ApplicationStaticValue;

import java.util.Map;

/**
 * 用户登录的数据模型类
 *
 * @author 超悟空
 * @version 1.0 2015/6/11
 * @since 1.0
 */
public class LoginData extends JsonDataModel {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "LoginData.";

    /**
     * 用户输入的用户名
     */
    private String userName = null;

    /**
     * 用户输入的密码
     */
    private String password = null;

    /**
     * 设备UUID
     */
    private String deviceToken = null;

    /**
     * 设备类型，默认{@link ApplicationStaticValue#DEVICE_TYPE}
     */
    private String deviceType = ApplicationStaticValue.DEVICE_TYPE;

    /**
     * 应用标识
     */
    private String appName = null;

    /**
     * 登录成功后返回的用户唯一标识符
     */
    private String userID = null;

    /**
     * 登录成功后返回的用户名称
     */
    private String nickname = null;

    /**
     * 部门编码
     */
    private String departmentCode = null;

    /**
     * 公司编码
     */
    private String companyCode = null;

    /**
     * 获取用户的标识符
     *
     * @return 返回标识符字符串
     */
    public String getUserID() {
        return userID;
    }

    /**
     * 设置用户密码
     *
     * @param password 密码字符串
     */
    public void setPassword(String password) {
        this.password = password;
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
     * 设置设备UUID
     *
     * @param deviceToken 唯一标识
     */
    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    /**
     * 设置设备类型
     *
     * @param deviceType 设备类型码，默认{@link ApplicationStaticValue#DEVICE_TYPE}
     */
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    /**
     * 设置应用标识
     *
     * @param appName 应用标识码
     */
    public void setAppName(String appName) {
        this.appName = appName;
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
     * 获取部门编号
     *
     * @return 编号字符串
     */
    public String getDepartmentCode() {
        return departmentCode;
    }

    /**
     * 获取公司编号
     *
     * @return 编号字符串
     */
    public String getCompanyCode() {
        return companyCode;
    }

    @Override
    protected void onFillRequestParameters(Map<String, String> dataMap) {
        // 加入用户名和密码
        dataMap.put("Logogram", userName);
        Log.i(LOG_TAG + "onFillRequestParameters", "Logogram is " + userName);
        dataMap.put("Password", password);
        Log.i(LOG_TAG + "onFillRequestParameters", "Password is " + password);
        dataMap.put("DeviceToken", deviceToken);
        Log.i(LOG_TAG + "onFillRequestParameters", "DeviceToken is " + deviceToken);
        dataMap.put("DeviceType", deviceType);
        Log.i(LOG_TAG + "onFillRequestParameters", "DeviceType is " + deviceType);
        dataMap.put("AppName", appName);
        Log.i(LOG_TAG + "onFillRequestParameters", "AppName is " + appName);
    }

    @Override
    protected boolean onRequestResult(JSONObject jsonResult) throws JSONException {
        // 得到执行结果
        String resultState = jsonResult.getString("IsLogin");

        return resultState != null && "yes".equals(resultState.trim().toLowerCase());
    }

    @Override
    protected String onRequestMessage(boolean result, JSONObject jsonResult) throws JSONException {
        return result ? null : jsonResult.getString("Message");
    }

    @Override
    protected void onRequestSuccess(JSONObject jsonResult) throws JSONException {
        userID = jsonResult.getString("Code_User");
        departmentCode = jsonResult.getString("Code_Department");
        companyCode = jsonResult.getString("Code_Company");
        nickname = jsonResult.getString("UserName");

        Log.i(LOG_TAG + "onRequestSuccess", "userID is " + this.userID);
        Log.i(LOG_TAG + "onRequestSuccess", "departmentCode is " + this.departmentCode);
        Log.i(LOG_TAG + "onRequestSuccess", "companyCode is " + this.companyCode);
        Log.i(LOG_TAG + "onRequestSuccess", "nickname is " + this.nickname);
    }
}
