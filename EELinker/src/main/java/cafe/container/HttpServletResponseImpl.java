package cafe.container;


import cafe.config.ContainerConfig;
import cafe.utils.ContentTypeFind;
import cafe.utils.HttpResponseMessage;
import cafe.utils.MessageConstruction;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HttpServletResponseImpl {

    private HttpServletRequestImpl httpRequest;
    public static int BUFFER_SIZE = 4096;
    OutputStream output;
    private boolean committed = false;
    private HashMap<String, List<String>> headers = new HashMap();
    private int status;

    public HttpServletResponseImpl(Socket socket) {
        try {
            this.output = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setRequest(HttpServletRequestImpl httpRequest) {
        this.httpRequest = httpRequest;
    }

    public void returnData(String data) {
        try {
            byte[] bytes = new byte[BUFFER_SIZE];
            InputStream fis= new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
            int ch = fis.read(bytes, 0, BUFFER_SIZE);
            int messageLength = 0;
            while (ch != -1){
                messageLength += ch;
                ch = fis.read(bytes, 0, BUFFER_SIZE);
            }
            setHeaders("content-length",String.valueOf(messageLength));
            setHeaders("content-type", "text/html;charset=UTF-8");
            this.status = HttpResponseMessage.SC_OK.getStatus();
            sendHeaders();
            fis = new ByteArrayInputStream(bytes);
            ch = fis.read(bytes, 0, BUFFER_SIZE);
            while (ch!=-1){
                output.write(bytes);
                ch = fis.read(bytes, 0, BUFFER_SIZE);
            }

        } catch (Exception e) {} finally {}
    }

    public void returnPage(String pageName) throws IOException {
        byte[] bytes = new byte[BUFFER_SIZE];
        FileInputStream fis = null;
        File file = new File(ContainerConfig.webRootDir, pageName);

        try {
            fis = new FileInputStream(file);
            int messageLength = 0;
            String fileName = file.getName();
            int ch = fis.read(bytes, 0, BUFFER_SIZE);
            while (ch!=-1) {
                messageLength += ch;
                ch = fis.read(bytes, 0, BUFFER_SIZE);
            }
            setHeaders("content-length",String.valueOf(messageLength));
            setHeaders("content-type", ContentTypeFind.findTheType(fileName));
            // send the header
            this.status = HttpResponseMessage.SC_OK.getStatus();
            sendHeaders();
            // get the content
            fis = new FileInputStream(file);
            ch = fis.read(bytes, 0, BUFFER_SIZE);
            while (ch!=-1){
                output.write(bytes);
                ch = fis.read(bytes, 0, BUFFER_SIZE);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Server Logger : Unknown " + pageName + " Same as 404 Page");
            this.returnData("This is 404 Page, Please Check Your Router");
        } finally {
            if (fis!=null) {
                fis.close();
            }
        }
    }

    public void sendStaticResource() throws IOException {
        this.returnPage(httpRequest.getRequestURI());
    }

    public void setHeaders(String name, String value) {
        if (isCommitted()){
            return;
        }
        ArrayList<String> values = new ArrayList<String>();
        values.add(value);
        synchronized (headers) {
            headers.put(name, values);
        }
        String match = name.toLowerCase();
        if (match.equals("content-length")) {
            int contentLength = -1;
            try {
                contentLength = Integer.parseInt(value);
            }
            catch (NumberFormatException e) {
                e.printStackTrace();
            }
            if (contentLength >= 0)
                setContentLength(contentLength);
        }
        else if (match.equals("content-type")) {
            setContentType(value);
        }
    }

    public void sendHeaders() throws IOException {
        if (isCommitted()){
            return;
        }
        StringBuilder sb = new StringBuilder();

        if (httpRequest != null){
            sb.append("HTTP/");
            sb.append(httpRequest.getHttpVersion());
        } else {
            sb.append(MessageConstruction.getDefaultVersion());
        }
        sb.append(MessageConstruction.getBlank());
        sb.append(status);
        sb.append(MessageConstruction.getBlank());
        sb.append(HttpResponseMessage.SC_OK.getMessage(status));
        sb.append(MessageConstruction.getCRLF());
        //header construction
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            sb.append(entry.getKey());
            sb.append(MessageConstruction.getColon());
            List<String> temp = entry.getValue();
            for (String tempValue : temp){
                sb.append(MessageConstruction.getBlank());
                sb.append(tempValue);
            }
            sb.append(MessageConstruction.getCRLF());
        }
        sb.append(MessageConstruction.getCRLF());
        output.write(String.valueOf(sb).getBytes());
        committed = true;
    }

    public boolean isCommitted(){
        return committed;
    }

    public void setContentLength(int length){
        int contentLength = length;
    }

    public void setContentType(String type){
        String contentType = type;
    }


}
