package org.mobile.library.network.util;
/**
 * Created by 超悟空 on 2015/11/4.
 */


import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * 包装的请求体，
 * 带上传进度处理
 *
 * @author 超悟空
 * @version 2.0 2016/3/7
 * @since 1.0
 */
public class ProgressRequestBody extends RequestBody {

    /**
     * 实际的待包装请求体
     */
    private RequestBody requestBody;

    /**
     * 进度回调接口
     */
    private NetworkProgressListener progressListener;

    /**
     * 构造函数
     *
     * @param requestBody      待包装请求体
     * @param progressListener 进度回调接口
     */
    public ProgressRequestBody(RequestBody requestBody, NetworkProgressListener progressListener) {
        this.requestBody = requestBody;
        this.progressListener = progressListener;
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        // 包装
        BufferedSink bufferedSink = Okio.buffer(sink(sink));
        // 写入
        requestBody.writeTo(bufferedSink);
        // 必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();
        bufferedSink.close();
    }

    /**
     * 包装写入流回调进度接口
     *
     * @param sink Sink 原写入流
     *
     * @return Sink 包装后写入流
     */
    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            // 当前写入字节数
            long bytesWritten = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);

                // 增加当前写入的字节数
                bytesWritten += byteCount;
                // 回调进度
                progressListener.onRefreshProgress(bytesWritten, contentLength(), bytesWritten ==
                        contentLength());
            }
        };
    }
}
