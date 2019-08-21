package com.woniu.woniuticket.order.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AlipayConfig {
    //↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016101000655434";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCfp5n11Ls1BR9PX8IcdmmxWPHLIGemxzzk/ImzP2uD69Gv+ruDyLq3qJvf6ZfSlQflIVfic7leB4GPdRNFhG1PgwBxgZ7+xWQgHY9jvLYZlZ088hC+t47SoOg6V/EYgC6tHHkm2u+HvyJk7UcHe9EShKQ+FKQxpDUFqCcWVL4Z7VFCn1mJepwEPN47Y9v/7Jc+/fH/L98oty9VD4tcFnvpNy4OT6LXl7p5VVeRZpgbbB7R6mRiya+q1pPFEL4NUEyt+0ClMFpQFs2BZvXpYqWcIy4uhEdRMRa5xcblRnRXIL7MLAC5DJT2gW1ixDEDjtUbHyGNf6DX6iWQMgLoNX9/AgMBAAECggEBAJf7IVned5PCW4rg2lTj+DtGM26tW15tXa+SI7dJ0RyU+Vivs5+BDxXUYQYnRUrGaeyZ8CJ3DWfQd2pMBdtwc+p0B5EF1ogq+eSEn0JPm34BadxhhsU20bP6TvdzTeSH6WcLqtztH4Y20hQHuE0MdftN31flqGRFL+BBsWvVod3lAQqJUewm6ASkSyPIPQA7HCD4WEgXby7uz3uzR27w6RK0yhB70X9S46l9m9LegI88fY5pipblGbnWyUY+NgRnnZbfFxBtWIFrq85X84MDoL0cZHuxKI8x2E5HYcMX6+8W1amfG0GdijVG810r5s7HKRDaj8MIJsMVpHpf8pYfiDkCgYEA7lleLmbMQMI1gnL3pQsvhNvjuSJhnsVnZ5u2G2uJeeIFvBWlCAPK/2j8aOO49EBKP+vV6fkAa6x4fGImdZa1P26K3iUNrupGcPp7v0WhyYO+JeDZdfoV5TMZe0lT2IULR1DxqEiJahVpf8evHg5kXubN6rD+tkatQH43NX4QULUCgYEAq3pXfkoQ/3Q4ccwkmT06psENZ/bAtHr407daGwbVxyQ5s8TAHXBHJeg5Gc/FsleVrq4MIsuUhIhOVGRM1XRJ60HXP+HIZhAtzsMViWS5ab4GRye1P2JheYpiQMplRTjXthbmd82WQu0SGUHo6tCz+NhIfwFWEsWL7a7ouymOk+MCgYBOs6OtnrQDDh2tHVk4XiEqtZDUKM4jJvul8nyHQVL7s+5A1MTRiK8W9wuhdtAzlqbtxGm2AEprQ1/lcmNQoJrofw/LmmCSSTlJROxnlA6AA8hdqYkks5dXPzBCzUuqSwQWYFvrPUhihv92+fCQ9B+gVoXX4Om6Nkwu535DY4TvoQKBgBNqGpXV1O7XPbzEnDAtWoKewOHQcij880clLzJ4J6QQWegckpPCrouAK1bcy0lR2USZuA4Idpw6woOTh49zRxs3rSMycPn9QvJx6/JOUR/Dv/o2aLLwdJDKNi2XvLeFpAaduSI/SI/zP2VUrQZI8tdtxWd7wz8E1r7mWrs2DUp5AoGAK+eK/TZGfa2wccIuu+cAQK3xEvU1gpTnSHB91RYcKTAtMf6aXs6o9ut53ivXeDY+tsBK1FRyB95Y/03+nRy8hjVSX6zG1hBsVz0fHcS5c/w1sUb4DaLXCDJTDIBph2aYBSz/xzq2eAQC15tk7pMFDA2H6JiuN0SojurThxvyo6E=";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApvzlitp5J6svMw33OhUdnA/YjTtcGcc3CdfwDGMF2lRFwF34s4odlu93H9gv68NLSj/Aoi2/ZEWt8CgLSqCgYAC/O+Jbfe+cd5RrJixVP9zXWtEbj2rAciuoFQQLjljDM7qc+p9bCaTXrzijokNe4AZhKbMa0/dG3BMhWM8mzm2o2tcDS2NA/r9LiLisEPGjxx1YAc4Gkbkfmu33E1trgRVossQsEX50uwY9bwK/H4PAds2Vrc4HmRccHvHT9fIuw6PJwWAvtljEp4kBIUHf0du94QNznO415Kprr1hou+ml+UCuoIu0JBz0/lebkQbcHVhDzf+zo+iTNRLkDwEMIwIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    @Value("${notifyUrl}")
    public static String notify_url;

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    @Value("${returnUrl}")
    public static String return_url;

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "F:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
}
