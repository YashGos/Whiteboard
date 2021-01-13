using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace Whiteboard_Communication_Service.GSuite_Integration
{
    class Enable_Integration
    {
        protected const string clientID = "686230519389-1lpsi7dl2lfi4p0sd3buholvihdemi5s.apps.googleusercontent.com";
        protected const string clientSecret = "_W5PR-SSZbUVJwoZJQ6sFphx";

        public static int GetRandomUnusedPort()
        {
            var listener = new TcpListener(IPAddress.Loopback, 0);
            listener.Start();
            var port = ((IPEndPoint)listener.LocalEndpoint).Port;
            listener.Stop();
            return port;
        }

        public static async void enableGSuiteAsync(String authCode, String codeVerifier)
        {
            // Creates a redirect URI using an available port on the loopback address.
            string redirectURI = string.Format("http://{0}:{1}/", IPAddress.Loopback, GetRandomUnusedPort());
            System.Diagnostics.Debug.Print(redirectURI);

            // Creates an HttpListener to listen for requests on that redirect URI.
            var http = new HttpListener();
            http.Prefixes.Add(redirectURI);
            http.Start();

            string tokenRequestURI = "https://www.googleapis.com/oauth2/v4/token";

            string tokenRequestBody = string.Format("code={0}&redirect_uri={1}&client_id={2}&code_verifier={3}&client_secret={4}&scope=&grant_type=authorization_code",
                System.Web.HttpUtility.UrlEncode(authCode),
                System.Web.HttpUtility.UrlEncode(redirectURI),
                clientID,
                codeVerifier,
                clientSecret
                );

            System.Diagnostics.Debug.WriteLine(System.Web.HttpUtility.UrlEncode(authCode));
            System.Diagnostics.Debug.WriteLine(System.Web.HttpUtility.UrlEncode(redirectURI));
            System.Diagnostics.Debug.WriteLine(clientID);
            System.Diagnostics.Debug.WriteLine(codeVerifier);
            System.Diagnostics.Debug.WriteLine(clientSecret);

            HttpWebRequest tokenRequest = (HttpWebRequest)WebRequest.Create(tokenRequestURI);
            tokenRequest.Method = "POST";
            tokenRequest.ContentType = "application/x-www-form-urlencoded";
            tokenRequest.Accept = "Accept=text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
            byte[] _byteVersion = Encoding.ASCII.GetBytes(tokenRequestBody);
            tokenRequest.ContentLength = _byteVersion.Length;
            Stream stream = tokenRequest.GetRequestStream();
            await stream.WriteAsync(_byteVersion, 0, _byteVersion.Length);
            stream.Close();

            try
            {
                // gets the response
                WebResponse tokenResponse = await tokenRequest.GetResponseAsync();
                using (StreamReader reader = new StreamReader(tokenResponse.GetResponseStream()))
                {
                    // reads response body
                    string responseText = await reader.ReadToEndAsync();
                    Console.WriteLine(responseText);

                    // converts to dictionary
                    Dictionary<string, string> tokenEndpointDecoded = JsonConvert.DeserializeObject<Dictionary<string, string>>(responseText);

                    string access_token = tokenEndpointDecoded["access_token"];
                    string refresh_token = tokenEndpointDecoded["refresh_token"];
                    System.Diagnostics.Debug.Print(access_token);
                    System.Diagnostics.Debug.Print(refresh_token);
                }
            }
            catch (WebException webException)
            {
                Event_Viewer_Integration.EventViewer.logEvent(webException.Message, System.Diagnostics.EventLogEntryType.Warning);
            }
        }
    }
}
