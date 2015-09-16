package org.mobile.library.parser;
/**
 * Created by 超悟空 on 2015/1/6.
 */

/**
 * 服务器返回数据解析接口
 *
 * @param <Result> 解析结果的数据类型
 * @param <Source> 数据源的数据类型
 *
 * @author 超悟空
 * @version 1.0 2015/1/6
 * @since 1.0
 */
public interface IResponseDataParser<Result, Source> {
    /**
     * 解析服务器响应的数据
     *
     * @param data 要解析的数据对象
     *
     * @return 解析后数据
     */
    Result DataParser(Source data);
}
