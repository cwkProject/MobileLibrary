package org.mobile.library.parser;

import android.util.Log;

import org.ksoap2.serialization.SoapObject;
import org.mobile.library.util.XMLReader;
import org.xmlpull.v1.XmlPullParserException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Soap协议的返回结果的XML解析器,
 * 用于解析至多三级XML的序列值,
 * 第一级下可以有若干单键和至多一个二级键，
 * 第二级下只能包含若干组同名三级键，
 * 第三级下只能包含若干单键，
 * 不在此规则内的数据会全部丢失
 *
 * @author 超悟空
 * @version 1.0 2015/1/6
 * @since 1.0
 */
public class SoapResponseXmlToMapParser implements IResponseDataParser<Map<String, Object>, Object> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "SoapResponseXmlToMapParser.";

    /**
     * 根标签名称字符串
     */
    private String rootKey = null;

    /**
     * 集合开始的标签
     */
    private String collectionKey = null;

    /**
     * 行开始的标签
     */
    private String rowKey = null;

    @Override
    public Map<String, Object> DataParser(Object data) {

        Log.i(LOG_TAG + "DataParser", "DataParser(Object) start");

        if (data == null || !(data instanceof SoapObject)) {
            // 不是SoapObject类型
            Log.d(LOG_TAG + "DataParser", "Data isn't SoapObject");
            return null;
        }

        // 提取出传回的结果XML字符串
        String stringXML = ((SoapObject) data).getPropertyAsString(0);

        if (stringXML == null || stringXML.length() == 0) {
            Log.d(LOG_TAG + "DataParser", "stringXML is null");
            return null;
        }
        Log.i(LOG_TAG + "DataParser", "XML is " + stringXML);

        // 判断是否设置了根标签名
        if (rootKey == null || stringXML.length() == 0) {
            Log.d(LOG_TAG + "DataParser", "root node name is null");
            return null;
        }
        Log.i(LOG_TAG + "DataParser", "root node name is " + rootKey);
        Log.i(LOG_TAG + "DataParser", "collection node name is " + collectionKey);
        Log.i(LOG_TAG + "DataParser", "row node name is " + rowKey);

        // 创建XML读取器
        XMLReader reader;
        try {
            reader = new XMLReader(stringXML);
        } catch (XmlPullParserException e) {
            Log.e(LOG_TAG + "DataParser", "stringXML is error");
            Log.e(LOG_TAG + "DataParser", "XmlPullParserException is " + e.getMessage());
            return null;
        }

        if (reader.getXmlPullParser() == null) {
            Log.d(LOG_TAG + "DataParser", "xmlPullParser is null");
            return null;
        }

        // 将要返回的结果集
        Map<String, Object> dataMap = new HashMap<>();

        // 存放一行结果的序列
        Map<String, String> dataRow = null;

        // 存放全部数据行
        List<Map<String, String>> dataList = null;

        if (reader.isStartDocument()) {
            reader.next();
        }

        Log.i(LOG_TAG + "DataParser", "Parser start");

        while (!reader.isEndDocument()) {

            if (reader.isStartTag()) {
                Log.i(LOG_TAG + "DataParser", "this start tag");
                String name = reader.getNodeName();
                Log.i(LOG_TAG + "DataParser", "node name is " + name);

                if (name.equals(rootKey)) {
                    // 这个标签为序列化结构的根标签
                    Log.i(LOG_TAG + "DataParser", "this root tag");
                    // 读取下一个标签
                    goNext(reader);
                    continue;
                }

                if (collectionKey != null && name.equals(collectionKey)) {
                    // 这个标签为序列化结构，结果集的开始
                    Log.i(LOG_TAG + "DataParser", "this collection tag");
                    // 在这里新建队列
                    dataList = new ArrayList<>();
                    goNext(reader);
                    continue;
                }

                if (rowKey != null && name.equals(rowKey)) {
                    // 这是序列化的集合分支标签
                    Log.i(LOG_TAG + "DataParser", "this row tag");
                    // 新建一行记录
                    dataRow = new HashMap<>();
                    goNext(reader);
                    continue;
                }

                if (dataRow != null) {
                    // 行集合未关闭则将数据压入行
                    Log.i(LOG_TAG + "DataParser", "row put data");
                    dataRow.put(name, reader.nextText());
                    continue;
                }

                if (dataList == null) {
                    // 列表集合未开启则将数据压入根集合
                    Log.i(LOG_TAG + "DataParser", "root put data");
                    dataMap.put(name, reader.nextText());
                    continue;
                }

                goNext(reader);
                continue;

            }

            if (reader.isEndTag()) {
                Log.i(LOG_TAG + "DataParser", "this end tag");
                String name = reader.getNodeName();
                Log.i(LOG_TAG + "DataParser", "node name is " + name);

                if (rowKey != null && name.equals(rowKey) && dataList != null) {
                    // 将一行数据加入队列
                    Log.i(LOG_TAG + "DataParser", "collection put row");
                    dataList.add(dataRow);
                    // 将行游标置空
                    dataRow = null;
                    goNext(reader);
                    continue;
                }
                if (collectionKey != null && name.equals(collectionKey)) {
                    // 将一组结果压入结果集
                    Log.i(LOG_TAG + "DataParser", "root put collection");
                    dataMap.put(name, dataList);
                    // 将集合游标置空
                    dataList = null;
                    goNext(reader);
                    continue;
                }
                if (name.equals(rootKey)) {
                    // 读取下一个标签
                    goNext(reader);
                    continue;
                }
            }
            goNext(reader);
        }

        return dataMap;

    }

    /**
     * 前进到下一个标签，在每个标签处理之后执行， 如果需要特殊操作，请重写此方法
     *
     * @param reader 当前XML读取对象
     */
    public void goNext(XMLReader reader) {
        // 读取下一个标签
        reader.next();
    }

    /**
     * 设置根节点标签名称
     *
     * @param rootKey 标签名
     */
    public void setRootKey(String rootKey) {
        this.rootKey = rootKey;
    }

    /**
     * 设置集合开始节点标签名称
     *
     * @param collectionKey 标签名
     */
    public void setCollectionKey(String collectionKey) {
        this.collectionKey = collectionKey;
    }

    /**
     * 设置行节点标签名称
     *
     * @param rowKey 标签名
     */
    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

}
