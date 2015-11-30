package org.mobile.library.model.work;
/**
 * Created by 超悟空 on 2015/11/30.
 */

import org.mobile.library.model.data.base.DownloadDataModel;
import org.mobile.library.network.factory.NetworkType;

/**
 * 默认实现的网络下载任务模型基类<br>
 * 基于{@link DefaultWorkModel}实现特化
 *
 * @param <Parameters>    功能所需参数类型
 * @param <Result>        结果数据类型
 * @param <DataModelType> 任务请求使用的数据模型类型
 *
 * @author 超悟空
 * @version 1.0 2015/11/30
 * @since 1.0
 */
public abstract class DownloadWorkModel<Parameters, Result, DataModelType extends
        DownloadDataModel> extends DefaultWorkModel<Parameters, Result, DataModelType> {

    @Override
    protected final String onTaskUri() {
        return onTaskUri(getParameters());
    }

    /**
     * 设置文件下载地址
     *
     * @param parameters 任务传入参数，即{@link #onCheckParameters(Object[])}检测通过后的参数列表
     *
     * @return 下载地址
     */
    @SuppressWarnings("unchecked")
    protected abstract String onTaskUri(Parameters... parameters);

    @Override
    protected NetworkType onNetworkType() {
        return NetworkType.DOWNLOAD;
    }

    @Override
    protected String onParseSuccessSetMessage(boolean state, DataModelType data) {
        return null;
    }

    @Override
    protected String onParseFailedSetMessage(DataModelType data) {
        return null;
    }

    @Override
    protected final Result onRequestFailedSetResult(DataModelType data) {
        return null;
    }

    @Override
    protected final void onParseSuccess(DataModelType data) {
        super.onParseSuccess(data);
    }

    @Override
    protected final void onParseFailed(DataModelType data) {
        super.onParseFailed(data);
    }
}
