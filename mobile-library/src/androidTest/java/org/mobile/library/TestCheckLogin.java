package org.mobile.library;
/**
 * Created by 超悟空 on 2015/9/16.
 */

import org.junit.Before;
import org.junit.Test;
import org.mobile.library.model.work.WorkBack;
import org.mobile.library.model.work.implement.CheckLogin;
import org.mobile.library.util.LoginStatus;

import static org.junit.Assert.assertEquals;

/**
 * 测试统一登录认证类
 *
 * @author 超悟空
 * @version 1.0 2015/9/16
 * @since 1.0
 */
public class TestCheckLogin {

    /**
     * 登录验证任务
     */
    private CheckLogin login = null;

    private boolean isLogin = false;

    private String message = null;

    @Before
    public void setUp() throws Exception {
        login = new CheckLogin();

        login.setWorkEndListener(new WorkBack<String>() {
            @Override
            public void doEndWork(boolean state, String data) {
                isLogin = state;
                message = data;
            }
        });
    }

    @Test
    public void trueIdentity() throws Exception {

        login.execute("xuehui", "123456", "LHGL");

        assertEquals(true, isLogin);
    }

    @Test
    public void falseIdentity() throws Exception {

        login.execute("xuehui", "1234", "LHGL");

        assertEquals(false, isLogin);
    }

    @Test
    public void falseMessage() throws Exception {
        login.execute("xuehui", "1234", "LHGL");

        assertEquals("密码错误！", message);
    }

    @Test
    public void trueMessage() throws Exception {
        login.execute("xuehui", "123456", "LHGL");

        assertEquals(null, message);
    }

    @Test
    public void successStatus() throws Exception {
        login.execute("xuehui", "123456", "LHGL");

        LoginStatus status = LoginStatus.getLoginStatus();

        assertEquals("227", status.getUserID());
        assertEquals("薛辉", status.getNickname());
        assertEquals("2", status.getCodeCompany());
        assertEquals("2", status.getCodeDepartment());
    }
}
