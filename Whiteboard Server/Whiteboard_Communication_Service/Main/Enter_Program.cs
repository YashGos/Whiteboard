using System;
using System.Collections.Generic;
using System.Linq;
using System.ServiceProcess;
using System.Text;
using System.Threading.Tasks;

namespace Whiteboard_Communication_Service
{
    static class Enter_Program
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        static void Main()
        {
           /* ServiceBase[] ServicesToRun;
            ServicesToRun = new ServiceBase[]
            {
                new Initialize_Service()
            };
            ServiceBase.Run(ServicesToRun);*/

            // Debug

            Communication_Layer.StartCOMLayer();
        }
    }
}
