using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Whiteboard_Communication_Service.Database_Integration.Query
{
    class Grades
    {
        public static string fetch(char applicationRole, string courseName, string accessToken)
        {
            string grades_queryString = "SELECT AssignmentName,AssignmentType,Grade FROM Grades INNER JOIN Logins ON Grades.AssociatedLogin = Logins.ID INNER JOIN Courses ON Grades.AssociatedCourse = Courses.ID INNER JOIN Assignments ON Grades.AssociatedAssignment = Assignments.ID WHERE Courses.DisplayName = @courseName AND  Logins.AccessToken = @accessToken";

            SqlParameter[] grades_queryParameters = new SqlParameter[2];
            grades_queryParameters[0] = new SqlParameter("@courseName", courseName);
            grades_queryParameters[1] = new SqlParameter("@accessToken", accessToken);

            string sqlResult = "";

            using (SqlConnection sqlConnection = new SqlConnection(SQL_Interface.connectionString()))
            {
                try
                {
                    SqlCommand command = new SqlCommand(grades_queryString, sqlConnection);
                    command.Parameters.AddRange(grades_queryParameters);

                    sqlConnection.Open();

                    using (SqlDataReader sqlReader = command.ExecuteReader())
                    {
                        while (sqlReader.Read())
                        {
                            sqlResult += sqlReader.GetString(0) + ";" + sqlReader.GetString(1) + ";" + (sqlReader.GetDecimal(2)).ToString();
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
