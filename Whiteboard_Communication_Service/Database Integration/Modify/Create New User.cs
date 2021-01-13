using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Whiteboard_Communication_Service.Database_Integration.Modify
{
    class Create_New_User
    {
        public static bool start(string newUserDisplayName, string newUserName, string newUserRole, string newUserHash, string newUserSalt)
        {
            SqlParameter[] addUser_queryParameters = new SqlParameter[5];
            addUser_queryParameters[0] = new SqlParameter("@newUserDisplayName", newUserDisplayName);
            addUser_queryParameters[1] = new SqlParameter("@newUserName", newUserName);
            addUser_queryParameters[2] = new SqlParameter("@newUserRole", newUserRole.First());
            addUser_queryParameters[3] = new SqlParameter("@newUserPWHash", Encoding.ASCII.GetBytes(newUserHash));
            addUser_queryParameters[4] = new SqlParameter("@newUserPWSalt", Encoding.ASCII.GetBytes(newUserSalt));

            string addUser_queryString = "INSERT INTO dbo.Logins (DisplayName,UserName,ApplicationRole,Aux_ApplicationRole,LoggedIn,PasswordHash,PasswordSalt,AccessToken) VALUES(@newUserDisplayName, @newUserName, @newUserRole, NULL, 0, @newUserPWHash, @newUserPWSalt, NULL)";

            using (SqlConnection sqlConnection = new SqlConnection(SQL_Interface.connectionString()))
            {
                try
                {
                    SqlCommand command = new SqlCommand(addUser_queryString, sqlConnection);
                    command.Parameters.AddRange(addUser_queryParameters);

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
