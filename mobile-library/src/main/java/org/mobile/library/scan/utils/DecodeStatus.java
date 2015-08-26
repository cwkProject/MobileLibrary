package org.mobile.library.scan.utils;
/**
 * Created by 超悟空 on 2015/8/26.
 */

/**
 * 解码状态枚举
 *
 * @author 超悟空
 * @version 1.0 2015/8/26
 * @since 1.0
 */
public interface DecodeStatus {

    int DECODE = 1;
    int QUIT = 2;
    int DECODE_FAILED = 3;
    int DECODE_SUCCEEDED = 4;
    int LAUNCH_PRODUCT_QUERY = 5;
    int RESTART_PREVIEW = 6;
    int RETURN_SCAN_RESULT = 7;
}
