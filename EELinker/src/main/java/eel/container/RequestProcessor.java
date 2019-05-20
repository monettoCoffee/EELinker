package eel.container;

import eel.router.DispatcherServlet;
import eel.router.MappingReflect;

import java.net.Socket;

public class RequestProcessor {

    private DispatcherServlet dispatcher = new DispatcherServlet();

    private ContainerConnector httpConnector;
    private HttpServletRequestImpl httpRequest = null;
    private HttpServletResponseImpl httpResponse = null;
    private static int BUFFER_SIZE = 4096;

    public RequestProcessor(ContainerConnector httpConnector) {
        this.httpConnector = httpConnector;
    }

    public void process(Socket socket) {
        try {
            httpRequest = new HttpServletRequestImpl(socket,BUFFER_SIZE);
            httpResponse = new HttpServletResponseImpl(socket);

            httpRequest.parseRequest();
            String requestUri = httpRequest.getRequestURI();

            httpResponse.setRequest(httpRequest);
            httpResponse.setHeaders("Server", "Servlet Container");
            if (MappingReflect.controllers.containsKey(requestUri)){
                dispatcher.service(httpRequest, httpResponse);
            } else {
                StaticResourceProcessor.staticProcess(httpRequest, httpResponse);
            }

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
