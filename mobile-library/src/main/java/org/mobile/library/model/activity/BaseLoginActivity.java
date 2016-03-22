package org.mobile.library.model.activity;
/**
 * Created by 超悟空 on 2015/6/11.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import org.mobile.library.R;
import org.mobile.library.common.dialog.SimpleDialog;
import org.mobile.library.global.ApplicationConfig;
import org.mobile.library.global.GlobalApplication;
import org.mobile.library.global.LoginStatus;
import org.mobile.library.model.work.IWorkEndListener;
import org.mobile.library.model.work.implement.CheckLogin;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 登录Activity模板
 *
 * @author 超悟空
 * @version 1.0 2015/6/11
 * @since 1.0
 */
public abstract class BaseLoginActivity extends AppCompatActivity {

    /**
     * 用户名编辑框
     */
    private EditText userNameEditText = null;

    /**
     * 密码编辑框
     */
    private EditText passwordEditText = null;

    /**
     * 进度条
     */
    protected ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(onActivityLoginLayout());

        // 重置用户登录参数
        GlobalApplication.getLoginStatus().Reset();

        // 初始化界面
        init();
    }

    /**
     * 提供登录activity布局ID
     *
     * @return activity布局文件ID，默认为{@link R.layout#activity_login}
     */
    protected int onActivityLoginLayout() {
        return R.layout.activity_login;
    }

    /**
     * 初始化界面
     */
    private void init() {
        // 初始化Toolbar
        initToolbar();
        // 初始化编辑框
        initEdit();
        // 初始化自定义登录按钮事件
        onCustomLoginButton();
        // 初始化自定义注册按钮
        onCustomRegister();
        // 自定义界面初始化
        onInitCustomView();
    }

    /**
     * 初始化标题栏
     */
    protected void initToolbar() {
        // 得到Toolbar标题栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // 关联ActionBar
        setSupportActionBar(toolbar);

        if (onSetCenterTitle()) {
            // 标题居中
            // 取消原actionBar标题
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            // 得到标题文本
            TextView titleTextView = (TextView) findViewById(R.id.toolbar_title);

            titleTextView.setText(R.string.title_login);

            titleTextView.setVisibility(View.VISIBLE);
        } else {
            setTitle(R.string.title_login);
        }

        if (onSetHasNavigation()) {
            // 显示后退
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 与返回键相同
                    onBackPressed();
                }
            });
        }
    }

    /**
     * 设置是否居中显示标题
     *
     * @return true表示居中，默认为false
     */
    protected boolean onSetCenterTitle() {
        return false;
    }

    /**
     * 设置是否带有返回导航
     *
     * @return true表示开启返回导航，默认为false
     */
    protected boolean onSetHasNavigation() {
        return false;
    }

    /**
     * 初始化自定义登录按钮事件，
     * 登录按钮默认为{@link R.id#login_content_layout_login_button}，
     * 按钮类型{@link android.support.v7.widget.AppCompatButton}
     */
    protected void onCustomLoginButton() {
    }


    /**
     * 初始化自定义注册按钮
     */
    protected void onCustomRegister() {

    }

    /**
     * 自定义界面初始化
     */
    protected void onInitCustomView() {
    }

    /**
     * 提供用户名编辑框ID
     *
     * @return 用户名编辑框ID，默认为{@link R.id#login_content_layout_user_name_editText}
     */
    protected int onUserNameEditTextID() {
        return R.id.login_content_layout_user_name_editText;
    }

    /**
     * 提供密码编辑框ID
     *
     * @return 密码编辑框ID，默认为{@link R.id#login_content_layout_password_editText}
     */
    protected int onPasswordEditTextID() {
        return R.id.login_content_layout_password_editText;
    }

    /**
     * 初始化编辑框
     */
    private void initEdit() {
        // 文本框初始化
        userNameEditText = (EditText) findViewById(onUserNameEditTextID());
        passwordEditText = (EditText) findViewById(onPasswordEditTextID());

        // 尝试填充数据
        if (GlobalApplication.getApplicationConfig().getUserName() != null) {
            // 填充用户
            userNameEditText.setText(GlobalApplication.getApplicationConfig().getUserName());

            // 让密码框拥有焦点
            setSoftInput(passwordEditText);
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
                InputMethodManager inputManager = (InputMethodManager) editText.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(editText, 0);
            }

        }, 600);
    }

    /**
     * 登录成功后执行，设置跳转
     *
     * @param message 成功消息
     */
    protected abstract void onLoginSuccess(String message);

    /**
     * 登录失败后执行
     *
     * @param message 失败消息
     */
    protected void onLoginFailed(String message) {
        SimpleDialog.showDialog(this, message);
    }

    /**
     * 登录按钮点击事件
     *
     * @param view 按钮
     */
    public void onLoginClick(View view) {

        // 获取用户名和密码
        final String userName = userNameEditText.getText().toString();
        final String password = passwordEditText.getText().toString();

        // 判断是否输入用户名和密码
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
            return;
        }

        // 进行登录验证
        CheckLogin login = new CheckLogin();

        // 设置回调监听
        login.setWorkEndListener(new IWorkEndListener<String>() {
            @Override
            public void doEndWork(boolean state, String message, String data) {

                if (progressDialog != null && progressDialog.isShowing()) {
                    // 关闭进度条
                    progressDialog.cancel();
                }

                LoginStatus loginStatus = GlobalApplication.getLoginStatus();

                loginStatus.setLogin(state);
                loginStatus.setUserID(data);

                // 登录完成执行
                onLoginData(userName, password, state, data);

                if (state) {
                    // 登录成功

                    // 执行登录成功后的事件
                    onLoginSuccess(message);
                } else {
                    // 登录失败
                    onLoginFailed(message);
                }
            }
        });

        // 打开旋转进度条
        startProgressDialog();

        // 执行登录任务
        login.beginExecute(userName, password);
    }

    /**
     * 登录执行结果装配，
     * 用于保存用户名密码等
     *
     * @param userName 用户名
     * @param password 密码
     * @param state    登录结果
     * @param data     用户id
     */
    protected void onLoginData(String userName, String password, boolean state, String data) {
        if (state) {
            // 登录成功

            // 保存当前设置
            ApplicationConfig config = GlobalApplication.getApplicationConfig();
            config.setUserName(userName);
            config.setPassword(password);

            // 保存设置
            config.Save();
        }
    }

    /**
     * 打开进度条
     */
    protected void startProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // 设置提醒
        progressDialog.setMessage(getString(R.string.login_loading));
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    /**
     * 注册按钮点击事件
     *
     * @param view 按钮
     */
    public void onRegisterClick(View view) {
    }
}

