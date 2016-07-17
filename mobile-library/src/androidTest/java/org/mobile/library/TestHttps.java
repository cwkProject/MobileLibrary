package org.mobile.library;
/**
 * Created by 超悟空 on 2016/7/16.
 */

import android.util.Log;

import org.junit.Test;
import org.mobile.library.network.factory.CommunicationBuilder;
import org.mobile.library.network.factory.NetworkType;
import org.mobile.library.network.util.SyncCommunication;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * 测试信任的https连接
 *
 * @author 超悟空
 * @version 1.0 2016/7/16
 * @since 1.0
 */
public class TestHttps {

    /**
     * 连通性测试
     *
     * @throws Exception
     */
    @Test
    public void connect() throws Exception {
        // 网络连接工具
        SyncCommunication communication;

        // 新建通讯工具
        communication = CommunicationBuilder.CreateSyncCommunication(NetworkType.GET);

        communication.setTaskName("https://www.iportpay.cn");

        communication.Request(null);

        String result = (String) communication.Response();

        communication.close();
        assertEquals("This is the gateway Rest api.", result.trim());
    }

    /**
     * post测试
     *
     * @throws Exception
     */
    @Test
    public void post() throws Exception {
        // 网络连接工具
        SyncCommunication communication;

        // 新建通讯工具
        communication = CommunicationBuilder.CreateSyncCommunication(NetworkType.POST);

        communication.setTaskName("https://www.iportpay.cn/order");

        // 参数
        Map<String, String> map = new HashMap<>();

        map.put("app_id", "10000002");
        map.put("function_type", "1");
        map.put("payment_mode", "'direct'");
        map.put("subject", "速配货信息费");
        map.put("total_fee", "0.01");
        map.put("payee", "200100000001");
        map.put("payer", "200100000002");
        map.put("body", "速配货信息费");
        map.put("sign", "tzpAiWETx7IyBVfWMJxy3HR75b3IUNNp8efG9qKbS+9Tc6R65KQk05d28LjR8o8XUxg" +
                "/d15CAeyF8emUoQWzLdcH9JP5ZLQaij+xCLohRPfzVNqiKTijHUDSOC" +
                "/MioTw9CQgr3Y6lVDytbfbtmqMbPi9XUW6XlLfQw/ZKS8WVtc=");

        communication.Request(map);

        String result = (String) communication.Response();

        communication.close();
        assertNotNull(result);
        Log.i("post", result);
    }
}
