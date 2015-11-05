package org.mobile.library.model.data.base;
/**
 * Created by 超悟空 on 2015/11/4.
 */

import org.json.JSONObject;

/**
 * 带有文件上传的数据模型基类<br>
 * 请求参数Map集合的值包含文件类型，
 * 支持的文件类型为{@link java.io.File}，{@link org.mobile.library.struct.FileInfo}两种，
 * {@link org.mobile.library.struct.FileInfo}为文件的包装类型，
 * 用于设定上传时使用的文件名和MIME类型，
 * 同时也可以发送普通的文本类型参数
 *
 * @author 超悟空
 * @version 1.0 2015/11/4
 * @since 1.0
 */
public abstract class UploadDataModel extends StandardDataModel<JSONObject, String, Object> {

    @Override
    protected final boolean onCheckResponse(String response) {
        return response != null;
    }

    @Override
    protected final JSONObject onCreateHandle(String response) throws Exception {
        return new JSONObject(response);
    }
}
