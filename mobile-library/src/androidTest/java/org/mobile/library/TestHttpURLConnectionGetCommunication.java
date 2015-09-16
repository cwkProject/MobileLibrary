package org.mobile.library;
/**
 * Created by 超悟空 on 2015/9/15.
 */

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
     * 网络连接工具
     */
    private ICommunication communication = null;

    @Before
    public void setUp() throws Exception {
        // 新建通讯工具
        communication = CommunicationFactory.Create(NetworkType.HTTP_CONNECTION_GET);

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

        map.put("Data", "123abc");

        communication.Request(map);

        // 流解析器
        InputStreamToStringParser parser = new InputStreamToStringParser();

        String result = parser.DataParser((InputStream) communication.Response());

        communication.close();
        assertEquals("123abc", result);
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
}
