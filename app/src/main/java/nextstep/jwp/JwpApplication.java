package nextstep.jwp;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.scan.StandardJarScanFilter;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.stream.Stream;

public class JwpApplication {

    private static final Logger log = LoggerFactory.getLogger(JwpApplication.class);

    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) throws Exception {
        final int port = defaultPortIfNull(args);

        final Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        final Context context = addWebapp(tomcat);

        // 톰캣 실행할 때 불필요한 설정은 skip
        skipBindOnInit(tomcat);
        skipTldScan(context);

        tomcat.start();
    }

    private static int defaultPortIfNull(String[] args) {
        return Stream.of(args)
                .findFirst()
                .map(Integer::parseInt)
                .orElse(DEFAULT_PORT);
    }

    private static Context addWebapp(Tomcat tomcat) {
        final String docBase = new File("app/webapp/").getAbsolutePath();
        final Context context = tomcat.addWebapp("/", docBase);
        log.info("configuring app with basedir: {}", docBase);
        return context;
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
