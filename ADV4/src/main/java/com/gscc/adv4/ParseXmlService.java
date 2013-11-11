package com.gscc.adv4;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Sonia on 2013/11/11.
 */
public class ParseXmlService {


    public HashMap<String, String> parseXml(InputStream inStream) throws Exception
    {
        HashMap<String, String> hashMap = new HashMap<String, String>();

//實例化一個文檔構建器工廠
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//通過文檔構建器工廠獲取一個文檔構建器
        DocumentBuilder builder = factory.newDocumentBuilder();
//通過文檔通過文檔構建器構建一個文檔實例
        Document document = builder.parse(inStream);
//獲取XML文件根節點
        Element root = document.getDocumentElement();
//獲得所有子節點
        NodeList childNodes = root.getChildNodes();
        for (int j = 0; j < childNodes.getLength(); j++)
        {
//遍歷子節點
            Node childNode = (Node) childNodes.item(j);
            if (childNode.getNodeType() == Node.ELEMENT_NODE)
            {
                Element childElement = (Element) childNode;
//版本號
                if ("version".equals(childElement.getNodeName()))
                {
                    hashMap.put("version",childElement.getFirstChild().getNodeValue());
                }
//軟件名稱
                else if (("name".equals(childElement.getNodeName())))
                {
                    hashMap.put("name",childElement.getFirstChild().getNodeValue());
                }
//下載地址
                else if (("url".equals(childElement.getNodeName())))
                {
                    hashMap.put("url",childElement.getFirstChild().getNodeValue());
                }
            }
        }
        return hashMap;
    }
}
