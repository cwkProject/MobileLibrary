package org.mobile.model.data.implement;
/**
 * Created by 超悟空 on 2015/6/11.
 */

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.mobile.model.data.IDataModel;
import org.mobile.parser.HttpResponseHttpEntityToStringParser;
import org.mobile.util.StaticValueUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户登录的数据模型类
 *
 * @author 超悟空
 * @version 1.0 2015/6/11
 * @since 1.0
 */
public class LoginData implements IDataModel<Object, Map<String, String>> {

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
     * 设备类型，默认{@link org.mobile.util.StaticValueUtil#DEVICE_TYPE}
     */
    private String deviceType = StaticValueUtil.DEVICE_TYPE;

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
     * 登录结果消息字符串
     */
    private String message = null;

    /**
     * 登录结果
     */
    private boolean login = false;

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
     * @param deviceType 设备类型码，默认{@link org.mobile.util.StaticValueUtil#DEVICE_TYPE}
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
     * 获取登录结果的消息串
     *
     * @return 返回字符串
     */
    public String getMessage() {
        return message;
    }

    /**
     * 判断是否成功登录
     *
     * @return 成功返回true，失败返回false
     */
    public boolean isLogin() {
        return login;
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
    public Map<String, String> serialization() {
        Log.i(LOG_TAG + "serialization", "serialization start");
        // 序列化后的参数集
        Map<String, String> dataMap = new HashMap<>();

        // 加入用户名和密码
        dataMap.put("Logogram", userName);
        Log.i(LOG_TAG + "serialization", "Logogram is " + userName);
        dataMap.put("Password", password);
        Log.i(LOG_TAG + "serialization", "Password is " + password);
        dataMap.put("DeviceToken", deviceToken);
        Log.i(LOG_TAG + "serialization", "DeviceToken is " + deviceToken);
        dataMap.put("DeviceType", deviceType);
        Log.i(LOG_TAG + "serialization", "DeviceType is " + deviceType);
        dataMap.put("AppName", appName);
        Log.i(LOG_TAG + "serialization", "AppName is " + appName);

        return dataMap;
    }

    @Override
    public boolean parse(Object data) {
        Log.i(LOG_TAG + "parse", "parse start");

        if (data == null) {
            // 通信异常
            Log.d(LOG_TAG + "parse", "data is null");
            return false;
        }

        // 新建解析器
        HttpResponseHttpEntityToStringParser parser = new HttpResponseHttpEntityToStringParser();

        // 获取结果字符串
        String resultString = parser.DataParser(data);
        Log.i(LOG_TAG + "parse", "result string is " + resultString);

        try {
            // 将结果转换为JSON对象
            JSONObject jsonObject = new JSONObject(resultString);

            // 得到执行结果
            String resultState = jsonObject.getString("IsLogin");

            // 得到结果消息
            //message = jsonObject.getString("Message");

            if (resultState != null && "yes".equals(resultState.trim().toLowerCase())) {
                // 登录成功，提取结果
                login = true;
                userID = jsonObject.getString("Code_User");
                departmentCode = jsonObject.getString("Code_Department");
                companyCode = jsonObject.getString("Code_Company");
                nickname = jsonObject.getString("UserName");
            } else {
                // 登录失败
                login = false;
                message = jsonObject.getString("Error");
            }

            Log.i(LOG_TAG + "parse", "login result " + this.login);
            Log.i(LOG_TAG + "parse", "userID is " + this.userID);
            Log.i(LOG_TAG + "parse", "departmentCode is " + this.departmentCode);
            Log.i(LOG_TAG + "parse", "companyCode is " + this.companyCode);
            Log.i(LOG_TAG + "parse", "nickname is " + this.nickname);
            Log.i(LOG_TAG + "parse", "message is " + this.message);

            // 登录请求成功结束
            return true;
        } catch (JSONException e) {
            Log.e(LOG_TAG + "parse", "JSONException is " + e.getMessage());
            return false;
        }
    }
}
