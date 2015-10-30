package org.mobile.library;
/**
 * Created by 超悟空 on 2015/10/28.
 */

import android.os.Environment;
import android.util.Log;

import org.junit.Assert;
import org.junit.Test;
import org.mobile.library.network.util.SyncCommunication;
import org.mobile.library.network.factory.CommunicationFactory;
import org.mobile.library.network.factory.NetworkType;
import org.mobile.library.parser.InputStreamToStringParser;
import org.mobile.library.struct.FileInfo;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传单元测试
 *
 * @author 超悟空
 * @version 1.0 2015/10/28
 * @since 1.0
 */
public class TestUpload {

    /**
     * 文件上传地址
     */
    private static final String URL = "http://218.92.115.55/M_Lhgl/Service/Handover/UploadPicture" +
            ".aspx";

    /**
     * 文件参数名
     */
    private static final String NAME = "PicName";

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "TestUpload.";

    @Test
    public void upload() throws Exception {
        // 要上传的文件
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment
                .DIRECTORY_DOWNLOADS), "sphsj.apk");
        Assert.assertNotNull(file);
        Log.d(LOG_TAG + "upload", "file is " + file);
        // 参数
        Map<String, Object> map = new HashMap<>();

        // 加入文件
        map.put(NAME, new FileInfo(file));

        SyncCommunication communication = CommunicationFactory.CreateSyncCommunication(NetworkType.UPLOAD);

        communication.setTaskName(URL);

        communication.Request(map);

        // 流解析器
        InputStreamToStringParser parser = new InputStreamToStringParser();

        String result = parser.DataParser((InputStream) communication.Response());

        communication.close();
        Log.d(LOG_TAG + "upload", "result is " + result);
        Assert.assertNotNull(result);
    }
}
