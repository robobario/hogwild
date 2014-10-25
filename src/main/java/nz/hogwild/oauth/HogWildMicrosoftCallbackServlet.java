package nz.hogwild.oauth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nz.hogwild.service.SessionStore;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.OAuthProviderType;
import org.apache.oltu.oauth2.common.message.types.GrantType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class HogWildMicrosoftCallbackServlet extends HttpServlet {

    private String host;
    private String microsoftClientId;
    private String secret;

    public HogWildMicrosoftCallbackServlet(String host1, String microsoftClientId1, String secret1) {
        host = host1;
        microsoftClientId = microsoftClientId1;
        secret = secret1;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(true);
        try {
            OAuthAuthzResponse oar = OAuthAuthzResponse.oauthCodeAuthzResponse(req);
            String code = oar.getCode();
            OAuthClientRequest request = OAuthClientRequest
                    .tokenProvider(OAuthProviderType.MICROSOFT)
                    .setGrantType(GrantType.AUTHORIZATION_CODE)
                    .setClientId(microsoftClientId)
                    .setClientSecret(secret)
                    .setRedirectURI("http://" + host + "/oauth2microsoft")
                    .setCode(code)
                    .buildBodyMessage();
            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
            OAuthJSONAccessTokenResponse oAuthResponse = oAuthClient.accessToken(request);

            String accessToken = oAuthResponse.getAccessToken();

            OAuthClientRequest bearerClientRequest = new OAuthBearerClientRequest("https://apis.live.net/v5.0/me")
                    .setAccessToken(accessToken).buildQueryMessage();
            bearerClientRequest.setHeader("Accept", OAuth.ContentType.JSON);
            OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerClientRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);
            JsonNode jsonNode = new ObjectMapper().reader().readTree(resourceResponse.getBody());
            JsonNode email = jsonNode.get("emails").get("account");
            SessionStore.sessionStore().addUser(session.getId(), email.textValue());
            resp.sendRedirect("/");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
