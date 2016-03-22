package org.mobile.library.model.data.implement;
/**
 * Created by 超悟空 on 2016/3/21.
 */

import org.json.JSONObject;
import org.mobile.library.model.data.base.SimpleJsonDataModel;

import java.util.Map;

/**
 * 手机号校验的数据模型
 *
 * @author 超悟空
 * @version 1.0 2016/3/21
 * @since 1.0
 */
public class VerificationMobileData extends SimpleJsonDataModel {

    /**
     * 手机号
     */
    private String mobile = null;

    /**
     * 验证码
     */
    private String verificationCode = null;

    /**
     * 设置手机号
     *
     * @param mobile 手机号
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 设置验证码
     *
     * @param verificationCode 验证码
     */
    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    @Override
    protected void onExtractData(JSONObject jsonData) throws Exception {

    }

    @Override
    protected void onFillRequestParameters(Map<String, String> dataMap) {
        dataMap.put("Mobile", mobile);
        dataMap.put("AuthCode", verificationCode);
    }
}
