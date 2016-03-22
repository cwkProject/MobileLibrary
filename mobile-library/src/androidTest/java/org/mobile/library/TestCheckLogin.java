package org.mobile.library;
/**
 * Created by 超悟空 on 2015/9/16.
 */

import org.junit.Before;
import org.junit.Test;
import org.mobile.library.global.GlobalApplication;
import org.mobile.library.model.work.IWorkEndListener;
import org.mobile.library.model.work.WorkBack;
import org.mobile.library.model.work.implement.CheckLogin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * 测试统一登录认证类
 *
 * @author 超悟空
 * @version 1.0 2015/9/16
 * @since 1.0
 */
public class TestCheckLogin {

    private static final String APP_CODE = "APPTEST";

    private static final String APP_TOKEN = "2E62847737E0C040E053A864016AC040";

    @Before
    public void setUp() throws Exception {
        GlobalApplication.getApplicationAttribute().setAppCode(APP_CODE);
        GlobalApplication.getApplicationAttribute().setAppToken(APP_TOKEN);

    }

    @Test
    public void trueIdentity() throws Exception {

        CheckLogin login = new CheckLogin();

        login.setWorkEndListener(new WorkBack<String>() {
            @Override
            public void doEndWork(boolean state, String data) {
                assertEquals(true, state);
            }
        });

        login.execute("xuehui", "123456");
    }

    @Test
    public void falseIdentity() throws Exception {

        CheckLogin login = new CheckLogin();

        login.setWorkEndListener(new WorkBack<String>() {
            @Override
            public void doEndWork(boolean state, String data) {
                assertEquals(false, state);
            }
        });

        login.execute("xuehui", "1234");
    }

    @Test
    public void falseMessage() throws Exception {

        CheckLogin login = new CheckLogin();

        login.setWorkEndListener(new IWorkEndListener<String>() {
            @Override
            public void doEndWork(boolean state, String message, String data) {
                assertEquals("用户名或密码错误！", message);
            }
        });

        login.execute("xuehui", "1234");
    }

    @Test
    public void successStatus() throws Exception {
        CheckLogin login = new CheckLogin();

        assertTrue(login.execute("xuehui", "123456"));
        assertEquals("登陆成功！", login.getMessage());

        assertEquals("227", login.getResult());
    }
}
