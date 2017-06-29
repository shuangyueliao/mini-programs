package com.springmvc.util;

import java.io.UnsupportedEncodingException;

import java.security.MessageDigest;  
import java.security.NoSuchAlgorithmException;  
import java.util.Arrays;  
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

  
/**
 * 请求校验工具�?
 * @author yanghb
 * @email hai_bo_yang@126.com
 * @data 2016�?5�?23�?17:51:55
 */
public class SignUtil {  
    // 与接口配置信息中的Token要一�?  
    private static String token = "wechatDemo";  
  
    /** 
     * 验证签名 
     *  
     * @param signature 
     * @param timestamp 
     * @param nonce 
     * @return 
     */  
    public static boolean checkSignature(String signature, String timestamp, String nonce) {  
        String[] arr = new String[] { token, timestamp, nonce };  
        //1.将token、timestamp、nonce三个参数进行字典序排�?  
        Arrays.sort(arr);  
        StringBuilder content = new StringBuilder();  
        for (int i = 0; i < arr.length; i++) {  
            content.append(arr[i]);  
        }  
        MessageDigest md = null;  
        String tmpStr = null;  
  
        try {  
            md = MessageDigest.getInstance("SHA-1");  
            //2.将三个参数字符串拼接成一个字符串进行sha1加密  
            byte[] digest = md.digest(content.toString().getBytes());  
            tmpStr = byteArrToHexStr(digest);  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        }  
  
        content = null;  
        //3.将sha1加密后的字符串可与signature对比，标识该请求来源于微�?  
        return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;  
    }  
    
    /** 
     * 将字节数组转换为十六进制字符�? 
     *  
     * @param byteArray 
     * @return 
     */  
    private static String byteArrToHexStr(byte[] byteArray) {  
        String strDigest = "";  
        for (int i = 0; i < byteArray.length; i++) {  
            //strDigest += byteToHexStr(byteArray[i]); 
        	int v = byteArray[i] & 0Xff;
            String s = Integer.toHexString(v).toUpperCase();
            strDigest += s;
        }  
        return strDigest;  
    }  
  
    /** 
     * 将字节转换为十六进制字符�? 
     *  
     * @param mByte 
     * @return 
     */  
    @SuppressWarnings("unused")
	@Deprecated
    private static String byteToHexStr(byte mByte) {  
        char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };  
        char[] tempArr = new char[2];  
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];  //1011 0111 >>>4  0000 1011 & 1111 = 1011
        tempArr[1] = Digit[mByte & 0X0F];  
  
        String s = new String(tempArr); 
        return s;  
    }  
    
    
    /**
     * 获取jsapi签名
     * @return
     */
    public static Map<String, String> jsapiSign(String jsapi_ticket, String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有�?
        string1 = "jsapi_ticket=" + jsapi_ticket +
                  "&noncestr=" + nonce_str +
                  "&timestamp=" + timestamp +
                  "&url=" + url;
        System.out.println(string1);

        try
        {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);

        return ret;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash)
        {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    /**
     * get random string 
     * @return
     */
    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    /**
     * get timestamp 
     * @return
     */
    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
}  