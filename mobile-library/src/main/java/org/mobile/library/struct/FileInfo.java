package org.mobile.library.struct;
/**
 * Created by 超悟空 on 2015/10/28.
 */

import org.mobile.library.util.MIMEUtil;

import java.io.File;

/**
 * 文件上传使用的文件信息包装类
 *
 * @author 超悟空
 * @version 1.0 2015/10/28
 * @since 1.0
 */
public class FileInfo {

    private static final String LOG_TAG = "FileInfo.";

    /**
     * 要上传的文件
     */
    private File file = null;

    /**
     * 文件的mime类型
     */
    private String mimeType = null;

    /**
     * 要上传的新文件名
     */
    private String fileName = null;

    /**
     * 构造函数
     *
     * @param path 文件路径
     */
    public FileInfo(String path) {
        this(path, null);
    }

    /**
     * 构造函数
     *
     * @param file 文件对象
     */
    public FileInfo(File file) {
        this.file = file;
    }

    /**
     * 构造函数
     *
     * @param file     文件对象
     * @param fileName 新文件名
     * @param mimeType 文件mime类型
     */
    public FileInfo(File file, String fileName, String mimeType) {
        this.file = file;
        this.mimeType = mimeType;
        this.fileName = fileName;
    }

    /**
     * 构造函数
     *
     * @param file     文件对象
     * @param fileName 新文件名
     */
    public FileInfo(File file, String fileName) {
        this.file = file;
        this.fileName = fileName;
    }

    /**
     * 构造函数
     *
     * @param path     文件路径
     * @param fileName 新文件名
     * @param mimeType 文件mime类型
     */
    public FileInfo(String path, String fileName, String mimeType) {
        this.file = new File(path);
        this.fileName = fileName;
        this.mimeType = mimeType;
    }

    /**
     * 构造函数
     *
     * @param path     文件路径
     * @param fileName 新文件名
     */
    public FileInfo(String path, String fileName) {
        this(path, fileName, null);
    }

    /**
     * 获取文件
     *
     * @return 文件对象
     */
    public File getFile() {
        return file;
    }

    /**
     * 获取文件MimeType
     *
     * @return MimeType
     */
    public String getMimeType() {

        if (mimeType == null) {
            mimeType = MIMEUtil.getMimeType(file);
        }

        return mimeType;
    }

    /**
     * 设置文件MimeType
     *
     * @param mimeType MimeType
     */
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * 获取上传时使用的新文件名
     *
     * @return 新文件名，包含后缀，如果未设置则返回默认文件名
     */
    public String getFileName() {

        if (fileName == null) {
            fileName = file.getName();
        }

        return fileName;
    }

    /**
     * 设置上传时使用的新文件名
     *
     * @param fileName 新文件名，包含后缀
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
