package com.service;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.entity.WebParameter;

import android.util.Log;

/**
 * Webservice解析方法 参数： 1.webservice命名空间 2.调用的方法名 3.webservice路径 4.参数数组
 *
 * @author Administrator
 */
public class WebService {

    private static final String TAG = "WebService";

    /**
     * URL2=url+"/cash_box";
     *
     * @param methodName
     * @param parameter
     * @return
     * @throws Exception
     */
    public static SoapObject getSoapObject2(String methodName, WebParameter[] parameter) throws Exception {
        // 创建Soap对象，并指定命名空间和方法名

        SoapObject request = new SoapObject(FixationValue.NAMESPACE, methodName);
        // 遍历添加参数 WebParameter为泛型类 属性1：参数名称 属性2：参数值
        if (parameter != null) {
            for (WebParameter webParameter : parameter) {
                request.addProperty(webParameter.getName(), webParameter.getValue());
            }
        }
        // 指定版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        // 设置Bodyout属性
        envelope.bodyOut = request;
        (new MarshalBase64()).register(envelope);

        envelope.encodingStyle = "UTF-8";

        // 创建HTTPtransports对象，并指定WSDL文档的URL
        HttpTransportSE ht = new HttpTransportSE(FixationValue.URL2, 20000);

        // 调用webservice
        String patch = FixationValue.URL2 + "/" + methodName;
        Log.e(TAG, "----------访问的地址：" + patch);
        ht.call(patch, envelope);
        // 返回结果集
        SoapObject soapObject = (SoapObject) envelope.getResponse();

        return soapObject;
    }

    /**
     * url+"/cash_pda";
     *
     * @param methodName
     * @param parameter
     * @return
     * @throws Exception
     */
    public static SoapObject getSoapObject(String methodName, WebParameter[] parameter) throws Exception {
        // 创建Soap对象，并指定命名空间和方法名

        SoapObject request = new SoapObject(FixationValue.NAMESPACE, methodName);
        // 遍历添加参数 WebParameter为泛型类 属性1：参数名称 属性2：参数值
        if (parameter != null) {
            for (WebParameter webParameter : parameter) {
                PropertyInfo property = new PropertyInfo();
                property.setName(webParameter.getName());
                property.setType(PropertyInfo.STRING_CLASS);
                property.setValue(webParameter.getValue());
                request.addProperty(property);

                //property.getValue()会报空
                //Log.e(TAG,"----------访问的参数：" + property.getValue().toString());
            }
        }

        // 指定版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        // 设置Bodyout属性
        envelope.bodyOut = request;
        (new MarshalBase64()).register(envelope);

        envelope.encodingStyle = "UTF-8";

        // 创建HTTPtransports对象，并指定WSDL文档的URL
        HttpTransportSE ht = new HttpTransportSE(FixationValue.URL, 30000);

        // 调用webservice
        String patch = FixationValue.URL + "/" + methodName;
        Log.e(TAG, ("----------访问的地址：" + patch));
        ht.call(patch, envelope);
        // 返回结果集
        SoapObject soapObject = (SoapObject) envelope.getResponse();

        return soapObject;
    }

//账户中心的url

    public static SoapObject getSoapObjectZHAccountCenter(String methodName, WebParameter[] parameter)
            throws Exception {
        // 创建Soap对象，并指定命名空间和方法名

        SoapObject request = new SoapObject(FixationValue.NAMESPACEZH, methodName);
        // 遍历添加参数 WebParameter为泛型类 属性1：参数名称 属性2：参数值
        if (parameter != null) {
            for (WebParameter webParameter : parameter) {
                request.addProperty(webParameter.getName(), webParameter.getValue());
            }
        }
        // 指定版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = false;// 后加入
        // 设置Bodyout属性
        envelope.bodyOut = request;
        (new MarshalBase64()).register(envelope);
        envelope.encodingStyle = "UTF-8";

        // 创建HTTPtransports对象，并指定WSDL文档的URL
        HttpTransportSE ht = new HttpTransportSE(FixationValue.URL7, 20000);

        // 调用webservice
        String patch = FixationValue.URL7 + "/" + methodName;
        Log.e(TAG, ("----------访问的地址：" + patch));
        ht.call(patch, envelope);
        // 返回结果集

        SoapObject soapObject = (SoapObject) envelope.getResponse();
        Log.e(TAG, ("----------soapObject：" + soapObject));
        return soapObject;
    }

