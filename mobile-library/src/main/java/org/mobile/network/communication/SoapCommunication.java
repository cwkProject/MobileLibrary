package org.mobile.network.communication;

import android.util.Log;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Map;

/**
 * 基于SOAP协议的通讯组件
 *
 * @author 超悟空
 * @version 1.0 2015/1/6
 * @since 1.0
 */
public class SoapCommunication implements ICommunication<Map<String, String>, SoapObject> {

    /**
     * 日志标签前缀
     */
    private static final String LOG_TAG = "SoapCommunication.";

    /**
     * 服务器结果对象
     */
    private Object response = null;

    /**
     * WebService地址
     */
    private String url = null;

    /**
     * 超时时间，0为默认
     */
    private int timeout = 0;

    /**
     * WebService命名空间，默认为"http://tempuri.org/"
     */
    private String nameSpace = "http://tempuri.org/";

    /**
     * WebService服务器版本，默认为版本11
     */
    private WebServiceVersion version = WebServiceVersion.VERSION11;

    /**
     * 指示服务器是否为.NET框架，默认为true
     */
    private boolean isDoNet = true;

    /**
     * 调用方法名
     */
    private String methodName = null;

    /**
     * 指示是否开启DEBUG模式
     */
    private boolean debug = false;

    /**
     * 预填充前导请求参数
     */
    private Map<String, String> preParameters = null;

    /**
     * 预填充后继请求参数
     */
    private Map<String, String> subParameters = null;

    /**
     * 设置WebService地址
     *
     * @param url WebService地址串
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 设置WebService地址和超时时间
     *
     * @param url     WebService地址串
     * @param timeout 超时时间（毫秒）
     */
    public void setUrl(String url, int timeout) {
        this.url = url;
        this.timeout = timeout;
    }

    /**
     * 设置DEBUG模式
     *
     * @param debug DEBUG状态，默认为fasle
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /**
     * 设置服务器版本号，默认为版本11
     *
     * @param version 版本号枚举
     */
    public void setVersion(WebServiceVersion version) {
        this.version = version;
    }

    /**
     * 设置服务器是否为.NET类型
     *
     * @param flag 默认为true
     */
    public void setDoNet(boolean flag) {
        this.isDoNet = flag;
    }

    /**
     * 设置命名空间，默认为"http://tempuri.org/"
     *
     * @param nameSpace 命名空间字符串
     */
    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    /**
     * 设置预填充前导请求参数，
     * 每次调用{@link #Request}都会在传入的参数前附加本方法设置的参数
     *
     * @param parameters 参数列表，
     *                   key为webservice服务的参数名，
     *                   value为webservice服务的参数值
     */
    public void setPreParameters(Map<String, String> parameters) {
        this.preParameters = parameters;
    }

    /**
     * 设置预填充后继请求参数，
     * 每次调用{@link #Request}都会在传入的参数后附加本方法设置的参数
     *
     * @param parameters 参数列表，
     *                   key为webservice服务的参数名，
     *                   value为webservice服务的参数值
     */
    public void setSubParameters(Map<String, String> parameters) {
        this.subParameters = parameters;
    }

