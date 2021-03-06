package nz.hogwild;

import nz.hogwild.oauth.HogWildGoogleCallbackServlet;
import nz.hogwild.oauth.HogWildGoogleServlet;
import nz.hogwild.oauth.HogWildMicrosoftCallbackServlet;
import nz.hogwild.oauth.HogWildMicrosoftServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.postgresql.Driver;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class Application {
    public static void main(String[] args) {
        try {
            Server server = new Server(8080);
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            server.setHandler(context);
            context.setBaseResource(Resource.newClassPathResource("static"));
            String hogwildHostname = env("HOGWILD_HOSTNAME");
            String hogwildGoogleClientId = env("HOGWILD_GOOGLE_CLIENT_ID");
            String hogwildGoogleSecret = env("HOGWILD_GOOGLE_SECRET");
            String hogwildMicrosoftClientId = env("HOGWILD_MICROSOFT_CLIENT_ID");
            String hogwildMicrosoftSecret = env("HOGWILD_MICROSOFT_SECRET");
            context.addServlet(new ServletHolder(new HogWildGoogleServlet(hogwildHostname, hogwildGoogleClientId)), "/google");
            context.addServlet(new ServletHolder(new HogWildMicrosoftServlet(hogwildHostname, hogwildMicrosoftClientId)), "/microsoft");
            context.addServlet(new ServletHolder(new HogWildGoogleCallbackServlet(hogwildHostname, hogwildGoogleClientId, hogwildGoogleSecret)), "/oauth2callback");
            context.addServlet(new ServletHolder(new HogWildMicrosoftCallbackServlet(hogwildHostname, hogwildMicrosoftClientId, hogwildMicrosoftSecret)), "/oauth2microsoft");
            AnnotationConfigWebApplicationContext webApplicationContext = new AnnotationConfigWebApplicationContext();
            webApplicationContext.register(Context.class);
            context.addServlet(new ServletHolder(new DispatcherServlet(webApplicationContext)), "/*");
            server.start();
            server.join();
        }catch (Throwable t){
            t.printStackTrace();
        }
    }

    private static String env(String prop) {
        String getenv = System.getenv(prop);
        if(getenv == null || getenv.isEmpty()){
            throw new RuntimeException("prop " + prop + " was null or empty");
        }
        return getenv;
    }
}
