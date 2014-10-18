package nz.hogwild.oauth;

import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.ResponseType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HogWildGoogleServlet extends HttpServlet {

    private String host;
    private String googleClientId;

    public HogWildGoogleServlet(String host1, String googleClientId1) {
        host = host1;
        googleClientId = googleClientId1;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession(true);
        try {
            OAuthClientRequest request = OAuthClientRequest
                    .authorizationProvider(OAuthProviderType.GOOGLE)
                    .setClientId(googleClientId).setResponseType(ResponseType.CODE.toString())
                    .setRedirectURI("http://" + host + "/oauth2callback").setScope("openid email")
                    .buildQueryMessage();
            resp.sendRedirect(request.getLocationUri());
        } catch (OAuthSystemException e) {
            throw new RuntimeException(e);
        }
    }
}