    /**
     * 设置调用的方法名
     *
     * @param methodName 方法名字符串
     */
    @Override
    public void setTaskName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public void Request(Map<String, String> sendData) {
        Log.i(LOG_TAG + "Request", "Request(Map<String, String>) start");
        Log.i(LOG_TAG + "Request", "namespace is " + this.nameSpace);
        Log.i(LOG_TAG + "Request", "method name is " + this.methodName);

        //新建要发送的数据对象，指定WebService的命名空间和调用方法
        SoapObject request = new SoapObject(this.nameSpace, this.methodName);

        // 遍历preParameters集合并加入请求参数对象
        if (this.preParameters != null) {
            Log.i(LOG_TAG + "Request", "pre parameters count is " + preParameters.size());
            for (Map.Entry<String, String> dataEntry : this.preParameters.entrySet()) {
                request.addProperty(dataEntry.getKey(), dataEntry.getValue());
            }
        }

        // 遍历sendData集合并加入请求参数对象
        if (sendData != null) {
            Log.i(LOG_TAG + "Request", "sendData count is " + sendData.size());
            for (Map.Entry<String, String> dataEntry : sendData.entrySet()) {
                request.addProperty(dataEntry.getKey(), dataEntry.getValue());
            }
        }

        // 遍历subParameters集合并加入请求参数对象
        if (this.subParameters != null) {
            Log.i(LOG_TAG + "Request", "sub parameters count is " + this.subParameters.size());
            for (Map.Entry<String, String> dataEntry : this.subParameters.entrySet()) {
                request.addProperty(dataEntry.getKey(), dataEntry.getValue());
            }
        }

        Log.i(LOG_TAG + "Request", "webservice version is " + this.version.getValue());

        // 生成调用WebService方法调用的soap信息，并且指定Soap版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(this.version.getValue());

        Log.i(LOG_TAG + "Request", "dotNet " + (this.isDoNet ? "on" : "off"));
        // 是否调用DotNet开发的WebService
        envelope.dotNet = this.isDoNet;
        // 设置发送数据
        envelope.bodyOut = request;
        // HttpTransportSE请求对象
        HttpTransportSE androidHttpTransport = null;

        Log.i(LOG_TAG + "Request", "url is " + this.url);
        Log.i(LOG_TAG + "Request", "timeout is " + this.timeout);

        if (this.timeout > 0) {
            // 有超时时间
            androidHttpTransport = new HttpTransportSE(this.url, this.timeout);
        } else {
            // 无超时时间
            androidHttpTransport = new HttpTransportSE(this.url);
        }

        Log.i(LOG_TAG + "Request", "HttpTransportSE debug " + this.debug);
        // 设置DEBUG模式
        androidHttpTransport.debug = this.debug;

        try {
            Log.i(LOG_TAG + "Request", "HttpTransportSE call start");
            // 执行请求
            androidHttpTransport.call(this.nameSpace + this.methodName, envelope);

            // 获取服务器返回结果
            this.response = envelope.bodyIn;
        } catch (HttpResponseException e) {
            Log.e(LOG_TAG + "Request", "HttpResponseException is " + e.getMessage());
            if (this.debug) {
                Log.e(LOG_TAG + "Request>>requestDump", androidHttpTransport.requestDump);
                Log.e(LOG_TAG + "Request>>responseDump", androidHttpTransport.responseDump);
            }
        } catch (SoapFault e) {
            Log.e(LOG_TAG + "Request", "SoapFault is " + e.getMessage());
            if (this.debug) {
                Log.e(LOG_TAG + "Request>>requestDump", androidHttpTransport.requestDump);
                Log.e(LOG_TAG + "Request>>responseDump", androidHttpTransport.responseDump);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG + "Request", "IOException is " + e.getMessage());
            if (this.debug) {
                Log.e(LOG_TAG + "Request>>requestDump", androidHttpTransport.requestDump);
                Log.e(LOG_TAG + "Request>>responseDump", androidHttpTransport.responseDump);
            }
        } catch (XmlPullParserException e) {
            Log.e(LOG_TAG + "Request", "XmlPullParserException is " + e.getMessage());
            if (this.debug) {
                Log.e(LOG_TAG + "Request>>requestDump", androidHttpTransport.requestDump);
                Log.e(LOG_TAG + "Request>>responseDump", androidHttpTransport.responseDump);
            }
        }
    }

    @Override
    public SoapObject Response() {
        if (this.response == null) {
            Log.d(LOG_TAG + "Response", "response is null");
            return null;
        } else {
            return (SoapObject) this.response;
        }
    }

    @Override
    public void Close() {
        // TODO Auto-generated method stub
    }

}
