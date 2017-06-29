package com.springmvc.util;

import java.io.IOException;

import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

/**
 * message util class 
 * @author admin
 *
 */
public class MessageUtil {
	//request type
	public static final String req_msgtype_text = "text";
	public static final String req_msgtype_image = "image";
	public static final String req_msgtype_music = "music";
	public static final String req_msgtype_event = "event";
	
	//response type
	public static final String resp_msgtype_text = "text";
	public static final String resp_msgtype_image = "image";
	public static final String resp_msgtype_music = "music";
	public static final String resp_msgtype_news = "news";
	
	/**
	 * analysis xml
	 * @param req
	 * @return
	 */
	public static Map<String, String> parseXml(HttpServletRequest req) {
		//store result into HashMap
		Map<String,String> reqMap = null;
		try {
			reqMap = new HashMap<String,String>();
			//get input stream from request
			InputStream inputStream = req.getInputStream();
			//load input stream,dom for java
			SAXReader reader = new SAXReader();
			Document document = reader.read(inputStream);
			//get root node
			Element root = document.getRootElement();
			//get all of son nodes
			List<Element> elementList = root.elements();
			//traverse all of son nodes
			for(Element e : elementList){
				reqMap.put(e.getName(), e.getText());
			}
			//release resource
			inputStream.close();
			inputStream = null;
			
		} catch (IOException | DocumentException e) {
			e.printStackTrace();
		}
		
		return reqMap;
	}
	
	/** 
     * 文本消息对象转换成xml 
     *  
     * @param textMessage 文本消息对象 
     * @return xml 
     */  
    public static String textMessageToXml(TextMessage textMessage) {  
        xstream.alias("xml", textMessage.getClass());  
        return xstream.toXML(textMessage);  
    }  
    
    public static String textMessageToXml1(BackMessage textMessage) {  
        xstream.alias("xml", textMessage.getClass());  
        return xstream.toXML(textMessage);  
    }  
     
   
    /** 
     * 扩展xstream，使其支持CDATA�? 
     *  
     */  
    private static XStream xstream = new XStream(new XppDriver() {  
        public HierarchicalStreamWriter createWriter(Writer out) {  
            return new PrettyPrintWriter(out) {  //-----1
                // 对所有xml节点的转换都增加CDATA标记  
                boolean cdata = true;  
  
                public void startNode(String name, Class clazz) {  
                    super.startNode(name, clazz);  
                }  
  
                protected void writeText(QuickWriter writer, String text) {  
                    if (cdata) {  
                        writer.write("<![CDATA[");  
                        writer.write(text);  
                        writer.write("]]>");  
                    } else {  
                        writer.write(text);  
                    }  
                }  
            };  
        }//-------1  
    });  

}
