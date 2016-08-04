package org.mobile.library;
/**
 * Created by 超悟空 on 2015/9/16.
 */

import org.junit.Test;
import org.mobile.library.global.ApplicationStaticValue;
import org.mobile.library.global.Global;
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
        checkVersion.execute(ApplicationStaticValue.AppConfig.DEVICE_TYPE, "HMW", ApplicationStaticValue
                .Url.UPDATE_REQUEST_URL);

        assertEquals(false, Global.getApplicationVersion().isLatestVersion());
        assertNotNull(Global.getApplicationVersion().getLatestVersionUrl());
        assertNotNull(Global.getApplicationVersion().getLatestVersionName());
    }
}
