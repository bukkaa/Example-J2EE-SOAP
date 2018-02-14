package ws.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import path.to.AppNameMdcInterceptor;

import javax.interceptor.Interceptors;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import static java.util.jar.Attributes.Name.*;

@Interceptors(AppNameMdcInterceptor.class)
public class ContextListener implements ServletContextListener {
    private static final Logger LOG = LoggerFactory.getLogger(ContextListener.class);

    private final static String BUILD_TIME_ATTRIBUTE_NAME = "Build-Time";
    private final static String MANIFEST = "/META-INF/MANIFEST.MF";

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        LOG.info("Web Service start");
        try {
            ServletContext application = servletContextEvent.getServletContext();
            Manifest manifest = new Manifest(application.getResourceAsStream(MANIFEST));
            Attributes attrs = manifest.getMainAttributes();
            LOG.info("application: "  + attrs.getValue(IMPLEMENTATION_TITLE));
            LOG.info("version: "      + attrs.getValue(IMPLEMENTATION_VERSION));
            LOG.info("spec title: "   + attrs.getValue(SPECIFICATION_TITLE));
            LOG.info("spec version: " + attrs.getValue(SPECIFICATION_VERSION));
            LOG.info("build time: "   + attrs.getValue(BUILD_TIME_ATTRIBUTE_NAME));
        } catch(Exception e) {
            LOG.warn("failed to read Manifest: "+e.getMessage(), e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent ignored) {
        LOG.info("Web Service stop");
    }
}
