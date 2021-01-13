using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Whiteboard_Communication_Service.Authentication_Framework
{
    class Action_Authentication
    {
        public static bool authenticateAction(string currentUserName, string currentToken, bool requiresAdmin)
        {
            if (Database_Integration.Query.AccountStatus.fetch(currentUserName, currentToken))
            {
                if (requiresAdmin)
                {
                    if ((Database_Integration.Query.ApplicationRole.fetch(currentUserName)).Equals('A'))
                    {
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                else
                {
                    return true;
                }
            }
            else
            {
                return false;
            }
        }
    }
}
