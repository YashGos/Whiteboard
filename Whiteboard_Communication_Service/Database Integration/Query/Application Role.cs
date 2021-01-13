using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Whiteboard_Communication_Service.Database_Integration.Query
{
    class ApplicationRole
    {
        public static char fetch(string userName)
        {
            SqlParameter[] appRole_queryParameters = new SqlParameter[1];
            appRole_queryParameters[0] = new SqlParameter("@userName", userName);

            string appRole_queryString = "SELECT ApplicationRole FROM dbo.Logins WHERE UserName = @userName";

            using (SqlConnection sqlConnection = new SqlConnection(SQL_Interface.connectionString()))
            {
                try
                {
                    SqlCommand command = new SqlCommand(appRole_queryString, sqlConnection);
                    command.Parameters.AddRange(appRole_queryParameters);

                    sqlConnection.Open();

                    using (SqlDataReader sqlReader = command.ExecuteReader())
                    {
                        while (sqlReader.Read())
                        {
                            return (sqlReader.GetString(0))[0];
                        }
                    }

                    sqlConnection.Close();
                    sqlConnection.Dispose();
                }
                catch (Exception queryException)
                {
                    Whiteboard_Communication_Service.Event_Viewer_Integration.EventViewer.logEvent(queryException.Message, System.Diagnostics.EventLogEntryType.Warning);
                }
            }
            return 'N';
        }
    }
}
