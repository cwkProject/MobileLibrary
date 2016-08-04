package org.mobile.library.global;
/**
 * Created by 超悟空 on 2016/7/21.
 */

import android.app.Application;

/**
 * 全局应用入口，用于单元测试
 *
 * @author 超悟空
 * @version 1.0 2016/7/21
 * @since 1.0
 */
public class GlobalApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Global.init(this);
    }
}
