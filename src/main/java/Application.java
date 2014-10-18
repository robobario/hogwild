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
            String hogwildHostname = System.getenv("HOGWILD_HOSTNAME");
            String hogwildGoogleClientId = System.getenv("HOGWILD_GOOGLE_CLIENT_ID");
            String hogwildGoogleSecret = System.getenv("HOGWILD_GOOGLE_SECRET");
            context.addServlet(new ServletHolder(new HogWildGoogleServlet(hogwildHostname, hogwildGoogleClientId)), "/google");
            context.addServlet(new ServletHolder(new HogWildGoogleCallbackServlet(hogwildHostname, hogwildGoogleClientId, hogwildGoogleSecret)), "/oauth2callback");
            server.start();
            server.join();
        }catch (Throwable t){
            t.printStackTrace();
        }
    }
}