    /***
     * 新接口 查库车盘库 URL8 查库车：查/盘库补录任务列
     *
     */
    public static SoapObject getSoapObjectZHTaskAndErrorCount(String methodName, WebParameter[] parameter)
            throws Exception {
        // 创建Soap对象，并指定命名空间和方法名
// 地址空间发生了改变
        SoapObject request = new SoapObject(FixationValue.NAMESPACEZH, methodName);
        // 遍历添加参数 WebParameter为泛型类 属性1：参数名称 属性2：参数值
        if (parameter != null) {
            for (WebParameter webParameter : parameter) {
                request.addProperty(webParameter.getName(), webParameter.getValue());
            }
        }
        // 指定版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = false;// 后加入

        // 设置Bodyout属性
        envelope.bodyOut = request;
        (new MarshalBase64()).register(envelope);
        envelope.encodingStyle = "UTF-8";

        // 创建HTTPtransports对象，并指定WSDL文档的URL
        HttpTransportSE ht = new HttpTransportSE(FixationValue.URL8, 20000);

        // 调用webservice
        String patch = FixationValue.URL8 + "/" + methodName;
        Log.e(TAG, ("----------访问的地址：" + patch));
        ht.call(patch, envelope);
        // 返回结果集z

        SoapObject soapObject = (SoapObject) envelope.getResponse();
        Log.e(TAG, ("----------soapObject：" + soapObject));
        return soapObject;
    }

    public static SoapObject getSoapObjectZHTaskAndErrorCountAA(String methodName, WebParameter[] parameter)
            throws Exception {
        // 创建Soap对象，并指定命名空间和方法名
// 地址空间发生了改变
        SoapObject request = new SoapObject(FixationValue.NAMESPACEZH, methodName);
        // 遍历添加参数 WebParameter为泛型类 属性1：参数名称 属性2：参数值
        if (parameter != null) {
            for (WebParameter webParameter : parameter) {
                request.addProperty(webParameter.getName(), webParameter.getValue());
            }
        }
        // 指定版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = false;// 后加入

        // 设置Bodyout属性
        envelope.bodyOut = request;
        (new MarshalBase64()).register(envelope);
        envelope.encodingStyle = "UTF-8";

        // 创建HTTPtransports对象，并指定WSDL文档的URL
        HttpTransportSE ht = new HttpTransportSE(FixationValue.url15, 50000);

        // 调用webservice
        String patch = FixationValue.url15 + "/" + methodName;
        Log.e(TAG, ("----------访问的地址：" + patch));
        ht.call(patch, envelope);
        // 返回结果集z

        SoapObject soapObject = (SoapObject) envelope.getResponse();
        Log.e(TAG, ("----------soapObject：" + soapObject));
        return soapObject;
    }

    public static SoapObject getSoapObjectZHTaskAndErrorCountdizhi(String methodName, WebParameter[] parameter)
            throws Exception {
        // 创建Soap对象，并指定命名空间和方法名
// 地址空间发生了改变
        SoapObject request = new SoapObject(FixationValue.NAMESPACEZH, methodName);
        // 遍历添加参数 WebParameter为泛型类 属性1：参数名称 属性2：参数值
        if (parameter != null) {
            for (WebParameter webParameter : parameter) {
                request.addProperty(webParameter.getName(), webParameter.getValue());
            }
        }
        // 指定版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = false;// 后加入

        // 设置Bodyout属性
        envelope.bodyOut = request;
        (new MarshalBase64()).register(envelope);
        envelope.encodingStyle = "UTF-8";

        // 创建HTTPtransports对象，并指定WSDL文档的URL
        HttpTransportSE ht = new HttpTransportSE(FixationValue.URL8, 20000);

        // 调用webservice
        String patch = FixationValue.URL8 + "/" + methodName;
        Log.e(TAG, ("----------访问的地址：" + patch));
        ht.call(patch, envelope);
        // 返回结果集z

        SoapObject soapObject = (SoapObject) envelope.getResponse();
        Log.e(TAG, "----------soapObject：" + soapObject);
        return soapObject;
    }

