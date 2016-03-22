package org.mobile.library.model.work.implement;
/**
 * Created by 超悟空 on 2016/3/21.
 */

import org.mobile.library.global.ApplicationStaticValue;
import org.mobile.library.model.data.implement.VerificationMobileData;
import org.mobile.library.model.work.DefaultWorkModel;
import org.mobile.library.network.factory.NetworkType;

/**
 * 发送手机验证码任务
 *
 * @author 超悟空
 * @version 1.0 2016/3/21
 * @since 1.0
 */
public class SendMobileVerificationCode extends DefaultWorkModel<String, String,
        VerificationMobileData> {

    @Override
    protected boolean onCheckParameters(String... parameters) {
        return parameters != null && parameters.length == 1;
    }

    @Override
    protected NetworkType onNetworkType() {
        return NetworkType.HTTP_POST;
    }

    @Override
    protected String onTaskUri() {
        return ApplicationStaticValue.Url.SEND_MOBILE_VERIFICATION_CODE_URL;
    }

    @Override
    protected String onRequestSuccessSetResult(VerificationMobileData data) {
        return null;
    }

    @Override
    protected VerificationMobileData onCreateDataModel(String... parameters) {
        VerificationMobileData data = new VerificationMobileData();

        data.setMobile(parameters[0]);
        return data;
    }
}
