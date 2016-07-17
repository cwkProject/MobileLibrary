package org.mobile.library.model.activity;
/**
 * Created by 超悟空 on 2016/3/22.
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.mobile.library.R;
import org.mobile.library.common.dialog.SimpleDialog;
import org.mobile.library.global.ApplicationConfig;
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
    protected EditText userNameEditText = null;

    /**
     * 密码1编辑框
     */
    protected EditText password1EditText = null;

    /**
     * 密码2编辑框
     */
    protected EditText password2EditText = null;

    /**
     * 手机号编辑框
     */
    protected EditText mobileEditText = null;

    /**
     * 验证码编辑框
     */
    protected EditText verificationCodeEditText = null;

    /**
     * 用户名提示框
     */
    protected TextInputLayout userNameTextInputLayout = null;

    /**
     * 手机号提示框
     */
    protected TextInputLayout mobileTextInputLayout = null;

    /**
     * 验证码提示框
     */
    protected TextInputLayout verificationCodeTextInputLayout = null;

    /**
     * 密码1提示框
     */
    protected TextInputLayout password1TextInputLayout = null;

    /**
     * 密码2提示框
     */
    protected TextInputLayout password2TextInputLayout = null;

    /**
     * 根布局
     */
    private View rootView = null;

    /**
     * 进度条
     */
    protected ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(onActivityRegisterLayout());

        rootView = findViewById(R.id.activity_register_root_layout);

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
    protected int onActivityRegisterLayout() {
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
    @SuppressWarnings("ConstantConditions")
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
        AppCompatButton button = (AppCompatButton) findViewById(R.id
                .register_content_layout_register_button);

        TypedArray typedArray = getTheme().obtainStyledAttributes(new int[]{R.attr.colorPrimary});

        if (button != null) {
            button.setSupportBackgroundTintList(typedArray.getColorStateList(0));
        }

        typedArray.recycle();

        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRegisterClick();
            }
        });
    }

    /**
     * 初始化自定义验证码按钮
     * 登录按钮默认为{@link R.id#register_content_layout_verification_code_send_button}，
     * 按钮类型{@link android.support.v7.widget.AppCompatButton}
     */
    protected void onCustomVerificationCode() {
        AppCompatButton button = (AppCompatButton) findViewById(R.id
                .register_content_layout_verification_code_send_button);

        TypedArray typedArray = getTheme().obtainStyledAttributes(new int[]{R.attr.colorPrimary});

        if (button != null) {
            button.setSupportBackgroundTintList(typedArray.getColorStateList(0));
        }

        typedArray.recycle();

        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendVerificationCodeClick(v);
            }
        });
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
            if (userNameLayout != null) {
                userNameLayout.setVisibility(View.GONE);
            }
        }

        if (onSetMobileRegister()) {
            mobileEditText = (EditText) findViewById(R.id.register_content_layout_mobile_editText);
            verificationCodeEditText = (EditText) findViewById(R.id
                    .register_content_layout_verification_code_editText);
        } else {
            View mobileLayout = findViewById(R.id.register_content_layout_mobile_linearLayout);

            if (mobileLayout != null) {
                mobileLayout.setVisibility(View.GONE);
            }
        }

        userNameTextInputLayout = (TextInputLayout) findViewById(R.id
                .register_content_layout_user_name_textInputLayout);
        mobileTextInputLayout = (TextInputLayout) findViewById(R.id
                .register_content_layout_mobile_textInputLayout);
        verificationCodeTextInputLayout = (TextInputLayout) findViewById(R.id
                .register_content_layout_verification_code_textInputLayout);
        password1TextInputLayout = (TextInputLayout) findViewById(R.id
                .register_content_layout_password1_textInputLayout);
        password2TextInputLayout = (TextInputLayout) findViewById(R.id
                .register_content_layout_password2_textInputLayout);

        // 绑定错误提示
        onBindEditHint();
    }

    /**
     * 绑定输入框错误提示
     */
    protected void onBindEditHint() {

        // 用户名提示
        if (onSetUserNameRegister() && userNameTextInputLayout != null) {

            userNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        String userName = userNameEditText.getText().toString();

                        // 检测用户名
                        checkUserName(userName);
                    }
                }
            });

            userNameEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String userName = s.toString();
                    if (!userName.contains(" ") && userName.length() >= 3) {
                        userNameTextInputLayout.setError(null);
                        userNameTextInputLayout.setErrorEnabled(false);
                    }
                }
            });
        }

        // 手机号提示
        if (onSetMobileRegister() && mobileTextInputLayout != null) {

            mobileEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        String mobile = mobileEditText.getText().toString();
                        // 检测手机号
                        checkMobile(mobile);
                    }
                }
            });

            mobileEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String mobile = s.toString();
                    if (mobile.length() == 11) {
                        mobileTextInputLayout.setError(null);
                        mobileTextInputLayout.setErrorEnabled(false);
                    }
                }
            });
        }

        // 验证码提示
        if (onSetMobileRegister() && verificationCodeTextInputLayout != null) {

            verificationCodeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        String verificationCode = verificationCodeEditText.getText().toString();

                        // 检测验证码
                        checkVerificationCode(verificationCode);
                    }
                }
            });

            verificationCodeEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String verificationCode = s.toString();
                    if (verificationCode.length() == 6) {
                        verificationCodeTextInputLayout.setError(null);
                        verificationCodeTextInputLayout.setErrorEnabled(false);
                    }
                }
            });
        }

        // 密码1提示
        if (password1TextInputLayout != null) {

            password1EditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        String password = password1EditText.getText().toString();

                        // 检测密码
                        checkPassword1(password);
                    }
                }
            });

            password1EditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String password = s.toString();
                    if (password.length() >= 6) {
                        password1TextInputLayout.setError(null);
                        password1TextInputLayout.setErrorEnabled(false);
                    }
                }
            });
        }

        // 密码2提示
        if (password2TextInputLayout != null) {

            password2EditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        String password1 = password1EditText.getText().toString();
                        String password2 = password2EditText.getText().toString();

                        // 检测密码
                        checkPassword2(password1, password2);
                    }
                }
            });

            password2EditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String password = s.toString();
                    if (password.length() >= 6) {
                        password2TextInputLayout.setError(null);
                        password2TextInputLayout.setErrorEnabled(false);
                    }
                }
            });
        }
    }

    /**
     * 检测手机号
     *
     * @param mobile 手机号
     *
     * @return true表示检测通过
     */
    protected boolean checkMobile(String mobile) {
        if (mobileTextInputLayout != null) {
            if (TextUtils.isEmpty(mobile)) {
                mobileTextInputLayout.setError(getString(R.string.prompt_mobile_null));
                return false;
            }

            if (mobile.length() != 11) {
                mobileTextInputLayout.setError(getString(R.string.prompt_mobile_short));
                return false;
            }
        } else {
            if (mobile.length() != 11) {
                if (rootView != null) {
                    Snackbar.make(rootView, R.string.prompt_mobile_short, Snackbar.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(this, R.string.prompt_mobile_short, Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        }

        return true;
    }

    /**
     * 检测验证码
     *
     * @param verificationCode 验证码
     *
     * @return true表示检测通过
     */
    protected boolean checkVerificationCode(String verificationCode) {
        if (verificationCodeTextInputLayout != null) {
            if (TextUtils.isEmpty(verificationCode)) {
                verificationCodeTextInputLayout.setError(getString(R.string
                        .prompt_verification_code_null));
                return false;
            }

            if (verificationCode.length() != 6) {
                verificationCodeTextInputLayout.setError(getString(R.string
                        .prompt_verification_code_short));
                return false;
            }
        } else {
            if (verificationCode.length() != 6) {
                if (rootView != null) {
                    Snackbar.make(rootView, R.string.prompt_verification_code_short, Snackbar
                            .LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.prompt_verification_code_short, Toast
                            .LENGTH_SHORT).show();
                }
                return false;
            }
        }

        return true;
    }

    /**
     * 检测密码1
     *
     * @param password 密码
     *
     * @return true表示检测通过
     */
    protected boolean checkPassword1(String password) {

        if (password1TextInputLayout != null) {

            if (TextUtils.isEmpty(password)) {
                password1TextInputLayout.setError(getString(R.string.prompt_password_null_error));
                return false;
            }

            if (password.length() < 6) {
                password1TextInputLayout.setError(getString(R.string.prompt_password_short));
                return false;
            }
        } else {
            if (password.length() < 6) {
                if (rootView != null) {
                    Snackbar.make(rootView, R.string.prompt_password_short, Snackbar
                            .LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.prompt_password_short, Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        }

        return true;
    }

    /**
     * 检测密码2
     *
     * @param password1 密码1
     * @param password2 密码2
     *
     * @return true表示检测通过
     */
    protected boolean checkPassword2(String password1, String password2) {
        if (password2TextInputLayout != null) {

            if (!password1.equals(password2)) {
                password2TextInputLayout.setError(getString(R.string
                        .prompt_password_different_error));
                return false;
            }
        } else {
            if (!password1.equals(password2)) {
                if (rootView != null) {
                    Snackbar.make(rootView, R.string.prompt_password_different_error, Snackbar
                            .LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.prompt_password_different_error, Toast
                            .LENGTH_SHORT).show();
                }
                return false;
            }
        }

        return true;
    }

    /**
     * 检测用户名
     *
     * @param userName 用户名
     *
     * @return true表示检测通过
     */
    protected boolean checkUserName(String userName) {

        if (userNameTextInputLayout != null) {

            if (TextUtils.isEmpty(userName)) {
                userNameTextInputLayout.setError(getString(R.string.prompt_user_name_null));
                return false;
            }

            if (userName.contains(" ")) {
                userNameTextInputLayout.setError(getString(R.string.prompt_user_name_blank));
                return false;
            }

            if (userName.length() < 3) {
                userNameTextInputLayout.setError(getString(R.string.prompt_user_name_short));
                return false;
            }
        } else {
            if (userName.contains(" ") || userName.length() < 3) {
                if (rootView != null) {
                    Snackbar.make(rootView, R.string.prompt_user_name_error, Snackbar
                            .LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.prompt_user_name_error, Toast.LENGTH_SHORT)
                            .show();
                }
                return false;
            }
        }

        return true;
    }

    /**
     * 获取验证码按钮点击事件
     *
     * @param view 按钮
     */
    protected void onSendVerificationCodeClick(final View view) {
        // 启动倒计时动画
        final AppCompatButton button = (AppCompatButton) view;
        button.setEnabled(false);

        if (!onSetMobileRegister() || mobileEditText == null) {
            button.setEnabled(true);
            return;
        }

        String mobile = mobileEditText.getText().toString();

        if (!checkMobile(mobile)) {
            button.setEnabled(true);
            return;
        }

        SendMobileVerificationCode sendMobileVerificationCode = new SendMobileVerificationCode();

        sendMobileVerificationCode.setWorkEndListener(new IWorkEndListener<String>() {
            @Override
            public void doEndWork(boolean state, String message, String data) {
                Toast.makeText(BaseRegisterActivity.this, message, Toast.LENGTH_SHORT).show();

                if (state) {

                    // 重发消息后缀
                    final String resendText = getString(R.string.resend_verification_code);

                    ValueAnimator valueAnimator = ValueAnimator.ofInt(60, 0).setDuration(60000);
                    valueAnimator.setInterpolator(new LinearInterpolator());
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            button.setText(String.format("%s%s", String.valueOf(animation
                                    .getAnimatedValue()), resendText));
                        }
                    });

                    valueAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            button.setEnabled(true);
                            button.setText(R.string.send_verification_code_button);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            button.setEnabled(true);
                            button.setText(R.string.send_verification_code_button);
                        }
                    });

                    valueAnimator.start();
                } else {
                    button.setEnabled(true);
                    button.setText(R.string.send_verification_code_button);
                }
            }
        });

        sendMobileVerificationCode.beginExecute(mobile);
    }

    /**
     * 注册按钮点击事件
     */
    protected void onRegisterClick() {

        String userName = null;

        if (onSetUserNameRegister()) {
            userName = userNameEditText.getText().toString();

            if (!checkUserName(userName)) {
                return;
            }
        }

        String mobile = null;
        String verificationCode = null;

        if (onSetMobileRegister()) {

            mobile = mobileEditText.getText().toString();
            verificationCode = verificationCodeEditText.getText().toString();

            if (!checkMobile(mobile) || !checkVerificationCode(verificationCode)) {
                return;
            }
        }

        final String password1 = password1EditText.getText().toString();
        final String password2 = password2EditText.getText().toString();

        if (!checkPassword1(password1) || !checkPassword2(password1, password2)) {
            return;
        }

        // 打开旋转进度条
        startProgressDialog();

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
                        if (progressDialog != null && progressDialog.isShowing()) {
                            // 关闭进度条
                            progressDialog.cancel();
                        }
                        // 注册失败
                        onRegisterFailed(message);
                    }
                }
            });

            verifyMobile.beginExecute(mobile, verificationCode);
        } else {
            onDoRegister(userName, null, password1, password2);
        }
    }

    /**
     * 执行注册
     *
     * @param parameters 参数
     */
    protected void onDoRegister(final String... parameters) {
        UserRegister userRegister = new UserRegister();
        userRegister.setWorkEndListener(new IWorkEndListener<String>() {
            @Override
            public void doEndWork(boolean state, String message, String data) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    // 关闭进度条
                    progressDialog.cancel();
                }

                // 注册完成执行
                onLoginData(parameters[1] != null ? parameters[1] : parameters[0], parameters[2],
                        state, data);

                if (state) {
                    // 注册成功
                    onRegisterSuccess();
                } else {
                    // 注册失败
                    onRegisterFailed(message);
                }
            }
        });

        userRegister.beginExecute(parameters[0], parameters[1], parameters[2], parameters[3]);
    }

    /**
     * 注册执行结果装配，
     * 用于保存用户名密码等
     *
     * @param userName 用户名
     * @param password 密码
     * @param state    注册结果
     * @param data     用户id
     */
    protected void onLoginData(String userName, String password, boolean state, String data) {
        LoginStatus loginStatus = GlobalApplication.getLoginStatus();

        loginStatus.setLogin(state);
        loginStatus.setUserID(data);

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
        progressDialog.setMessage(getString(R.string.register_loading));
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    /**
     * 注册失败执行
     *
     * @param message 返回消息
     */
    protected void onRegisterFailed(String message) {
        SimpleDialog.showDialog(this, message);
    }

    /**
     * 注册成功执行，设置跳转
     */
    protected abstract void onRegisterSuccess();
}
