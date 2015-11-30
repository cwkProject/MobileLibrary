package org.mobile.library.network.util;
/**
 * Created by 超悟空 on 2015/11/27.
 */

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * 包装的响应体，
 * 带下载进度处理
 *
 * @author 超悟空
 * @version 1.0 2015/11/27
 * @since 1.0
 */
public class ProgressResponseBody extends ResponseBody {

    /**
     * 实际包装的响应体
     */
    private final ResponseBody responseBody;

    /**
     * 进度回调接口
     */
    private final NetworkProgressListener progressListener;

    /**
     * 包装后的数据源
     */
    private BufferedSource bufferedSource;

    /**
     * 构造函数
     *
     * @param responseBody     待包装响应体
     * @param progressListener 进度监听器
     */
    public ProgressResponseBody(ResponseBody responseBody, NetworkProgressListener
            progressListener) {
        this.responseBody = responseBody;
        this.progressListener = progressListener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() throws IOException {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    /**
     * 包装数据源回调进度接口
     *
     * @param source Sink 原数据源
     *
     * @return Source 包装后数据源
     */
    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;
            long length = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;

                // 记录一次总长度
                if (length == 0L) {
                    length = contentLength();
                }

                progressListener.onRefreshProgress(totalBytesRead, length, bytesRead == -1);
                return bytesRead;
            }
        };
    }
}
