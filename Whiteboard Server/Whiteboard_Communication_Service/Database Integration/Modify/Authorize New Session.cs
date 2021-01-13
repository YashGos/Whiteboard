using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Whiteboard_Communication_Service.Database_Integration.Modify
{
    class Authorize_New_Session
    {
        public static bool start(string UserName, Guid accessToken)
        {
            SqlParameter[] authSession_queryParameters = new SqlParameter[2];
            authSession_queryParameters[0] = new SqlParameter("@UserName", UserName);
            authSession_queryParameters[1] = new SqlParameter("@accessToken", accessToken);

            string authSession_queryString = "UPDATE dbo.Logins SET LoggedIn = 1 , AccessToken = @accessToken WHERE UserName = @UserName";

            using (SqlConnection sqlConnection = new SqlConnection(SQL_Interface.connectionString()))
            {
                try
                {
                    SqlCommand command = new SqlCommand(authSession_queryString, sqlConnection);
                    command.Parameters.AddRange(authSession_queryParameters);

                    sqlConnection.Open();

                    command.ExecuteNonQuery();

                    sqlConnection.Close();
                    sqlConnection.Dispose();

                    return true;
                }
                catch (Exception queryException)
                {
                    Whiteboard_Communication_Service.Event_Viewer_Integration.EventViewer.logEvent(queryException.Message, System.Diagnostics.EventLogEntryType.Warning);
                    return false;
                }
            }
        }
    }
}
