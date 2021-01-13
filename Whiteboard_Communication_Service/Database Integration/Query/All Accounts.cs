using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Whiteboard_Communication_Service.Database_Integration.Query
{
    class AllAccounts
    {
        public static string fetch()
        {
            System.Diagnostics.Debug.Write("Here");
            string accounts_queryString = "SELECT DisplayName FROM dbo.Logins";

            string sqlResult = "";

            using (SqlConnection sqlConnection = new SqlConnection(SQL_Interface.connectionString()))
            {
                try
                {
                    SqlCommand command = new SqlCommand(accounts_queryString, sqlConnection);

                    sqlConnection.Open();

                    using (SqlDataReader sqlReader = command.ExecuteReader())
                    {
                        while (sqlReader.Read())
                        {
                            sqlResult += (sqlReader.GetValue(0) + ";");
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
            System.Diagnostics.Debug.Write(sqlResult);
            return sqlResult;
        }
    }
}
