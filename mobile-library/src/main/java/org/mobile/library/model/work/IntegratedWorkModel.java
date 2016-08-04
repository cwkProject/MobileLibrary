package org.mobile.library.model.work;
/**
 * Created by 超悟空 on 2016/7/23.
 */


import org.mobile.library.model.data.IIntegratedDataModel;
import org.mobile.library.network.communication.Communication;

/**
 * 进一步简化的集成式网络任务模型基类<br>
 * 内部使用{@link IIntegratedDataModel}作为默认的数据模型类，
 * 使用{@link Communication}作为网络请求工具
 *
 * @author 超悟空
 * @version 1.0 2016/7/23
 * @since 1.0
 */
public abstract class IntegratedWorkModel<Parameters, Result> extends
        DefaultWorkModel<Parameters, Result, IIntegratedDataModel<Parameters, Result, ?, ?>> {

    @SafeVarargs
    @Override
    protected final IIntegratedDataModel<Parameters, Result, ?, ?> onCreateDataModel
            (Parameters... parameters) {
        IIntegratedDataModel<Parameters, Result, ?, ?> data = onCreateDataModel();
        data.setParameters(parameters);
        return data;
    }

    /**
     * 创建数据模型对象并填充参数
     *
     * @return 参数设置完毕后的数据模型对象
     */
    protected abstract IIntegratedDataModel<Parameters, Result, ?, ?> onCreateDataModel();

    @Override
    protected Result onRequestSuccessSetResult(IIntegratedDataModel<Parameters, Result, ?, ?>
                                                           data) {
        return data.getResult();
    }

    @Override
    protected Result onRequestFailedSetResult(IIntegratedDataModel<Parameters, Result, ?, ?> data) {
        return data.getResult();
    }
}
