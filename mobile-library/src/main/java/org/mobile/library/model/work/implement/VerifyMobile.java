package org.mobile.library.model.work.implement;
/**
 * Created by 超悟空 on 2016/3/21.
 */

import org.mobile.library.R;
import org.mobile.library.global.ApplicationStaticValue;
import org.mobile.library.global.GlobalApplication;
import org.mobile.library.model.data.implement.VerificationMobileData;
import org.mobile.library.model.work.DefaultWorkModel;
import org.mobile.library.network.factory.NetworkType;

/**
 * 验证手机号任务
 *
 * @author 超悟空
 * @version 1.0 2016/3/21
 * @since 1.0
 */
public class VerifyMobile extends DefaultWorkModel<String, String, VerificationMobileData> {
    @Override
    protected boolean onCheckParameters(String... parameters) {
        return parameters != null && parameters.length == 2;
    }

    @Override
    protected NetworkType onNetworkType() {
        return NetworkType.HTTP_POST;
    }

    @Override
    protected String onTaskUri() {
        return ApplicationStaticValue.Url.VERIFY_MOBILE_URL;
    }

    @Override
    protected String onRequestSuccessSetResult(VerificationMobileData data) {
        return null;
    }

    @Override
    protected String onParseFailedSetMessage(VerificationMobileData data) {
        return GlobalApplication.getGlobal().getString(R.string.verify_error_field_required);
    }

    @Override
    protected VerificationMobileData onCreateDataModel(String... parameters) {
        VerificationMobileData data = new VerificationMobileData();
        data.setMobile(parameters[0]);
        data.setVerificationCode(parameters[1]);
        return data;
    }
}
