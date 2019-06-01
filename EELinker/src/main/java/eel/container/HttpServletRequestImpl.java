package eel.container;

import eel.utils.Utils;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.*;

/**
 * @author monetto
 */
public class HttpServletRequestImpl {

    private Map<String, String[]> requestParam = new HashMap<>();
    private Map<String, Object> requestInfo = new HashMap<>(32);
    private String requestType;
    private String requestUri;
    public String inputString;
    byte[] buffer;
    private HashMap<String, String> cookies = new HashMap<String, String>();

    public void parseRequest() throws IOException{
        String[] requestInput = this.inputString.split("\n");
        this.parseRequestType(requestInput[0]);
        this.parseRequestInfo(requestInput);
    }

    private void parseRequestType(String str) {
        String params[] = str.split(" ");
        this.requestType = params[0];
        this.requestUri = params[1];
        int paramEdge = this.requestUri.indexOf("?");
        if (paramEdge != -1) {
            // 存在 ? 的情况下是GET附带参数
            this.parseRequestGetParams(this.requestUri.substring(paramEdge+1));
            this.requestUri = this.requestUri.substring(0, paramEdge);
        }
        this.requestInfo.put("httpVersion", params[2]);
    }

    private void parseRequestGetParams(String str){
        int length = str.length();
        StringBuilder key = new StringBuilder("");
        StringBuilder value = new StringBuilder("");
        boolean isValue = false;

        for (int index = 0; index < length; ++ index){
            char c = str.charAt(index);
            if (c == '='){
                isValue = true;
                continue;
            } else if (c == '&'){
                isValue = false;
                String []parameter = new String[1];
                parameter[0] = charConvert(value.toString());
                this.requestParam.put(key.toString(), parameter);
                key = new StringBuilder("");
                value = new StringBuilder("");
                continue;
            }
            if (isValue){
                value.append(c);
            } else {
                key.append(c);
            }
        }
        String []parameter = new String[1];
        parameter[0] = charConvert(value.toString());
        this.requestParam.put(key.toString(), parameter);
    }

    private void parseRequestInfo(String []requestInfo) {
        int length = requestInfo.length;
        for (int index = 1; index < length; ++index){
            String requestStr = requestInfo[index];
            int edge = requestStr.indexOf(": ");
            if (edge != -1){
                this.requestInfo.put(requestStr.substring(0, edge), requestStr.substring(edge+2));
            } else {
                // 请求中没有 :空格 的情况
                if("POST".equals(this.requestType)){
                    // 判断是否附带有表单数据
                    if (requestStr.contains("=")){
                        String []formParameters = requestStr.split("&");
                        for (String postParam : formParameters){
                            String []keyAndValue = postParam.split("=");
                            String []parameterValue = new String[1];
                            parameterValue[0] = charConvert(keyAndValue[1]);
                            this.requestParam.put(keyAndValue[0], parameterValue);
                        }
                    }
                }
            }
        }
    }

    public HttpServletRequestImpl(Socket socket, int bufferSize) throws IOException {
        this.inputString = Utils.inputStreamToString(socket.getInputStream());
        buffer = new byte[bufferSize];
    }



    public static String charConvert(String str){
        try{
            String _str = URLDecoder.decode(str.replaceAll("%", "%25"), "UTF-8");
            _str = URLDecoder.decode(_str, "UTF-8");
            return _str;
        } catch (Exception e){
            return str;
        }
    }

    public String getCookie(String cookieName) {
        return this.cookies.get(cookieName);
    }

    public String getRequestURI(){
        return this.requestUri;
    }

    public void setRequestUri(String uri){
        this.requestUri = uri;
    }

    public String getHttpVersion(){
        return requestInfo.get("httpVersion").toString();
    }

    public String getMethod() {
        return this.requestType;
    }

    public Map<String, String[]> getRequestParam() {
        return this.requestParam;
    }



}
