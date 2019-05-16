package cafe.container;

import java.io.IOException;

public class StaticResourceProcessor {
    public static void staticProcess(HttpServletRequestImpl httpRequest, HttpServletResponseImpl httpResponse) {
        try {
            httpResponse.sendStaticResource();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void returnPage(HttpServletResponseImpl httpResponse, String pageName) throws IOException {
        httpResponse.returnPage(pageName);
    }

    public static void returnData(HttpServletResponseImpl httpResponse, String data) throws IOException {
        httpResponse.returnData(data);
    }

}
