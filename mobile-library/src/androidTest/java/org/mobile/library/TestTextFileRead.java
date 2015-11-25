package org.mobile.library;
/**
 * Created by 超悟空 on 2015/11/25.
 */

import android.os.Environment;
import android.util.Log;

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * 测试文本读写
 *
 * @author 超悟空
 * @version 1.0 2015/11/25
 * @since 1.0
 */
public class TestTextFileRead {

    @Test
    public void read() throws Exception {
        String s = "测试测试";

        File file = new File(Environment.getExternalStoragePublicDirectory(Environment
                .DIRECTORY_DOWNLOADS), "test.txt");

        Scanner scanner = null;

        try {

            if (!file.exists()) {
                file.createNewFile();
            }

            FileOutputStream fout = new FileOutputStream(file);

            try {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fout));

                writer.write(s);

                writer.flush();

                writer.close();
            } catch (IOException e) {
                Log.d("TestTextFileRead.read", "IOException is " + e.getMessage());
                assertNull(e);
            }

            scanner = new Scanner(file);
            scanner.useDelimiter("");

            // 得到字符串
            StringBuilder builder = new StringBuilder();

            while (scanner.hasNext()) {
                builder.append(scanner.next());
            }

            String text = builder.toString();

            Log.i("TestTextFileRead.read","result is "+text);
            assertEquals(s, text);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            assertNull(e);
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }
}
