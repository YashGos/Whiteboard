using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.IO;
using System.Security.Cryptography;


namespace Whiteboard_Communication_Service.Authentication_Framework
{
    class Login_Authentication
    {
        // Client has initated a login attempt, assign them a salt based off the provided username.
        public static byte[] initateLogin(string userName)
        {
            return (Database_Integration.Query.PasswordSalt.fetch(userName));
        }
        // Checks login attempt agains credentials stored in database. Returns true if credentials are vaild, false if invalid.
        public static (bool, Guid, char) authenticateLogin(string userName, string hashedPass)
        {
            // Convert the client-side password hash to a byte array.
            byte[] clienthashBytes = Encoding.UTF8.GetBytes(hashedPass);

            // Check against database.
            byte[] databaseHash = (Database_Integration.Query.PasswordHash.fetch(userName));

            if (clienthashBytes.SequenceEqual(databaseHash))
            {
                try
                {
                    Guid accessToken = Guid.NewGuid();
                    return (Database_Integration.Modify.Authorize_New_Session.start(userName, accessToken), accessToken, Database_Integration.Query.ApplicationRole.fetch(userName));
                }
                catch(Exception authorizationError)
                {
                    Event_Viewer_Integration.EventViewer.logEvent(authorizationError.Message, System.Diagnostics.EventLogEntryType.Warning);
                    return (false, Guid.Parse("00112233-4455-6677-8899-aabbccddeeff"), 'N');
                }
            }
            else
            {
                return (false, Guid.Parse("00112233-4455-6677-8899-aabbccddeeff"), 'N');
            }
        }
    }
}
