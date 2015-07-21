package org.mobile.util;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * XML读取器
 *
 * @author 超悟空
 * @version 1.0 2015/1/6
 * @since 1.0
 */
public class XMLReader {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "XMLReader.";

    /**
     * 存放XmlPullParser的XML解析器对象
     */
    private XmlPullParser xmlPullParser = null;

    /**
     * XML格式的字符串
     */
    private String stringXML = null;

    /**
     * 存放XML输入流对象
     */
    private InputStream inStream = null;

    /**
     * 默认字符编码
     */
    private static final String DEFAULT_ENCODING = "UTF-8";

    /**
     * 生成解析器对象
     *
     * @throws XmlPullParserException
     */
    private void initXmlPullParser() throws XmlPullParserException {
        initXmlPullParser(DEFAULT_ENCODING);
    }

    /**
     * 生成指定字符编码的解析器对象
     *
     * @param encoding 字符编码
     *
     * @throws XmlPullParserException
     */
    private void initXmlPullParser(String encoding) throws XmlPullParserException {
        this.xmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
        this.xmlPullParser.setInput(inStream, encoding);
        Log.i(LOG_TAG + "initXmlPullParser", "encode is " + encoding);
    }

    /**
     * 传入XML序列化字符串的构造函数
     *
     * @param stringXML XML格式字符串
     *
     * @throws org.xmlpull.v1.XmlPullParserException
     */
    public XMLReader(String stringXML) throws XmlPullParserException {
        this(stringXML, DEFAULT_ENCODING);
    }

    /**
     * 传入InputStream输入流的构造函数
     *
     * @param inStream XML格式的输入流
     *
     * @throws org.xmlpull.v1.XmlPullParserException
     */
    public XMLReader(InputStream inStream) throws XmlPullParserException {
        this(inStream, DEFAULT_ENCODING);
    }

    /**
     * 传入XML序列化字符串和编码的构造函数
     *
     * @param stringXML XML格式字符串
     * @param encoding  字符编码
     *
     * @throws org.xmlpull.v1.XmlPullParserException
     */
    public XMLReader(String stringXML, String encoding) throws XmlPullParserException {
        this.stringXML = stringXML;
        Log.i(LOG_TAG + "XMLReader", "XML is " + stringXML);
        this.inStream = new ByteArrayInputStream(stringXML.getBytes());

        initXmlPullParser(encoding);
    }

    /**
     * 传入InputStream输入流和编码的构造函数
     *
     * @param inStream XML格式的输入流
     * @param encoding 字符编码
     *
     * @throws org.xmlpull.v1.XmlPullParserException
     */
    public XMLReader(InputStream inStream, String encoding) throws XmlPullParserException {
        this.inStream = inStream;
        Log.i(LOG_TAG + "XMLReader", "inStream is " + inStream.toString());
        initXmlPullParser(encoding);
    }

    /**
     * 空构造函数
     */
    public XMLReader() {
        try {
            this.xmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
            Log.i(LOG_TAG + "XMLReader", "XMLReader() is invoked");
        } catch (XmlPullParserException e) {
            Log.e(LOG_TAG + "XMLReader", "XmlPullParserException is " + e.getMessage());
        }
    }

    /**
     * 设置XML源
     *
     * @param stringXML XML格式字符串
     *
     * @throws org.xmlpull.v1.XmlPullParserException
     */
    public void setSource(String stringXML) throws XmlPullParserException {
        setSource(stringXML, DEFAULT_ENCODING);
    }

    /**
     * 设置XML源
     *
     * @param inStream XML输入流
     *
     * @throws org.xmlpull.v1.XmlPullParserException
     */
    public void setSource(InputStream inStream) throws XmlPullParserException {
        setSource(inStream, DEFAULT_ENCODING);
    }

    /**
     * 设置XML源和编码
     *
     * @param stringXML XML格式字符串
     * @param encoding  字符编码
     *
     * @throws org.xmlpull.v1.XmlPullParserException
     */
    public void setSource(String stringXML, String encoding) throws XmlPullParserException {
        this.stringXML = stringXML;
        Log.i(LOG_TAG + "setSource", "XML is " + stringXML);
        this.inStream = new ByteArrayInputStream(stringXML.getBytes());

        initXmlPullParser(encoding);
    }

    /**
     * 设置XML源和编码
     *
     * @param inStream XML输入流
     * @param encoding 字符编码
     *
     * @throws org.xmlpull.v1.XmlPullParserException
     */
    public void setSource(InputStream inStream, String encoding) throws XmlPullParserException {
        this.inStream = inStream;
        Log.i(LOG_TAG + "setSource", "inStream is " + inStream.toString());
        initXmlPullParser(encoding);
    }

    /**
     * 获取XMLreader对象的StringXML字符串
     *
     * @return 字符串格式
     */
    public String getStringXML() {
        return stringXML;
    }

    /**
     * 获取XMLreader对象的InputStream输入流
     *
     * @return 返回InputStream流对象
     */
    public InputStream getInStream() {
        return inStream;
    }

    /**
     * 获取XMLreader对象的XmlPullParser类型XML解析器
     *
     * @return 返回XmlPullParser对象
     */
    public XmlPullParser getXmlPullParser() {
        return xmlPullParser;
    }

