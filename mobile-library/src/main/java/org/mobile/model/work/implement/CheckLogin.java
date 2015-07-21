package org.mobile.model.work.implement;
/**
 * Created by 超悟空 on 2015/6/11.
 */

import android.util.Log;

import org.mobile.library.R;
import org.mobile.model.data.implement.LoginData;
import org.mobile.model.work.WorkModel;
import org.mobile.network.communication.ICommunication;
import org.mobile.network.factory.CommunicationFactory;
import org.mobile.network.factory.NetworkType;
import org.mobile.util.BroadcastUtil;
import org.mobile.util.ContextUtil;
import org.mobile.util.LoginStatus;

/**
 * 登录检查任务类
 *
 * @author 超悟空
 * @version 1.0 2015/6/11
 * @since 1.0
 */
public class CheckLogin extends WorkModel<String, String> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "CheckLogin.";

    @Override
    protected boolean onDoWork(String... parameters) {
        if (parameters == null || parameters.length < 2 || parameters[0] == null || parameters[1] == null) {
            // 数据异常
            Log.d(LOG_TAG + "onDoWork", "userName or password is null");
            // 设置失败原因
            setResult(ContextUtil.getContext().getString(R.string.login_error_parameter));
            // 发送登录状态改变广播
            BroadcastUtil.sendBroadcast(ContextUtil.getContext(), BroadcastUtil.MEMORY_STATE_LOGIN);
            return false;
        }

        // 登录相关临时参数
        LoginStatus config = LoginStatus.getLoginStatus();

        // 新建带有服务器公钥的通讯工具
        ICommunication communication = CommunicationFactory.Create(NetworkType.HTTP_GET);

        // 新建登录数据对象
        LoginData data = new LoginData();
        data.setUserName(parameters[0]);
        data.setPassword(parameters[1]);

        if (parameters.length>=3)
        {
            // 设置应用标识
            data.setAppName(parameters[2]);
        }

        if (parameters.length>=4)
        {
            // 设置设备UUID
            data.setDeviceToken(parameters[3]);
        }

        if (parameters.length>=5)
        {
            // 设置设备类型
            data.setDeviceType(parameters[4]);
        }

        // 设置调用的方法名
        communication.setTaskName(config.getLoginUrl());
        Log.i(LOG_TAG + "onDoWork", "task name is "+config.getLoginUrl());

        // 发送请求
        //noinspection unchecked
        communication.Request(data.serialization());

        // 解析数据
        if (!data.parse(communication.Response())) {
            // 解析失败
            // 设置失败原因
            setResult(ContextUtil.getContext().getString(R.string.login_error_field_required));
            // 发送登录状态改变广播
            BroadcastUtil.sendBroadcast(ContextUtil.getContext(), BroadcastUtil.MEMORY_STATE_LOGIN);
            return false;
        } else {
            // 解析成功的处理
            // 设置回显结果
            setResult(data.getMessage());

            if (data.isLogin()) {
                // 登录成功设置参数
                config.setLogin(true);
                config.setUserID(data.getUserID());
                config.setCodeCompany(data.getCompanyCode());
                config.setCodeDepartment(data.getDepartmentCode());
                config.setNickname(data.getNickname());
                // 发送登录状态改变广播
                BroadcastUtil.sendBroadcast(ContextUtil.getContext(), BroadcastUtil.MEMORY_STATE_LOGIN);
                return true;
            } else {
                // 登录失败
                // 发送登录状态改变广播
                BroadcastUtil.sendBroadcast(ContextUtil.getContext(), BroadcastUtil.MEMORY_STATE_LOGIN);
                return false;
            }
        }
    }
}
