package org.mobile.library;
/**
 * Created by 超悟空 on 2015/9/15.
 */

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mobile.library.network.communication.OkHttpGetSyncCommunication;
import org.mobile.library.network.factory.CommunicationFactory;
import org.mobile.library.network.factory.NetworkType;
import org.mobile.library.network.util.AsyncCommunication;
import org.mobile.library.network.util.NetworkCallback;
import org.mobile.library.network.util.SyncCommunication;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * HttpGet网络连接单元测试
 *
 * @author 超悟空
 * @version 2.0 2015/11/2
 * @since 1.0
 */
public class TestHttpGetCommunication {

    /**
     * 网络连接工具
     */
    private SyncCommunication communication = null;

    @Before
    public void setUp() throws Exception {
        // 新建通讯工具
        communication = CommunicationFactory.CreateSyncCommunication(NetworkType.HTTP_GET);

        communication.setTaskName("http://218.92.115.55/WlkgbsgsApp/Service/test.aspx");
    }

    /**
     * 连通性测试
     *
     * @throws Exception
     */
    @Test
    public void connect() throws Exception {

        communication.Request(null);

        assertTrue(communication.Response() instanceof String);
    }

    /**
     * 流解析器测试
     *
     * @throws Exception
     */
    @Test
    public void parser() throws Exception {

        // 参数
        Map<String, String> map = new HashMap<>();

        map.put("Data", "123\nabc\n456");

        communication.Request(map);

        String result = (String) communication.Response();

        communication.close();
        assertEquals("123\nabc\n456", result.trim());
    }

    /**
     * 请求编码测试
     *
     * @throws Exception
     */
    @Test
    public void requestEncode() throws Exception {

        // 参数
        Map<String, String> map = new HashMap<>();

        map.put("Data", "测试测试");

        communication.Request(map);

        String result = (String) communication.Response();
        communication.close();
        assertEquals("测试测试", result.trim());
    }

    /**
     * 响应编码测试
     *
     * @throws Exception
     */
    @Test
    public void responseEncode() throws Exception {

        // 参数
        Map<String, String> map = new HashMap<>();

        map.put("Data", "测试测试");

        OkHttpGetSyncCommunication okHttpGetSyncCommunication = (OkHttpGetSyncCommunication)
                communication;
        okHttpGetSyncCommunication.setEncoded("GBK");
        okHttpGetSyncCommunication.Request(map);

        String result = okHttpGetSyncCommunication.Response();
        okHttpGetSyncCommunication.close();
        assertNotEquals("测试测试", result.trim());
    }

    /**
     * 响应结果转为json对象
     *
     * @throws Exception
     */
    @Test
    public void jsonConvert() throws Exception {
        // 参数
        Map<String, String> map = new HashMap<>();

        map.put("Data", "{\"123\":\"abc\",\"456\":\"ab cd\"}");

        communication.Request(map);

        String result = (String) communication.Response();

        communication.close();

        JSONObject jsonObject = new JSONObject(result);

        assertEquals("{\"123\":\"abc\",\"456\":\"ab cd\"}", jsonObject.toString());
    }

    /**
     * 多线程模拟
     *
     * @throws Exception
     */
    @Test
    public void threadPool() throws Exception {

        // 参数
        Map<String, String> map1 = new HashMap<>();

        map1.put("Data", "测试1");

        // 参数
        Map<String, String> map2 = new HashMap<>();

        map2.put("Data", "测试2");

        // 参数
        Map<String, String> map3 = new HashMap<>();

        map3.put("Data", "测试3");

        SyncCommunication communication1 = CommunicationFactory.CreateSyncCommunication
                (NetworkType.HTTP_GET);

        communication1.setTaskName("http://218.92.115.55/WlkgbsgsApp/Service/test.aspx");

        communication1.Request(map1);

        String result = (String) communication1.Response();
        communication1.close();

        assertEquals("测试1", result.trim());

        SyncCommunication communication2 = CommunicationFactory.CreateSyncCommunication
                (NetworkType.HTTP_GET);

        communication2.setTaskName("http://218.92.115.55/WlkgbsgsApp/Service/test.aspx");

        communication2.Request(map2);

        SyncCommunication communication3 = CommunicationFactory.CreateSyncCommunication
                (NetworkType.HTTP_GET);

        communication3.setTaskName("http://218.92.115.55/WlkgbsgsApp/Service/test.aspx");

        communication3.Request(map3);

        result = (String) communication2.Response();
        communication2.close();

        assertEquals("测试2", result.trim());

        communication3.Request(map3);
        communication2.close();

        result = (String) communication3.Response();
        communication3.close();

        assertEquals("测试3", result.trim());
    }

    /**
     * 异步通信测试
     *
     * @throws Exception
     */
    @Test
    public void async() throws Exception {

        final Integer LOCK = 1;

        // 参数
        Map<String, String> map = new HashMap<>();

        map.put("Data", "测试测试");

        AsyncCommunication communication = CommunicationFactory.CreateAsyncCommunication
                (NetworkType.HTTP_GET);

        communication.setTaskName("http://218.92.115.55/WlkgbsgsApp/Service/test.aspx");

        communication.Request(map, new NetworkCallback<String>() {
            @Override
            public void onFinish(boolean result, String response) {

                assertTrue(result);
                assertEquals("测试测试", response.trim());

                synchronized (LOCK) {
                    LOCK.notify();
                }
            }
        });

        synchronized (LOCK) {
            LOCK.wait();
        }
    }
}
