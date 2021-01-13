using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Whiteboard_Communication_Service.Database_Integration.Query
{
    class Associated_Assignments
    {
        public static string fetch(char applicationRole, string courseName)
        {
            string assignments_queryString = "SELECT * FROM Assignments INNER JOIN Courses ON Assignments.CourseID = Courses.ID WHERE Courses.DisplayName = @CourseName";

            SqlParameter[] assignments_queryParameters = new SqlParameter[1];
            assignments_queryParameters[0] = new SqlParameter("@courseName", courseName);

            string sqlResult = "";

            using (SqlConnection sqlConnection = new SqlConnection(SQL_Interface.connectionString()))
            {
                try
                {
                    SqlCommand command = new SqlCommand(assignments_queryString, sqlConnection);
                    command.Parameters.AddRange(assignments_queryParameters);

                    sqlConnection.Open();

                    using (SqlDataReader sqlReader = command.ExecuteReader())
                    {
                        while (sqlReader.Read())
                        {
                            sqlResult += sqlReader.GetString(4) + ";" + sqlReader.GetString(5) + ";" + ((DateTime)sqlReader.GetDateTime(3)).ToShortDateString();
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