    /**
     * 清空XMLreader对象
     */
    public void Clear() {
        Log.i(LOG_TAG + "Clear", "Clear() is invoked");
        this.stringXML = null;

        if (inStream != null) {
            try {
                this.inStream.close();
            } catch (IOException e) {
                Log.e(LOG_TAG + "Clear", "IOException is " + e.getMessage());
                return;
            }
        }
        this.inStream = null;
        this.xmlPullParser = null;
    }

    /**
     * 判断是否为XML文档根节点
     *
     * @return 返回布尔值
     */
    public boolean isStartDocument() {
        if (this.xmlPullParser == null) {
            Log.d(LOG_TAG + "isStartDocument", "xmlPullParser is null");
            return false;
        }
        try {
            return this.xmlPullParser.getEventType() == XmlPullParser.START_DOCUMENT;
        } catch (XmlPullParserException e) {
            Log.e(LOG_TAG + "isStartDocument", "XmlPullParserException is " + e.getMessage());
            return false;
        }
    }

    /**
     * 判断是否为标签开始节点
     *
     * @return 返回布尔值
     */
    public boolean isStartTag() {
        if (this.xmlPullParser == null) {
            Log.d(LOG_TAG + "isStartTag", "xmlPullParser is null");
            return false;
        }
        try {
            return this.xmlPullParser.getEventType() == XmlPullParser.START_TAG;
        } catch (XmlPullParserException e) {
            Log.e(LOG_TAG + "isStartTag", "XmlPullParserException is " + e.getMessage());
            return false;
        }
    }

    /**
     * 判断是否为标签结束节点
     *
     * @return 返回布尔值
     */
    public boolean isEndTag() {
        if (this.xmlPullParser == null) {
            Log.d(LOG_TAG + "isEndTag", "xmlPullParser is null");
            return false;
        }
        try {
            return this.xmlPullParser.getEventType() == XmlPullParser.END_TAG;
        } catch (XmlPullParserException e) {
            Log.e(LOG_TAG + "isEndTag", "XmlPullParserException is " + e.getMessage());
            return false;
        }
    }

    /**
     * 判断是否为XML文档结束节点
     *
     * @return 返回布尔值
     */
    public boolean isEndDocument() {
        if (this.xmlPullParser == null) {
            Log.d(LOG_TAG + "isEndDocument", "xmlPullParser is null");
            return false;
        }
        try {
            return this.xmlPullParser.getEventType() == XmlPullParser.END_DOCUMENT;
        } catch (XmlPullParserException e) {
            Log.e(LOG_TAG + "isEndDocument", "XmlPullParserException is " + e.getMessage());
            return false;
        }
    }

    /**
     * 获取当前标签名
     *
     * @return 返回标签名字符串，不在标签位置时返回null
     */
    public String getNodeName() {
        if (this.xmlPullParser == null) {
            Log.d(LOG_TAG + "getNodeName", "xmlPullParser is null");
            return null;
        }

        return xmlPullParser.getName();
    }

    /**
     * 获取指定名称空间和属性名的属性值
     *
     * @param namespace 名称空间，为null表示不检查
     * @param name      要获取的属性名
     *
     * @return 返回属性的字符串格式的值
     */
    public String getAttribute(String namespace, String name) {
        if (this.xmlPullParser == null) {
            Log.d(LOG_TAG + "getAttribute", "xmlPullParser is null");
            return null;
        }

        return xmlPullParser.getAttributeValue(namespace, name);
    }

    /**
     * 下一个标签，
     * 下一个位置必须是标签，
     * 如果是text或超出文件末尾则返回false
     *
     * @return 返回执行结果
     */
    public boolean next() {
        if (this.xmlPullParser == null) {
            Log.d(LOG_TAG + "next", "xmlPullParser is null");
            return false;
        }

        try {
            return xmlPullParser.next() != XmlPullParser.END_DOCUMENT;
        } catch (XmlPullParserException e) {
            Log.e(LOG_TAG + "next", "XmlPullParserException is " + e.getMessage());
            return false;
        } catch (IOException e) {
            Log.e(LOG_TAG + "next", "IOException is " + e.getMessage());
            return false;
        }
    }

    /**
     * 获取当前标签下的text值并且去到下一个标签，
     * 当前位置必须是开始标签，
     * 下一个标签一般是结束标签
     *
     * @return 返回字符串值
     */
    public String nextText() {
        if (this.xmlPullParser == null) {
            Log.d(LOG_TAG + "nextText", "xmlPullParser is null");
            return null;
        }

        try {
            return xmlPullParser.nextText();
        } catch (XmlPullParserException e) {
            Log.e(LOG_TAG + "nextText", "XmlPullParserException is " + e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e(LOG_TAG + "nextText", "IOException is " + e.getMessage());
            return null;
        }
    }

    /**
     * 获取当前标签下的text值
     *
     * @return 返回字符串值
     */
    public String getText() {
        if (this.xmlPullParser == null) {
            Log.d(LOG_TAG + "getText", "xmlPullParser is null");
            return null;
        }

        return xmlPullParser.getText();
    }
}
