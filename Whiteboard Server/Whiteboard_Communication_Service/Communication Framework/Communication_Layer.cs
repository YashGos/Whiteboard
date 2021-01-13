using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Security.Cryptography.X509Certificates;
using System.Net.Security;

namespace Whiteboard_Communication_Service
{
    public partial class Communication_Layer
    {
        // Create a TCP listner and bind it to the communication port.
        private static TcpListener listener = new TcpListener(IPAddress.Any, 49001);
        private static ManualResetEvent tcpClientConnected = new ManualResetEvent(false);

        // Import an SSL cert to encrypt communication.
        private static X509Certificate serverCertificate = X509Certificate.CreateFromCertFile("C:/Program Files (x86)/Whiteboard Communication Service/Server Certificate Signed by RA/Whiteboard_Com_Public.crt");

        // Entry point for communication layer.
        public static void StartCOMLayer()
        {
            startListening();
        }

        // Begin COM layer.
        private static void startListening()
        {
            // Attempt to start the TCP listener. Catch exceptions.
            try
            {
                listener.Start();
            }
            catch (Exception portError)
            {
                // Log errors.
                Whiteboard_Communication_Service.Event_Viewer_Integration.EventViewer.logEvent(portError.Message, System.Diagnostics.EventLogEntryType.Warning);

                // Attempt on backup port.
                try
                {
                    // Attempt to start listener on backup port.
                    listener = new TcpListener(IPAddress.Any, 49002);
                }
                catch (Exception backup_portError)
                {
                    // Handle error
                    Whiteboard_Communication_Service.Event_Viewer_Integration.EventViewer.logEvent(backup_portError.Message, System.Diagnostics.EventLogEntryType.Warning);

                    // Quit
                    Environment.Exit(1);
                }
            }

            // If the listener starts properly, enter a loop so listener does not stop after first connection closes.
            while(true)
            {
                try
                {
                    // Reset listener state.
                    tcpClientConnected.Reset();
                    // Accept new connection.
                    listener.BeginAcceptTcpClient(new AsyncCallback(acceptConnection), listener);
                    // Handle connection before reseting.
                    tcpClientConnected.WaitOne();
                }
                catch (Exception connectionError)
                {
                    // Handle error
                    Whiteboard_Communication_Service.Event_Viewer_Integration.EventViewer.logEvent(connectionError.Message, System.Diagnostics.EventLogEntryType.Warning);
                }
            }
        }

