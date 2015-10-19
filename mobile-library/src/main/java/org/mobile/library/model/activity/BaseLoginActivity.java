package org.mobile.library.model.activity;
/**
 * Created by 超悟空 on 2015/6/11.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import org.mobile.library.R;
import org.mobile.library.common.dialog.SimpleDialog;
import org.mobile.library.model.work.WorkBack;
import org.mobile.library.model.work.implement.CheckLogin;
import org.mobile.library.util.ApplicationAttribute;
import org.mobile.library.util.ConfigUtil;
import org.mobile.library.util.LoginStatus;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 登录Activity模板
 *
 * @author 超悟空
 * @version 1.0 2015/6/11
 * @since 1.0
 */
public abstract class BaseLoginActivity extends Activity {
    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "BaseLoginActivity.";

    /**
     * 用户名编辑框
     */
    private EditText userNameEditText = null;

    /**
     * 密码编辑框
     */
    private EditText passwordEditText = null;

    /**
     * 保存密码复选框
     */
    private CheckBox loginSaveCheck = null;

    /**
     * 自动登陆复选框
     */
    private CheckBox loginAutoCheck = null;

    /**
     * 保存密码复选框ID
     */
    private int loginSaveCheckID = 0;

    /**
     * 自动登陆复选框ID
     */
    private int loginAutoCheckID = 0;

    /**
     * 进度条
     */
    private ProgressDialog progressDialog = null;

    /**
     * 本地广播管理器
     */
    private LocalBroadcastManager localBroadcastManager = null;

    /**
     * 设置保存密码复选框ID
     *
     * @param loginSaveCheckID 保存密码复选框ID
     */
    protected void setLoginSaveCheckID(int loginSaveCheckID) {
        this.loginSaveCheckID = loginSaveCheckID;
    }

    /**
     * 设置自动登录复选框ID
     *
     * @param loginAutoCheckID 自动登录复选框ID
     */
    protected void setLoginAutoCheckID(int loginAutoCheckID) {
        this.loginAutoCheckID = loginAutoCheckID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(onActivityLoginLayout());

        // 重置用户登录参数
        LoginStatus.getLoginStatus().Reset();

        // 初始化界面
        init();
    }

    /**
     * 提供登录activity布局ID
     *
     * @return activity布局文件ID
     */
    protected abstract int onActivityLoginLayout();

    /**
     * 初始化界面
     */
    private void init() {
        // 初始化复选框
        initCheck();
        // 初始化编辑框
        initEdit();
        // 初始化自定义登录按钮事件
        onCustomLoginButton();
        // 自定义界面初始化
        onInitCustomView();
    }

    /**
     * 初始化自定义登录按钮事件
     */
    protected void onCustomLoginButton() {
    }

    /**
     * 自定义界面初始化
     */
    protected void onInitCustomView() {
    }

    /**
     * 提供保存密码和自动登录复选框ID，
     * 重写方法并在方法体中使用以下方法赋值
     * {@link #setLoginSaveCheckID(int)},
     * {@link #setLoginAutoCheckID(int)}
     */
    protected void onLoginCheckID() {
    }

