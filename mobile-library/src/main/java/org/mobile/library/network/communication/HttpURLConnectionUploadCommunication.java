package org.mobile.library.network.communication;
/**
 * Created by 超悟空 on 2015/10/27.
 */

import android.util.Log;

import org.mobile.library.network.util.SyncCommunication;
import org.mobile.library.struct.FileInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 基于HttpURLConnection的Post文件上传组件
 *
 * @author 超悟空
 * @version 1.0 2015/10/27
 * @since 1.0
 */
public class HttpURLConnectionUploadCommunication implements SyncCommunication<Map<String, Object>,
        InputStream> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "HttpURLConnectionUploadCommunication.";

    /**
     * 表单类型
     */
    private static final String MULTIPART_FORM_DATA = "multipart/form-data";

    /**
     * 数据结束符
     */
    private static final String PREFIX = "--";

    /**
     * 数据分隔符
     */
    private static final String BOUNDARY = "----" + UUID.randomUUID().toString();

    /**
     * 换行符
     */
    private static final String LINE_END = "\r\n";

    /**
     * 请求地址的完整路径
     */
    protected String url = null;

    /**
     * 请求数据编码，默认使用UTF-8
     */
    protected String encoded = "UTF-8";

    /**
     * 请求结果数据流
     */
    protected InputStream response = null;

    /**
     * 请求超时时间
     */
    protected int timeout = 0;

    /**
     * 网络连接
     */
    private HttpURLConnection httpURLConnection = null;

    /**
     * 设置超时时间
     *
     * @param timeout 超时时间，单位毫秒
     */
    public void setTimeout(int timeout) {
        Log.i(LOG_TAG + "setTimeout", "timeout is " + timeout);
        this.timeout = timeout;
    }

    /**
     * 设置编码格式
     *
     * @param encoded 编码字符串，默认为UTF-8
     */
    public void setEncoded(String encoded) {
        Log.i(LOG_TAG + "setEncoded", "encoded is " + encoded);
        this.encoded = encoded;
    }

    /**
     * 设置请求地址
     *
     * @param url 完整地址
     */
    @Override
    public void setTaskName(String url) {
        this.url = url;
        Log.i(LOG_TAG + "setTaskName", "url is " + this.url);
    }

    @Override
    public void Request(Map<String, Object> sendData) {
        Log.i(LOG_TAG + "Request", "Request(Map<String, Object>) start");
        Log.i(LOG_TAG + "Request", "url is " + url);

        if (url == null || !url.trim().startsWith("http://")) {
            // 地址不合法
            response = null;
            Log.d(LOG_TAG + "Request", "url is error");
            return;
        }

        try {
            // 新建URL对象
            URL urlObject = new URL(url);

            // 创建的连接对象
            httpURLConnection = (HttpURLConnection) urlObject.openConnection();

            // 设置连接参数
            onRequestProperty();

            // 新建要发送的文件集
            Map<String, FileInfo> fileInfoMap = new HashMap<>();

            // 提取请求参数
            String textForm = onExtractParameters(sendData, fileInfoMap);

            // 打开链接
            httpURLConnection.connect();

            // 获取发送流
            OutputStream outputStream = httpURLConnection.getOutputStream();

            // 写入文本数据
            if (textForm.length() > 0) {
                outputStream.write(textForm.getBytes(encoded));
            }

            // 写入文件数据
            onWriteFile(outputStream, fileInfoMap);

            Log.i(LOG_TAG + "Request", "request start");

            long startTime = System.currentTimeMillis();

            // 得到响应码
            int responseCode = httpURLConnection.getResponseCode();
            Log.i(LOG_TAG + "Request", "response code is " + responseCode);

            // 判断请求是否正常
            if (responseCode != HttpURLConnection.HTTP_OK) {
                response = null;
            } else {
                Log.i(LOG_TAG + "Request", "response success");
                // 得到响应结果
                response = httpURLConnection.getInputStream();
            }

            Log.i(LOG_TAG + "Request", "request end, time cast " + (System.currentTimeMillis() -
                    startTime));

        } catch (IOException e) {
            Log.e(LOG_TAG + "Request", "response error IOException class is " + e.toString());
            Log.e(LOG_TAG + "Request", "response error IOException message is " + e.getMessage());
            response = null;
        }

    }

    @Override
    public boolean isSuccessful() {
        return false;
    }

    /**
     * 设置连接请求参数
     */
    private void onRequestProperty() throws ProtocolException {
        httpURLConnection.setRequestMethod("POST");
        // 不使用缓存
        httpURLConnection.setUseCaches(false);
        // 设置可写请求参数
        httpURLConnection.setDoOutput(true);

        // 设置超时时间
        httpURLConnection.setConnectTimeout(timeout);

        // 设置请求头
        httpURLConnection.setRequestProperty("connection", "keep-alive");
        httpURLConnection.setRequestProperty("Charset", encoded);
        httpURLConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
        httpURLConnection.setRequestProperty("Content-Type", MULTIPART_FORM_DATA + ";" +
                "boundary=" + BOUNDARY);

    }

    /**
     * 写入上传文件数据
     *
     * @param outputStream 发送流
     * @param fileInfoMap  文件集合
     */
    private void onWriteFile(OutputStream outputStream, Map<String, FileInfo> fileInfoMap) throws
            IOException {
        Log.i(LOG_TAG + "onWriteFile", "writ file start");

        long startTime = System.currentTimeMillis();

        // 遍历并追加参数
        for (Map.Entry<String, FileInfo> fileEntry : fileInfoMap.entrySet()) {

            // 文件流
            InputStream inputStream = fileEntry.getValue().getFileStream();

            if (inputStream == null) {
                Log.i(LOG_TAG + "onWriteFile", "file " + fileEntry.getKey() + " is null");
                continue;
            }

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(PREFIX).append(BOUNDARY).append(LINE_END);
            stringBuilder.append("Content-Disposition: form-data; name=\"").append(fileEntry
                    .getKey()).append("\"; filename=\"").append(fileEntry.getValue().getFileName
                    ()).append("\"").append(LINE_END);
            stringBuilder.append("Content-Type:").append(fileEntry.getValue().getMimeType())
                    .append(LINE_END);
            stringBuilder.append(LINE_END);

            String head = stringBuilder.toString();
            Log.i(LOG_TAG + "onWriteFile", "file form head is " + head);

            // 写入文件参数
            outputStream.write(head.getBytes(encoded));

            // 写入文件流
            byte[] buffer = new byte[1024];
            int len = 0;

            // 循环写入
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            // 关闭文件流
            inputStream.close();

            // 写入换行
            outputStream.write(LINE_END.getBytes(encoded));
        }

        // 写入结束标识
        outputStream.write((PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes(encoded));

        outputStream.flush();
        outputStream.close();

        Log.i(LOG_TAG + "onWriteFile", "writ file end, time cast " + (System.currentTimeMillis()
                - startTime));
    }

    /**
     * 提取请求参数
     *
     * @param sendData    发送数据集
     * @param fileInfoMap 文件数据集
     *
     * @return 填充好的字符串数据表单
     */
    private String onExtractParameters(Map<String, Object> sendData, Map<String, FileInfo>
            fileInfoMap) {

        // 请求参数装配器参数
        StringBuilder params = new StringBuilder();

        // 遍历sendData集合并加入请求参数对象
        if (sendData != null && !sendData.isEmpty()) {
            Log.i(LOG_TAG + "Request", "sendData count is " + sendData.size());

            // 遍历并追加参数
            for (Map.Entry<String, Object> dataEntry : sendData.entrySet()) {

                if (dataEntry.getValue() instanceof FileInfo) {
                    // 是要发送的文件对象
                    // 加入文件列表
                    fileInfoMap.put(dataEntry.getKey(), (FileInfo) dataEntry.getValue());
                } else {
                    // 作为字符串进行拼接
                    params.append(PREFIX).append(BOUNDARY).append(LINE_END);
                    params.append("Content-Disposition: form-data;").append("name=\"").append
                            (dataEntry.getKey()).append("\"").append(LINE_END);
                    params.append(LINE_END);
                    params.append(dataEntry.getValue());
                    params.append(LINE_END);
                }

            }
        }

        String parameter = params.toString();

        Log.i(LOG_TAG + "Request", "text form is " + parameter);
        return parameter;
    }

    @Override
    public InputStream Response() {
        return response;
    }

    @Override
    public void close() {
        // 关闭连接
        if (httpURLConnection != null) {
            httpURLConnection.disconnect();
        }
    }

    @Override
    public void cancel() {

    }
}
