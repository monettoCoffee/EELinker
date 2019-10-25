package eel.container;

import eel.config.DefaultContainerConfig;
import eel.scanner.BeanMapping;
import eel.thread.AsyncThread;
import eel.utils.ContentTypeFind;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;

/**
 * @author monetto
 */
public class ContainerConnector implements Runnable {

    private volatile boolean stopped;
    private RequestProcessor processor = new RequestProcessor(this);


    public ContainerConnector(){
        this.start();
    }

    // Plan Done Version 0.2.0
    public ContainerConnector(String configPackage){
        if (configPackage == null){
            // todo 包为null, 采用默认配置
        } else {
            URL url = this.getClass().getClassLoader().getResource(configPackage.replaceAll("\\.", File.separator));
            if (url == null){
                // todo url为null, 采用默认配置
            }
            File dir = new File(url.getFile());
            for (File file : dir.listFiles()) {
                if(file.isDirectory()){
                    // todo 向下继续扫包
                }else{
                    try {
                        String className = configPackage +"." +file.getName().replace(".class", "");
                        Class clazz = Class.forName(className);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
//        this.start();
    }

    @Override
    public void run() {
        // 初始化Bean容器
//        BeanMapping.
        ServerSocket serverSocket = null;
        int port = DefaultContainerConfig.listenPort;
        try {
            serverSocket =  new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("Server Logger : Version " + DefaultContainerConfig.version + " Listening Port " + DefaultContainerConfig.listenPort);

        while (!stopped) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (Exception e) {
                continue;
            }

            try {
                AsyncThread.run(processor, "process", socket);
                //multi meanwhile processor.process(socket);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void start() {
        ContentTypeFind.initMap();
        Thread thread = new Thread(this);
        thread.start();
    }
}
