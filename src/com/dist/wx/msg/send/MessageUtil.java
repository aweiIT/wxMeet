package com.dist.wx.msg.send;
   
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.dist.wx.msg.resp.Article;
import com.dist.wx.msg.resp.ImageMessage;
import com.dist.wx.msg.resp.MusicMessage;
import com.dist.wx.msg.resp.NewsMessage;
import com.dist.wx.msg.resp.TextMessage;
import com.dist.wx.msg.resp.VideoMessage;
import com.dist.wx.msg.resp.VoiceMessage;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
public class MessageUtil {  
  
    public static final String RESP_MESSAGE_TYPE_TEXT = "text";  
  
    public static final String RESP_MESSAGE_TYPE_MUSIC = "music";  
    public static final String RESP_MESSAGE_TYPE_NEWS = "news";  
    public static final String REQ_MESSAGE_TYPE_TEXT = "text";  
    public static final String REQ_MESSAGE_TYPE_IMAGE = "image";  
    public static final String REQ_MESSAGE_TYPE_LINK = "link";  
    public static final String REQ_MESSAGE_TYPE_LOCATION = "location";  
    public static final String REQ_MESSAGE_TYPE_VOICE = "voice";  
    public static final String REQ_MESSAGE_TYPE_EVENT = "event";  
    public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";  
    public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";  
    public static final String EVENT_TYPE_CLICK = "CLICK";  
    public static final String REQ_MESSAGE_TYPE_VIDEO = "video";
	public static final String EVENT_TYPE_SCAN = "scan";
    @SuppressWarnings("unchecked")  
    public static Map<String, String> parseXml(String request) throws Exception {  
        Map<String, String> map = new HashMap<String, String>();  
        InputStream inputStream = new ByteArrayInputStream(request.getBytes("UTF-8"));  
        SAXReader reader = new SAXReader();  
        Document document = reader.read(inputStream);  
        Element root = document.getRootElement();  
        List<Element> elementList = root.elements();  
        for (Element e : elementList)  
            map.put(e.getName(), e.getText());  
        inputStream.close();  
        inputStream = null;  
        return map;  
    }  
    public static String textMessageToXml(TextMessage textMessage) {  
        xstream.alias("xml", textMessage.getClass());  
        return xstream.toXML(textMessage);  
    }  
    public static String musicMessageToXml(MusicMessage musicMessage) {  
        xstream.alias("xml", musicMessage.getClass());  
        return xstream.toXML(musicMessage);  
    }  
	public static String imageMessageToXml(ImageMessage imageMessage) {
		xstream.alias("xml", imageMessage.getClass());
		return xstream.toXML(imageMessage);
	}
	public static String voiceMessageToXml(VoiceMessage voiceMessage) {
		xstream.alias("xml", voiceMessage.getClass());
		return xstream.toXML(voiceMessage);
	}
	public static String videoMessageToXml(VideoMessage videoMessage) {
		xstream.alias("xml", videoMessage.getClass());
		return xstream.toXML(videoMessage);
	}
    public static String newsMessageToXml(NewsMessage newsMessage) {  
        xstream.alias("xml", newsMessage.getClass());  
        xstream.alias("item", new Article().getClass());  
        return xstream.toXML(newsMessage);  
    }  
    private static XStream xstream = new XStream(new XppDriver() {  
        public HierarchicalStreamWriter createWriter(Writer out) {  
            return new PrettyPrintWriter(out) {  
                // ������xml�ڵ��ת��������CDATA���  
                boolean cdata = true;  
  
                @SuppressWarnings("unchecked")  
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
        }  
    });  
}  