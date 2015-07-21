package org.mobile.common.function;
/**
 * Created by 超悟空 on 2015/6/4.
 */

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.mobile.library.R;
import org.mobile.model.work.WorkModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * 下载任务
 *
 * @author 超悟空
 * @version 1.0 2015/6/4
 * @since 1.0
 */
public class DownLoadTask extends WorkModel<String, File> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "DownLoadTask.";

    /**
     * 上下文
     */
    private Context context = null;

    /**
     * 构造函数
     */
    public DownLoadTask(Context context) {
        this.context = context;
    }

    @Override
    protected boolean onDoWork(String... parameters) {
        if (parameters.length < 2) {
            Log.d(LOG_TAG + "onDoWork", "url or file name is null");
            return false;
        }

        InputStream in = null;
        FileOutputStream out = null;

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(context, context.getString(R.string.no_sdcard), Toast.LENGTH_SHORT).show();
            Log.d(LOG_TAG + "onDoWork", "no sdcard");
        }

        // 要输出的文件
        File sdFile = new File(context.getExternalFilesDir(null), parameters[1]);

        try {
            // 新建url
            Log.i(LOG_TAG + "onDoWork", "url is " + parameters[0]);
            URL url = new URL(parameters[0]);

            // 获取下载流
            URLConnection conn = url.openConnection();
            in = conn.getInputStream();

            // 新建保存文件流
            Log.i(LOG_TAG + "onDoWork", "file name is " + sdFile.getAbsolutePath());
            out = new FileOutputStream(sdFile);

            // 缓存
            byte[] b = new byte[6 * 1024];
            int len;

            // 写入文件
            while ((len = in.read(b)) != -1) {
                out.write(b, 0, len);
            }

        } catch (IOException e) {
            Log.e(LOG_TAG + "onDoWork", e.getMessage());
            return false;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }

                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                Log.e(LOG_TAG + "run", e.getMessage());
            }
        }

        // 设置结果
        setResult(sdFile);
        return true;
    }
}
