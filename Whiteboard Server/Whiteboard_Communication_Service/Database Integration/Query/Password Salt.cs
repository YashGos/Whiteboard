using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Whiteboard_Communication_Service.Database_Integration.Query
{
    class PasswordSalt
    {
        public static byte[] fetch(string userName)
        {
            SqlParameter[] passwordSalt_queryParameters = new SqlParameter[1];
            passwordSalt_queryParameters[0] = new SqlParameter("@userName", userName);

            string passwordSalt_queryString = "SELECT PasswordSalt FROM dbo.Logins WHERE UserName = @userName";

            using (SqlConnection sqlConnection = new SqlConnection(SQL_Interface.connectionString()))
            {
                try
                {
                    SqlCommand command = new SqlCommand(passwordSalt_queryString, sqlConnection);
                    command.Parameters.AddRange(passwordSalt_queryParameters);

                    sqlConnection.Open();

                    byte[] sqlResult = command.ExecuteScalar() as byte[];

                    sqlConnection.Close();
                    sqlConnection.Dispose();

                    return (sqlResult);
                }
                catch (Exception queryException)
                {
                    Whiteboard_Communication_Service.Event_Viewer_Integration.EventViewer.logEvent(queryException.Message, System.Diagnostics.EventLogEntryType.Warning);
                }
            }
            return (byte[])Convert.ChangeType(0, typeof(byte[]));
        }
    }
}
