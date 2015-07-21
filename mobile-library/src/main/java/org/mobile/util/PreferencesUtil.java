package org.mobile.util;
/**
 * Created by 超悟空 on 2015/1/8.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * 基于{@link android.content.SharedPreferences}的对象属性持久化工具,
 * 只支持Sting,int,boolean,float,long的属性值，其他类型将不会被保存
 *
 * @author 超悟空
 * @version 1.0 2015/1/8
 * @since 1.0
 */
public class PreferencesUtil {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "PreferencesUtil.";

    /**
     * 上下文对象
     */
    private Context context = null;

    /**
     * 保存文件的名称
     */
    private String fileName = null;

    /**
     * 传入上下文和文件名的构造函数
     *
     * @param context 上下文
     */
    public PreferencesUtil(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
        Log.i(LOG_TAG + "PreferencesUtil", "file name is " + fileName);
    }

    /**
     * 保存指定对象的属性值到文件
     *
     * @param obj 要保存的对象
     */
    public void Save(Object obj) {

        Log.i(LOG_TAG + "Save", "Save(Object) start");

        if (isNullObject(obj)) {
            Log.d(LOG_TAG + "Save", "object is null");
            return;
        }

        // 获取文件编辑器
        SharedPreferences.Editor editor = getEditor();

        // 获取对象类名
        String objectName = obj.getClass().getName();
        Log.i(LOG_TAG + "Save", "object name is " + objectName);

        // 获取对象的全部属性
        Field[] fields = obj.getClass().getDeclaredFields();

        // 遍历全部对象属性
        for (Field field : fields) {
            // 写一个属性值
            put(editor, field, obj, objectName);
        }

        // 提交保存
        editor.commit();
        Log.i(LOG_TAG + "Save", "Save(Object) end");
    }

    /**
     * 判断传入的对象是否为空
     *
     * @param obj 要检查的对象
     *
     * @return 为空返回true
     */
    private boolean isNullObject(Object obj) {
        if (obj == null) {
            Log.d(LOG_TAG + "isNullObject", "The target Object is null");
            return true;
        }
        return false;
    }

    /**
     * 向文件写入属性值
     *
     * @param editor 指定的编辑器
     * @param field  要写入的属性
     * @param obj    取值对象
     * @param pre    key前缀
     */
    private void put(SharedPreferences.Editor editor, Field field, Object obj, String pre) {
        // 关闭属性访问权限检查
        field.setAccessible(true);

        try {
            Log.i(LOG_TAG + "put", "field type is " + field.getType().getName());
            Log.i(LOG_TAG + "put", "field name is " + field.getName());
            Log.i(LOG_TAG + "put", "field value is " + field.get(obj));
            // 根据属性类型选择操作
            switch (field.getType().getName()) {
                case "java.lang.String":
                    editor.putString(pre + "." + field.getName(), (String) field.get(obj));
                    break;
                case "int":
                    editor.putInt(pre + "." + field.getName(), field.getInt(obj));
                    break;
                case "boolean":
                    editor.putBoolean(pre + "." + field.getName(), field.getBoolean(obj));
                    break;
                case "float":
                    editor.putFloat(pre + "." + field.getName(), field.getFloat(obj));
                    break;
                case "long":
                    editor.putLong(pre + "." + field.getName(), field.getLong(obj));
                    break;
            }
        } catch (IllegalAccessException e) {
            Log.d(LOG_TAG + "put", "IllegalAccessException is " + e.getMessage());
        }
    }

    /**
     * 从文件读取对象属性值，如果文件中没有对应值，则会保留原属性值
     *
     * @param obj 要填充的对象
     */
    public void Read(Object obj) {
        Log.i(LOG_TAG + "Read", "Read(Object) start");

        if (isNullObject(obj)) {
            Log.d(LOG_TAG + "Read", "object is null");
            return;
        }

        // 获取存储对象
        SharedPreferences reader = getSharedPreferences();

        // 获取对象类名
        String objectName = obj.getClass().getName();
        Log.i(LOG_TAG + "Read", "object name is " + objectName);

        // 获取对象的全部属性
        Field[] fields = obj.getClass().getDeclaredFields();

        // 遍历全部对象属性
        for (Field field : fields) {
            // 读一个属性值
            push(reader, field, obj, objectName);
        }

        Log.i(LOG_TAG + "Read", "Read(Object) end");
    }

    /**
     * 从文件读取属性值
     *
     * @param reader 读取器
     * @param field  要读取的属性
     * @param obj    目标对象
     * @param pre    key前缀
     */
    private void push(SharedPreferences reader, Field field, Object obj, String pre) {
        // 关闭属性访问权限检查
        field.setAccessible(true);

        try {
            Log.i(LOG_TAG + "push", "field type is " + field.getType().getName());
            Log.i(LOG_TAG + "push", "field name is " + field.getName());

            // 根据属性类型选择操作
            switch (field.getType().getName()) {
                case "java.lang.String":
                    field.set(obj, reader.getString(pre + "." + field.getName(), (String) field.get(obj)));
                    break;
                case "int":
                    field.setInt(obj, reader.getInt(pre + "." + field.getName(), field.getInt(obj)));
                    break;
                case "boolean":
                    field.setBoolean(obj, reader.getBoolean(pre + "." + field.getName(), field.getBoolean(obj)));
                    break;
                case "float":
                    field.setFloat(obj, reader.getFloat(pre + "." + field.getName(), field.getFloat(obj)));
                    break;
                case "long":
                    field.setLong(obj, reader.getLong(pre + "." + field.getName(), field.getLong(obj)));
                    break;
            }
            Log.i(LOG_TAG + "push", "field value is " + field.get(obj));
        } catch (IllegalAccessException e) {
            Log.d(LOG_TAG + "push", "IllegalAccessException is " + e.getMessage());
        }
    }

    /**
     * 从文件清空对象属性
     */
    public void Clear(Object obj) {
        Log.i(LOG_TAG + "Clear", "Clear(Object) start");

        if (isNullObject(obj)) {
            Log.d(LOG_TAG + "Clear", "object is null");
            return;
        }

        // 获取文件编辑器
        SharedPreferences.Editor editor = getEditor();

        // 获取对象类名
        String objectName = obj.getClass().getName();
        Log.i(LOG_TAG + "Clear", "object name is " + objectName);

        // 获取对象的全部属性
        Field[] fields = obj.getClass().getDeclaredFields();

        // 遍历全部对象属性
        for (Field field : fields) {
            Log.i(LOG_TAG + "Clear", "field name is " + field.getName());
            // 移除一个属性值
            editor.remove(objectName + "." + field.getName());
        }

        // 提交保存
        editor.commit();
        Log.i(LOG_TAG + "Clear", "Clear(Object) end");
    }

    /**
     * 获取当前的存储器对象
     *
     * @return SharedPreferences对象
     */
    public SharedPreferences getSharedPreferences() {
        return this.context.getSharedPreferences(this.fileName, ContextUtil.MODE_PRIVATE);
    }

    /**
     * 获取当前的文件编辑器对象
     *
     * @return Editor对象
     */
    public SharedPreferences.Editor getEditor() {
        return getSharedPreferences().edit();
    }
}
