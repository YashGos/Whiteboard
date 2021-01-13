using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Whiteboard_Communication_Service.Database_Integration
{
    class SQL_Interface
    {
        public static string connectionString()
        {
            return "Persist Security Info=False;Integrated Security=SSPI;database=Whiteboard;server=(local)";
        }
    }
}
