package com.springmvc.util;

import net.sf.json.JSONObject;

public class WeixinUtil {
	public static String appid="wx61e6d99d24b1a8a3";
	public static String appsecret="76c15d0fd330d80f9fd815b77854a71d";
	public static String access_token=null;
	public static JSONObject getAccess_Token(){
		String tmpaccesstokenurl="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
		String accesstokenurl=tmpaccesstokenurl.replace("APPID", "wx61e6d99d24b1a8a3").replace("APPSECRET", "76c15d0fd330d80f9fd815b77854a71d");
		JSONObject jsonObject=HttpUtil.httpRequest(accesstokenurl, "GET", null);
		access_token=jsonObject.getString("access_token");
		return jsonObject;
	}
}
