package cafe.config;

import java.io.File;

public class ContainerConfig {

    public static final String webRootDir = System.getProperty("user.dir") + File.separator  + "src/main/webapp";
    public static final int listenPort = 8080;
    public static final String version = "Alpha_190419";

}