    /**
     * 初始化复选框
     */
    private void initCheck() {

        // 尝获取复选框ID
        onLoginCheckID();

        if (loginSaveCheckID > 0) {
            // 获取保存密码复选框
            loginSaveCheck = (CheckBox) findViewById(loginSaveCheckID);
            // 设置复选框初状态
            loginSaveCheck.setChecked(ConfigUtil.getInstance().isLoginSave());
        }

        if (loginAutoCheckID > 0) {
            // 获取自动登录复选框
            loginAutoCheck = (CheckBox) findViewById(loginAutoCheckID);
            // 设置复选框初状态
            loginAutoCheck.setChecked(ConfigUtil.getInstance().isLoginAuto());
        }

        // 如果同时存在
        if (loginSaveCheck != null && loginAutoCheck != null) {

            // 设置监听器使两个复选框联动
            loginSaveCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!isChecked) {
                        loginAutoCheck.setChecked(false);
                    }
                }
            });

            loginAutoCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        loginSaveCheck.setChecked(true);
                    }
                }
            });
        }
    }

    /**
     * 提供用户名编辑框ID
     *
     * @return 用户名编辑框ID
     */
    protected abstract int onUserNameEditTextID();

    /**
     * 提供密码编辑框ID
     *
     * @return 密码编辑框ID
     */
    protected abstract int onPasswordEditTextID();

    /**
     * 初始化编辑框
     */
    private void initEdit() {
        // 文本框初始化
        userNameEditText = (EditText) findViewById(onUserNameEditTextID());
        passwordEditText = (EditText) findViewById(onPasswordEditTextID());

        // 尝试填充数据
        if (ConfigUtil.getInstance().getUserName() != null) {
            // 填充用户
            userNameEditText.setText(ConfigUtil.getInstance().getUserName());

            if ((loginSaveCheck != null && loginSaveCheck.isChecked()) || (loginAutoCheck != null && loginAutoCheck.isChecked())) {
                // 记住密码状态或自动登录状态，填充密码
                passwordEditText.setText(ConfigUtil.getInstance().getPassword());
            } else {
                // 让密码框拥有焦点
                setSoftInput(passwordEditText);
            }
        } else {
            // 让用户名框拥有焦点
            setSoftInput(userNameEditText);
        }
    }

    /**
     * 设置指定编辑框获取焦点并弹出软键盘
     *
     * @param editText 要获取焦点的编辑框
     */
    private void setSoftInput(final EditText editText) {

        // 获取焦点
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();

        // 延迟弹出软键盘
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
            }

        }, 600);
    }

    /**
     * 登录按钮点击事件触发时最先执行
     *
     * @param userNameEditText 用户名编辑框
     * @param passwordEditText 密码编辑框
     */
    protected void onPreClickLoginButton(EditText userNameEditText, EditText passwordEditText) {

    }

    /**
     * 登录成功后执行
     */
    protected void onPostClickLoginButton() {
    }

    /**
     * 设置应用相关属性，
     * 用于填充登录请求参数
     */
    protected void onApplicationAttribute(ApplicationAttribute applicationAttribute) {

    }

    /**
     * 设置应用标识
     *
     * @return 应用标识串
     */
    protected abstract String onAppName();

    /**
     * 登录按钮
     *
     * @param view 按钮
     */
    public void LoginButton(View view) {

        // 执行前置事件
        onPreClickLoginButton(userNameEditText, passwordEditText);

        // 获取用户名和密码
        final String userName = userNameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();

        // 判断是否输入用户名和密码
        if (userName.length() == 0 || password.length() == 0) {
            return;
        }

        // 进行登录验证
        CheckLogin login = new CheckLogin();

        // 设置回调监听
        login.setWorkEndListener(new WorkBack<String>() {
            @Override
            public void doEndWork(boolean state, String data) {
                // 关闭进度条
                progressDialog.cancel();

                if (state) {
                    // 登录成功

                    // 保存当前设置
                    ConfigUtil config = ConfigUtil.getInstance();
                    config.setUserName(userName);
                    if (loginAutoCheck != null) {
                        config.setLoginAuto(loginAutoCheck.isChecked());
                    }
                    if (loginSaveCheck != null) {
                        config.setLoginSave(loginSaveCheck.isChecked());
                    }

                    // 检查是否要保存用户名和密码
                    if ((loginSaveCheck != null && loginSaveCheck.isChecked()) || (loginAutoCheck
                            != null && loginAutoCheck.isChecked())) {
                        config.setPassword(password);
                    }

                    // 保存设置
                    config.Save();

                    // 执行登录成功后的事件
                    onPostClickLoginButton();
                } else {
                    // 登录失败
                    SimpleDialog.showDialog(BaseLoginActivity.this, data);
                }
            }
        });

        // 打开旋转进度条
        startProgressDialog();

        // 设置应用相关参数
        onApplicationAttribute(ApplicationAttribute.getApplicationAttribute());

        // 执行登录任务
        login.beginExecute(userName, password, onAppName(), ApplicationAttribute.getApplicationAttribute().getDeviceToken(), ApplicationAttribute.getApplicationAttribute().getDeviceType());
    }

    /**
     * 打开进度条
     */
    private void startProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // 设置提醒
        progressDialog.setMessage(getString(R.string.login_loading));
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
}

