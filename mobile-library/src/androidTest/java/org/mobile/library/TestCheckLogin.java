package org.mobile.library;
/**
 * Created by 超悟空 on 2015/9/16.
 */

import org.junit.Test;
import org.mobile.library.global.GlobalApplication;
import org.mobile.library.global.LoginStatus;
import org.mobile.library.model.work.WorkBack;
import org.mobile.library.model.work.implement.CheckLogin;

import static org.junit.Assert.assertEquals;

/**
 * 测试统一登录认证类
 *
 * @author 超悟空
 * @version 1.0 2015/9/16
 * @since 1.0
 */
public class TestCheckLogin {

    @Test
    public void trueIdentity() throws Exception {

        CheckLogin login = new CheckLogin();

        login.setWorkEndListener(new WorkBack<String>() {
            @Override
            public void doEndWork(boolean state, String data) {
                assertEquals(true, state);
            }
        });

        login.execute("xuehui", "123456", "HMW");
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

        login.execute("xuehui", "1234", "HMW");
    }

    @Test
    public void falseMessage() throws Exception {

        CheckLogin login = new CheckLogin();

        login.setWorkEndListener(new WorkBack<String>() {
            @Override
            public void doEndWork(boolean state, String data) {
                assertEquals("密码错误！", data);
            }
        });

        login.execute("xuehui", "1234", "HMW");
    }

    @Test
    public void trueMessage() throws Exception {
        CheckLogin login = new CheckLogin();

        login.setWorkEndListener(new WorkBack<String>() {
            @Override
            public void doEndWork(boolean state, String data) {
                assertEquals(null, data);
            }
        });

        login.execute("xuehui", "123456", "HMW");

    }

    @Test
    public void successStatus() throws Exception {
        CheckLogin login = new CheckLogin();

        login.execute("xuehui", "123456", "HMW");

        LoginStatus status = GlobalApplication.getGlobal().getLoginStatus();

        assertEquals("227", status.getUserID());
        assertEquals("薛辉", status.getNickname());
        assertEquals("2", status.getCodeCompany());
        assertEquals("2", status.getCodeDepartment());
    }

    @Test
    public void async() throws Exception {
        final Integer LOCK = 1;

        CheckLogin login = new CheckLogin();

        login.setWorkEndListener(new WorkBack<String>() {
            @Override
            public void doEndWork(boolean state, String data) {
                LoginStatus status = GlobalApplication.getGlobal().getLoginStatus();

                assertEquals("227", status.getUserID());
                assertEquals("薛辉", status.getNickname());
                assertEquals("2", status.getCodeCompany());
                assertEquals("2", status.getCodeDepartment());

                synchronized (LOCK) {
                    LOCK.notify();
                }
            }
        });

        login.beginExecute("xuehui", "123456", "HMW");

        synchronized (LOCK) {
            LOCK.wait();
        }
    }
}