        // Once a sucessful connection is made handle it asynchronusly.
        private static void acceptConnection(IAsyncResult result)
        {
            // Get listener that called method.
            TcpListener current_listener = (TcpListener)result.AsyncState;

            // Create a TCP client for any client application that initiates contact.
            TcpClient client = current_listener.EndAcceptTcpClient(result);

            try
            {
                // Initialize data stream with connected client.
                byte[] bufferBytes = new byte[4096];
                NetworkStream dataStream = client.GetStream();

                /* Initialize encrypted data stream - will replace regular network stream.
                 * 
                 * SslStream dataStream = new SslStream(client.GetStream(), false);
                 * dataStream.AuthenticateAsServer(serverCertificate, clientCertificateRequired: false, enabledSslProtocols: System.Security.Authentication.SslProtocols.Tls12, checkCertificateRevocation: true);
                 */

                // The following takes a look at said data stream and ensures that a full message is recieved. It then handles said message.
                int bytesRead;
                bool messageRecieved = false;
                string message = "null";

                ASCIIEncoding encoder = new ASCIIEncoding();

                while (client.Connected)
                {
                    while (!messageRecieved)
                    {
                        bytesRead = 0;

                        try
                        {
                            // Blocks until a server app sends a message.
                            bytesRead = dataStream.Read(bufferBytes, 0, bufferBytes.Length);
                        }
                        catch
                        {
                            // A socket error has occured. RIP.
                            break;
                        }

                        if (bytesRead == 0)
                        {
                            // The server app has disconnected. 
                            break;
                        }

                        // Message has successfully been received. Let's encode it.
                        message = encoder.GetString(bufferBytes, 0, bytesRead);
                        messageRecieved = true;
                    }

                    messageRecieved = false;

                    // Client requests server terminate communication.
                    if (message.Contains("CloseConnection"))
                    {
                        break;
                    }

                    // Handle login request.
                    else if (message.Contains("InitiateLogin"))
                    {
                        string userName = (message.Split(';')[1]);
                        byte[] salt = Whiteboard_Communication_Service.Authentication_Framework.Login_Authentication.initateLogin(userName);

                        dataStream.Write(salt, 0, salt.Length);
                        dataStream.Flush();

                        break;
                    }

                    // Handle login authentication.
                    else if (message.Contains("AuthenticateLogin"))
                    {
                        string userName = (message.Split(';'))[1];
                        string passwordHash = (message.Split(new string[] { ";;;" }, StringSplitOptions.None))[1];

                        (bool loginAuthenticated, Guid accessToken, char accessLevel) = Whiteboard_Communication_Service.Authentication_Framework.Login_Authentication.authenticateLogin(userName, passwordHash);

                        byte[] buffer = encoder.GetBytes(accessToken.ToString() + ';' + accessLevel);

                        dataStream.Write(buffer, 0, buffer.Length);
                        dataStream.Flush();

                        break;
                    }

                    else if (message.Contains("CreateUser"))
                    {
                        string currentUserName = (message.Split(';')[1]);
                        string currentToken = (message.Split(';')[2]);

                        bool actionResult = false;

                        if(Authentication_Framework.Action_Authentication.authenticateAction(currentUserName, currentToken, true))
                        {
                            string newUserDisplayName = (message.Split(';')[3]);
                            string newUserName = (message.Split(';')[4]);
                            string newUserRole = (message.Split(';')[5]);
                            
                            string newUserHash = (message.Split(new string[] { ";;;" }, StringSplitOptions.None))[1];
                            string newUserSalt = (message.Split(new string[] { ";;;" }, StringSplitOptions.None))[2];

                            actionResult = Database_Integration.Modify.Create_New_User.start(newUserDisplayName, newUserName, newUserRole, newUserHash, newUserSalt);
                        }

                        byte[] buffer = BitConverter.GetBytes(actionResult);

                        dataStream.Flush();
                    }

                    else if (message.Contains("CreateCourse"))
                    {
                        string currentUserName = (message.Split(';')[1]);
                        string currentToken = (message.Split(';')[2]);

                        bool actionResult = false;

                        if (Authentication_Framework.Action_Authentication.authenticateAction(currentUserName, currentToken, true))
                        {
                            string newCourseDisplayName = (message.Split(';')[3]);
                            string newCourseInstructor = (message.Split(';')[4]);
                            string newCourseStudents = (message.Split(';')[5]);
                            string newCourseAssignmentTypes = (message.Split(';')[6]);
                            string newCourseTerm = (message.Split(';')[7]);

                            actionResult = Database_Integration.Modify.Create_New_Course.start(newCourseDisplayName, newCourseInstructor, newCourseStudents, newCourseAssignmentTypes, newCourseTerm);
                        }

                        byte[] buffer = BitConverter.GetBytes(actionResult);

                        dataStream.Flush();
                    }

                    else if (message.Contains("GetCurrentAccounts"))
                    {
                        string currentUserName = (message.Split(';')[1]);
                        string currentToken = (message.Split(';')[2]);

                        string currentAccounts = "";

                        if (Authentication_Framework.Action_Authentication.authenticateAction(currentUserName, currentToken, true))
                        {
                            currentAccounts = Database_Integration.Query.AllAccounts.fetch();
                            System.Diagnostics.Debug.Write(currentAccounts);
                        }

                        byte[] buffer = encoder.GetBytes(currentAccounts);

                        dataStream.Write(buffer, 0, buffer.Length);

                        dataStream.Flush();
                    }

                    else if (message.Contains("GetCurrentCourses"))
                    {
                        string currentUserName = (message.Split(';')[1]);
                        string currentToken = (message.Split(';')[2]);

                        string currentCourses = "";

                        if (Authentication_Framework.Action_Authentication.authenticateAction(currentUserName, currentToken, true))
                        {
                            currentCourses = Database_Integration.Query.All_Courses.fetch();
                            System.Diagnostics.Debug.Write(currentCourses);
                        }

                        byte[] buffer = encoder.GetBytes(currentCourses);

                        dataStream.Write(buffer, 0, buffer.Length);

                        dataStream.Flush();
                    }

                    else if (message.Contains("GetAssociatedCourses"))
                    {
                        string currentUserName = (message.Split(';')[1]);
                        string currentToken = (message.Split(';')[2]);

                        string associatedCourses = "";

                        if (Authentication_Framework.Action_Authentication.authenticateAction(currentUserName, currentToken, false))
                        {
                            associatedCourses = Database_Integration.Query.Associated_Courses.fetch(Database_Integration.Query.ApplicationRole.fetch(currentUserName), currentUserName);
                        }

                        byte[] buffer = encoder.GetBytes(associatedCourses);

                        dataStream.Write(buffer, 0, buffer.Length);

                        dataStream.Flush();
                    }

                    else if (message.Contains("GetAssociatedAssignments"))
                    {
                        string currentUserName = (message.Split(';')[1]);
                        string currentToken = (message.Split(';')[2]);
                        string currentCourse = (message.Split(';')[3]);

                        string associatedAssignments = "";

                        if (Authentication_Framework.Action_Authentication.authenticateAction(currentUserName, currentToken, false))
                        {
                            associatedAssignments = Database_Integration.Query.Associated_Assignments.fetch(Database_Integration.Query.ApplicationRole.fetch(currentUserName), currentCourse);
                        }

                        byte[] buffer = encoder.GetBytes(associatedAssignments);

                        dataStream.Write(buffer, 0, buffer.Length);

                        dataStream.Flush();
                    }

                    else if (message.Contains("GetGrades"))
                    {
                        string currentUserName = (message.Split(';')[1]);
                        string currentToken = (message.Split(';')[2]);
                        string currentCourse = (message.Split(';')[3]);

                        string grades = "";

                        if (Authentication_Framework.Action_Authentication.authenticateAction(currentUserName, currentToken, false))
                        {
                            grades = Database_Integration.Query.Grades.fetch(Database_Integration.Query.ApplicationRole.fetch(currentUserName), currentCourse, currentToken);
                        }

                        byte[] buffer = encoder.GetBytes(grades);

                        dataStream.Write(buffer, 0, buffer.Length);

                        dataStream.Flush();
                    }


                    else if (message.Contains("AuthorizeNewGSuiteUser"))
                    {
                        string status = (message.Split(';')[1]);

                        string codeVerifier = (message.Split(';')[2]);
                        codeVerifier = codeVerifier.Remove(codeVerifier.Length - 1);

                        string authCode = (message.Split(new string[] { ";;;" }, StringSplitOptions.None))[1];
                        authCode = authCode.Substring(7);
                        authCode = authCode.Remove(authCode.Length - 47);

                        bool actionResult = false;

                        if (status.Equals("true"))
                        {
                            GSuite_Integration.Enable_Integration.enableGSuiteAsync(authCode, codeVerifier);
                        }

                        byte[] buffer = BitConverter.GetBytes(actionResult);

                        dataStream.Flush();
                    }

                    else
                    {
                        System.Diagnostics.Debug.Write(message);
                        // Notify client of error and flush connection.
                        byte[] buffer = encoder.GetBytes("null");

                        dataStream.Write(buffer, 0, buffer.Length);
                        dataStream.Flush();

                        break;
                    }
                    client.Close();
                }
            }
            // Handle unspecified communication error.
            catch (Exception comError)
            {
                Whiteboard_Communication_Service.Event_Viewer_Integration.EventViewer.logEvent(comError.Message, System.Diagnostics.EventLogEntryType.Warning);
            }
            // Ensure our listener doesn't get too obsessed with any one client.
            client.Close();
            tcpClientConnected.Set();
        }
    }
}
