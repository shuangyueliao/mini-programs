package com.springmvc.controller;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.springmvc.aes.AesException;
import com.springmvc.aes.WXBizMsgCrypt;
import com.springmvc.util.HttpUtil;
import com.springmvc.util.MessageUtil;
import com.springmvc.util.SignUtil;
import com.springmvc.util.TextMessage;
import com.springmvc.util.WXAppletUserInfo;
import com.springmvc.util.WeixinUtil;

@Controller
public class EmployeeController {

	public EmployeeController() {
		super();
		System.out.println("controller--------------------------------");
	}

	@RequestMapping(value = "/hello", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String test() throws InterruptedException {
		try {
			//new Program().main();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("test!");
		return "{\"success\":\"okokokok\"}";
	}

	@RequestMapping(value = "/hello1", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String test1() {
		System.out.println("test1111111111111111!");
		return "{\"success\":\"nonononono中文\"}";
	}

	// 上传文件会自动绑定到MultipartFile中
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String upload(HttpServletRequest request,
			@RequestParam("file") MultipartFile file) throws Exception {

		System.out.println("enter");
		// 如果文件不为空，写入上传路径
		if (!file.isEmpty()) {
			// 上传文件路径
			String path = request.getServletContext().getRealPath("/images/");
			// 上传文件名
			String filename = file.getOriginalFilename();
			File filepath = new File(path, filename);
			// 判断路径是否存在，如果不存在就创建一个
			if (!filepath.getParentFile().exists()) {
				filepath.getParentFile().mkdirs();
			}
			// 将上传文件保存到一个目标文件当中
			file.transferTo(new File(path + File.separator + filename));
			return "success";
		} else {
			return "error";
		}

	}

	@RequestMapping(value = "/download")
	public ResponseEntity<byte[]> download(HttpServletRequest request,
			@RequestParam("filename") String filename, Model model)
			throws Exception {
		// 下载文件路径
		String path = request.getServletContext().getRealPath("/images/");
		File file = new File(path + File.separator + filename);
		HttpHeaders headers = new HttpHeaders();
		// 下载显示的文件名，解决中文名称乱码问题
		String downloadFielName = new String(filename.getBytes("UTF-8"),
				"iso-8859-1");
		// 通知浏览器以attachment（下载方式）打开图片
		headers.setContentDispositionFormData("attachment", downloadFielName);
		// application/octet-stream ： 二进制流数据（最常见的文件下载）。
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),
				headers, HttpStatus.CREATED);
	}

	@RequestMapping("/login")
	@ResponseBody
	public String login(String code) {
		String tmpurl = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";
		String url = tmpurl.replace("APPID", "wx61e6d99d24b1a8a3")
				.replace("SECRET", "76c15d0fd330d80f9fd815b77854a71d")
				.replace("JSCODE", code);
		JSONObject jsonObject = HttpUtil.httpRequest(url, "GET", null);
		System.out.println(jsonObject.toString());
		return jsonObject.toString();
	}

	@RequestMapping("/serviceMessage")
	public void serviceMessage(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (req.getMethod().equals("GET")) {
			String signature = req.getParameter("signature");
			String timestamp = req.getParameter("timestamp");
			String nonce = req.getParameter("nonce");
			String echostr = req.getParameter("echostr");
			if (SignUtil.checkSignature(signature, timestamp, nonce)) {
				resp.getWriter().write(echostr);
			}
		} else {
			req.setCharacterEncoding("utf-8");
			resp.setCharacterEncoding("utf-8");

			try {
				String respMessage = processRequest(req);
			} catch (AesException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			resp.getWriter().write("success");
		}
	}

	@RequestMapping("templateMessage")
	@ResponseBody
	public String templateMessage(HttpServletRequest req) {
		String form_id = req.getParameter("form_id");
		String thing = req.getParameter("thing");
		String price = req.getParameter("price");

		System.out.println("form_id:" + form_id + "thing:" + thing + "price"
				+ price);

		if (WeixinUtil.access_token == null) {
			WeixinUtil.getAccess_Token();
		}
		String sendtemplateurl = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=ACCESS_TOKEN"
				.replace("ACCESS_TOKEN", WeixinUtil.access_token);
		JSONObject jsonData = new JSONObject();
		jsonData.put("touser", "oTsoc0Z3ZOOFjggX2AS2qt8-3j5Q");
		jsonData.put("template_id",
				"LL0-b0fLjpNDYh1rqDv0QIEv21t096B97toXkAUjNyQ");
		jsonData.put("page", "pages/index/index");
		jsonData.put("form_id", form_id);

		JSONObject data = new JSONObject();
		data.put("keyword1", "{\"value\":\"" + thing
				+ "\",\"color\":\"#173177\"}");
		data.put("keyword2", "{\"value\":\"" + price
				+ "\",\"color\":\"#173177\"}");
		jsonData.put("data", data);
		JSONObject json = HttpUtil.httpRequest(sendtemplateurl, "POST",
				jsonData.toString());
		System.out.println(json.toString());
		return json.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/get", produces = "application/json; charset=utf-8")
	public String get(HttpServletRequest req) {
		String encryptedData = req.getParameter("encryptedData");
		String sessionKey = req.getParameter("sessionKey");
		String iv = req.getParameter("iv");
		String zhongwen = req.getParameter("zhongwen");
		System.out.println("zhongwen:" + zhongwen + "\nencryptedData:"
				+ encryptedData + "\nsessionKey:" + sessionKey + "\niv" + iv);
		JSONObject json = WXAppletUserInfo.getUserInfo(encryptedData,
				sessionKey, iv);
		System.out.println("get:" + json.toString());
		return json.toString();
	}

	// ///////////////////////////////////////////////////////////////////////////
	public String processRequest(HttpServletRequest req) throws AesException {
		Map<String, String> reqMap = MessageUtil.parseXml(req);
		String msgSignature=req.getParameter("msg_signature");
		String timestamp=req.getParameter("timestamp");
		String nonce=req.getParameter("nonce");
		System.out.println("msgSignature:"+msgSignature+"\ntimestamp"+timestamp+"\nnonce"+nonce);
		System.out.println("mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm");
		for (Entry<String, String> m : reqMap.entrySet()) {
			System.out.println(m.getKey() + "\t" + m.getValue());
		}
		System.out.println("mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm");
		
		WXBizMsgCrypt pc = new WXBizMsgCrypt("wechatDemo", "KCzWY24oVRlEaBhiq6yfNTqvhqKeI2RMberz6PQvfAM", "wx61e6d99d24b1a8a3");
		String format = "<xml><ToUserName><![CDATA[toUser]]></ToUserName><Encrypt><![CDATA[%1$s]]></Encrypt></xml>";
		String fromXML = String.format(format, reqMap.get("Encrypt"));
		String result2 = pc.decryptMsg(msgSignature, timestamp, nonce, fromXML);
		System.out.println("解密后明文: " + result2);
		
		String respContent = "北京欢迎您";
		String ToUserName = reqMap.get("ToUserName");
	

		String accesstoken = WeixinUtil.getAccess_Token().getString(
				"access_token");

		String tmpanswerurl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
		String answerurl = tmpanswerurl.replace("ACCESS_TOKEN", accesstoken);

		JSONObject json = new JSONObject();
		json.put("touser", "oTsoc0Z3ZOOFjggX2AS2qt8-3j5Q");
		json.put("msgtype", "text");
		JSONObject json1 = new JSONObject();
		json1.put("content", respContent);
		json.put("text", json1);
		


		
		Object obj = HttpUtil.httpRequest(answerurl, "POST",json.toString());
		System.out.println("obj:" + obj);

		return obj.toString();
	}
	
}