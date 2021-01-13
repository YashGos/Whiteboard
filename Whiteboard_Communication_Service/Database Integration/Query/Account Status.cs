using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Whiteboard_Communication_Service.Database_Integration.Query
{
    class AccountStatus
    {
        public static bool fetch(string userName, string token)
        {
            SqlParameter[] accountStatus_queryParameters = new SqlParameter[2];
            accountStatus_queryParameters[0] = new SqlParameter("@userName", userName);
            accountStatus_queryParameters[1] = new SqlParameter("@token", token);

            string accountStatus_queryString = "SELECT LoggedIn FROM dbo.Logins WHERE UserName = @userName AND AccessToken = @token";

            using (SqlConnection sqlConnection = new SqlConnection(SQL_Interface.connectionString()))
            {
                try
                {
                    SqlCommand command = new SqlCommand(accountStatus_queryString, sqlConnection);
                    command.Parameters.AddRange(accountStatus_queryParameters);

                    sqlConnection.Open();

                    var sqlResult = command.ExecuteScalar();

                    sqlConnection.Close();
                    sqlConnection.Dispose();

                    return (bool)Convert.ChangeType(sqlResult, typeof(bool));
                }
                catch (Exception queryException)
                {
                    Whiteboard_Communication_Service.Event_Viewer_Integration.EventViewer.logEvent(queryException.Message, System.Diagnostics.EventLogEntryType.Warning);
                }
            }
            return false;
        }
    }
}
