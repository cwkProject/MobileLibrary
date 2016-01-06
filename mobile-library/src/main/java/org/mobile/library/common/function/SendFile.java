package org.mobile.library.common.function;
/**
 * Created by 超悟空 on 2015/6/4.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import java.io.File;

/**
 * 将文件发送给其他应用打开
 *
 * @author 超悟空
 * @version 1.0 2015/6/4
 * @since 1.0
 */
public class SendFile {

    private Context context = null;

    public SendFile(Context context) {
        this.context = context;
    }

    /**
     * 用第三方应用打开文件
     *
     * @param file 要打开的文件
     */
    public void openFile(File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // 设置intent的Action属性
        intent.setAction(Intent.ACTION_VIEW);
        // 获取文件file的MIME类型
        String type = getMIMEType(file);
        // 设置intent的data和Type属性。
        intent.setDataAndType(Uri.fromFile(file), type);
        // 跳转
        context.startActivity(intent);
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file 指定的文件
     */
    private String getMIMEType(File file) {
        String type = "*/*";
        String fName = file.getName();
        // 获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        // 获得后缀
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(end);
    }
}
