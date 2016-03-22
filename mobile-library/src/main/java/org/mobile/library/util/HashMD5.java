package org.mobile.library.util;
/**
 * Created by 超悟空 on 2016/3/19.
 */

import android.text.TextUtils;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5工具
 *
 * @author 超悟空
 * @version 1.0 2016/3/19
 * @since 1.0
 */
public class HashMD5 {

    /**
     * 日志前缀
     */
    private static final String LOG_TAG = "HashMD5.";

    /**
     * 默认编码
     */
    private static final String CHARSET = "UTF-8";

    /**
     * 使用md5进行加密
     *
     * @param text    要加密的字符串
     * @param charset 字符串编码
     *
     * @return 加密后的字符串，加密失败会返回空字符串
     */
    public static String hash(String text, String charset) {
        try {
            if (TextUtils.isEmpty(charset)) {
                charset = CHARSET;
            }

            byte[] hash = MessageDigest.getInstance("MD5").digest(text.getBytes(charset));

            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & 0xFF) < 0x10)
                    hex.append('0');
                hex.append(Integer.toHexString(b & 0xFF));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e(LOG_TAG + "hash", "NoSuchAlgorithmException is " + e.getMessage());
            return "";
        } catch (UnsupportedEncodingException e) {
            Log.e(LOG_TAG + "hash", "UnsupportedEncodingException is " + e.getMessage());
            return "";
        }
    }

    /**
     * 使用md5进行加密，
     * 默认使用UTF-8对字符串编码
     *
     * @param text 要加密的字符串
     *
     * @return 加密后的字符串，加密失败会返回空字符串
     */
    public static String hash(String text) {
        return hash(text, CHARSET);
    }
}
