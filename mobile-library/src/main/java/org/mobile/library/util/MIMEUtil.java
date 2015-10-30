package org.mobile.library.util;
/**
 * Created by 超悟空 on 2015/10/28.
 */

import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.Locale;

/**
 * 文件MIME类型工具
 *
 * @author 超悟空
 * @version 1.0 2015/10/28
 * @since 1.0
 */
public class MIMEUtil {

    /**
     * 获取文件后缀
     *
     * @param file 文件
     *
     * @return 后缀名
     */
    private static String getSuffix(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }
        String fileName = file.getName();

        int index = fileName.lastIndexOf(".");
        if (index >= 0) {
            return fileName.substring(index + 1).toLowerCase(Locale.US);
        } else {
            return null;
        }
    }

    /**
     * 获取文件的MIMEType
     *
     * @param file 文件
     *
     * @return MIME类型
     */
    public static String getMimeType(File file) {
        String suffix = getSuffix(file);
        if (suffix == null) {
            return "file/*";
        }
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
        if (type != null && type.length() != 0) {
            return type;
        }
        return "file/*";
    }
}
