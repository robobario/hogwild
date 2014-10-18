package nz.hogwild;

import nz.hogwild.oauth.HogWildGoogleCallbackServlet;
import nz.hogwild.oauth.HogWildGoogleServlet;
import nz.hogwild.web.HogWildServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Application {
    public static void main(String[] args) {
        try {
            Server server = new Server(8080);
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            server.setHandler(context);
            String hogwildHostname = env("HOGWILD_HOSTNAME");
            String hogwildGoogleClientId = env("HOGWILD_GOOGLE_CLIENT_ID");
            String hogwildGoogleSecret = env("HOGWILD_GOOGLE_SECRET");
            context.addServlet(new ServletHolder(new HogWildGoogleServlet(hogwildHostname, hogwildGoogleClientId)), "/google");
            context.addServlet(new ServletHolder(new HogWildGoogleCallbackServlet(hogwildHostname, hogwildGoogleClientId, hogwildGoogleSecret)), "/oauth2callback");
            context.addServlet(new ServletHolder(new HogWildServlet()), "/");
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