    /***
     * 赵辉
     */
    public static SoapObject getSoapObjectZH(String methodName, WebParameter[] parameter) throws Exception {
        // 创建Soap对象，并指定命名空间和方法名
        SoapObject request = new SoapObject(FixationValue.NAMESPACEZH, methodName);
        // 遍历添加参数 WebParameter为泛型类 属性1：参数名称 属性2：参数值
        if (parameter != null) {
            for (WebParameter webParameter : parameter) {
                request.addProperty(webParameter.getName(), webParameter.getValue());
            }
        }
        // 指定版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        // 设置Bodyout属性
        envelope.bodyOut = request;
        (new MarshalBase64()).register(envelope);
        envelope.encodingStyle = "UTF-8";

        // 创建HTTPtransports对象，并指定WSDL文档的URL
        HttpTransportSE ht = new HttpTransportSE(FixationValue.URL6, 80000);

        // 调用webservice
        String patch = FixationValue.URL6 + "/" + methodName;
        Log.e(TAG, "----------访问的地址：" + patch);
        ht.call(patch, envelope);
        // 返回结果集
        SoapObject soapObject = (SoapObject) envelope.getResponse();
        Log.e(TAG, "----------soapObject：" + soapObject);
        return soapObject;
    }

    /**
     * 8 lianc 押运员指纹验证后去查看自己是否有上缴和请领任务
     *
     * @param methodName
     * @param parameter
     * @return
     * @throws Exception
     */

    public static SoapObject getSoapObjectZHyayun(String methodName, WebParameter[] parameter) throws Exception {
//		// 创建Soap对象，并指定命名空间和方法名

        SoapObject request = new SoapObject(FixationValue.NAMESPACEZH, methodName);
        // 遍历添加参数 WebParameter为泛型类 属性1：参数名称 属性2：参数值
        if (parameter != null) {
            for (WebParameter webParameter : parameter) {
                request.addProperty(webParameter.getName(), webParameter.getValue());
            }
        }
        // 指定版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        // 设置Bodyout属性
        envelope.bodyOut = request;
        (new MarshalBase64()).register(envelope);
        envelope.encodingStyle = "UTF-8";

        // 创建HTTPtransports对象，并指定WSDL文档的URL
        HttpTransportSE ht = new HttpTransportSE(FixationValue.url11, 20000);

        // 调用webservice
        String patch = FixationValue.url11 + "/" + methodName;
        Log.e(TAG, "----------访问的地址：" + patch);
        ht.call(patch, envelope);
        // 返回结果集
        SoapObject soapObject = (SoapObject) envelope.getResponse();
        Log.e(TAG, "----------soapObject：" + soapObject);
        return soapObject;
    }

    /***
     * 钞袋模块下的数据提交
     *
     * @param methodName
     * @param parameter
     * @return
     * @throws Exception
     */
    public static SoapObject getSoapObjectCD(String methodName, WebParameter[] parameter) throws Exception {
        // 创建Soap对象，并指定命名空间和方法名
        SoapObject request = new SoapObject(FixationValue.NAMESPACEZH, methodName);
        // 遍历添加参数 WebParameter为泛型类 属性1：参数名称 属性2：参数值
        if (parameter != null) {
            for (WebParameter webParameter : parameter) {
                request.addProperty(webParameter.getName(), webParameter.getValue());
            }
        }
        // 指定版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        // 设置Bodyout属性
        envelope.bodyOut = request;
        (new MarshalBase64()).register(envelope);
        envelope.encodingStyle = "UTF-8";

        // 创建HTTPtransports对象，并指定WSDL文档的URL
        HttpTransportSE ht = new HttpTransportSE(FixationValue.url15, 20000);

        // 调用webservice
        String patch = FixationValue.url15 + "/" + methodName;
        Log.e(TAG, "----------访问的地址：" + patch);
        ht.call(patch, envelope);
        // 返回结果集
        SoapObject soapObject = (SoapObject) envelope.getResponse();
        Log.e(TAG, "----------soapObject：" + soapObject);
        return soapObject;
    }

    /***
     * 这里有问题不懂 三期新增lc 201910.28
     *
     * @param methodName
     * @param parameter
     * @return
     * @throws Exception
     */

