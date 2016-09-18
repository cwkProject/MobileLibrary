package org.mobile.library.util;
/**
 * Created by 超悟空 on 2016/9/14.
 */

import android.util.Base64;

import java.security.Key;

import javax.crypto.spec.SecretKeySpec;

/**
 * DES/CBC/PKCS5Padding的PreferencesUtil.DataCipher加解密工具
 *
 * @author 超悟空
 * @version 1.0 2016/9/14
 * @since 1.0
 */
public class DesDataCipher implements PreferencesUtil.DataCipher {

    /**
     * 一个默认密钥
     */
    private static final String DEFAULT_KEY = "x9mACP2GNK4=";

    /**
     * 生成key使用的算法
     */
    private static final String KEY_ALGORITHM = "DES";

    /**
     * 加密器使用的算法
     */
    private static final String CIPHER_ALGORITHM = "DES/CBC/PKCS5Padding";

    /**
     * 加解密工具
     */
    private SymmetricCipherUtil cipherUtil = null;

    /**
     * 构造函数
     */
    public DesDataCipher() {
        this(null);
    }

    /**
     * 构造函数
     *
     * @param key 指定的用默认密钥加密的base64密钥
     */
    public DesDataCipher(String key) {
        cipherUtil = new SymmetricCipherUtil(CIPHER_ALGORITHM, new SecretKeySpec(Base64.decode
                (DEFAULT_KEY, Base64.DEFAULT), KEY_ALGORITHM));

        if (key != null) {
            cipherUtil.setKey(cipherUtil.getKey(Base64.decode(key, Base64.DEFAULT)));
        }
    }

    /**
     * 创建一个新key并将当前加密工具替换为该新key
     *
     * @return 用默认密钥加密后的BASE64编码新key
     */
    public String createNewKey() {
        Key key = SymmetricCipherUtil.createKey(KEY_ALGORITHM, 56);

        byte[] byteKey = cipherUtil.getBinaryKey(key);

        cipherUtil.setKey(key);

        return Base64.encodeToString(byteKey, Base64.DEFAULT);
    }


    /**
     * 加密
     *
     * @param data 要加密的数据，非String类型成员会被转换为String类型
     *
     * @return 编码为base64的密文
     */
    @Override
    public String encrypt(String data) {
        if (data == null) {
            return null;
        }
        return Base64.encodeToString(cipherUtil.encrypt(data.getBytes()), Base64.DEFAULT);
    }

    /**
     * 解密
     *
     * @param cipherText base64编码的密文
     *
     * @return 原始数据
     */
    @Override
    public String decrypt(String cipherText) {
        if (cipherText == null) {
            return null;
        }
        return new String(cipherUtil.decrypt(Base64.decode(cipherText, Base64.DEFAULT)));
    }
}
