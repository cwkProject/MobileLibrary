package org.mobile.library.model.data.util;
/**
 * Created by 超悟空 on 2016/3/19.
 */

import android.util.Log;

import org.mobile.library.global.GlobalApplication;
import org.mobile.library.struct.FileInfo;
import org.mobile.library.util.HashMD5;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 请求参数签名工具
 *
 * @author 超悟空
 * @version 1.0 2016/3/19
 * @since 1.0
 */
public class RequestSign {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "RequestSign.";

    /**
     * 应用编号
     */
    private static final String APP_CODE = "AppName";

    /**
     * 应用令牌
     */
    private static final String SIGN = "Sign";

    /**
     * 参数签名
     *
     * @param sendData 要发送的参数
     */
    public static void sign(Map<String, Object> sendData) {
        // 遍历sendData集合并加入请求参数对象
        if (sendData != null && !sendData.isEmpty() && GlobalApplication.getApplicationAttribute
                ().getAppCode() != null && GlobalApplication.getApplicationAttribute()
                .getAppToken() != null) {

            // key数组
            List<String> keyList = new ArrayList<>();

            // 遍历并追加参数
            for (Map.Entry<String, ?> dataEntry : sendData.entrySet()) {

                if (dataEntry.getValue() instanceof FileInfo || dataEntry.getValue() instanceof
                        File) {
                    // 排除文件类型
                    continue;
                }

                // 处理剩余情况，包括String，Integer，Boolean等类型
                if (dataEntry.getValue() != null) {
                    // 参数不为空，才参与签名
                    keyList.add(dataEntry.getKey());
                }
            }

            // 加入应用编号
            sendData.put(APP_CODE, GlobalApplication.getApplicationAttribute().getAppCode());
            keyList.add(APP_CODE);

            // 排序
            Collections.sort(keyList);

            StringBuilder builder = new StringBuilder();
            // 拼接
            for (String key : keyList) {
                builder.append(key).append(sendData.get(key));
            }

            // 拼接应用令牌
            builder.append(GlobalApplication.getApplicationAttribute().getAppToken());

            String sign = HashMD5.hash(builder.toString());

            // 加入签名串
            sendData.put(SIGN, sign);

            Log.i(LOG_TAG + "sign", "app code:" + GlobalApplication.getApplicationAttribute()
                    .getAppCode() + " sign:" + sign);
        } else {
            Log.i(LOG_TAG + "sign", "parameters has null");
        }
    }

    /**
     * 参数签名，针对纯文本参数优化
     *
     * @param sendData 要发送的参数
     */
    public static void signForText(Map<String, String> sendData) {
        // 遍历sendData集合并加入请求参数对象
        if (sendData != null && !sendData.isEmpty() && GlobalApplication.getApplicationAttribute
                ().getAppCode() != null && GlobalApplication.getApplicationAttribute()
                .getAppToken() != null) {

            // key数组
            List<String> keyList = new ArrayList<>();

            // 遍历并追加参数
            for (Map.Entry<String, String> dataEntry : sendData.entrySet()) {

                if (dataEntry.getValue() != null) {
                    // 参数不为空，才参与签名
                    keyList.add(dataEntry.getKey());
                }
            }

            // 加入应用编号
            sendData.put(APP_CODE, GlobalApplication.getApplicationAttribute().getAppCode());
            keyList.add(APP_CODE);

            // 排序
            Collections.sort(keyList);

            StringBuilder builder = new StringBuilder();
            // 拼接
            for (String key : keyList) {
                builder.append(key).append(sendData.get(key));
            }

            // 拼接应用令牌
            builder.append(GlobalApplication.getApplicationAttribute().getAppToken());

            String sign = HashMD5.hash(builder.toString());

            // 加入签名串
            sendData.put(SIGN, sign);

            Log.i(LOG_TAG + "signForText", "app code:" + GlobalApplication
                    .getApplicationAttribute().getAppCode() + " sign:" + sign);
        } else {
            Log.i(LOG_TAG + "signForText", "parameters has null");
        }
    }
}
