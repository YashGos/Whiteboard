import java.awt.*;

import com.sun.net.httpserver.*;
import org.apache.commons.lang3.RandomStringUtils;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

interface GSuiteAuthorizationListener
{
    void AuthorizationComplete(String Status, String authURI);
}

public class AuthorizeGSuite
{
    protected static boolean validResponse = false;
    protected static String codeVerifier = "";

    AuthorizeGSuite()
    {
        launchAuthWindow(buildAuthWindowURI());
        listenforAuth();
    }

    public static String buildAuthWindowURI()
    {
        String scope = "scope=https://www.googleapis.com/auth/calendar&";
        String responseType = "response_type=code&";
        String redirectURI = "redirect_uri=http://127.0.0.1:7762&";
        codeVerifier = RandomStringUtils.randomAlphanumeric(128) + "&";
        String clientID = "client_id=686230519389-1lpsi7dl2lfi4p0sd3buholvihdemi5s.apps.googleusercontent.com";

        return "https://accounts.google.com/o/oauth2/v2/auth?" + scope + responseType + redirectURI + "code_challenge=" + codeVerifier + clientID;
    }

    public static void launchAuthWindow(String authURI)
    {
        try
        {
            Desktop.getDesktop().browse(URI.create(authURI));
        }
        catch (Exception uriError)
        {
            //
        }
    }

    public static void listenforAuth()
    {
        try
        {
            HttpServer temporaryAuthListener = HttpServer.create(new InetSocketAddress(7762), 0);
            authHandler authorizationHandler = new authHandler();
            authorizationHandler.addListener((status,authURI) -> {byte[] temp = COM.COM("AuthorizeNewGSuiteUser;" + status + ";" + codeVerifier + ";;;" + authURI);});
            temporaryAuthListener.createContext("/", authorizationHandler);
            temporaryAuthListener.setExecutor(null); // creates a default executor
            temporaryAuthListener.start();

            Thread.sleep(60000);

            temporaryAuthListener.stop(0);

        }
        catch(Exception httpserverException)
        {
            //
        }
    }

    public static class authHandler implements HttpHandler
    {
        private List<GSuiteAuthorizationListener> listeners = new ArrayList<GSuiteAuthorizationListener>();
        public void addListener(GSuiteAuthorizationListener toAdd)
        {
            listeners.add(toAdd);
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException
        {
            try
            {
                URI authURI = new URI("http://null.null");

                if (!validResponse)
                {
                    authURI = exchange.getRequestURI();

                    for (GSuiteAuthorizationListener al : listeners)
                    {
                        if(authURI != new URI("http://null.null"))
                        {
                            al.AuthorizationComplete("true", authURI.toString());
                        }
                        else
                        {
                            al.AuthorizationComplete("false", authURI.toString());
                        }
                        validResponse = true;
                    }
                }
            }
            catch (Exception URIError)
            {

            }

            exchange.close();
            //return authURI.toString();
        }
    }
}
