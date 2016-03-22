package org.mobile.library;
/**
 * Created by 超悟空 on 2016/3/19.
 */

import android.util.Log;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mobile.library.global.GlobalApplication;
import org.mobile.library.model.data.base.SimpleJsonDataModel;
import org.mobile.library.network.factory.CommunicationFactory;
import org.mobile.library.network.factory.NetworkType;
import org.mobile.library.network.util.SyncCommunication;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * 测试带有签名的服务接口
 *
 * @author 超悟空
 * @version 1.0 2016/3/19
 * @since 1.0
 */
public class TestHttpWithSign {

    private class TestData extends SimpleJsonDataModel {
        /**
         * 参数1
         */
        public String param1 = null;

        /**
         * 参数2
         */
        public String param2 = null;

        /**
         * 返回结果
         */
        public String result = null;

        @Override
        protected void onExtractData(JSONObject jsonData) throws Exception {
            result = jsonData.getJSONArray(DATA_TAG).toString();
        }

        @Override
        protected void onFillRequestParameters(Map<String, String> dataMap) {
            dataMap.put("Param1", param1);
            dataMap.put("Param2", param2);
        }
    }

    private static final String URL = "http://218.92.115.55/M_Platform/Test/Test.aspx";

    private static final String APP_CODE = "APPTEST";

    private static final String APP_TOKEN = "2E62847737E0C040E053A864016AC040";

    private TestData testData = null;

    @Before
    public void setUp() throws Exception {
        GlobalApplication.getApplicationAttribute().setAppCode(APP_CODE);
        GlobalApplication.getApplicationAttribute().setAppToken(APP_TOKEN);

        testData = new TestData();

        testData.param1 = "参数1";
        testData.param2 = "参数2";
    }

    @Test
    public void httpGet() throws Exception {
        SyncCommunication communication = CommunicationFactory.CreateSyncCommunication(NetworkType.HTTP_GET);

        communication.setTaskName(URL);

        // 任务开始时间
        final long startTime = System.currentTimeMillis();

        Map<String, String> map = testData.serialization();

        Log.i("TestHttpWithSign" + "httpGet", "sign cast time " +
                (System.currentTimeMillis() - startTime));

        String sign = map.get("Sign");

        communication.Request(map);

        Log.i("TestHttpWithSign" + "httpGet", "request cast time " + (System.currentTimeMillis() -
                startTime));

        assertTrue(communication.isSuccessful());

        Object result=communication.Response();
        assertNotNull(result);

        testData.parse((String) result);

        assertEquals(String.format("[\"参数1\",\"参数2\",\"APPTEST\",\"%s\"]", sign), testData.result);
    }
}