    public static SoapObject getSoapObjectMakeCardby(String methodName, com.example.app.entity.WebParameter[] parameter)
            throws Exception {
        // 创建Soap对象，并指定命名空间和方法名
        SoapObject request = new SoapObject(FixationValue.NAMESPACEZH, methodName);
        // 遍历添加参数 WebParameter为泛型类 属性1：参数名称 属性2：参数值
        if (parameter != null) {
            for (com.example.app.entity.WebParameter webParameter : parameter) {
                request.addProperty(webParameter.getName(), webParameter.getValue());
            }
        }
        // 指定版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        // 设置Bodyout属性
        envelope.bodyOut = request;
        (new MarshalBase64()).register(envelope);
        envelope.encodingStyle = "UTF-8";

        // 创建HTTPtransports对象，并指定WSDL文档的URL
        HttpTransportSE ht = new HttpTransportSE(FixationValue.url16, 20000);

        // 调用webservice
        String patch = FixationValue.url16 + "/" + methodName;
        Log.e(TAG, "----------访问的地址：" + patch);
        ht.call(patch, envelope);
        // 返回结果集
        SoapObject soapObject = (SoapObject) envelope.getResponse();
        Log.e(TAG, "----------soapObject：" + soapObject);
        return soapObject;
    }

    /**
     * 非尾零状态下的装袋网络请求 方法
     *
     * @param methodName
     * @param parameter
     * @return
     * @throws Exception
     */
    public static SoapObject getSoapObjectZHTaskAndErrorCountbag(String methodName, WebParameter[] parameter)
            throws Exception {
        // 创建Soap对象，并指定命名空间和方法名
        // 地址空间发生了改变
        SoapObject request = new SoapObject(FixationValue.NAMESPACEZH, methodName);
        // 遍历添加参数 WebParameter为泛型类 属性1：参数名称 属性2：参数值
        if (parameter != null) {
            for (WebParameter webParameter : parameter) {
                request.addProperty(webParameter.getName(), webParameter.getValue());
            }
        }
        // 指定版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = false;// 后加入

        // 设置Bodyout属性
        envelope.bodyOut = request;
        (new MarshalBase64()).register(envelope);
        envelope.encodingStyle = "UTF-8";

        // 创建HTTPtransports对象，并指定WSDL文档的URL
        HttpTransportSE ht = new HttpTransportSE(FixationValue.url15, 20000);

        // 调用webservice
        String patch = FixationValue.url15 + "/" + methodName;
        Log.e(TAG, "----------访问的地址：" + patch);
        ht.call(patch, envelope);
        // 返回结果集z

        SoapObject soapObject = (SoapObject) envelope.getResponse();
        Log.e(TAG, "----------soapObject：" + soapObject);
        return soapObject;
    }

    /*********
     * 账户资料和后督账包操作的网络请求工具类 url17 20200420
     */
    /**
     * 非尾零状态下的装袋网络请求 方法
     *
     * @param methodName
     * @param parameter
     * @return
     * @throws Exception
     */
    public static SoapObject getSoapObjectAccountAndPostman(String methodName, WebParameter[] parameter)
            throws Exception {
        // 创建Soap对象，并指定命名空间和方法名
        // 地址空间发生了改变
        SoapObject request = new SoapObject(FixationValue.NAMESPACEZH, methodName);
        // 遍历添加参数 WebParameter为泛型类 属性1：参数名称 属性2：参数值
        if (parameter != null) {
            for (WebParameter webParameter : parameter) {
                request.addProperty(webParameter.getName(), webParameter.getValue());
            }
        }
        // 指定版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = false;// 后加入

        // 设置Bodyout属性
        envelope.bodyOut = request;
        (new MarshalBase64()).register(envelope);
        envelope.encodingStyle = "UTF-8";

        // 创建HTTPtransports对象，并指定WSDL文档的URL
        HttpTransportSE ht = new HttpTransportSE(FixationValue.URL17, 20000);

        // 调用webservice
        String patch = FixationValue.URL17 + "/" + methodName;
        Log.e(TAG, "----------访问的地址：" + patch);
        ht.call(patch, envelope);
        // 返回结果集z

        SoapObject soapObject = (SoapObject) envelope.getResponse();
        Log.e(TAG, "----------soapObject：" + soapObject);
        return soapObject;
    }

//	public static SoapObject getSoapObjectZHyayun01(String methodName,
//			WebParameter[] parameter) throws Exception {
//		// 创建Soap对象，并指定命名空间和方法名
//		SoapObject request = new SoapObject(FixationValue.NAMESPACEZH,
//				methodName);
//		// 遍历添加参数 WebParameter为泛型类 属性1：参数名称 属性2：参数值
//		if (parameter != null) {
//			for (WebParameter webParameter : parameter) {
//				request.addProperty(webParameter.getName(),
//						webParameter.getValue());
//			}
//		}
//		// 指定版本
//		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
//				SoapEnvelope.VER11);
//
//		// 设置Bodyout属性
//		envelope.bodyOut = request;
//		(new MarshalBase64()).register(envelope);
//		envelope.encodingStyle = "UTF-8";
//
//		// 创建HTTPtransports对象，并指定WSDL文档的URL
//		HttpTransportSE ht = new HttpTransportSE(FixationValue.url11, 20000);
//
//		// 调用webservice
//		String patch = FixationValue.url11 + "/" + methodName;
//		System.out.println("----------访问的地址：" + patch);
//		ht.call(patch, envelope);
//		// 返回结果集
//		SoapObject soapObject = (SoapObject) envelope.getResponse();
//		System.out.println("----------soapObject：" + soapObject);
//		return soapObject;
//	}


