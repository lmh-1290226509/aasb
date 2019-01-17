//package com.ddadai.basehttplibrary.response;
//
//
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.ddadai.basehttplibrary.HttpUtils;
//import com.ddadai.basehttplibrary.interfaces.CheckSignInterface;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.w3c.dom.Document;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.SAXException;
//
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//
///**
// * Created by shi on 2017/2/8.
// */
//
//public class XmlCallBack extends BaseStringCallBack<JSONObject> {
//    @Override
//    protected void makeResponse(Response_<JSONObject> result, String response) {
//        try {
//            JSONObject jsonObject = xmlStringToHashMap(response);
//            Log.d("fuiou", "makeResponse: "+jsonObject.toString());
////            JSONObject jsonObject = new JSONObject(response);
//            result.data=jsonObject;
//            if (result.data != null) {
//                result.code = result.data.optString(HttpUtils.RSP_CODE);
//                result.msg = result.data.optString(HttpUtils.RSP_DESC);
////                result.isShowMsg=result.data.optBoolean("Message_Is_Show",false);
//            }
//        }  catch (ParserConfigurationException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (SAXException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    protected boolean checkSign(Response_<JSONObject> result, CheckSignInterface checkSignInterface) {
//        return checkSignInterface.checkSign(result.data);
//    }
//
//
//    // 解析xml成hashMap
//    public static JSONObject xmlStringToHashMap(String responseString) throws ParserConfigurationException, IOException, SAXException {
//        JSONObject hashMap = new JSONObject();
//        if (TextUtils.isEmpty(responseString)) {
//            return hashMap;
//        }
//        Document doc = DocumentBuilderFactory.newInstance()
//                .newDocumentBuilder()
//                .parse(new ByteArrayInputStream(responseString.getBytes()));
//        NodeList nodeList = doc.getFirstChild().getChildNodes();
//        for (int i = 0; i < nodeList.getLength(); i++) {
//            Node node = nodeList.item(i);
//            paserNode(node, hashMap);
//        }
//        return hashMap;
//    }
//
//    private static void paserNode(Node pNode, JSONObject hashMap) {
//        NodeList nodeList = pNode.getChildNodes();
//        if (nodeList.getLength() == 1 && nodeList.item(0).getNodeType() == Node.TEXT_NODE) {
//            addNodeValue(hashMap, pNode.getNodeName(), nodeList.item(0).getNodeValue());
//        } else if(nodeList.getLength() == 0){
//            try {
//                hashMap.put(pNode.getNodeName(),"");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }else {
//            JSONObject inHashMap = new JSONObject();
//            addNodeValue(hashMap, pNode.getNodeName(), inHashMap);
//            for (int i = 0; i < nodeList.getLength(); i++) {
//                paserNode(nodeList.item(i), inHashMap);
//            }
//        }
//    }
//
//    private static void addNodeValue(JSONObject hashMap, String key, Object value) {
//        if (hashMap.opt(key) == null) {
//            try {
//                hashMap.put(key, value);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        } else if ((hashMap.opt(key)) instanceof JSONArray) {
//            ((JSONArray) hashMap.opt(key)).put(value);
//        } else {
//            JSONArray array = new JSONArray();
//            array.put(hashMap.opt(key));
//            array.put(value);
//            hashMap.remove(key);
//            try {
//                hashMap.put(key, array);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
