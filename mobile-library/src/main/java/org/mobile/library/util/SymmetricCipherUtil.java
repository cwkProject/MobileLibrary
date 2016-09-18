package org.mobile.library.util;
/**
 * Created by 超悟空 on 2016/9/13.
 */

import android.util.Log;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

/**
 * 对称加解密工具
 *
 * @author 超悟空
 * @version 1.0 2016/9/13
 * @since 1.0
 */
public class SymmetricCipherUtil {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "SymmetricCipherUtil.";

    /**
     * 加密器
     */
    private Cipher cipher = null;

    /**
     * 密钥
     */
    private Key key = null;

    /**
     * 算法
     */
    private String algorithm = null;

    /**
     * 构造函数
     *
     * @param algorithm 算法名称
     * @param key       密钥
     */
    public SymmetricCipherUtil(String algorithm, Key key) {
        try {
            this.cipher = Cipher.getInstance(algorithm);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            Log.e(LOG_TAG + "SymmetricCipherUtil", "create cipher error", e);
        }
        this.algorithm = algorithm;
        this.key = key;
    }

    /**
     * 设置一个新key
     *
     * @param key 新key
     */
    public void setKey(Key key) {
        this.key = key;
    }

    /**
     * 加密
     *
     * @param sourceText 待加密数据
     *
     * @return 密文
     */
    public byte[] encrypt(byte[] sourceText) {

        byte[] cipherText = null;

        if (cipher != null) {

            try {
                cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(key.getEncoded()));
                cipherText = cipher.doFinal(sourceText);
            } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException |
                    InvalidAlgorithmParameterException e) {
                Log.e(LOG_TAG + "encrypt", "encrypt error", e);
            }
        }

        return cipherText;
    }

    /**
     * 解密
     *
     * @param cipherText 密文
     *
     * @return 原数据
     */
    public byte[] decrypt(byte[] cipherText) {
        byte[] sourceText = null;

        if (cipher != null) {
            try {
                cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(key.getEncoded()));
                sourceText = cipher.doFinal(cipherText);
            } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException |
                    InvalidAlgorithmParameterException e) {
                Log.e(LOG_TAG + "decrypt", "decrypt error", e);
            }
        }

        return sourceText;
    }

    /**
     * 生成密钥
     *
     * @param algorithm 密钥
     * @param length    密钥长度
     *
     * @return 密钥实例
     */
    public static Key createKey(String algorithm, int length) {
        KeyGenerator keyGenerator = null;
        Key key = null;
        try {
            keyGenerator = KeyGenerator.getInstance(algorithm);
            keyGenerator.init(length);
            key = keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            Log.e(LOG_TAG + "createKey", "create key error", e);
        }

        return key;
    }

    /**
     * 将文本转为Key
     *
     * @param keyByte key的字节流
     *
     * @return key实例
     */
    public Key getKey(byte[] keyByte) {

        Key key = null;

        try {
            cipher.init(Cipher.UNWRAP_MODE, this.key, new IvParameterSpec(this.key.getEncoded()));
            key = cipher.unwrap(keyByte, algorithm, Cipher.SECRET_KEY);
        } catch (InvalidKeyException | NoSuchAlgorithmException |
                InvalidAlgorithmParameterException e) {
            Log.e(LOG_TAG + "getKey", "convert key error", e);
        }

        return key;
    }

    /**
     * 获取密钥包装成的字节流
     *
     * @param key key实例
     *
     * @return 字节流
     */
    public byte[] getBinaryKey(Key key) {
        byte[] bk = null;
        try {
            cipher.init(Cipher.WRAP_MODE, this.key, new IvParameterSpec(this.key.getEncoded()));
            bk = cipher.wrap(key);
        } catch (IllegalBlockSizeException | InvalidKeyException |
                InvalidAlgorithmParameterException e) {
            Log.e(LOG_TAG + "getBinaryKey", "convert key error", e);
        }
        return bk;
    }
}
