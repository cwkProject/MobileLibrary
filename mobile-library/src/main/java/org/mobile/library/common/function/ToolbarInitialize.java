package org.mobile.library.common.function;
/**
 * Created by 超悟空 on 2016/7/29.
 */

import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.mobile.library.R;

/**
 * toolbar初始化工具
 *
 * @author 超悟空
 * @version 1.0 2016/7/29
 * @since 1.0
 */
public class ToolbarInitialize {

    /**
     * 初始化Toolbar
     *
     * @param activity Activity
     * @param titleId  标题资源id
     * @param back     true表示开启返回按钮
     * @param center   true表示居中标题，须在布局中引入{@link org.mobile.library.R.layout#toolbar_center_title}布局
     */
    @SuppressWarnings("ConstantConditions")
    public static void initToolbar(final AppCompatActivity activity, @StringRes int titleId,
                                   boolean back, boolean center) {
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);

        if (center) {
            TextView title = (TextView) activity.findViewById(R.id.toolbar_title);

            if (title != null) {
                title.setText(titleId);
            }
        } else {
            activity.setTitle(titleId);
        }

        activity.setSupportActionBar(toolbar);

        if (back) {

            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.finish();
                }
            });
        }
    }

    /**
     * 初始化Toolbar，带有返回按钮，标题局左
     *
     * @param activity Activity
     * @param titleId  标题资源id
     */
    public static void initToolbar(AppCompatActivity activity, @StringRes int titleId) {
        initToolbar(activity, titleId, true, false);
    }

    /**
     * 初始化Toolbar，带有返回按钮
     *
     * @param activity Activity
     * @param titleId  标题资源id
     * @param center   true表示居中标题，须在布局中引入{@link org.mobile.library.R.layout#toolbar_center_title}布局
     */
    public static void initToolbar(AppCompatActivity activity, @StringRes int titleId, boolean
            center) {
        initToolbar(activity, titleId, true, true);
    }
}
