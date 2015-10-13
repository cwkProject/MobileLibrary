package org.mobile.library;
/**
 * Created by 超悟空 on 2015/9/15.
 */

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mobile.library.network.communication.ICommunication;
import org.mobile.library.network.factory.CommunicationFactory;
import org.mobile.library.network.factory.NetworkType;
import org.mobile.library.parser.InputStreamToStringParser;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * HttpURLConnectionGetCommunication网络连接单元测试
 *
 * @author 超悟空
 * @version 1.0 2015/9/15
 * @since 1.0
 */
public class TestHttpURLConnectionGetCommunication {

    /**
     * 线程池线程数
     */
    private static final int POOL_COUNT = Runtime.getRuntime().availableProcessors() * 2 + 2;

    /**
     * 网络连接工具
     */
    private ICommunication communication = null;

    @Before
    public void setUp() throws Exception {
        // 新建通讯工具
        communication = CommunicationFactory.Create(NetworkType.HTTP_GET);

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

        assertTrue(communication.Response() instanceof InputStream);
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

        // 流解析器
        InputStreamToStringParser parser = new InputStreamToStringParser();

        String result = parser.DataParser((InputStream) communication.Response());

        communication.close();
        assertEquals("123\nabc\n456", result);
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


        // 流解析器
        InputStreamToStringParser parser = new InputStreamToStringParser();

        String result = parser.DataParser((InputStream) communication.Response());
        communication.close();
        assertEquals("测试测试", result);
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

        communication.Request(map);

        // 流解析器
        InputStreamToStringParser parser = new InputStreamToStringParser();

        parser.setEncoded("GBK");
        String result = parser.DataParser((InputStream) communication.Response());
        communication.close();
        assertNotEquals("测试测试", result);
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

        // 流解析器
        InputStreamToStringParser parser = new InputStreamToStringParser();

        String result = parser.DataParser((InputStream) communication.Response());

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

        ICommunication communication1= CommunicationFactory.Create(NetworkType.HTTP_GET);

        communication1.setTaskName("http://218.92.115.55/WlkgbsgsApp/Service/test.aspx");

        communication1.Request(map1);

        // 流解析器
        InputStreamToStringParser parser = new InputStreamToStringParser();

        String result = parser.DataParser((InputStream) communication1.Response());
        communication1.close();

        assertEquals("测试1", result);

        ICommunication communication2= CommunicationFactory.Create(NetworkType.HTTP_GET);

        communication2.setTaskName("http://218.92.115.55/WlkgbsgsApp/Service/test.aspx");

        communication2.Request(map2);

        ICommunication communication3= CommunicationFactory.Create(NetworkType.HTTP_GET);

        communication3.setTaskName("http://218.92.115.55/WlkgbsgsApp/Service/test.aspx");

        communication3.Request(map3);

        result = parser.DataParser((InputStream) communication2.Response());
        communication2.close();

        assertEquals("测试2", result);

        communication3.Request(map3);
        communication2.close();

        result = parser.DataParser((InputStream) communication3.Response());
        communication3.close();

        assertEquals("测试3", result);
    }

}
