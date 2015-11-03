package org.mobile.library;
/**
 * Created by 超悟空 on 2015/9/16.
 */

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mobile.library.global.ApplicationStaticValue;
import org.mobile.library.global.GlobalApplication;
import org.mobile.library.model.work.WorkBack;
import org.mobile.library.model.work.implement.CheckVersion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * 版本升级测试
 *
 * @author 超悟空
 * @version 1.0 2015/9/16
 * @since 1.0
 */
@RunWith(AndroidJUnit4.class)
public class TestUpdateVersion {

    @Test
    public void hasNewVersion() throws Exception {

        // 新建检查更新任务
        CheckVersion checkVersion = new CheckVersion();

        checkVersion.setWorkEndListener(new WorkBack<String>() {
            @Override
            public void doEndWork(boolean state, String data) {
                assertEquals(true, state);
                assertNotNull(data);
            }
        });

        // 执行任务
        checkVersion.execute(ApplicationStaticValue.DEVICE_TYPE, "HMW", ApplicationStaticValue
                .UPDATE_REQUEST_URL);

        assertEquals(false, GlobalApplication.getGlobal().getApplicationVersion().isLatestVersion
                ());
        assertNotNull(GlobalApplication.getGlobal().getApplicationVersion().getLatestVersionUrl());
        assertNotNull(GlobalApplication.getGlobal().getApplicationVersion().getLatestVersionName());
    }
}
