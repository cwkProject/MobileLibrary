package org.mobile.library.model.data.base;
/**
 * Created by 超悟空 on 2015/11/30.
 */

import org.mobile.library.global.GlobalApplication;
import org.mobile.library.network.util.RequestSign;

import java.io.InputStream;
import java.util.Map;

/**
 * 用于下载任务的数据模型基类<br>
 * 请求参数为纯文本，响应数据为输入流
 *
 * @author 超悟空
 * @version 2.0 2016/3/19
 * @since 1.0
 */
public abstract class DownloadDataModel extends StandardDataModel<InputStream, InputStream,
        String> {

    /**
     * 处理下载数据<br>
     * 仅网络请求成功建立连接时该方法才会被调用，
     * 在这里应该将{@code inputStream}写入到文件或内存中，
     * 只有这里读取{@code inputStream}后网络连接才会真正开始向服务器拉取数据，
     * 所以该方法并不是下载完成后被调用，
     * 而是成功建立连接并开始下载数据时才被调用。<br>
     * 由于该方法的父框架需要同步返回执行结果，
     * 所以该方法不必新开线程进行IO操作，
     * 如果该数据模型依赖的任务模型{@link DownloadDataModel}通过异步启动，
     * 则该方法本身就会运行在新线程中，
     * 如果任务通过同步启动，
     * 则该方法会在用户建立的任务执行线程中运行。
     *
     * @param inputStream 响应的下载数据流
     *
     * @throws Exception
     */
    @Override
    protected abstract void onRequestSuccess(InputStream inputStream) throws Exception;

    @Override
    protected final boolean onCheckResponse(InputStream inputStream) {
        return inputStream != null;
    }

    @Override
    protected final InputStream onCreateHandle(InputStream inputStream) throws Exception {
        return inputStream;
    }

    @Override
    protected final boolean onRequestResult(InputStream handleResult) throws Exception {
        return true;
    }

    @Override
    protected final String onRequestMessage(boolean result, InputStream handleResult) throws
            Exception {
        return null;
    }

    @Override
    protected final void onRequestFailed(InputStream handleResult) throws Exception {
    }

    /**
     * 表示是否对请求参数进行校验签名
     *
     * @return true表示进行签名，默认为false
     */
    @Override
    protected boolean onIsRequestSign() {
        return false;
    }

    @Override
    protected void onRequestParametersSign(Map<String, String> dataMap) {
        if (GlobalApplication.getApplicationAttribute().getAppCode() != null && GlobalApplication
                .getApplicationAttribute().getAppToken() != null) {
            RequestSign.signForText(dataMap);
        }
    }
}