    /***
     * 20210214  清分管理员复查合计的URL18 公共类
     * @param methodName
     * @param parameter
     * @return
     * @throws Exception
     */
    public static SoapObject getSoapObjectCleanMangerChece(String methodName, WebParameter[] parameter)
            throws Exception {
        // 创建Soap对象，并指定命名空间和方法名
        // 地址空间发生了改变
        SoapObject request = new SoapObject(FixationValue.NAMESPACEZH, methodName);
        // 遍历添加参数 WebParameter为泛型类 属性1：参数名称 属性2：参数值
        if (parameter != null) {
            for (WebParameter webParameter : parameter) {
                request.addProperty(webParameter.getName(), webParameter.getValue());
            }
        }
        // 指定版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = false;// 后加入

        // 设置Bodyout属性
        envelope.bodyOut = request;
        (new MarshalBase64()).register(envelope);
        envelope.encodingStyle = "UTF-8";

        // 创建HTTPtransports对象，并指定WSDL文档的URL
        HttpTransportSE ht = new HttpTransportSE(FixationValue.url18, 20000);

        // 调用webservice
        String patch = FixationValue.url18 + "/" + methodName;
        Log.e(TAG, "----------访问的地址：" + patch);
        ht.call(patch, envelope);
        // 返回结果集z

        SoapObject soapObject = (SoapObject) envelope.getResponse();
        Log.e(TAG, "----------soapObject：" + soapObject);
        return soapObject;
    }





    /***
     * 20210916 清分管理员复查合计的URL19 公共类
     * @param methodName
     * @param parameter
     * @return
     * @throws Exception
     *
     * 角色29
     */
    public static SoapObject getSoapObjectDZLiabraryManger(String methodName, WebParameter[] parameter)
            throws Exception {
        // 创建Soap对象，并指定命名空间和方法名
        // 地址空间发生了改变
        SoapObject request = new SoapObject(FixationValue.NAMESPACEZH, methodName);
        // 遍历添加参数 WebParameter为泛型类 属性1：参数名称 属性2：参数值
        if (parameter != null) {
            for (WebParameter webParameter : parameter) {
                request.addProperty(webParameter.getName(), webParameter.getValue());
            }
        }
        // 指定版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = false;// 后加入

        // 设置Bodyout属性
        envelope.bodyOut = request;
        (new MarshalBase64()).register(envelope);
        envelope.encodingStyle = "UTF-8";

        // 创建HTTPtransports对象，并指定WSDL文档的URL
        HttpTransportSE ht = new HttpTransportSE(FixationValue.url19, 20000);

        // 调用webservice
        String patch = FixationValue.url19 + "/" + methodName;
        Log.e(TAG, "----------访问的地址：" + patch);
        ht.call(patch, envelope);
        // 返回结果集z

        SoapObject soapObject = (SoapObject) envelope.getResponse();
        Log.e(TAG, "----------soapObject：" + soapObject);
        return soapObject;
    }

}

//  http://192.168.1.232:9080/webcash_up/webservice
