package org.mobile.library.model.activity;
/**
 * Created by 超悟空 on 2016/3/22.
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.mobile.library.R;
import org.mobile.library.global.GlobalApplication;
import org.mobile.library.global.LoginStatus;
import org.mobile.library.model.work.IWorkEndListener;
import org.mobile.library.model.work.implement.SendMobileVerificationCode;
import org.mobile.library.model.work.implement.UserRegister;
import org.mobile.library.model.work.implement.VerifyMobile;

/**
 * 注册Activity模板
 *
 * @author 超悟空
 * @version 1.0 2016/3/22
 * @since 1.0
 */
public abstract class BaseRegisterActivity extends AppCompatActivity {

    /**
     * 用户名编辑框
     */
    private EditText userNameEditText = null;

    /**
     * 密码1编辑框
     */
    private EditText password1EditText = null;

    /**
     * 密码2编辑框
     */
    private EditText password2EditText = null;

    /**
     * 手机号编辑框
     */
    private EditText mobileEditText = null;

    /**
     * 验证码编辑框
     */
    private EditText verificationCodeEditText = null;

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
     * 提供注册activity布局ID
     *
     * @return activity布局文件ID，默认为{@link R.layout#activity_register}
     */
    protected int onActivityLoginLayout() {
        return R.layout.activity_register;
    }

    /**
     * 初始化界面
     */
    private void init() {
        // 初始化Toolbar
        initToolbar();
        // 初始化编辑框
        onInitEdit();
        // 初始化自定义注册按钮
        onCustomRegister();
        // 初始化自定义验证码按钮
        onCustomVerificationCode();
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

            titleTextView.setText(R.string.title_register);

            titleTextView.setVisibility(View.VISIBLE);
        } else {
            setTitle(R.string.title_register);
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
     * @return true表示开启返回导航，默认为true
     */
    protected boolean onSetHasNavigation() {
        return true;
    }


    /**
     * 初始化自定义注册按钮
     * 登录按钮默认为{@link R.id#register_content_layout_register_button}，
     * 按钮类型{@link android.support.v7.widget.AppCompatButton}
     */
    protected void onCustomRegister() {

    }

    /**
     * 初始化自定义验证码按钮
     * 登录按钮默认为{@link R.id#register_content_layout_verification_code_send_button}，
     * 按钮类型{@link android.support.v7.widget.AppCompatButton}
     */
    protected void onCustomVerificationCode() {

    }

    /**
     * 自定义界面初始化
     */
    protected void onInitCustomView() {
    }

    /**
     * 设置是否通过手机号注册
     *
     * @return true表示使用手机号注册，默认为true
     */
    protected boolean onSetMobileRegister() {
        return true;
    }

    /**
     * 设置是否需要用户名
     *
     * @return true表示需要用户名，默认为false
     */
    protected boolean onSetUserNameRegister() {
        return false;
    }

    /**
     * 初始化编辑框
     */
    protected void onInitEdit() {
        // 文本框初始化
        password1EditText = (EditText) findViewById(R.id
                .register_content_layout_password1_editText);
        password2EditText = (EditText) findViewById(R.id
                .register_content_layout_password2_editText);

        if (onSetUserNameRegister()) {
            userNameEditText = (EditText) findViewById(R.id
                    .register_content_layout_user_name_editText);
        } else {
            View userNameLayout = findViewById(R.id.register_content_layout_user_name_linearLayout);
            userNameLayout.setVisibility(View.GONE);
        }

        if (onSetMobileRegister()) {
            mobileEditText = (EditText) findViewById(R.id.register_content_layout_mobile_editText);
            verificationCodeEditText = (EditText) findViewById(R.id
                    .register_content_layout_verification_code_editText);
        } else {
            View mobileLayout = findViewById(R.id.register_content_layout_mobile_linearLayout);
            mobileLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 获取验证码按钮点击事件
     *
     * @param view 按钮
     */
    public void onSendVerificationCodeClick(final View view) {
        if (!onSetMobileRegister() || mobileEditText == null) {
            return;
        }

        String mobile = mobileEditText.getText().toString().trim();

        if (mobile.length() != 11) {
            Toast.makeText(this, R.string.prompt_mobile_error, Toast.LENGTH_SHORT).show();
        }

        SendMobileVerificationCode sendMobileVerificationCode = new SendMobileVerificationCode();

        sendMobileVerificationCode.setWorkEndListener(new IWorkEndListener<String>() {
            @Override
            public void doEndWork(boolean state, String message, String data) {
                Toast.makeText(BaseRegisterActivity.this, message, Toast.LENGTH_SHORT).show();

                if (state) {

                    // 启动倒计时动画
                    final AppCompatButton button = (AppCompatButton) view;
                    button.setEnabled(false);

                    ValueAnimator valueAnimator = ValueAnimator.ofInt(120, 0).setDuration(120000);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            button.setText(String.valueOf(animation.getAnimatedValue()));
                        }
                    });

                    valueAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            button.setEnabled(true);
                            button.setText(R.string.send_verification_code_button);
                        }
                    });
                }
            }
        });

        sendMobileVerificationCode.beginExecute(mobile);
    }

    /**
     * 注册按钮点击事件
     *
     * @param view 按钮
     */
    public void onRegisterClick(View view) {

        String userName = null;

        if (onSetUserNameRegister()) {
            userName = userNameEditText.getText().toString().trim();

            if (userName.length() < 3) {
                Toast.makeText(this, R.string.prompt_user_name_error, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        String mobile = null;
        String verificationCode = null;

        if (onSetMobileRegister()) {

            mobile = mobileEditText.getText().toString().trim();
            verificationCode = verificationCodeEditText.getText().toString().trim();

            if (mobile.length() != 11) {
                Toast.makeText(this, R.string.prompt_mobile_error, Toast.LENGTH_SHORT).show();
                return;
            }

            if (verificationCode.length() == 0) {
                Toast.makeText(this, R.string.prompt_verification_code_error, Toast.LENGTH_SHORT)
                        .show();
                return;
            }
        }

        final String password1 = password1EditText.getText().toString().trim();
        final String password2 = password2EditText.getText().toString().trim();

        if (password1.length() == 0 || password2.length() == 0) {
            Toast.makeText(this, R.string.prompt_password_null_error, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password1.equals(password2)) {
            Toast.makeText(this, R.string.prompt_password_different_error, Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        if (onSetMobileRegister()) {
            VerifyMobile verifyMobile = new VerifyMobile();

            final String finalUserName = userName;
            final String finalMobile = mobile;

            verifyMobile.setWorkEndListener(new IWorkEndListener<String>() {
                @Override
                public void doEndWork(boolean state, String message, String data) {
                    if (state) {
                        // 手机验证成功
                        onDoRegister(finalUserName, finalMobile, password1, password2);
                    } else {
                        Toast.makeText(BaseRegisterActivity.this, message, Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            });
        } else {
            onDoRegister(userName, null, password1, password2);
        }
    }

    /**
     * 执行注册
     *
     * @param parameters 参数
     */
    protected void onDoRegister(String... parameters) {
        UserRegister userRegister = new UserRegister();
        userRegister.setWorkEndListener(new IWorkEndListener<String>() {
            @Override
            public void doEndWork(boolean state, String message, String data) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    // 关闭进度条
                    progressDialog.cancel();
                }

                LoginStatus loginStatus = GlobalApplication.getLoginStatus();

                loginStatus.setLogin(state);
                loginStatus.setUserID(data);

                if (state) {
                    // 注册成功
                    onRegisterSuccess();
                } else {
                    // 注册失败
                    onRegisterFailed(message);
                }
            }
        });

        // 打开旋转进度条
        startProgressDialog();

        userRegister.beginExecute(parameters[0], parameters[1], parameters[2], parameters[3]);
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
     * 注册失败执行
     *
     * @param message 返回消息
     */
    protected void onRegisterFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 注册成功执行，设置跳转
     */
    protected abstract void onRegisterSuccess();
}
