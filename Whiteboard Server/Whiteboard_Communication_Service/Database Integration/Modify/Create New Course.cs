using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Whiteboard_Communication_Service.Database_Integration.Modify
{
    class Create_New_Course
    {
        public static bool start(string newCourseDisplayName, string newCourseInstructor, string newCourseStudents, string newCourseAssignmentTypes, string newCourseTerm)
        {
            SqlParameter[] addCourse_queryParameters = new SqlParameter[5];
            addCourse_queryParameters[0] = new SqlParameter("@newCourseDisplayName", newCourseDisplayName);
            addCourse_queryParameters[1] = new SqlParameter("@newCourseInstructor", newCourseInstructor);
            addCourse_queryParameters[2] = new SqlParameter("@newCourseStudents", newCourseStudents);
            addCourse_queryParameters[3] = new SqlParameter("@newCourseAssignmentTypes", newCourseAssignmentTypes);
            addCourse_queryParameters[4] = new SqlParameter("@newCourseTerm", Int32.Parse(newCourseTerm));

            string addCourse_queryString = "INSERT INTO dbo.Courses (DisplayName,Students,Instructor,TeachingAssistants,Modules,AssignmentTypes,Term) VALUES(@newCourseDisplayName, @newCourseStudents, @newCourseInstructor, NULL, 'DEFAULT', @newCourseAssignmentTypes, @newCourseTerm)";

            using (SqlConnection sqlConnection = new SqlConnection(SQL_Interface.connectionString()))
            {
                try
                {
                    SqlCommand command = new SqlCommand(addCourse_queryString, sqlConnection);
                    command.Parameters.AddRange(addCourse_queryParameters);

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
