package nextstep.jwp;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.scan.StandardJarScanFilter;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class JwpApplication {

    private static final Logger log = LoggerFactory.getLogger(JwpApplication.class);

    public static void main(String[] args) throws Exception {
        final int port = WebServer.defaultPortIfNull(args);
        final Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);

        final String path = JwpApplication.class.getClassLoader().getResource("static").getPath();
        final File docBase = new File(path);
        final Context context = tomcat.addWebapp(null, "/", docBase.getAbsolutePath());
        log.info("docBase : {}", docBase.getAbsolutePath());

        skipBindOnInit(tomcat);
        skipTldScan(context);

        tomcat.start();
    }

    private static void skipBindOnInit(Tomcat tomcat) {
        final Connector connector = tomcat.getConnector();
        connector.setProperty("bindOnInit", "false");
    }

    private static void skipTldScan(Context context) {
        final StandardJarScanner scanner = (StandardJarScanner) context.getJarScanner();
        final StandardJarScanFilter filter = (StandardJarScanFilter) scanner.getJarScanFilter();
        filter.setDefaultTldScan(false);
        filter.setDefaultPluggabilityScan(false);
    }
}
