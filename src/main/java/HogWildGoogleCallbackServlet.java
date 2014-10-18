import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.GitHubTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HogWildGoogleCallbackServlet extends HttpServlet {

    private String host;
    private String googleClientId;
    private String secret;

    public HogWildGoogleCallbackServlet(String host1, String googleClientId1, String secret1) {
        host = host1;
        googleClientId = googleClientId1;
        secret = secret1;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
        req.getSession(true);
        try {
            OAuthAuthzResponse oar = OAuthAuthzResponse.oauthCodeAuthzResponse(req);
            String code = oar.getCode();
            OAuthClientRequest request = OAuthClientRequest
                    .tokenProvider(OAuthProviderType.GOOGLE)
                    .setGrantType(GrantType.AUTHORIZATION_CODE)
                    .setClientId(googleClientId)
                    .setClientSecret(secret)
                    .setRedirectURI("http://" + host + "/oauth2callback")
                    .setCode(code)
                    .buildQueryMessage();
            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
            OAuthJSONAccessTokenResponse oAuthResponse = oAuthClient.accessToken(request);

            String accessToken = oAuthResponse.getAccessToken();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
