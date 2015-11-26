package org.mobile.library;
/**
 * Created by 超悟空 on 2015/9/16.
 */

import org.junit.Before;
import org.junit.Test;
import org.mobile.library.network.factory.CommunicationFactory;
import org.mobile.library.network.factory.NetworkType;
import org.mobile.library.network.util.AsyncCommunication;
import org.mobile.library.network.util.NetworkCallback;
import org.mobile.library.network.util.SyncCommunication;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * HttpPost网络连接单元测试
 *
 * @author 超悟空
 * @version 2.0 2015/11/2
 * @since 1.0
 */
public class TestHttpPostCommunication {

    /**
     * 网络连接工具
     */
    private SyncCommunication communication = null;

    @Before
    public void setUp() throws Exception {
        // 新建通讯工具
        communication = CommunicationFactory.CreateSyncCommunication(NetworkType.HTTP_POST);

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

        map.put("Data", "123abc");

        communication.Request(map);

        String result = (String) communication.Response();

        communication.close();
        assertEquals("123abc", result.trim());
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
                (NetworkType.HTTP_POST);

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

    /**
     * 临时测试接口连通性
     *
     * @throws Exception
     */
    @Test
    public void tempAsync() throws Exception {

        final Integer LOCK = 1;

        // 参数
        Map<String, String> map = new HashMap<>();

        map.put("fileName", "image.jpg");

        AsyncCommunication communication = CommunicationFactory.CreateAsyncCommunication
                (NetworkType.HTTP_POST);

        communication.setTaskName("http://10.199.10.220:8080/Service/Handover/UploadFile.aspx");

        communication.Request(map, new NetworkCallback<String>() {
            @Override
            public void onFinish(boolean result, String response) {

                assertTrue(result);
                //assertEquals("测试测试", response.trim());

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
